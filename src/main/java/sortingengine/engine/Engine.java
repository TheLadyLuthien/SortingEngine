package sortingengine.engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import sortingengine.engine.data.TagSet;
import sortingengine.engine.data.database.FileLookup;
import sortingengine.engine.data.database.ItemRecords;
import sortingengine.engine.data.item.Item;
import sortingengine.engine.data.tag.Tag;
import sortingengine.engine.data.tag.TagCatagory;
import sortingengine.engine.data.tag.TagWithCatagory;

public class Engine
{
    public static final Logger LOGGER = LoggerFactory.getLogger("Engine");
    public static final Logger FILESYSTEM_LOGGER = LoggerFactory.getLogger("Filesystem");

    private FileLookup fileLookupDb = new FileLookup();

    private HashMap<TagCatagory, TagSet> globalTagList = new HashMap<>();

    public Tag createTag(TagCatagory tagCatagory, String path)
    {
        Tag tag = Tag.of(path);
        this.globalTagList.computeIfAbsent(tagCatagory, key -> new TagSet()).add(tag);

        return tag;
    }

    public Tag ensureTag(TagCatagory tagCatagory, Tag tag)
    {
        this.globalTagList.computeIfAbsent(tagCatagory, key -> new TagSet()).add(tag);

        return tag;
    }

    public Tag ensureTag(TagWithCatagory twc)
    {
        return ensureTag(twc.catagory(), twc.tag());
    }

    public FileLookup getFileLookupDb()
    {
        return fileLookupDb;
    }

    private ItemRecords itemRecordDb = new ItemRecords();

    public ItemRecords getItemRecordDb()
    {
        return itemRecordDb;
    }

    public void saveDatabases() throws JsonProcessingException, IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        String items = mapper.writeValueAsString(this.itemRecordDb);
        String files = mapper.writeValueAsString(this.fileLookupDb);
        String tags = mapper.writeValueAsString(this.globalTagList);

        FileHelper.writeStringAsFile(files, Path.of(FileHelper.FILE_LOOKUP_FILE));
        FileHelper.writeStringAsFile(items, Path.of(FileHelper.ITEM_RECORD_FILE));
        FileHelper.writeStringAsFile(tags, Path.of(FileHelper.TAG_DATABASE_FILE));

        LOGGER.info("Databases saved");
    }

    public void loadAndReplaceDatabases()
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        try
        {
            String itemData = FileHelper.readFileAsString(Path.of(FileHelper.ITEM_RECORD_FILE));
            this.itemRecordDb = mapper.readValue(itemData, ItemRecords.class);
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to load item database");
            LOGGER.debug("Exception thrown!", e);
        }

        try
        {
            final String fileData = FileHelper.readFileAsString(Path.of(FileHelper.FILE_LOOKUP_FILE));
            this.fileLookupDb = mapper.readValue(fileData, FileLookup.class);
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to load file lookup database");
            LOGGER.debug("Exception thrown!", e);
        }

        try
        {
            final String tagData = FileHelper.readFileAsString(Path.of(FileHelper.TAG_DATABASE_FILE));
            this.globalTagList = mapper.readValue(tagData, new TypeReference<HashMap<TagCatagory, TagSet>>()
            {});
        }
        catch (IOException e)
        {
            LOGGER.error("Failed to load tag database");
            LOGGER.debug("Exception thrown!", e);
        }
    }

    public void onboardImportFolder(Path source) throws IOException
    {
        final LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy_HH:mm:ss");

        String importName = "import_" + now.format(formatter);
        final Path destination = Path.of(FileHelper.REPOSITORY_ROOT).resolve(importName);
        // Files.copy(path, destination, null)

        LOGGER.info("Import started");

        FileHelper.copyDirectoryAndContents(source, destination);
        LOGGER.info("Folder copied");

        LOGGER.info("Starting item processing");
        Files.walk(destination).forEach(path -> {
            if (!Files.isDirectory(path))
            {
                try
                {
                    Item item = Item.createProperItemForFile(path, this);

                    this.itemRecordDb.registerItem(item);
                    this.fileLookupDb.registerItem(item, path);
                }
                catch (Exception e)
                {
                    Item.LOADER_LOGGER.error("Failed to load item {}", path);
                    Item.LOADER_LOGGER.debug("Exception Thrown", e);
                }
            }
        });

        saveDatabases();
        LOGGER.info("Import complete");
    }
}

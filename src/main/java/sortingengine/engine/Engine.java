package sortingengine.engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sortingengine.engine.data.database.FileLookup;
import sortingengine.engine.data.database.ItemRecords;
import sortingengine.engine.data.item.Item;

public class Engine
{
    public static final Logger FILESYSTEM_LOGGER = LoggerFactory.getLogger("Filesystem"); 

    private FileLookup fileLookupDb = new FileLookup();
    public FileLookup getFileLookupDb()
    {
        return fileLookupDb;
    }

    private ItemRecords itemRecordDb = new ItemRecords();

    public ItemRecords getItemRecordDb()
    {
        return itemRecordDb;
    }

    public void serializeAndSaveDatabases() throws JsonProcessingException, IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);

        String items = mapper.writeValueAsString(this.itemRecordDb);
        String files = mapper.writeValueAsString(this.fileLookupDb);

        FileHelper.writeStringAsFile(files, Path.of(FileHelper.FILE_LOOKUP_FILE));
        FileHelper.writeStringAsFile(items, Path.of(FileHelper.ITEM_RECORD_FILE));
    }

    public void loadAndReplaceDatabases() throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        
        final String itemData = FileHelper.readFileAsString(Path.of(FileHelper.ITEM_RECORD_FILE));
        this.itemRecordDb = mapper.readValue(itemData, ItemRecords.class);
        
        final String fileData = FileHelper.readFileAsString(Path.of(FileHelper.FILE_LOOKUP_FILE));
        this.fileLookupDb = mapper.readValue(fileData, FileLookup.class);
    }

    public void onboardImportFolder(Path source) throws IOException
    {
        final LocalDateTime now = LocalDateTime.now();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy_hh:mm:ss");
        
        String importName = "import_" + now.format(formatter);
        final Path destination = Path.of(FileHelper.REPOSITORY_ROOT).resolve(importName);
        // Files.copy(path, destination, null)
        
        FileHelper.copyDirectoryAndContents(source, destination);
        Files.walk(destination).forEach(path -> {
            if (!Files.isDirectory(path))
            {
                // TODO: properly construct the correct type of item
                Item item = new Item(UUID.randomUUID());
                this.itemRecordDb.registerItem(item);
                this.fileLookupDb.registerItem(item, path);
            }
        });
    }
}

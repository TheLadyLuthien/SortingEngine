package sortingengine.engine.data.item;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import sortingengine.conf.RuntimeConfig;
import sortingengine.engine.Engine;
import sortingengine.engine.data.TagSet;
import sortingengine.engine.data.tag.TagCatagory;
import sortingengine.engine.post.ImportPostProcessor;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Photo.class, name = "Photo"),
    @JsonSubTypes.Type(value = Video.class, name = "Video")
})
public class Item
{
    private final UUID uuid;

    public UUID getUuid()
    {
        return uuid;
    }

    private final HashMap<TagCatagory, TagSet> tags = new HashMap<>();

    public TagSet getTags(TagCatagory tagCatagory)
    {
        this.tags.computeIfAbsent(tagCatagory, key -> new TagSet());
        return this.tags.get(tagCatagory);
    }

    public void removeAllTags()
    {
        this.tags.clear();
    }

    public Item(@JsonProperty("uuid") UUID uuid)
    {
        this.uuid = uuid;
    }

    @Override
    public int hashCode()
    {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Item other = (Item)obj;
        if (uuid == null)
        {
            if (other.uuid != null)
                return false;
        }
        else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

    public static final Logger LOADER_LOGGER = LoggerFactory.getLogger("Item Loader");
    private static final List<BiFunction<UUID, Path, Item>> ITEM_CONSTRUCTORS = List.of(Video::tryCreateFromFile, Photo::tryCreateFromFile);

    public static Item createProperItemForFile(Path path, Engine engine)
    {
        Item item = null;

        UUID uuid = UUID.randomUUID();

        final ArrayList<ImportPostProcessor> postProcessors = RuntimeConfig.IMPORT_POST_PROCESSORS.getValue();

        for (var processor : ITEM_CONSTRUCTORS)
        {
            item = processor.apply(uuid, path);

            if (item != null)
            {
                for (ImportPostProcessor importPostProcessor : postProcessors)
                {
                    importPostProcessor.apply(item, path, engine);
                }
                break;
            }
        }

        return item;
    }
}

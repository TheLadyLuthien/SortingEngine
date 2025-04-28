package sortingengine.engine.data.item;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import sortingengine.engine.data.TagSet;
import sortingengine.engine.data.tag.TagCatagory;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({@JsonSubTypes.Type(value = Photo.class, name = "Photo")})
public class Item
{
    private final UUID uuid;

    public UUID getUuid()
    {
        return uuid;
    }

    private final HashMap<TagCatagory, TagSet> tags = new HashMap<>();

    public HashMap<TagCatagory, TagSet> getTags()
    {
        return tags;
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

    public static Item createProperItemForFile(Path path)
    {
        Item item = null;

        final List<BiFunction<UUID, Path, Item>> processors = List.of(Photo::tryCreateFromFile);
        UUID uuid = UUID.randomUUID();

        for (var processor : processors)
        {
            item = processor.apply(uuid, path);
            
            if (item != null)
            {
                break;
            }
        }

        return item;
    }
}

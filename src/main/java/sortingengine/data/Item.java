package sortingengine.data;

import java.util.HashMap;
import java.util.UUID;

import sortingengine.data.tag.TagCatagory;

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

    public Item(UUID uuid)
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
}

package sortingengine.data;

import java.util.HashMap;
import java.util.UUID;

public class Photo
{
    public final UUID uuid;

    private final HashMap<TagCatagory, TagSet> tags = new HashMap<>();
    
    public Photo(UUID uuid)
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
        Photo other = (Photo)obj;
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

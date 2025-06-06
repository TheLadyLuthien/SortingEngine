package sortingengine.engine.data.tag;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tag
{
    private static final char TAG_PATH_SEPERATOR = '/';

    @Deprecated
    public Tag(@JsonProperty("path") String path)
    {
        this.path = path;
    }

    @Deprecated
    public static Tag of(String path)
    {
        return new Tag(path);
    }

    public final String path;

    @JsonIgnore
    public String getName()
    {
        return path.substring(path.lastIndexOf(TAG_PATH_SEPERATOR));
    }

    @JsonIgnore
    @Nullable
    public Tag getParentTag()
    {
        if (hasParent())
        {
            return of(this.path.substring(0, this.path.lastIndexOf(TAG_PATH_SEPERATOR)));
        }
        else
        {
            return null;
        }
    }

    public boolean hasParent()
    {
        return this.path.contains("/");
    }

    @Override
    public int hashCode()
    {
        return path.hashCode();
    }

    public boolean isSubTagOf(Tag tag)
    {
        return this.path.startsWith(tag.path + "/");
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
        Tag other = (Tag)obj;
        if (path == null)
        {
            if (other.path != null)
                return false;
        }
        else if (!path.equals(other.path))
            return false;
        return true;
    }
}

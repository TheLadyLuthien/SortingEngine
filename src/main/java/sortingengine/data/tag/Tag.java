package sortingengine.data.tag;

public class Tag
{
    private static final char TAG_PATH_SEPERATOR = '/';

    private Tag(String path)
    {
        this.path = path;
    }

    public static Tag of(String path)
    {
        return new Tag(path);
    }

    public final String path;

    public String getName()
    {
        return path.substring(path.lastIndexOf(TAG_PATH_SEPERATOR));
    }

    public Tag getParentTag()
    {
        return of(this.path.substring(0, this.path.lastIndexOf(TAG_PATH_SEPERATOR)));
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

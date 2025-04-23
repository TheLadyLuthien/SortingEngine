package sortingengine.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TagSet
{
    private final HashSet<Tag> set = new HashSet<>();

    public void add(Tag tag)
    {
        addTagOrPromoteToSubTagIfAvailable(tag);
        // if (!contains(tag))
        // {
            // set.add(tag);
        // }
    }

    public boolean contains(Tag tag)
    {
        return set.stream().anyMatch(t -> t.equals(tag) || t.isSubTagOf(tag));
    }

    public int size()
    {
        return set.size();
    }

    private void addTagOrPromoteToSubTagIfAvailable(Tag newTag)
    {
        set.removeAll(set.stream().filter(existingTag -> newTag.isSubTagOf(existingTag)).toList());
        set.add(newTag);
        //  set.stream().filter(existingTag -> newTag.isSubTagOf(newTag)).toList()
    }

    /**
     * @param tag
     * The tag to remove
     * 
     * In place of the tag that was removed, it's next-level parent will be added in its place
     */
    public void removeSubTag(Tag tag)
    {
        if (set.remove(tag))
        {
            add(tag.getParentTag());
        }
    }
}

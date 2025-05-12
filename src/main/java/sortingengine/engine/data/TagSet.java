package sortingengine.engine.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import sortingengine.engine.data.tag.Tag;

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
     * @param tagToRemove
     * The tag to remove
     * 
     * In place of the tag that was removed, it's next-level parent will be added
     * If the tag to remove has sub tags, those will be removed as well
     */
    public void removeSubTag(Tag tagToRemove)
    {
        removeTag(tagToRemove, true);
    }

    public void removeEntireTag(Tag tag)
    {
        removeTag(tag, false);
    }

    private void removeTag(Tag tagToRemove, boolean addParent)
    {
        List<Tag> tagsToRemove =  set.stream().filter(t -> t.equals(tagToRemove) || t.isSubTagOf(tagToRemove)).toList();
        if (set.removeAll(tagsToRemove))
        {
            if (addParent && tagToRemove.hasParent())
            {
                add(tagToRemove.getParentTag());
            }
        }
    }
}

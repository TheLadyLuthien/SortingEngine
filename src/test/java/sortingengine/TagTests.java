package sortingengine;

import org.junit.jupiter.api.Test;

import sortingengine.data.TagSet;
import sortingengine.data.tag.Tag;
import sortingengine.data.tag.TagCatagories;

import static org.junit.jupiter.api.Assertions.*;

public class TagTests
{
    @Test
    void tagSetAdd()
    {
        // Tag superTag = new Tag("root");
        Tag subTag = Tag.of("root/subTag");

        TagSet tagSet = new TagSet();
        tagSet.add(subTag);

        assertTrue(tagSet.contains(subTag));
    }

    @Test
    void tagSetContainsSuperTag()
    {
        Tag superTag = Tag.of("root");
        Tag subTag = Tag.of("root/subTag");

        TagSet tagSet = new TagSet();
        tagSet.add(subTag);

        assertTrue(tagSet.contains(superTag));
    }

    @Test
    void tagSetPromotion()
    {
        Tag superTag = Tag.of("root");
        Tag subTag = Tag.of("root/subTag");

        TagSet tagSet = new TagSet();
        tagSet.add(superTag);
        
        assertTrue(tagSet.contains(superTag));
        
        tagSet.add(subTag);
        assertTrue(tagSet.contains(superTag));
        assertTrue(tagSet.contains(subTag));
        assertEquals(1, tagSet.size());
    }

    @Test
    void tagGetParentEquals()
    {
        Tag superTag = Tag.of("root");
        Tag subTag = Tag.of("root/subTag");

        assertEquals(superTag, subTag.getParentTag());
    }

    @Test
    void removalOfTagLeavesParentBehind()
    {
        Tag superTag = Tag.of("root");
        Tag subTag = Tag.of("root/subTag");

        TagSet tagSet = new TagSet();
        tagSet.add(subTag);

        tagSet.removeSubTag(subTag);

        assertTrue(tagSet.contains(superTag));
        assertEquals(1, tagSet.size());
    }
}

package sortingengine;

import org.junit.jupiter.api.Test;

import sortingengine.data.Tag;
import sortingengine.data.TagCatagories;
import sortingengine.data.TagSet;

import static org.junit.jupiter.api.Assertions.*;

public class TagTests
{
    @Test
    void tagSetAdd()
    {
        // Tag superTag = new Tag("root");
        Tag subTag = Tag.of("root/subTag", TagCatagories.CONTENT);

        TagSet tagSet = new TagSet();
        tagSet.add(subTag);

        assertTrue(tagSet.contains(subTag));
    }

    @Test
    void tagSetContainsSuperTag()
    {
        Tag superTag = Tag.of("root", TagCatagories.CONTENT);
        Tag subTag = Tag.of("root/subTag", TagCatagories.CONTENT);

        TagSet tagSet = new TagSet();
        tagSet.add(subTag);

        assertTrue(tagSet.contains(superTag));
    }

    @Test
    void tagSetPromotion()
    {
        Tag superTag = Tag.of("root", TagCatagories.CONTENT);
        Tag subTag = Tag.of("root/subTag", TagCatagories.CONTENT);

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
        Tag superTag = Tag.of("root", TagCatagories.CONTENT);
        Tag subTag = Tag.of("root/subTag", TagCatagories.CONTENT);

        assertEquals(superTag, subTag.getParentTag());
    }

    @Test
    void removalOfTagLeavesParentBehind()
    {
        Tag superTag = Tag.of("root", TagCatagories.CONTENT);
        Tag subTag = Tag.of("root/subTag", TagCatagories.CONTENT);

        TagSet tagSet = new TagSet();
        tagSet.add(subTag);

        tagSet.removeSubTag(subTag);

        assertTrue(tagSet.contains(superTag));
        assertEquals(1, tagSet.size());
    }
}

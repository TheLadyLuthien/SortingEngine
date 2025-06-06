package sortingengine.engine.data.tag;

import org.junit.jupiter.api.Test;

import sortingengine.engine.data.TagSet;
import sortingengine.engine.data.tag.Tag;
import sortingengine.engine.data.tag.TagCatagory;

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

    @Test
    void removalOfRootTag()
    {
        Tag superTag = Tag.of("root");

        TagSet tagSet = new TagSet();
        tagSet.add(superTag);
        assertEquals(1, tagSet.size());
        
        tagSet.removeSubTag(superTag);
        
        assertEquals(0, tagSet.size());
        assertTrue(!tagSet.contains(superTag));
    }

    @Test
    void removalOfMiddleTag()
    {
        Tag superTag = Tag.of("root");
        Tag middleTag = Tag.of("root/middle");
        Tag subTag = Tag.of("root/middle/final");

        TagSet tagSet = new TagSet();
        tagSet.add(subTag);
        assertEquals(1, tagSet.size());
        
        tagSet.removeSubTag(middleTag);
        
        assertEquals(1, tagSet.size());
        assertTrue(!tagSet.contains(subTag));
        assertTrue(!tagSet.contains(middleTag));
        assertTrue(tagSet.contains(superTag));
    }

    @Test
    void removalOfEntireTagFromMiddleTag()
    {
        Tag superTag = Tag.of("root");
        Tag middleTag = Tag.of("root/middle");
        Tag subTag = Tag.of("root/middle/final");

        TagSet tagSet = new TagSet();
        tagSet.add(subTag);
        assertEquals(1, tagSet.size());
        
        tagSet.removeEntireTag(middleTag);
        
        assertEquals(0, tagSet.size());
        assertTrue(!tagSet.contains(subTag));
        assertTrue(!tagSet.contains(middleTag));
        assertTrue(!tagSet.contains(superTag));
    }
}

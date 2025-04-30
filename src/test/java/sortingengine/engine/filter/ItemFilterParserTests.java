package sortingengine.engine.filter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import sortingengine.engine.data.item.Item;
import sortingengine.engine.data.tag.Tag;
import sortingengine.engine.data.tag.TagCatagories;

public class ItemFilterParserTests
{
    @Test
    void parseComplexTest()
    {
        Predicate<Item> predicate = ItemFilterParser.parse("(lt:scotland/edinburgh&dt:tips/summer_2022) & !(c t:books|lego|sheep)");
        assertNotNull(predicate);
    }

    @Test
    void parseSimpleTest()
    {
        Predicate<Item> predicate = ItemFilterParser.parse("lt:scotland/edinburgh");
        assertNotNull(predicate);

        Item passingTestItem = new Item(UUID.randomUUID());
        passingTestItem.getTags(TagCatagories.LOCATION).add(Tag.of("scotland/edinburgh"));
        
        Item failingTestItem = new Item(UUID.randomUUID());
        
        assertTrue(predicate.test(passingTestItem));
        assertTrue(!predicate.test(failingTestItem));
    }
}

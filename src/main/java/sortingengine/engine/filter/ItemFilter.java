package sortingengine.engine.filter;

import java.time.LocalDateTime;
import java.util.function.Predicate;

import sortingengine.engine.data.item.Item;
import sortingengine.engine.data.item.Photo;
import sortingengine.engine.data.tag.Tag;
import sortingengine.engine.data.tag.TagCatagory;

public class ItemFilter
{
    public static Predicate<Item> tagMatch(TagCatagory catagory, Tag tag)
    {
        return (item) -> item.getTags(catagory).contains(tag);
    }

    public static Predicate<Item> customDateMatch(LocalDateTime start, LocalDateTime end)
    {
        return (item) -> {
            if (item instanceof Photo photo)
            {
                LocalDateTime dateTaken = photo.getDateTaken();
                return dateTaken.isAfter(start) && dateTaken.isBefore(end);
            }
            else
            {
                return false;
            }
        };
    }
}

package sortingengine.engine.filter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import sortingengine.engine.data.item.Item;
import sortingengine.engine.data.item.Photo;
import sortingengine.engine.data.item.interfaces.TimestampedItem;
import sortingengine.engine.data.tag.Tag;
import sortingengine.engine.data.tag.TagCatagory;

public class ItemFilter
{
    public static ItemFilter tagMatch(TagCatagory catagory, Tag tag)
    {
        return of((item) -> item.getTags(catagory).contains(tag));
    }

    public static ItemFilter customDateMatch(LocalDate start, LocalDate end)
    {
        return of((item) -> {
            if (item instanceof TimestampedItem photo)
            {
                LocalDateTime dateTaken = photo.getDateTaken();
                if (dateTaken != null)
                {
                    return dateTaken.toLocalDate().isAfter(start) && dateTaken.toLocalDate().isBefore(end);
                }
            }
            return false;
        });
    }

    public static ItemFilter typeMatch(String type)
    {
        return of((item) -> {
            return (item.getClass().getSimpleName().toLowerCase().equals(type.toLowerCase()));
        });
    }

    private final Predicate<Item> predicate;
    private ItemFilter(Predicate<Item> predicate)
    {
        this.predicate = predicate;
    }
    private static ItemFilter of(Predicate<Item> predicate)
    {
        return new ItemFilter(predicate);
    }

    public ItemFilter or(ItemFilter other)
    {
        return of(predicate.or(other.predicate));
    }

    public ItemFilter negate()
    {
        return of(predicate.negate());
    }

    public ItemFilter and(ItemFilter other)
    {
        return of(predicate.and(other.predicate));
    }

    public boolean test(Item item)
    {
        return predicate.test(item);
    }
}

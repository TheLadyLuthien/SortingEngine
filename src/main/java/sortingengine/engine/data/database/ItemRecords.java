package sortingengine.engine.data.database;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import sortingengine.engine.data.item.Item;
import sortingengine.engine.filter.ItemFilter;

public class ItemRecords implements Serializable
{
    private final Set<Item> items = new HashSet<>();

    public void registerItem(Item item)
    {
        items.add(item);
    }

    public int size()
    {
        return items.size();
    }

    public Stream<Item> findItems(ItemFilter itemFilter)
    {
        return items.stream().filter(itemFilter::test);
    }
}

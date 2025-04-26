package sortingengine.engine.data.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import sortingengine.engine.data.item.Item;

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
}

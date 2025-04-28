package sortingengine.engine;

import java.nio.file.Path;

import sortingengine.engine.data.item.Item;

public interface ImportPostProcessor
{
    void apply(Item item, Path path);
}

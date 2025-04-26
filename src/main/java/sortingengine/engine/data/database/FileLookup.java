package sortingengine.engine.data.database;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nullable;

import sortingengine.engine.data.item.Item;

public class FileLookup
{
    private final HashMap<UUID, String> fileIndex = new HashMap<>();

    @Nullable
    public Path getPath(Item item)
    {
        return Path.of(fileIndex.get(item.getUuid()));
    }

    @Nullable
    public Path getPath(UUID itemUuid)
    {
        return Path.of(fileIndex.get(itemUuid));
    }

    public void registerItem(Item item, Path path)
    {
        fileIndex.put(item.getUuid(), path.toString());
    }
}

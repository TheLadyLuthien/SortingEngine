package sortingengine.data.database;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.hash.HashCode;

import sortingengine.data.Item;

public class Database
{
    private final HashMap<UUID, Path> fileIndex = new HashMap<>();

    @Nullable
    public Path getPath(Item item)
    {
        return fileIndex.get(item.getUuid());
    }

    public void registerItem(Item item, Path path)
    {
        fileIndex.put(item.getUuid(), path);
    }
}

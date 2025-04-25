package sortingengine.data.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sortingengine.data.Item;

public class DatabaseTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger("DatabaseTests");

    @Test
    @Timeout(value = 1)
    void addAndFindItemPath()
    {
        Item item = new Item(UUID.randomUUID());
        Database db = new Database();
        Path path = Path.of("test/bar.png");
        db.registerItem(item, path);

        assertEquals(path, db.getPath(item));
    }

    @Test
    @Timeout(value = 40, unit = TimeUnit.MILLISECONDS)
    void addAndFindmManyItemPaths()
    {
        final int count = 200;
        Database db = new Database();

        Item[] items = new Item[count];
        for (int i = 0; i < count; i++)
        {
            Item item = new Item(UUID.randomUUID());
            Path path = Path.of("test/" + item.getUuid() + ".png");
            db.registerItem(item, path);
            items[i] = item;
        }

        for (Item item : items)
        {
            assertEquals(Path.of("test/" + item.getUuid() + ".png"), db.getPath(item));
        }
    }

    @Test
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void find1In150k()
    {
        final int count = 150_000;
        Database db = new Database();

        final Item[] items = new Item[count];
        for (int i = 0; i < count; i++)
        {
            Item item = new Item(UUID.randomUUID());
            Path path = Path.of("test/" + item.getUuid() + ".png");
            db.registerItem(item, path);
            items[i] = item;
        }

        // final long[] times = new long[count];
        double min = Long.MAX_VALUE;
        double max = Long.MIN_VALUE;
        double average = 0;
        double total = 0;

        for (int i = 0; i < count; i++)
        {
            double start = System.currentTimeMillis();
            final Item item = items[i];

            Path foundPath = db.getPath(item);
            assertEquals(Path.of("test/" + item.getUuid() + ".png"), foundPath);

            double end = System.currentTimeMillis();

            final double time = end - start;
            // times[i] = time;

            if (time < min)
            {
                min = time;
            }
            if (time > max)
            {
                max = time;
            }

            if (i == 0)
            {
                average = time;
            }
            else
            {
                average = (average + time) / 2.0;
            }

            total += time;
        }

        LOGGER.info("Total: {}, Average: {}, Min: {}, Max: {}", total, average, min, max);
        assertTrue(total < 100);
    }
}

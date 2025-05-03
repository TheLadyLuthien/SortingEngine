package sortingengine.engine.post;

import java.nio.file.Path;

import sortingengine.engine.Engine;
import sortingengine.engine.data.database.ItemRecords;
import sortingengine.engine.data.item.Item;

public class VideoDateNeighborProcessor implements ImportPostProcessor
{
    public VideoDateNeighborProcessor(int maxNeighborDistance, Mode dateCalculationMode)
    {
        this.maxNeighborDistance = maxNeighborDistance;
        this.dateCalculationMode = dateCalculationMode;
    }

    public final int maxNeighborDistance;
    public final Mode dateCalculationMode;

    public static enum Mode
    {
        AVERAGE,
        NEAREST,
        BEFORE,
        AFTER
    }

    @Override
    public void apply(Item item, Path path, Engine engine)
    {
        ItemRecords items = engine.getItemRecordDb();
        // int videoIndex = items.
    }
}

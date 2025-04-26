package sortingengine.engine.data.item;

import java.util.UUID;

public class Photo extends Item
{
    private String dateTaken;
    private String locationTaken;

    public Photo(UUID uuid)
    {
        super(uuid);
    }
}

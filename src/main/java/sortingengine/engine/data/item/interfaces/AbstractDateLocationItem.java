package sortingengine.engine.data.item.interfaces;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.annotation.Nullable;

import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.mov.metadata.QuickTimeMetadataDirectory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import sortingengine.engine.data.item.Item;
import sortingengine.engine.data.item.LocationData;

public abstract class AbstractDateLocationItem extends Item implements TimestampedItem, LocationMarkedItem
{
    public AbstractDateLocationItem(UUID uuid)
    {
        super(uuid);
    }


    protected String dateTaken;

    @JsonProperty("dateTaken")
    public String getDateTakenString()
    {
        return dateTaken;
    }

    @JsonProperty("dateTaken")
    protected void setDateTakenString(String value)
    {
        this.dateTaken = value;
    }


    @JsonIgnore
    @Nullable
    @Override
    public LocalDateTime getDateTaken()
    {
        if (dateTaken == null)
        {
            return null;
        }

        return LocalDateTime.parse(dateTaken, TimestampedItem.EXIF_DATE_TIME_FORMATTER);
    }

    @Nullable
    protected LocationData locationTaken = null;

    @Nullable
    @Override
    public LocationData getLocationTaken()
    {
        return locationTaken;
    }
}

package sortingengine.engine.data.item.interfaces;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    protected void setDateTakenFromImageMetadata(Metadata metadata)
    {
        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        if (directory != null)
        {
            this.dateTaken = directory.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
        }
    }

    private static final DateTimeFormatter VIDEO_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ssZ");
    protected void setDateTakenFromVideoMetadata(Metadata metadata)
    {
        QuickTimeMetadataDirectory directory = metadata.getFirstDirectoryOfType(QuickTimeMetadataDirectory.class);

        // ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        if (directory != null)
        {
            String videoDT = directory.getString(QuickTimeMetadataDirectory.TAG_CREATION_DATE);
            // QuickTimeMetadataDirectory.TAG_LOCATION_BODY
            
            // reformat to match image's EXIF format
            this.dateTaken = LocalDateTime.parse(videoDT, VIDEO_DATE_TIME_FORMATTER).format(EXIF_DATE_TIME_FORMATTER);
        }
    }

    protected void setLocationFromImageMetadata(Metadata metadata)
    {
        this.locationTaken = LocationData.fromExifMetadata(metadata);
    }

    protected void setLocationFromVideoMetadata(Metadata metadata)
    {
        this.locationTaken = LocationData.fromExifMetadata(metadata);
    }
}

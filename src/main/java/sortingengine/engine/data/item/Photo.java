package sortingengine.engine.data.item;

import java.nio.file.Path;
import java.util.UUID;

import javax.annotation.Nullable;


import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.fasterxml.jackson.annotation.JsonProperty;

import sortingengine.engine.data.item.interfaces.AbstractDateLocationItem;
import sortingengine.engine.data.item.interfaces.DeviceMarkedItem;

public class Photo extends AbstractDateLocationItem implements DeviceMarkedItem
{
    @Nullable
    private DeviceData deviceData = null;

    public Photo(@JsonProperty("uuid") UUID uuid)
    {
        super(uuid);
    }

    @Nullable
    public static Photo tryCreateFromFile(UUID uuid, Path path)
    {
        try
        {
            Photo photo = new Photo(uuid);
            
            Metadata metadata = ImageMetadataReader.readMetadata(path.toFile());

            photo.setDateTakenFromImageMetadata(metadata);
            photo.setLocationFromImageMetadata(metadata);
            photo.setDeviceDataFromImageMetadata(metadata);

            return photo;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    protected void setDeviceDataFromImageMetadata(Metadata metadata)
    {
        this.deviceData = DeviceData.fromMetadata(metadata);
    }

    @Nullable
    @Override
    public DeviceData getDeviceData()
    {
        return deviceData;
    }
}

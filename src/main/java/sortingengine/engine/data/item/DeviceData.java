package sortingengine.engine.data.item;

import javax.annotation.Nullable;

import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DeviceData(@JsonProperty("make") String make, @JsonProperty("model") String model)
{
    @Nullable
    public static DeviceData fromMetadata(Metadata metadata)
    {
        ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
        if (directory != null)
        {
            final String make = directory.getString(ExifIFD0Directory.TAG_MAKE);
            final String model = directory.getString(ExifIFD0Directory.TAG_MODEL);

            if (make != null || model != null)
            {
                return new DeviceData(make, model);
            }
        }

        return null;
    }
}

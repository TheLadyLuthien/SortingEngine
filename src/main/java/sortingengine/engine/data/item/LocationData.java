package sortingengine.engine.data.item;

import java.util.Arrays;

import javax.annotation.Nullable;

import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;

import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationData
{
    @Nullable
    public static LocationData fromExifMetadata(Metadata metadata)
    {
        GpsDirectory directory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

        if (directory != null)
        {
            var geo = directory.getGeoLocation();

            if (geo != null)
            {
                return new LocationData(geo.getLatitude(), geo.getLongitude());
            }
        }

        return null;
    }

    @Nullable
    public static LocationData fromQuickTimeMetadata(Metadata metadata)
    {
        // GpsDirectory directory = metadata.getFirstDirectoryOfType(QuickTIme.class);

        // if (directory != null)
        // {
        //     var geo = directory.getGeoLocation();

        //     if (geo != null)
        //     {
        //         return new LocationData(geo.getLatitude(), geo.getLongitude());
        //     }
        // }

        return null;
    }

    public LocationData(
        @JsonProperty("lat") double lat,
        @JsonProperty("lng") double lng
    )
    {
        this.lat = lat;
        this.lng = lng;
    }

    final double lat;
    final double lng;
}
package sortingengine.engine.data.item;

import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DeviceData(@JsonProperty("make") String make, @JsonProperty("model") String model)
{
    public static DeviceData forFile(JpegImageMetadata jpeg)
    {
        final String make = PhotoDataHelper.getFieldValue(jpeg, TiffTagConstants.TIFF_TAG_MAKE, String.class);
        final String model = PhotoDataHelper.getFieldValue(jpeg, TiffTagConstants.TIFF_TAG_MODEL, String.class);
        // final String lngRef = PhotoDataHelper.getFieldValue(jpeg, GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF, String.class);
        
        // final RationalNumber[] lat = PhotoDataHelper.getFieldValue(jpeg, GpsTagConstants.GPS_TAG_GPS_LATITUDE, RationalNumber[].class);
        // final RationalNumber[] lng = PhotoDataHelper.getFieldValue(jpeg, GpsTagConstants.GPS_TAG_GPS_LONGITUDE, RationalNumber[].class);

        if (make == null || model == null)
        {
            return null;
        }

        return new DeviceData(make, model);
    }
}

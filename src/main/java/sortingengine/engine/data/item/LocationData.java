package sortingengine.engine.data.item;

import java.util.Arrays;

import javax.annotation.Nullable;

import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationData
{
    @Nullable
    public static LocationData forFile(JpegImageMetadata jpeg)
    {
        final String latRef = PhotoDataHelper.getFieldValue(jpeg, GpsTagConstants.GPS_TAG_GPS_LATITUDE_REF, String.class);
        final String lngRef = PhotoDataHelper.getFieldValue(jpeg, GpsTagConstants.GPS_TAG_GPS_LONGITUDE_REF, String.class);
        
        final RationalNumber[] lat = PhotoDataHelper.getFieldValue(jpeg, GpsTagConstants.GPS_TAG_GPS_LATITUDE, RationalNumber[].class);
        final RationalNumber[] lng = PhotoDataHelper.getFieldValue(jpeg, GpsTagConstants.GPS_TAG_GPS_LONGITUDE, RationalNumber[].class);

        if (lat == null || lng == null || latRef == null || lngRef == null)
        {
            return null;
        }

        return new LocationData(latRef, lngRef, Arrays.stream(lat).mapToDouble(r -> r.doubleValue()).toArray(), Arrays.stream(lng).mapToDouble(r -> r.doubleValue()).toArray());
    }

    public LocationData(
        @JsonProperty("latRef") String latRef,
        @JsonProperty("lngRef") String lngRef,
        @JsonProperty("lat") double[] lat,
        @JsonProperty("lng") double[] lng
    )
    {
        this.latRef = latRef;
        this.lngRef = lngRef;
        this.lat = lat;
        this.lng = lng;
    }

    final String latRef;
    final String lngRef;
    
    final double[] lat;
    final double[] lng;
}
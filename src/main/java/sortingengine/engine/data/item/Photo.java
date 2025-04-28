package sortingengine.engine.data.item;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.annotation.Nullable;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;

public class Photo extends Item
{
    private String dateTaken;

    // "2024:07:09 13:26:57"
    private static final DateTimeFormatter EXIF_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy:MM:dd hh:mm:ss");
    
    public LocalDateTime getDateTaken()
    {
        return LocalDateTime.parse(dateTaken, EXIF_DATE_TIME_FORMATTER);
    }

    @Nullable
    private LocationData locationTaken;

    public Photo(UUID uuid)
    {
        super(uuid);
    }

    @Nullable
    public static Photo tryCreateFromFile(UUID uuid, Path path)
    {
        try
        {
            final ImageMetadata metadata = Imaging.getMetadata(path.toFile());
            if (metadata instanceof JpegImageMetadata jpeg)
            {
                Photo photo = new Photo(uuid);
                
                photo.locationTaken = LocationData.forFile(jpeg);
                photo.dateTaken = PhotoDataHelper.getFieldValue(jpeg, ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, String.class);

                return photo;
            }

            return null;
        }
        catch (IOException e)
        {
            return null;
        }
    }
}

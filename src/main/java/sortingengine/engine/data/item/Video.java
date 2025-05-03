package sortingengine.engine.data.item;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Nullable;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.ExifSubIFDDescriptor;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.fasterxml.jackson.annotation.JsonProperty;

import sortingengine.engine.data.item.interfaces.AbstractDateLocationItem;

public class Video extends AbstractDateLocationItem
{
    public static final String[] VIDEO_FILE_EXTENSIONS = {".mov", ".mkv", ".avi", ".m4v", ".mp4", ".webm"};

    public Video(@JsonProperty("uuid") UUID uuid)
    {
        super(uuid);
    }

    @Nullable
    public static Video tryCreateFromFile(UUID uuid, Path path)
    {
        if (Arrays.stream(VIDEO_FILE_EXTENSIONS).anyMatch(extension -> path.getFileName().toString().toLowerCase().endsWith(extension)))
        {
            try
            {
                Video video = new Video(uuid);

                Metadata metadata = ImageMetadataReader.readMetadata(path.toFile());

                video.setDateTakenFromVideoMetadata(metadata);
                video.setLocationFromImageMetadata(metadata);

                return video;
            }
            catch (Exception e)
            {
                return null;
            }
        }

        return null;
    }
}

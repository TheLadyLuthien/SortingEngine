package sortingengine.engine.data.item;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Nullable;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.mov.metadata.QuickTimeMetadataDirectory;
import com.drew.metadata.mp4.Mp4Directory;
import com.fasterxml.jackson.annotation.JsonProperty;

import sortingengine.engine.Engine;
import sortingengine.engine.data.item.interfaces.AbstractDateLocationItem;
import sortingengine.engine.data.item.interfaces.TimestampedItem;

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
                video.setLocationFromVideoMetadata(metadata);

                return video;
            }
            catch (Exception e)
            {
                return null;
            }
        }

        return null;
    }

    protected boolean setDateTakenFromVideoMetadata(Metadata metadata)
    {
        if (trySetDateTakenFromMetadataDirectory(metadata, QuickTimeMetadataDirectory.class, QuickTimeMetadataDirectory.TAG_CREATION_DATE, TimestampedItem.QT_DATE_TIME_FORMATTER))
        {
            return true;
        }

        if (trySetDateTakenFromMetadataDirectory(metadata, Mp4Directory.class, Mp4Directory.TAG_CREATION_TIME, TimestampedItem.MP4_DATE_TIME_FORMATTER))
        {
            return true;
        }

        return false;
    }

    private <T extends Directory> boolean trySetDateTakenFromMetadataDirectory(Metadata metadata, Class<T> clazz, int key, DateTimeFormatter dtf)
    {
        final Collection<T> directories = metadata.getDirectoriesOfType(clazz);
        for (T dir : directories)
        {
            var videoDT = dir.getString(key);

            if (videoDT != null)
            {
                try
                {
                    // reformat to match image's EXIF format
                    this.dateTaken = LocalDateTime.parse(videoDT, dtf).format(EXIF_DATE_TIME_FORMATTER);
                    return true;
                }
                catch (DateTimeParseException e)
                {
                    Engine.LOGGER.debug("Error parsing datetime string '" + videoDT + "'" , e);
                }
            }
        }

        return false;
    }

    // TODO: add quicktime support
    // since mp4's never end up with location data
    protected void setLocationFromVideoMetadata(Metadata metadata)
    {
        // final Mp4Directory mp4Directory = metadata.getFirstDirectoryOfType(Mp4Directory.class);
        // if (mp4Directory != null)
        // {
        // // String = mp4Directory.getString(Mp4Directory.TAG_LATITUDE);
        // // String videoDT = mp4Directory.getString(Mp4Directory.TAG_LONGITUDE);

        // // reformat to match image's EXIF format
        // // this.locationTaken = LocationData.fromExifMetadata(metadata);
        // }
    }
}

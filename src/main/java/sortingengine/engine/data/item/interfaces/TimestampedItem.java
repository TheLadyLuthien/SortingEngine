package sortingengine.engine.data.item.interfaces;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public interface TimestampedItem {
    // "2024:07:09 13:26:57"
    public DateTimeFormatter EXIF_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss").withResolverStyle(ResolverStyle.LENIENT);

    LocalDateTime getDateTaken();
}

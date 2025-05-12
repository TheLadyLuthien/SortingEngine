package sortingengine.engine.data.item.interfaces;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public interface TimestampedItem
{
    // "2024:07:09 13:26:57"
    DateTimeFormatter EXIF_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss").withResolverStyle(ResolverStyle.LENIENT);
    
    // 2024-07-06T21:35:26+0100
    DateTimeFormatter QT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX").withResolverStyle(ResolverStyle.LENIENT);
    // DateTimeFormatter QT_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    
    // "Tue Jul 23 15:53:22 PDT 2024"
    DateTimeFormatter MP4_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy").withResolverStyle(ResolverStyle.LENIENT);
    // DateTimeFormatter MP4_DATE_TIME_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;

    
    LocalDateTime getDateTaken();
}

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
    
    // "Tue Jul 23 15:53:22 PDT 2024"
    DateTimeFormatter MP4_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy").withResolverStyle(ResolverStyle.LENIENT);
    
    LocalDateTime getDateTaken();
}

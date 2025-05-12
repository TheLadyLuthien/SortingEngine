package sortingengine.engine.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAccessor;

import org.junit.jupiter.api.Test;

import sortingengine.engine.data.item.interfaces.TimestampedItem;

public class DateTimeTests
{
    @Test
    void MetadataMp4Parsing()
    {
        LocalDateTime ldt = LocalDateTime.parse("Tue Jul 23 15:53:22 PDT 2024", TimestampedItem.MP4_DATE_TIME_FORMATTER);
        assertNotNull(ldt);
        assertEquals(2024, ldt.getYear());
        assertEquals(53, ldt.getMinute());
        assertEquals(Month.JULY, ldt.getMonth());
    }

    @Test
    void MetadataQtParsing()
    {
        LocalDateTime ldt = LocalDateTime.parse("2024-07-06T21:35:26+0100", TimestampedItem.QT_DATE_TIME_FORMATTER);
        
        assertNotNull(ldt);
        assertEquals(2024, ldt.getYear());
        assertEquals(35, ldt.getMinute());
        assertEquals(Month.JULY, ldt.getMonth());
    }
}

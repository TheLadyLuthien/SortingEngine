package sortingengine.engine.data.item;

import javax.annotation.Nullable;

import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;

public class PhotoDataHelper {

    @Nullable
    @SuppressWarnings("unchecked")
    static <T> T getFieldValue(JpegImageMetadata jpeg, TagInfo tag, Class<T> clazz)
    {
        try
        {
            final TiffField field = jpeg.findExifValueWithExactMatch(tag);
            Object o = field.getValue();
    
            if (clazz.isAssignableFrom(o.getClass()))
            {
                return (T)o;
            }
            else
            {
                return null;
            }
        }
        catch (ImagingException e)
        {
            return null;
        }
    }
    
}

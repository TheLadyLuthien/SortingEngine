package sortingengine.engine.post;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import sortingengine.engine.Engine;
import sortingengine.engine.data.TagSet;
import sortingengine.engine.data.item.DeviceData;
import sortingengine.engine.data.item.Item;
import sortingengine.engine.data.item.Photo;
import sortingengine.engine.data.item.interfaces.DeviceMarkedItem;
import sortingengine.engine.data.tag.Tag;
import sortingengine.engine.data.tag.TagCatagory;
import sortingengine.engine.data.tag.TagCatagory;

// @JsonTypeName("DeviceTagger")
public class DeviceTaggerPostProcessor implements ImportPostProcessor
{
    public static final record DeviceDataTagInfo(@JsonProperty("device") DeviceData device, @JsonProperty("tags") String[] tags)
    {
    }

    public final List<DeviceDataTagInfo> deviceTagMap;

    public DeviceTaggerPostProcessor(@JsonProperty("deviceTagMap") List<DeviceDataTagInfo> deviceTagMap)
    {
        this.deviceTagMap = deviceTagMap;
    }

    @Override
    public void apply(Item item, Path path, Engine engine)
    {
        if (item instanceof DeviceMarkedItem photo)
        {
            List<String[]> tags = deviceTagMap.stream().filter(ddti -> ddti.device().equals(photo.getDeviceData())).map(ddti -> ddti.tags()).toList();
            final TagSet tagSet = item.getTags(TagCatagory.SOURCE);

            for (String[] ta : tags)
            {
                for (String tagPath : ta)
                {
                    if (tagPath != null)
                    {
                        Tag tag = engine.createTag(TagCatagory.SOURCE, tagPath);

                        tagSet.add(tag);
                    }
                }
            }
        }
    }
}

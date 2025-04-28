package sortingengine.engine.post;

import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import sortingengine.engine.Engine;
import sortingengine.engine.data.item.Item;
import sortingengine.engine.data.item.Photo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = DeviceTaggerPostProcessor.class, name = "DeviceTagger")})
public interface ImportPostProcessor
{
    void apply(Item item, Path path, Engine engine);
}

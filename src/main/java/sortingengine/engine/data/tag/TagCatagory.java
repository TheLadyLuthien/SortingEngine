package sortingengine.engine.data.tag;

import java.util.Arrays;

import javax.annotation.Nullable;

public enum TagCatagory
{
    LOCATION("location", "lt"),
    CONTENT("content", "ct"),
    DATE("date", "dt"),
    SOURCE("source", "st");

    public final String name;
    public final String selector;

    TagCatagory(String name, String selector)
    {
        this.name = name;
        this.selector = selector;
    }

    @Nullable
    public static TagCatagory bySelector(String selector)
    {
        return Arrays.stream(values()).filter(v -> v.selector.equals(selector)).findFirst().orElseGet(null);
    }
}

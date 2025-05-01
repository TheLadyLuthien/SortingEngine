package sortingengine.engine.data.tag;

import javax.annotation.Nullable;

public record TagWithCatagory(Tag tag, @Nullable TagCatagory catagory)
{
}

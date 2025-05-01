package sortingengine.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import sortingengine.command.CommandSource;
import sortingengine.engine.data.tag.Tag;
import sortingengine.engine.data.tag.TagCatagory;
import sortingengine.engine.data.tag.TagWithCatagory;

public class TagArgumentType implements ArgumentType<TagWithCatagory>
{
    public static TagArgumentType tagWithCategory()
    {
        return new TagArgumentType(true);
    }
    public static TagArgumentType tagWithoutCategory()
    {
        return new TagArgumentType(false);
    }

    public static TagWithCatagory getTagWithCatagory(CommandContext<CommandSource> context, String name)
    {
        return context.getArgument(name, TagWithCatagory.class);
    }

    public static Tag getTagWithoutCatagory(CommandContext<CommandSource> context, String name)
    {
        return context.getArgument(name, TagWithCatagory.class).tag();
    }

    private final boolean useCategory;

    private TagArgumentType(boolean useCategory)
    {
        this.useCategory = useCategory;
    }

    @Override
    public TagWithCatagory parse(StringReader reader) throws CommandSyntaxException
    {
        if (useCategory)
        {
            String[] parts = reader.getString().split(":");
            return new TagWithCatagory(Tag.of(parts[1]), TagCatagory.bySelector(parts[0]));
        }
        else
        {
            String tag = reader.getString();
            return new TagWithCatagory(Tag.of(tag), null);
        }
    }
}

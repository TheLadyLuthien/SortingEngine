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

    private static String readTagPath(StringReader reader) throws CommandSyntaxException
    {
        final StringBuilder result = new StringBuilder();
        boolean escaped = false;
        while (reader.canRead())
        {
            final char c = reader.read();
            if (escaped)
            {
                if (c == ' ' || c == '\\')
                {
                    result.append(c);
                    escaped = false;
                }
                else
                {
                    reader.setCursor(reader.getCursor() - 1);
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidEscape().create(String.valueOf(c));
                }
            }
            else if (c == '\\')
            {
                escaped = true;
            }
            else if (c == ' ')
            {
                return result.toString();
            }
            else
            {
                result.append(c);
            }
        }
        return result.toString();
    }

    @Override
    public TagWithCatagory parse(StringReader reader) throws CommandSyntaxException
    {
        if (useCategory)
        {
            String[] parts = readTagPath(reader).split(":");
            String catagorySelector = parts[0];
            String tagPath = parts[1];
            return new TagWithCatagory(Tag.of(tagPath), TagCatagory.bySelector(catagorySelector));
        }
        else
        {
            String tag = reader.readString();
            return new TagWithCatagory(Tag.of(tag), null);
        }
    }
}

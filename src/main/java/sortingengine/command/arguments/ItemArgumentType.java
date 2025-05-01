package sortingengine.command.arguments;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import sortingengine.command.CommandSource;
import sortingengine.engine.data.item.Item;
import sortingengine.engine.filter.ItemFilter;
import sortingengine.engine.filter.ItemFilterParser;

public class ItemArgumentType implements ArgumentType<ItemFilter>
{
    public static final SimpleCommandExceptionType NOT_ALLOWED_EXCEPTION = new SimpleCommandExceptionType(() -> "Invalid Item filter declaration");

    public static ItemArgumentType items()
    {
        return new ItemArgumentType();
    }

    public static List<Item> getItems(CommandContext<CommandSource> context, String name)
    {
        final ItemFilter filter = context.getArgument(name, ItemFilter.class);
        return context.getSource().getEngine().getItemRecordDb().findItems(filter).toList();
    }

    @Override
    public ItemFilter parse(StringReader reader) throws CommandSyntaxException
    {
        if (reader.peek() == '[')
        {
            reader.skip();

            String value = reader.readStringUntil(']');
            return ItemFilterParser.parse(value);
        }
        else
        {
            throw NOT_ALLOWED_EXCEPTION.create();
        }
    }

    @Override
    public Collection<String> getExamples()
    {
        return ArgumentType.super.getExamples();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder)
    {
        return ArgumentType.super.listSuggestions(context, builder);
    }

}

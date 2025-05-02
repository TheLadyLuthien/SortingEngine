package sortingengine.command;

import com.mojang.brigadier.CommandDispatcher;

import sortingengine.command.arguments.ItemArgumentType;
import sortingengine.command.arguments.TagArgumentType;
import sortingengine.command.commands.TagCommand;
import sortingengine.engine.Engine;
import sortingengine.engine.data.item.Item;
import sortingengine.engine.data.tag.TagWithCatagory;

import static sortingengine.command.CommandHelper.*;

import java.util.List;

public class Commands
{
    public static void register(CommandDispatcher<CommandSource> commandDispatcher)
    {
        TagCommand.register(commandDispatcher);

        commandDispatcher.register(
            literal("info")
            .then(
                literal("item_count")
                .executes(c -> {
                    CommandSource source = c.getSource();
                    int size = source.getEngine().getItemRecordDb().size();
                    
                    source.sendFeedback(() -> size + " items present in item record db.");
                    
                    return size;
                })
            )
        );

        commandDispatcher.register(
            literal("countof")
            .then(
                argument("items", ItemArgumentType.items())
                .executes(c -> {
                    List<Item> items = ItemArgumentType.getItems(c, "items");
                    c.getSource().sendFeedback(() -> "Found " + items.size() + " items");
                    return items.size();
                })
                // literal("item_count")
                // .executes(c -> {
                //     CommandSource source = c.getSource();
                //     int size = source.getEngine().getItemRecordDb().size();
                    
                //     source.sendFeedback(() -> size + " items present in item record db.");
                    
                //     return size;
                // })
            )
        );
    }
}

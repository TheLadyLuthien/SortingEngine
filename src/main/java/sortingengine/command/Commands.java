package sortingengine.command;

import com.mojang.brigadier.CommandDispatcher;

import sortingengine.command.arguments.ItemArgumentType;
import sortingengine.command.arguments.TagArgumentType;
import sortingengine.engine.Engine;
import sortingengine.engine.data.item.Item;
import sortingengine.engine.data.tag.TagWithCatagory;

import static sortingengine.command.CommandHelper.*;

import java.util.List;

public class Commands
{
    public static void register(CommandDispatcher<CommandSource> commandDispatcher)
    {
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

        commandDispatcher.register(
            literal("tag")
            .then(
                argument("items", ItemArgumentType.items())
                .then(
                    argument("tag", TagArgumentType.tagWithCategory())
                    .executes(c -> {
                        final Engine engine = c.getSource().getEngine();

                        List<Item> items = ItemArgumentType.getItems(c, "items");
                        TagWithCatagory twc = TagArgumentType.getTagWithCatagory(c, "tag");
                        
                        for (Item item : items) {
                            item.getTags(twc.catagory()).add(engine.ensureTag(twc));
                        }

                        c.getSource().sendFeedback(() -> "Tagged " + items.size() + " items with " + twc.catagory().selector + ":" + twc.tag().path);
                        return items.size();
                    })
                )
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

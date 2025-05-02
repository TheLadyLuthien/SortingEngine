package sortingengine.command.commands;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import sortingengine.command.CommandSource;
import sortingengine.command.arguments.ItemArgumentType;
import sortingengine.command.arguments.TagArgumentType;
import sortingengine.engine.Engine;
import sortingengine.engine.data.item.Item;
import sortingengine.engine.data.tag.TagWithCatagory;

import static sortingengine.command.CommandHelper.*;

public class TagCommand
{
    public static void register(CommandDispatcher<CommandSource> dispatcher)
    {
        dispatcher.register(
            literal("tag")
            .then(
                argument("items", ItemArgumentType.items())
                .then(
                    literal("add")
                    .then(
                        argument("tag", TagArgumentType.tagWithCategory())
                        .executes(TagCommand::executeAddTag)
                    )
                )
                .then(
                    literal("remove")
                    .then(
                        literal("sub")
                        .then(
                            argument("tag", TagArgumentType.tagWithCategory())
                            .executes(TagCommand::executeRemoveSubTag)
                        )
                    )
                    .then(
                        literal("entire")
                        .then(
                            argument("tag", TagArgumentType.tagWithCategory())
                            .executes(TagCommand::executeRemoveEntireTag)
                        )
                    )
                    .then(
                        literal("all")
                        .executes(TagCommand::executeRemoveAllTags)
                    )
                )
                .then(
                    argument("tag", TagArgumentType.tagWithCategory())
                    .executes(TagCommand::executeAddTag)
                )
            )
        );
    }

    private static int executeAddTag(CommandContext<CommandSource> c)
    {
        final Engine engine = c.getSource().getEngine();

        List<Item> items = ItemArgumentType.getItems(c, "items");
        TagWithCatagory twc = TagArgumentType.getTagWithCatagory(c, "tag");
        
        for (Item item : items) {
            item.getTags(twc.catagory()).add(engine.ensureTag(twc));
        }

        c.getSource().sendFeedback(() -> "Tagged " + items.size() + " items with " + twc.catagory().selector + ":" + twc.tag().path);
        return items.size();
    }

    private static int executeRemoveSubTag(CommandContext<CommandSource> c)
    {
        List<Item> items = ItemArgumentType.getItems(c, "items");
        TagWithCatagory twc = TagArgumentType.getTagWithCatagory(c, "tag");
        
        for (Item item : items) {
            item.getTags(twc.catagory()).removeSubTag(twc.tag());
        }

        if (twc.tag().hasParent())
        {
            c.getSource().sendFeedback(() -> "Removed " + twc.catagory().selector + ":" + twc.tag().path + " (and replaced with " + twc.catagory().selector + ":" + twc.tag().getParentTag().path + " if applicable) from " + items.size() + " items");
        }
        else
        {
            c.getSource().sendFeedback(() -> "Removed root tag " + twc.catagory().selector + ":" + twc.tag().path + " from " + items.size() + " items");
        }
        return items.size();
    }

    private static int executeRemoveEntireTag(CommandContext<CommandSource> c)
    {
        List<Item> items = ItemArgumentType.getItems(c, "items");
        TagWithCatagory twc = TagArgumentType.getTagWithCatagory(c, "tag");
        
        for (Item item : items) {
            item.getTags(twc.catagory()).removeEntireTag(twc.tag());
        }

        c.getSource().sendFeedback(() -> "Removed entire tag " + twc.catagory().selector + ":" + twc.tag().path + " from " + items.size() + " items");
        return items.size();
    }

    private static int executeRemoveAllTags(CommandContext<CommandSource> c)
    {
        List<Item> items = ItemArgumentType.getItems(c, "items");
        
        for (Item item : items) {
            item.removeAllTags();
        }

        c.getSource().sendFeedback(() -> "Removed all tags from " + items.size() + " items");
        return items.size();
    }
}

package sortingengine.command;

import com.mojang.brigadier.CommandDispatcher;

import static sortingengine.command.CommandHelper.*;

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
    }
}

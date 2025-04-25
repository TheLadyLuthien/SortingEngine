package sortingengine.test;

import java.nio.file.Path;
import java.util.Scanner;

import com.mojang.brigadier.CommandDispatcher;

import sortingengine.command.CommandSource;
import sortingengine.command.Commands;
import sortingengine.conf.LaunchConfig;
import sortingengine.data.tag.TagCatagories;

public class CmdApp
{
    public static void main(String[] args) throws Exception
    {
        final String launchConfigPath = args.length > 0 ? args[0] : LaunchConfig.DEFAULT_CONFIG_PATH;
        LaunchConfig.load(Path.of(launchConfigPath));

        TagCatagories.init();
        
        final CommandDispatcher<CommandSource> commandDispatcher = new CommandDispatcher<>();
        Commands.register(commandDispatcher);


        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        while (!exit)
        {
            String line = scanner.nextLine();

        }
    }
}

package sortingengine.test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import sortingengine.command.CommandHelper;
import sortingengine.command.CommandSource;
import sortingengine.command.Commands;
import sortingengine.conf.LaunchConfig;
import sortingengine.conf.RuntimeConfig;
import sortingengine.engine.Engine;
import sortingengine.engine.FileHelper;
import sortingengine.engine.data.tag.TagCatagory;

public class CmdApp
{
    private static boolean exit = false;
    private static final Logger LOGGER = LoggerFactory.getLogger("CmdApp");


    private static final Engine engine = new Engine();

    public static void main(String[] args) throws Exception
    {
        LOGGER.info("Command Testing App");
        
        final String launchConfigPath = args.length > 0 ? args[0] : LaunchConfig.DEFAULT_CONFIG_PATH;
        LaunchConfig.load(Path.of(launchConfigPath));
        
        RuntimeConfig.loadAllEntries();

        FileHelper.ensureBasicFileStructure();

        engine.loadAndReplaceDatabases();

        final CommandDispatcher<CommandSource> commandDispatcher = new CommandDispatcher<>();
        Commands.register(commandDispatcher);

        registerSpecialCommands(commandDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (!exit)
        {
            try
            {
                String line = scanner.nextLine();
                commandDispatcher.execute(line, new CommandSource(engine));
            }
            catch (Exception e)
            {
                LOGGER.error("error executing command", e);
            }
        }

        scanner.close();
    }

    private static void registerSpecialCommands(CommandDispatcher<CommandSource> commandDispatcher)
    {
        commandDispatcher.register(CommandHelper.literal("exit").executes(c -> {
            exit = true;
            return 0;
        }));

        commandDispatcher.register(CommandHelper.literal("import").then(CommandHelper.literal("folder").then(CommandHelper.argument("path", StringArgumentType.greedyString()).executes(c -> {
            try
            {
                engine.onboardImportFolder(Path.of(StringArgumentType.getString(c, "path")));
            }
            catch (IOException e)
            {
                return -1;
            }
            return 0;
        }))));

        commandDispatcher.register(CommandHelper.literal("db").then(CommandHelper.literal("save").executes(c -> {
            try
            {
                engine.saveDatabases();
            }
            catch (Exception e)
            {
                LOGGER.error("Failed to save db", e);
                return -1;
            }
            return 0;
        })).then(CommandHelper.literal("load").executes(c -> {
            engine.loadAndReplaceDatabases();
            return 0;
        })));
    }
}

package sortingengine.command;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sortingengine.engine.Engine;

public class CommandSource
{
    private final Engine engine;

    private static final Logger LOGGER = LoggerFactory.getLogger("Command Output");

    public Engine getEngine()
    {
        return engine;
    }

    public CommandSource(Engine engine)
    {
        this.engine = engine;
    }

    public void sendFeedback(Supplier<String> feedbackSupplier)
    {
        // TODO: actual system
        LOGGER.info(feedbackSupplier.get());
    }
}

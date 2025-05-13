package sortingengine.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerHelper
{
    public static Logger getLogger(String name)
    {
        return LoggerFactory.getLogger("sortingengine." + name);
    }
}

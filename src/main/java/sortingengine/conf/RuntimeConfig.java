package sortingengine.conf;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import sortingengine.engine.FileHelper;
import sortingengine.engine.ImportPostProcessor;

public class RuntimeConfig
{
    private static final Set<Entry<?>> entries = new HashSet<>();
    

    public static final Entry<ArrayList<ImportPostProcessor>> IMPORT_POST_PROCESSORS = register("import_post_processors", new ArrayList<ImportPostProcessor>());
    

    private static final Logger LOGGER = LoggerFactory.getLogger("Runtime Config");

    public static void loadAllEntries()
    {
        for (Entry<?> entry : entries)
        {
            try
            {
                entry.loadValue();
            }
            catch (IOException e)
            {
                LOGGER.warn("No config file for {}", entry.fileName);
                LOGGER.debug("IOException thrown!", e);
            }
        }
    }

    public static <T> Entry<T> register(String name, T defaultValue)
    {
        Entry<T> entry = new Entry<T>(name, defaultValue);
        entries.add(entry);
        return entry;
    }

    public static class Entry<T>
    {
        public Entry(String fileName, T value)
        {
            this.fileName = fileName;
            this.value = value;
        }

        private final String fileName;
        private T value;

        public void writeToFile() throws IOException
        {
            ObjectMapper mapper = new ObjectMapper();
            String data = mapper.writeValueAsString(value);

            FileHelper.writeStringAsFile(data, Path.of(FileHelper.CONFIG_ROOT, fileName + ".json"));
        }

        private static <T> T readFromFile(String fileName) throws IOException
        {
            ObjectMapper mapper = new ObjectMapper();

            String data = FileHelper.readFileAsString(Path.of(FileHelper.CONFIG_ROOT, fileName + ".json"));
            T entry = mapper.readValue(data, new TypeReference<T>(){});

            return entry;
        }

        protected void loadValue() throws IOException
        {
            this.value = readFromFile(this.fileName);
        }

        public T getValue()
        {
            return value;
        }
    }
}

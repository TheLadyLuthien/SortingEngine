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
import sortingengine.engine.post.ImportPostProcessor;

public class RuntimeConfig
{
    private static final Set<Entry<?>> entries = new HashSet<>();
    

    public static final Entry<ArrayList<ImportPostProcessor>> IMPORT_POST_PROCESSORS = register("import_post_processors", new ArrayList<ImportPostProcessor>(), new TypeReference<ArrayList<ImportPostProcessor>>() {});
    

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

    public static <T> Entry<T> register(String name, T defaultValue, @SuppressWarnings("rawtypes") TypeReference type)
    {
        Entry<T> entry = new Entry<T>(name, defaultValue, type);
        entries.add(entry);
        return entry;
    }

    public static class Entry<T>
    {
        public Entry(String fileName, T value, @SuppressWarnings("rawtypes") TypeReference typeReference)
        {
            this.fileName = fileName;
            this.value = value;
            this.typeReference = typeReference;
        }

        private final String fileName;
        
        @SuppressWarnings("rawtypes")
        private final TypeReference typeReference;
        private T value;

        public void writeToFile() throws IOException
        {
            ObjectMapper mapper = new ObjectMapper();
            String data = mapper.writeValueAsString(value);

            FileHelper.writeStringAsFile(data, Path.of(FileHelper.CONFIG_ROOT, fileName + ".json"));
        }

        private static <T> T readFromFile(Entry<T> entry) throws IOException
        {
            ObjectMapper mapper = new ObjectMapper();

            String data = FileHelper.readFileAsString(Path.of(FileHelper.CONFIG_ROOT, entry.fileName + ".json"));
            T value = mapper.readValue(data, entry.typeReference);

            return value;
        }

        protected void loadValue() throws IOException
        {
            this.value = readFromFile(this);
        }

        public T getValue()
        {
            return value;
        }
    }
}

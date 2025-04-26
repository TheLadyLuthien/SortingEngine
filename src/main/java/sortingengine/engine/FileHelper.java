package sortingengine.engine;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FileHelper
{
    public static final String REPOSITORY_ROOT = "items/repository";
    public static final String ITEM_RECORD_FILE = REPOSITORY_ROOT + "/items.json";
    public static final String FILE_LOOKUP_FILE = REPOSITORY_ROOT + "/files.json";
    
    private static final String FILE_STRUCTURE_TEMPLATE_RESOURCE_PATH = "runFileStructure.json";

    private static final Gson GSON = new Gson();

    public static void ensureBasicFileStructure() throws IOException
    {
        URL url = Resources.getResource(FILE_STRUCTURE_TEMPLATE_RESOURCE_PATH);
        String text = Resources.toString(url, StandardCharsets.UTF_8);

        final var collectionType = new TypeToken<HashMap<String, Object>>()
        {}.getType();
        final HashMap<String, Object> map = GSON.fromJson(text, collectionType);

        recurseFileStructureTemplate(Path.of(""), map);
        // foo.toString();

        // (FileHelper.class.getResource(FILE_STRUCTURE_TEMPLATE_RESOURCE_PATH).toURI());
        // Files.createDirectories(Path.of(""), null)
    }

    private static void recurseFileStructureTemplate(Path path, Map<String, Object> map) throws IOException
    {
        for (String folder : map.keySet())
        {
            Path p = path.resolve(folder);
            if (!Files.isDirectory(p))
            {
                Files.createDirectory(p);
            }

            Object value = map.get(folder);
            if (value instanceof Map)
            {
                recurseFileStructureTemplate(p, (Map<String, Object>)value);
            }
        }
    }

    public static void copyDirectoryAndContents(Path sourceDir, Path dest) throws IOException
    {
        Files.walk(sourceDir).forEach(sourcePath -> {
            try
            {
                Path targetPath = dest.resolve(sourceDir.relativize(sourcePath));
                Files.createDirectories(targetPath.getParent());
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException e)
            {
                Engine.FILESYSTEM_LOGGER.error("Failed to copy directory", e);
            }
        });
    }

    public static void writeStringAsFile(String data, Path path) throws IOException
    {
        Files.write(path, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static String readFileAsString(Path path) throws IOException
    {
        return new String(Files.readAllBytes(path));
    }
}

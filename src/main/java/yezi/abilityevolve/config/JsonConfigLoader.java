package yezi.abilityevolve.config;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonConfigLoader {
    public static Map<String, Map<String, List<String>>> loadJsonConfig(String filename, String defaultContent) {
        File file = FMLPaths.CONFIGDIR.get().resolve(filename).toFile();
        if (!file.exists()) {
            createDefaultJsonFile(file, defaultContent);
        }

        try (FileReader reader = new FileReader(file)) {
            Type mapType = new TypeToken<Map<String, Map<String, List<String>>>>() {}.getType();
            Map<String, Map<String, List<String>>> result = new Gson().fromJson(JsonParser.parseReader(reader), mapType);

            return result != null ? result : new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }


    private static void createDefaultJsonFile(File file, String content) {
        try {
            file.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


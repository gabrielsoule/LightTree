package com.gabrielsoule.lighttree;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private Map<String, String> keybinds;

    public Config(String filePath) {
        Yaml yaml = new Yaml();
        keybinds = new HashMap<>();
        Map<String, Object> yamlObject;
        try {
            yamlObject = yaml.load(new FileInputStream(new File(filePath)));
            keybinds = (Map<String, String>) yamlObject.get("general-keybinds");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find configuration file at " + filePath);
        } catch (Exception e) {
            System.out.println("An unknown error occurred:");
            e.printStackTrace();
        }
        finally {
            System.out.println("Failed to read configuration file. Exiting...");
        }
    }

    public String getBinding(String key) {
        return keybinds.get(key);
    }
}

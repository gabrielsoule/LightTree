package com.gabrielsoule.lighttree;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private Map<String, String> keybinds;
    private Map<String, Color> colors;
    private Map<String, Object> yamlObject;

    public Config(String filePath) {
        Yaml yaml = new Yaml();
        keybinds = new HashMap<>();
        colors = new HashMap<>();
        try {
            this.yamlObject = yaml.load(new FileInputStream(new File(filePath)));
            System.out.println(yamlObject.get("general-keybinds"));
            keybinds = (Map<String, String>) yamlObject.get("general-keybinds");
            Map<String, String> hexColors = (Map<String, String>) yamlObject.get("color-names");
            for(String key : hexColors.keySet()) {
                colors.put(key, new Color(Integer.decode("0x"+ hexColors.get(key))));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find configuration file at " + filePath);
        } catch (Exception e) {
            System.out.println("An unknown error occurred:");
            e.printStackTrace();
        }
    }

    public Map<String, Object> getYamlObject() {
        return yamlObject;
    }

    public String getBinding(String key) {
        return keybinds.get(key);
    }

//    public class EffectConfiguration {
//        public Color[] colors;
//        public int[] options;
//        public Class clazz;
//
//        public EffectConfiguration(String className, int[] options, Color[] colors) {
//            try {
//                this.clazz = Class.forName("com.gabrielsoule.lightTree" + className);
//            } catch (ClassNotFoundException e) {
//                System.out.println("[Error] Unable to locate class com.gabrielsoule.lighttree." + className + ", did you write the wrong class name in the configuration file?");
//                e.printStackTrace();
//            }
//            this.colors = colors;
//            this.options = options;
//        }
//
//        public LightEffect instantiateEffect()
//    }
}

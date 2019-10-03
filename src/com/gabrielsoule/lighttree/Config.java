package com.gabrielsoule.lighttree;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Config {
    public Config(String filePath) {
        Yaml yaml = new Yaml();
        try {
            yaml.load(new FileInputStream(new File(filePath)));
        } catch (FileNotFoundException e) {
            System.out.println("Unable to load configuration file " + filePath);
            System.exit(-1);
        }

        
    }
}

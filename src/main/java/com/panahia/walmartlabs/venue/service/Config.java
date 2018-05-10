package com.panahia.walmartlabs.venue.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by aliakbarpanahi on 5/10/18.
 */
public class Config {
    private static Config config;

    public static Config getInstance(){
        if(config == null){
            config = new Config();
        }
        return config;
    }

    private Config() {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            System.out.println(prop.getProperty("venueRowCount"));
            System.out.println(prop.getProperty("venueColumnCount"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

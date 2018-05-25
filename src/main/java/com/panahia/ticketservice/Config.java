package com.panahia.ticketservice;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Ali Panahi on 5/10/18.
 */
public class Config {
    private static Config config;

    private int venueRowCount;

    private int venueColumnCount;

    private int HoldSeatlifeTime;

    private int confirmationCodeLength;

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
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            input = loader.getResourceAsStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            this.venueRowCount = Integer.parseInt(prop.getProperty("venueRowCount"));
            this.venueColumnCount = Integer.parseInt(prop.getProperty("venueColumnCount"));
            this.HoldSeatlifeTime = Integer.parseInt(prop.getProperty("HoldSeatlifeTime"));
            this.confirmationCodeLength = Integer.parseInt(prop.getProperty("confirmationCodeLength"));

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

    public int getConfirmationCodeLength() {
        return confirmationCodeLength;
    }

    public int getVenueRowCount() {
        return venueRowCount;
    }

    public int getVenueColumnCount() {
        return venueColumnCount;
    }

    public int getHoldSeatLifeTime() {
        return HoldSeatlifeTime;
    }
}

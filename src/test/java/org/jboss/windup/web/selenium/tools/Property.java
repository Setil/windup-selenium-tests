package org.jboss.windup.web.selenium.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class Property {
    Properties properties;
    FileInputStream out;
    private static Property instance;
    private String defaultUrl;
    private String login;
    private String pass;
    private String rhamtBaseUrl;
    private long longTimeOut;
    private long timeOut;

    private Property() {
        properties = new Properties();
        try {
            out = new FileInputStream("src/test/java/org/jboss/windup/web/selenium/tools/conf.properties");
            properties.load(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        longTimeOut = Long.parseLong(getProp("LONGTIMEOUT","100000"));
        timeOut = Long.parseLong(getProp("TIMEOUT","10000"));
        rhamtBaseUrl = getProp("baseUrl", "");
    }

    public static Property getInstance() {
        if (instance == null) {
            instance = new Property();
        }
        return instance;
    }

    private String getProp(String nameProp, String defaultProp){
        String valueProp;
        if(System.getenv(nameProp) == null){
            valueProp = properties.getProperty(nameProp, defaultProp);
        } else {
            valueProp = System.getenv(nameProp);
        }
        return valueProp;
    }

    public String getDefaultUrl(){
        return rhamtBaseUrl;
    }

    public long getLongTimeout(){return longTimeOut;}

    public long getTimeOut(){return timeOut;};

}

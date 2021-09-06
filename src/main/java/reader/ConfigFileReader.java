package reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {

    private Properties properties;
    private final String filePath= "src"+System.getProperty("file.separator")+"env.properties";

    public ConfigFileReader(){
        BufferedReader readProperty = null;
        try {
            readProperty = new BufferedReader(new FileReader(filePath));
            properties = new Properties();
            try { properties.load(readProperty); }
            catch (IOException e) { e.getMessage(); }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(filePath+ "File Path not found");
        }finally {
            try { if(readProperty != null) readProperty.close(); }
            catch (IOException ignore) {ignore.getMessage();}
        }
    }

    public String getValueWithKey(String key) {
        String value = properties.getProperty(key);
        if(value != null){return value;} else return "";
    }

}

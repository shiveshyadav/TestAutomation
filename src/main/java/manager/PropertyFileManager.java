package manager;

import reader.ConfigFileReader;

public class PropertyFileManager {

    private static ConfigFileReader configFileReader;
    private static PropertyFileManager propertyFileManager = new PropertyFileManager();

    private PropertyFileManager() {
    }

    public static PropertyFileManager getInstance( ) {
        return propertyFileManager;
    }

    public ConfigFileReader getPropertyFileReader() {
        return (configFileReader == null) ? configFileReader = new ConfigFileReader() : configFileReader;
    }
}



package org.ibs.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropConst {

    public static String getProperty(String key) throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/application.properties");
        prop.load(fis);
        return prop.getProperty(key);
    }

    public static final String BASE_URL = "base.url";

    public static final String PATH_CHROME_DRIVER_MAC = "path.chrome.driver.mac";

}


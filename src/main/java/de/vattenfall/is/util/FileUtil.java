package de.vattenfall.is.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;

public class FileUtil {

    public static String deliverXmlFromFile(String filename) throws Exception {
        StringBuilder result = new StringBuilder();
        URL resource = Thread.currentThread().getContextClassLoader().getResource(filename);
        BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()));
        while (reader.ready()) {
            result.append(reader.readLine()).append('\n');
        }
        reader.close();
        return result.toString();
    }
}

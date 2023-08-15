package se.drutt.iacdemo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Loader
{
    public static String readFile(String path)
    {
        Path p = Path.of(path);
        try
        {
            return Files.readString(p);
        }
        catch (IOException e)
        {
            System.err.println("Failed to open file: " + path + "\n" + e);
            return null;
        }
    }
}

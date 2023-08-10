package se.drutt.iacdemo;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class InfrastructureAsCodeDemoApp
{
    public static final String PROD_CONFIG = "./src/main/resources/prod_config.txt";

    public static void main(final String[] args)
    {
        App app = new App();
        Properties config = null;

        try
        {
            config = loadConfig(PROD_CONFIG);
        }
        catch (IOException e)
        {
            System.err.println("Failed to load config: " + e);
            System.exit(1);
        }

        StackProps props = StackProps.builder().env(Environment.builder()
                    .account(config.getProperty("account"))
                    .region(config.getProperty("region"))
                    .build())
                .build();

        FrontEnd frontEnd = new FrontEnd(app, "FrontEndStack", props, config.getProperty("domainname"));

        WebContent webContent = new WebContent(app, "WebContentStack", props, frontEnd.webBucket);
        webContent.addDependency(frontEnd, "Needs the web bucket to place content inside.");

        app.synth();
    }

    private static Properties loadConfig(String fileName) throws IOException
    {
        File file = new File(fileName);
        if (!file.exists())
            throw new FileNotFoundException("File not found: " + fileName);

        Properties result = new Properties();
        result.load(new FileInputStream(file));
        return result;
    }
}


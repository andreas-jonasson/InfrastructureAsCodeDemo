package se.drutt.iacdemo;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.io.File;
import java.io.IOException;

public class InfrastructureAsCodeDemoApp
{
    public static final String PROD_CONFIG = "./src/main/resources/prod.json";

    public static void main(final String[] args)
    {
        App app = new App();
        Configuration conf = loadConfig(PROD_CONFIG);

        if (conf == null)
            System.exit(1);

        StackProps props = StackProps.builder().env(Environment.builder()
                    .account(conf.ACCOUNT)
                    .region(conf.REGION)
                    .build())
                .build();

        FrontEnd frontEnd = new FrontEnd(app, "FrontEndStack", props, conf);

        WebContent webContent = new WebContent(app, "WebContentStack", props, frontEnd.webBucket);
        webContent.addDependency(frontEnd, "Needs the web bucket to place content inside.");

        app.synth();
    }

    private static Configuration loadConfig(String fileName)
    {
        File file = new File(fileName);
        Configuration result = null;
        if (!file.exists())
        {
            System.err.println("Configuration file does not exists: " + fileName);
            return null;
        }

        try
        {
            result = Configuration.getInstance(fileName);
        }
        catch (IOException e)
        {
            System.err.println("Failed to load configuration from: " + fileName);
        }

        return result;
    }
}


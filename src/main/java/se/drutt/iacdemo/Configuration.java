package se.drutt.iacdemo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class Configuration
{
    public final String DNS_DOMAIN;
    public final String ACCOUNT;
    public final String REGION;
    public final String DNS_SUBDOMAIN;
    public final String FRONTEND_DOMAIN_NAME;
    public final String API_DOMAIN_NAME;
    public final String LOCAL_WEB_DIR;
    public final String HOSTED_ZONE_ID;
    public final String HOSTED_ZONE_ARN;

    public Configuration(String dnsDomain, String account, String region, String dnsSubdomain, String frontendDomainName, String apiDomainName, String localWebDir, String hostedZoneId, String hostedZoneArn) {
        DNS_DOMAIN = dnsDomain;
        ACCOUNT = account;
        REGION = region;
        DNS_SUBDOMAIN = dnsSubdomain;
        FRONTEND_DOMAIN_NAME = frontendDomainName;
        API_DOMAIN_NAME = apiDomainName;
        LOCAL_WEB_DIR = localWebDir;
        HOSTED_ZONE_ID = hostedZoneId;
        HOSTED_ZONE_ARN = hostedZoneArn;
    }

    public static Configuration getInstance(String configPath) throws FileNotFoundException {
        Gson gson = new GsonBuilder().create();
        FileReader fileReader;
        Configuration configuration;

        File configFile = new File(configPath);

        if (!(configFile.exists() && configFile.isFile()))
            throw new FileNotFoundException("Configuration file not found at: " + configPath);

        fileReader = new FileReader(configPath);
        configuration = gson.fromJson(fileReader, Configuration.class);

        return configuration;
    }

    public void save(String path) throws IOException
    {
        File contentFile = new File(path);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        FileWriter fileWriter = new FileWriter(contentFile);
        gson.toJson(this, fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }
}

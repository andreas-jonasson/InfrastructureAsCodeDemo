package se.drutt.iacdemo.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.drutt.iacdemo.Configuration;
import se.drutt.iacdemo.model.Card;

import java.io.File;
import java.io.IOException;

class CardHandlerTest
{
    public static final String PROD_CONFIG = "./src/main/resources/prod.json";
    private static final String TEST_SUBJECT = "TEST";
    private static final int TEST_NUMBER = 1;
    private final CardHandler cardHandler = new CardHandler();
    private final Configuration config = loadConfig(PROD_CONFIG);
    private final Card testCard = new Card("What is 5 times 5?", new String[]{"5", "25", "55"}, new int[]{1});

    @BeforeEach
    void setUp()
    {
        cardHandler.setConfig(config);
    }

    @Test
    void cardHandler_add()
    {
        cardHandler.add(TEST_SUBJECT, TEST_NUMBER, testCard);
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
package se.drutt.iacdemo.lambda;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.drutt.iacdemo.Configuration;
import se.drutt.iacdemo.Loader;
import se.drutt.iacdemo.model.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CardHandlerTest
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String PROD_CONFIG     = "../prod.json";
    private static final String TEST_SUBJECT    = "TEST";
    private static final String CARDS_PATH      = "./src/test/resources/TestCards.json";
    private static final String GATEWAY_REQUEST = "./src/test/resources/APIGateway-CardRequest.json";
    private static final int TEST_NUMBER = 1;
    private final Configuration config = loadConfig(PROD_CONFIG);
    private final CardHandler cardHandler = new CardHandler(config);
    private final Card testCard = new Card("What is 5 times 5?", new String[]{"5", "25", "55"}, new int[]{1});
    private boolean tableDeployed;

    @BeforeEach
    void setUp()
    {
        tableDeployed = true; // TODO Method to check if card-table is deployed.
    }

    @Test
    void addCard_addAndRetrieveIsEqualObject()
    {
        if (!tableDeployed)
            return;

        cardHandler.addCard(TEST_SUBJECT, TEST_NUMBER, testCard);
        String json = cardHandler.getCard(TEST_SUBJECT, TEST_NUMBER);
        assertNotNull(json);
        Card result = Card.getInstance(json);
        assertEquals(testCard, result);
    }

    @Test
    void addCards_addAndRetrieveIsEqualObjects()
    {
        if (!tableDeployed)
            return;

        Cards originalCards = Cards.getInstance(Loader.readFile(CARDS_PATH));
        cardHandler.addCards(TEST_SUBJECT, originalCards);
        String json = cardHandler.getCards(TEST_SUBJECT);
        assertNotNull(json);
        Cards resultCards = Cards.getInstance(json);
        assertEquals(originalCards, resultCards);
    }

    @Test
    void handleRequest_getCard()
    {
        if (!tableDeployed)
            return;

        APIGatewayProxyRequestEvent requestEvent = gson.fromJson(Loader.readFile(GATEWAY_REQUEST), APIGatewayProxyRequestEvent.class);
        APIGatewayProxyResponseEvent responseEvent = cardHandler.handleRequest(requestEvent, new LocalContext());
        CardResponse cardResponse = CardResponse.getInstance(responseEvent.getBody());
        assertEquals(cardResponse.message, "Success");
    }

    @Test
    void handleRequest_getAllCards()
    {
        if (!tableDeployed)
            return;

        APIGatewayProxyRequestEvent requestEvent = gson.fromJson(Loader.readFile(GATEWAY_REQUEST), APIGatewayProxyRequestEvent.class);
        CardRequest cardRequest = new CardRequest(CardRequest.requestTypes.GETALL, TEST_SUBJECT, 0);
        requestEvent.setBody(cardRequest.toJson());
        APIGatewayProxyResponseEvent responseEvent = cardHandler.handleRequest(requestEvent, new LocalContext());
        CardResponse cardResponse = CardResponse.getInstance(responseEvent.getBody());
        assertEquals(cardResponse.message, "Success");
        assertNotNull(cardResponse.cards);
        assertEquals(cardResponse.cards.length, 3);
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
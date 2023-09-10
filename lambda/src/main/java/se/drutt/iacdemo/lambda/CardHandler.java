package se.drutt.iacdemo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import se.drutt.iacdemo.Configuration;
import se.drutt.iacdemo.model.Card;
import se.drutt.iacdemo.model.CardRequest;
import se.drutt.iacdemo.model.CardResponse;
import se.drutt.iacdemo.model.Cards;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Map;

public class CardHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Configuration config;
    private final DynamoDbClient ddb;

    public static void main(final String[] args)
    {
        System.out.println("CardHandler");
    }
    public CardHandler(Configuration config)
    {
        // Use for testing locally
        this.config = config;
        ddb = DynamoDbClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.builder().profileName(config.AWS_CREDENTIALS_PROFILE).build())
                .region(Region.of(config.REGION))
                .build();
    }

    public CardHandler()
    {
        // Use when running in the cloud
        config = getConfigurationFromEnv();
        ddb = DynamoDbClient.builder().build();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context)
    {
        CardResponse response;
        LambdaLogger logger = context.getLogger();
        logger.log("Inside se.drutt.CardHandler.handleRequest(): class: " + event.getClass() + "    event:" + event);
        logger.log("body:\n" + event.getBody());

        CardRequest request = CardRequest.getInstance(event.getBody());

        response = handleServerRequest(request);

        APIGatewayProxyResponseEvent gatewayResponse = getAPIGatewayProxyResponseEvent(response);
        logger.log("Output is: " + gatewayResponse );

        return gatewayResponse;
    }

    CardResponse handleServerRequest(CardRequest request)
    {
        CardResponse response;

        if (request.isGetRequest())
            response = handleGetRequest(request);
        else
            response = new CardResponse("Request does not contain a valid command. Nothing done.");

        return response;
    }

    CardResponse handleGetRequest(CardRequest request)
    {
        if (request.subject == null)
            return new CardResponse("Get CardRequest does not contain any subject. Nothing done.");
        if (request.number < 1)
            return new CardResponse("Get CardRequest does not contain a number less than 1. Nothing done.");

        // TODO Something prevents the Card from having the correct state...
        CardResponse cardResponse = CardResponse.getInstance(getCard(request.subject, request.number));
        System.out.println("CardResponse: " + cardResponse);
        return cardResponse;
    }

    APIGatewayProxyResponseEvent getAPIGatewayProxyResponseEvent(CardResponse response)
    {
        APIGatewayProxyResponseEvent gatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
        gatewayProxyResponseEvent.withBody(response.toString());
        gatewayProxyResponseEvent.setStatusCode(200);
        Map<String, String> headers = new java.util.HashMap<>();
        headers.put("Content-Type", "application/json");
        gatewayProxyResponseEvent.setHeaders(headers);

        return gatewayProxyResponseEvent;
    }

    public void addCard(String topic, int number, Card card)
    {
        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(config.CARD_TABLE_NAME)
                .key(Map.of(config.CARD_PARTITION_KEY, AttributeValue.builder().s(topic).build(),
                        config.CARD_SORT_KEY, AttributeValue.builder().n(String.valueOf(number)).build()))
                .attributeUpdates(Map.of(config.CARD_CARD_KEY, AttributeValueUpdate.builder()
                                .value(AttributeValue.builder().s(card.toString()).build())
                                .action(AttributeAction.PUT).build()))
                .build();

        ddb.updateItem(request);
    }

    public String getCard(String topic, int number)
    {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(config.CARD_TABLE_NAME)
                .key(Map.of(config.CARD_PARTITION_KEY, AttributeValue.builder().s(topic).build(),
                        config.CARD_SORT_KEY, AttributeValue.builder().n(String.valueOf(number)).build()))
                .attributesToGet(config.CARD_CARD_KEY)
                .build();

        GetItemResponse response = ddb.getItem(request);
        return response.item().get(config.CARD_CARD_KEY).s();
    }

    public void addCards(String topic, Cards cards)
    {
        for (int i = 1;  i <= cards.cards.size(); i++)
            addCard(topic, i, cards.cards.get(i - 1));
    }

    public String getCards(String topic)
    {
        QueryRequest request = QueryRequest.builder()
                .tableName(config.CARD_TABLE_NAME)
                .keyConditionExpression("#pk = :" + config.CARD_PARTITION_KEY)
                .expressionAttributeNames(Map.of("#pk", config.CARD_PARTITION_KEY))
                .expressionAttributeValues(Map.of(":" + config.CARD_PARTITION_KEY, AttributeValue.builder().s(topic).build()))
                .build();

        QueryResponse response = ddb.query(request);

        return queryResponse2CardsJson(response);
    }

    private String queryResponse2CardsJson(QueryResponse queryResponse)
    {
        Cards result = new Cards();

        for (Map<String, AttributeValue> item : queryResponse.items())
        {
            Card card = Card.getInstance(item.get(config.CARD_CARD_KEY).s());
            result.add(card);
        }

        return gson.toJson(result);
    }

    private Configuration getConfigurationFromEnv()
    {
        return new Configuration(null, null, null,
                System.getenv("REGION"),
                null,null, null, null, null,
                null,null,
                System.getenv("CARD_TABLE_NAME"),
                System.getenv("CARD_PARTITION_KEY"),
                System.getenv("CARD_SORT_KEY"),
                System.getenv("CARD_CARD_KEY"));
    }
}

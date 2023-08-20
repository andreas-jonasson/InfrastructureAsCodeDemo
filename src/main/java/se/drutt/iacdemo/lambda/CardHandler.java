package se.drutt.iacdemo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import se.drutt.iacdemo.Configuration;
import se.drutt.iacdemo.model.Card;
import se.drutt.iacdemo.model.CardRequest;
import se.drutt.iacdemo.model.CardResponse;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

public class CardHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
{
    private Configuration config;

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
        return null;
    }

    APIGatewayProxyResponseEvent getAPIGatewayProxyResponseEvent(CardResponse response)
    {
        return null;
    }

    public void setConfig(Configuration config)
    {
        this.config = config;
    }
    private DynamoDbClient getClient()
    {
        return DynamoDbClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.builder().profileName(config.AWS_CREDENTIALS_PROFILE).build())
                .region(Region.of(config.REGION))
                .build();
    }

    public void add(String topic, int number, Card card)
    {
        DynamoDbClient ddb = getClient();

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
}

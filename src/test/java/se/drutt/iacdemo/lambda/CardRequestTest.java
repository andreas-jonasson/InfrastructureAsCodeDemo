package se.drutt.iacdemo.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CardRequestTest
{
    private static final String QUESTION_PATH = "./src/main/test/resources/CardRequest-Question.json";

    @BeforeEach
    void setUp()
    {
    }

    @Test
    void getInstance_returnsValidInstance()
    {
        CardRequest request = CardRequest.getInstance(QUESTION_PATH);
        assertNotNull(request);
        assertInstanceOf(request.getClass(), CardRequest.class);
    }
}
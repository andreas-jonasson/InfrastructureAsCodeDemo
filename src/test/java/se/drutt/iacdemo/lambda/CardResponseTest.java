package se.drutt.iacdemo.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.drutt.iacdemo.Loader;

import static org.junit.jupiter.api.Assertions.*;

class CardResponseTest
{
    private static final String QUESTION_PATH = "./src/test/resources/CardRequest-Question.json";

    @BeforeEach
    void setUp()
    {
    }

    @Test
    void getInstance_returnsValidInstance()
    {
        CardRequest request = CardRequest.getInstance(Loader.readFile(QUESTION_PATH));
        assertInstanceOf(CardRequest.class, request);
    }

    @Test
    void getInstance_containsAttributes()
    {
        CardRequest request = CardRequest.getInstance(Loader.readFile(QUESTION_PATH));
        assertNotNull(request.type);
        assertNotNull(request.subject);
    }

    @Test
    void getInstance_attributeValuesCorrect()
    {
        CardRequest request = CardRequest.getInstance(Loader.readFile(QUESTION_PATH));
        assertEquals(request.type, "question");
        assertEquals(request.subject, "cdk");
        assertEquals(request.number, 1);
    }
}
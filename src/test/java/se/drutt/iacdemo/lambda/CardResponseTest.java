package se.drutt.iacdemo.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.drutt.iacdemo.Loader;

import static org.junit.jupiter.api.Assertions.*;

class CardResponseTest
{
    private static final String QUESTION_PATH = "./src/test/resources/CardResponse-Question.json";

    @BeforeEach
    void setUp()
    {
    }

    @Test
    void getInstance_returnsValidInstance()
    {
        CardResponse response = CardResponse.getInstance(Loader.readFile(QUESTION_PATH));
        assertInstanceOf(CardResponse.class, response);
    }

    @Test
    void getInstance_containsAttributes()
    {
        CardResponse response = CardResponse.getInstance(Loader.readFile(QUESTION_PATH));
        assertNotNull(response.type);
        assertNotNull(response.subject);
        assertNotNull(response.question);
        assertNotNull(response.options);
        assertNotNull(response.correctOptions);
    }

    @Test
    void getInstance_attributeValuesCorrect()
    {
        CardResponse response = CardResponse.getInstance(Loader.readFile(QUESTION_PATH));
        assertEquals(response.type, "question");
        assertEquals(response.subject, "cdk");
        assertEquals(response.number, 1);
        assertEquals(response.question, "What is 5 x 5?");
        assertEquals(response.options[0], "5");
        assertEquals(response.correctOptions[0], 1);
    }
}
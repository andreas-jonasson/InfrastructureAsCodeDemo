package se.drutt.iacdemo.lambda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.drutt.iacdemo.Loader;
import se.drutt.iacdemo.model.CardResponse;

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
        assertNotNull(response.cards);
        assertNotNull(response.cards[0].question);
        assertNotNull(response.cards[0].options);
        assertNotNull(response.cards[0].correctOptions);
    }

    @Test
    void getInstance_attributeValuesCorrect()
    {
        CardResponse response = CardResponse.getInstance(Loader.readFile(QUESTION_PATH));
        assertEquals(response.type, "question");
        assertEquals(response.subject, "cdk");
        assertEquals(response.number, 1);
        assertEquals(response.cards[0].question, "What is 5 x 5?");
        assertEquals(response.cards[0].options[0], "5");
        assertEquals(response.cards[0].correctOptions[0], false);
        assertEquals(response.cards[0].correctOptions[1], true);
        assertEquals(response.cards[0].correctOptions[2], false);
    }
}
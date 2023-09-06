package se.drutt.iacdemo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.drutt.iacdemo.Loader;

import static org.junit.jupiter.api.Assertions.*;

class CardsTest
{
    private static final String CARDS_PATH = "./src/test/resources/TestCards.json";

    @BeforeEach
    void setUp()
    {
    }

    @Test
    void getInstance_returnsValidInstance()
    {
        Cards cards = Cards.getInstance(Loader.readFile(CARDS_PATH));
        assertNotNull(cards);
    }

    @Test
    void getInstance_containsAttributes()
    {
        Cards cards = Cards.getInstance(Loader.readFile(CARDS_PATH));
        assertNotNull(cards.cards);
        assertEquals(cards.cards.size(), 3);
    }

    @Test
    void getInstance_attributeValuesCorrect()
    {
        Cards cards = Cards.getInstance(Loader.readFile(CARDS_PATH));
        Card first = cards.cards.get(0);
        assertEquals(first.question, "What is 5 times 5?");
        assertEquals(first.correctOptions[0], 1);
    }
}
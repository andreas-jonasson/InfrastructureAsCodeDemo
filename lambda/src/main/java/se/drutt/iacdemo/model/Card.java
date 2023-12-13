package se.drutt.iacdemo.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.Objects;

public class Card
{
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public final String question;
    public final String[] options;
    public final boolean[] correctOptions;

    public Card(String question, String[] options, boolean[] correctOptions)
    {
        this.question = question;
        this.options = options;
        this.correctOptions = correctOptions;
    }

    public String toString()
    {
        return gson.toJson(this);
    }

    public static Card getInstance(String json)
    {
        return gson.fromJson(json, Card.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return Objects.equals(question, card.question) && Arrays.equals(options, card.options) && Arrays.equals(correctOptions, card.correctOptions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(question);
        result = 31 * result + Arrays.hashCode(options);
        result = 31 * result + Arrays.hashCode(correctOptions);
        return result;
    }
}

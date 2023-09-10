package se.drutt.iacdemo.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CardResponse
{
    private static final Gson gson = new GsonBuilder().create();
    public final String type;
    public final String subject;
    public final int number;
    public final Card[] cards;
    public final String message;

    public CardResponse(String type, String subject, int number, Card[] cards, String message) {
        this.type = type;
        this.subject = subject;
        this.number = number;
        this.cards = cards;
        this.message = message;
    }

    public CardResponse(String message)
    {
        this.type = null;
        this.subject = null;
        this.number = 0;
        this.cards = null;
        this.message = message;
    }

    public static CardResponse getInstance(String json)
    {
        return gson.fromJson(json, CardResponse.class);
    }

    public String toString()
    {
        return gson.toJson(this);
    }
}

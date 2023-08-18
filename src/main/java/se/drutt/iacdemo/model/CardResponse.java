package se.drutt.iacdemo.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CardResponse {
    public final String type;
    public final String subject;
    public final int number;
    public final Card[] cards;

    public CardResponse(String type, String subject, int number, Card[] cards) {
        this.type = type;
        this.subject = subject;
        this.number = number;
        this.cards = cards;
    }

    public static CardResponse getInstance(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, CardResponse.class);
    }
}

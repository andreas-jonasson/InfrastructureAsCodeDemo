package se.drutt.iacdemo.lambda;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CardRequest
{
    public final String type;
    public final String subject;
    public final int number;

    public CardRequest(String type, String subject, int number)
    {
        this.type = type;
        this.subject = subject;
        this.number = number;
    }

    public static CardRequest getInstance(String json)
    {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, CardRequest.class);
    }
}

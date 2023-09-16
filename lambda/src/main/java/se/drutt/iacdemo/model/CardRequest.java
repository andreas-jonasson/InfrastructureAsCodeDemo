package se.drutt.iacdemo.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CardRequest
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public enum requestTypes { GET, GETALL, ADD, ADDALL }
    public final String type;
    public final String subject;
    public final int number;

    public CardRequest(requestTypes type, String subject, int number)
    {
        this.type = String.valueOf(type);
        this.subject = subject;
        this.number = number;
    }

    public boolean isGetRequest()
    {
        return type.equalsIgnoreCase(String.valueOf(requestTypes.GET));
    }

    public boolean isGetAllRequest()
    {
        return type.equalsIgnoreCase(String.valueOf(requestTypes.GETALL));
    }

    public static CardRequest getInstance(String json)
    {
        return gson.fromJson(json, CardRequest.class);
    }

    public String toJson()
    {
        return gson.toJson(this);
    }
}

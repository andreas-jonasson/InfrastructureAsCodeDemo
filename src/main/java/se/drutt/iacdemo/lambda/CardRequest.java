package se.drutt.iacdemo.lambda;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CardRequest
{
    public static CardRequest getInstance(String json)
    {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, CardRequest.class);
    }
}

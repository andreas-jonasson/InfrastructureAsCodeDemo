package se.drutt.iacdemo.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Cards
{
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public ArrayList<Card> cards;

    public String toString()
    {
        return gson.toJson(this);
    }

    public static Cards getInstance(String json)
    {
        return gson.fromJson(json, Cards.class);
    }
}

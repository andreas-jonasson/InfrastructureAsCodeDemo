package se.drutt.iacdemo.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Card
{
    public final String question;
    public final String[] options;
    public final int[] correctOptions;

    public Card(String question, String[] options, int[] correctOptions)
    {
        this.question = question;
        this.options = options;
        this.correctOptions = correctOptions;
    }

    public String toString()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}

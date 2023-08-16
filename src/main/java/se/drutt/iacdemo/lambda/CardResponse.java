package se.drutt.iacdemo.lambda;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CardResponse
{
    public final String type;
    public final String subject;
    public final String question;
    public final int number;
    public final String[] options;
    public final int[] correctOptions;

    public CardResponse(String type, String subject, String question, int number, String[] options, int[] correctOptions)
    {
        this.type = type;
        this.subject = subject;
        this.question = question;
        this.number = number;
        this.options = options;
        this.correctOptions = correctOptions;
    }

    public static CardResponse getInstance(String json)
    {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, CardResponse.class);
    }
}

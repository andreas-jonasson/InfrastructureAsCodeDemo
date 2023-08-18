package se.drutt.iacdemo.model;

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
}

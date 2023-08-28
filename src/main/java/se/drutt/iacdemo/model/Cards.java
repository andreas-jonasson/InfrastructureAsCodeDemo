package se.drutt.iacdemo.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Objects;

public class Cards
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public ArrayList<Card> cards;

    public Cards()
    {
        cards = new ArrayList<>();
    }
    public void add(Card card)
    {
        cards.add(card);
    }
    public String toString()
    {
        return gson.toJson(this);
    }

    public static Cards getInstance(String json)
    {
        return gson.fromJson(json, Cards.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cards cards1 = (Cards) o;
        return Objects.equals(cards, cards1.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cards);
    }
}

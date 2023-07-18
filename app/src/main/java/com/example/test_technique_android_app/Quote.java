package com.example.test_technique_android_app;
// la classe Quote représente une citation avec son identifiant, son texte et son auteur
public class Quote {
    private int id;
    private String quote;
    private String author;

    /*
    *
    * Les méthodes getter (getId(), getQuote(), getAuthor()) permettent d'accéder aux valeurs des attributs correspondants. Elles renvoient les valeurs des attributs respectifs lorsqu'elles sont appelées.
    * */
    public int getId() {
        return id;
    }

    public String getQuote() {
        return quote;
    }

    public String getAuthor() {
        return author;
    }

}

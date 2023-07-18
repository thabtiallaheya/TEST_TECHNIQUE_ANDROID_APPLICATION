package com.example.test_technique_android_app;

import java.util.List;
/*
La classe QuoteData fournit simplement une méthode getter (getQuotes()) pour accéder
 à la liste des citations. Cela permet d'obtenir la liste complète de citations à partir
 d'une instance de QuoteData.
*/

public class QuoteData {
    private List<Quote> quotes; // Liste des citations

    public List<Quote> getQuotes() {
        return quotes; // Retourne la liste des citations
    }
}

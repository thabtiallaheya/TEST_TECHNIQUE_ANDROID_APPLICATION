package com.example.test_technique_android_app;
// La déclaration de la classe SingletonData, qui sera utilisée pour stocker les données de citation dans un design de Singleton.
public class SingletonData {
    /**
     * Ce sont les attributs privés de la classe SingletonData :
     * - instance qui représente l'unique instance de SingletonData
     * et - quoteData qui contient les données de citation.
     */
    // Instance unique de SingletonData
    private static SingletonData instance;
    // Données de citation
    private QuoteData quoteData;
     /*
     C'est le constructeur privé de la classe SingletonData. Elle est privée pour empêcher l'instanciation directe depuis l'extérieur de la classe.
     */
    private SingletonData() {
        // Empêche l'instanciation directe depuis l'extérieur de la classe.
    }
    /*
    * Cette méthode statique getInstance() permet d'obtenir l'unique instance de SingletonData.
    * Elle utilise la synchronisation pour garantir l'accès thread-safe à l'instance.
    * Si l'instance n'a pas encore été créée, elle est instanciée ici.
    * */
    public static synchronized SingletonData getInstance() {
        if (instance == null) { // Vérifie si l'instance n'a pas encore été créée
            instance = new SingletonData(); // Crée une nouvelle instance de SingletonData
        }
        return instance; // Retourne l'instance existante ou nouvellement créée
    }
    /*
    Cette méthode permet d'obtenir les données de citation actuellement stockées dans l'instance de SingletonData
    */
    public QuoteData getQuoteData() {
        return quoteData; // Retourne les données de citation actuellement stockées
    }
     /***
      * Cette méthode permet de définir les données de citation dans l'instance de SingletonData. Elle met à jour la variable quoteData avec les nouvelles données fournies en paramètre.
     ***/
    public void setQuoteData(QuoteData quoteData) {
        this.quoteData = quoteData; // Met à jour les données de citation avec les nouvelles données fournies
    }
}

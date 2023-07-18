package com.example.test_technique_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;

import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    /*
    * Ce sont les constantes utilisées pour les actions et les extras des intents dans la communication
    *  avec le BroadcastReceiver.*/

    private static final String ACTION_QUOTE = "com.lumeen.inside.technique.QuoteBroadcastReceiver.ACTION_QUOTE";
    private static final String EXTRA_QUOTE_ID = "com.lumeen.inside.technique.QuoteBroadcastReceiver.EXTRA_QUOTE_ID";
    private static final String ACTION_RESPONSE = "com.lumeen.inside.technique.QuoteBroadcastReceiver.ACTION_RESPONSE";
    private static final String EXTRA_QUOTE = "com.lumeen.inside.technique.QuoteBroadcastReceiver.EXTRA_QUOTE";
    private static final String EXTRA_AUTHOR = "com.lumeen.inside.technique.QuoteBroadcastReceiver.EXTRA_AUTHOR";
    /*
    Ces lignes déclarent les variables utilisées pour les éléments de l'interface utilisateur (Button, TextView),
     le BroadcastReceiver, l'objet Gson pour la désérialisation JSON et l'objet SingletonData pour stocker
      les données de citation. * */
    private Button btnRequest;
    private TextView tvQuote;
    private BroadcastReceiver responseBroadcastReceiver;
    private Gson gson;
    private SingletonData singletonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        * Ces deux lignes déclarent les variables utilisées pour les éléments de l'interface utilisateur (Button, TextView),
        *  le BroadcastReceiver.*/
        btnRequest = findViewById(R.id.btnRequest);
        tvQuote = findViewById(R.id.tvQuote);
        /* l'objet Gson pour la désérialisation JSON
        et l'objet SingletonData pour stocker les données de citation*/
        gson = new Gson();
        singletonData = SingletonData.getInstance();



                /* Cette ligne définit un écouteur de clic sur le bouton "btnRequest".
        Lorsque le bouton est cliqué, la méthode "performRequest()" est appelée.*/

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRequest();
            }
        });


        /**
         * Ceci est le BroadcastReceiver qui écoute les intents de l'action ACTION_RESPONSE.
         * Lorsqu'il reçoit un intent, il extrait les données (quote, author, quoteId)
         * et les affiche dans le TextView tvQuote.
         * Un toast est également affiché pour montrer l'ID de la citation reçue.
         * **/
        responseBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (ACTION_RESPONSE.equals(intent.getAction())) {
                    String quote = intent.getStringExtra(EXTRA_QUOTE);
                    String author = intent.getStringExtra(EXTRA_AUTHOR);
                    int quoteId = intent.getIntExtra(EXTRA_QUOTE_ID, -1);
                    String quoteText = "ID: " + quoteId + "\n" + quote + "\n- " + author;
                    tvQuote.setText(quoteText);
                    tvQuote.setText(quote + "\n- " + author);
                    Toast.makeText(MainActivity.this, "Received Quote ID: " + quoteId, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }



    /*
     * Cette méthode est appelée lorsque l'activité entre dans l'état de premier plan.
     * Elle enregistre le BroadcastReceiver responseBroadcastReceiver
     *  pour écouter les intents de l'action ACTION_RESPONSE.
     * */

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter responseFilter = new IntentFilter(ACTION_RESPONSE);
        registerReceiver(responseBroadcastReceiver, responseFilter);
    }

    /*
     * Cette méthode est appelée lorsque l'activité entre dans l'état de l'arrière-plan.
     * Elle désenregistre le BroadcastReceiver responseBroadcastReceiver pour arrêter d'écouter
     *  les intents de l'action ACTION_RESPONSE.
     *
     * */

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(responseBroadcastReceiver);
    }


    /**
    * Cette méthode effectue une requête HTTP GET vers l'URL "https://dummyjson.com/quotes" en utilisant OkHttp.
    **/

    private void performRequest() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://dummyjson.com/quotes")
                .build();


    /*
    Ces méthodes sont appelées respectivement en cas d'échec de la requête (onFailure) et de réponse réussie (onResponse) de la requête HTTP.
     Lorsqu'il y a une réponse réussie, le corps de la réponse est extrait, désérialisé en utilisant Gson et stocké dans l'objet quoteData.
     Ensuite, la méthode runOnUiThread est utilisée pour mettre à jour l'interface utilisateur avec les données de citation extraites.
      La réponse est également fermée pour libérer les ressources.
     */
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Erreur de requête", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("MainActivity", "Request failed", e);

            }
            // Multiple Intents
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String responseBody = response.body().string();
                    Log.d("MainActivity", "Response received: " + responseBody);

                    final QuoteData quoteData = gson.fromJson(responseBody, QuoteData.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (quoteData != null) {
                                singletonData.setQuoteData(quoteData);
                                // Obtenir la liste des citations
                                List<Quote> quotes = quoteData.getQuotes();


                                StringBuilder quoteTextBuilder = new StringBuilder();
                                for (Quote quote : quotes) {
                                    int quoteId = quote.getId();
                                    String quoteText = quote.getQuote();
                                    String authorText = quote.getAuthor();

                                    String formattedQuote = "ID: " + quoteId + "\n" + quoteText + "\n- " + authorText;
                                    quoteTextBuilder.append(formattedQuote).append("\n\n");
                                }

                                tvQuote.setText(quoteTextBuilder.toString());
                                Toast.makeText(MainActivity.this, "Requête réussie", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Erreur de désérialisation", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } finally {
                    response.close(); // Fermeture de la réponse pour libérer les ressources
                }
            }
            // Just one Intent
            /*@Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String responseBody = response.body().string();
                    Log.d("MainActivity", "Response received: " + responseBody);

                    // Parse la réponse JSON en utilisant le type adapté
                    QuoteData quoteData = gson.fromJson(responseBody, QuoteData.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (quoteData != null && quoteData.getQuotes() != null && !quoteData.getQuotes().isEmpty()) {
                                // Récupère la première citation de la liste

                                Quote quote = quoteData.getQuotes().get(0);

                                int quoteId = quote.getId();
                                String quoteText = quote.getQuote();
                                String authorText = quote.getAuthor();

                                tvQuote.setText("ID: " + quoteId + "\n" + quoteText + "\n- " + authorText);
                                Toast.makeText(MainActivity.this, "Requête réussie", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Erreur de désérialisation ou aucune citation disponible", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } finally {
                    response.close(); // Fermeture de la réponse pour libérer les ressources
                }
            }*/





        });
    }
    /*
    *
    *Cette méthode envoie un intent de diffusion avec l'action ACTION_QUOTE et l'extra EXTRA_QUOTE_ID.
    * Cela peut être utilisé pour envoyer une citation spécifique à d'autres composants de l'application.
    * */

        public void sendBroadcastIntent(int quoteId) {
            Intent quoteIntent = new Intent(ACTION_QUOTE);
            quoteIntent.putExtra(EXTRA_QUOTE_ID, quoteId);
            sendBroadcast(quoteIntent);
        }
    }




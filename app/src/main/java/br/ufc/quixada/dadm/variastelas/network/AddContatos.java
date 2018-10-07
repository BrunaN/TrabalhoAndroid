package br.ufc.quixada.dadm.variastelas.network;

import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import br.ufc.quixada.dadm.variastelas.MainActivity;
import br.ufc.quixada.dadm.variastelas.transactions.Agenda;
import br.ufc.quixada.dadm.variastelas.transactions.Constants;

public class AddContatos extends Thread{

    MainActivity activity;

    ListView view;

    public AddContatos(MainActivity activity, ListView view ){
        this.activity = activity;
        this.view = view;
    }

    public void run(){

        HttpURLConnection urlConnection = null;
        BufferedReader in = null;

        //URL do servidor
        //Alterar sempre que for rodar em uma rede diferente
        //linux, mac: ifconfig
        //windows: ipconfig
        String stringURL = Constants.SERVER_PATH+"/add";

        try {

            URL url = new URL(stringURL);
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);

            in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));

            String response = "";

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                response += inputLine;

            Log.d( "AndroidJSON", response );


            Gson gson = new Gson();

            final Agenda agenda = gson.fromJson( response, Agenda.class );

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    activity.updateListaContatos( agenda );

                }
            };

            view.post( runnable );

        } catch( MalformedURLException ex ){
           ex.printStackTrace();
        } catch( IOException ex ){
            ex.printStackTrace();
        }

    }
}
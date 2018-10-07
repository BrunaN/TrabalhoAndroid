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
import br.ufc.quixada.dadm.variastelas.transactions.Contato;

public class DeleteContatos extends Thread{

    MainActivity activity;

    Contato contato;

    public DeleteContatos(MainActivity activity, Contato contato){
        this.activity = activity;
        this.contato = contato;
    }

    public void run(){

        HttpURLConnection urlConnection = null;
        BufferedReader in = null;

        //URL do servidor
        //Alterar sempre que for rodar em uma rede diferente
        //linux, mac: ifconfig
        //windows: ipconfig
        String stringURL = Constants.SERVER_PATH+"/remove?id="+contato.getId();

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

        } catch( MalformedURLException ex ){
           ex.printStackTrace();
        } catch( IOException ex ){
            ex.printStackTrace();
        }

    }
}

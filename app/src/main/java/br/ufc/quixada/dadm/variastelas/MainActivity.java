package br.ufc.quixada.dadm.variastelas;

import android.util.Log;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


import br.ufc.quixada.dadm.variastelas.network.DownloadContatos;
import br.ufc.quixada.dadm.variastelas.transactions.Agenda;
import br.ufc.quixada.dadm.variastelas.transactions.Constants;
import br.ufc.quixada.dadm.variastelas.transactions.Contato;

public class MainActivity extends AppCompatActivity {

    int selected;
    ArrayList<Contato> listaContatos;
    ArrayAdapter adapter;
    ListView listViewContatos;

    ProgressBar progressBar;

    DownloadContatos downloadContatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selected = -1;

        listaContatos = new ArrayList<Contato>();

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaContatos );

        listViewContatos = ( ListView )findViewById( R.id.listViewContatos );
        listViewContatos.setAdapter( adapter );
        listViewContatos.setSelector( android.R.color.holo_blue_light );

        listViewContatos.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                Toast.makeText(MainActivity.this, "" + listaContatos.get( position ).toString(), Toast.LENGTH_SHORT).show();
                selected = position;
            }
        } );

        progressBar = findViewById( R.id.progressBar );
        progressBar.setIndeterminate( true );
        progressBar.setVisibility( View.VISIBLE );

        //Baixar a lista de contatos do servidor
        downloadContatos = new DownloadContatos( this, listViewContatos );
        downloadContatos.start();
    }

    public void updateListaContatos( Agenda agenda ){
        progressBar.setVisibility( View.INVISIBLE );

        Contato[] lista = agenda.getListaTelefone();
        for( Contato c: lista ) listaContatos.add( c );

        adapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_main_activity, menu );
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected( MenuItem item ) {

        switch(item.getItemId())
        {
            case R.id.add:
                clicarAdicionar();
                break;
            case R.id.edit:
                clicarEditar();
                break;
            case R.id.delete:
                apagarItemLista();
                break;
            case R.id.settings:
                break;
            case R.id.about:
                break;
        }
        return true;
    }

    private void apagarItemLista(){

        if( listaContatos.size() > 0 ){
            new Thread() {
                public void run() {
                    final Contato contato = listaContatos.get(selected);

                    HttpURLConnection urlConnection = null;
                    BufferedReader in = null;

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

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                listaContatos.remove(contato);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } catch( MalformedURLException ex ){
                        ex.printStackTrace();
                    } catch( IOException ex ){
                        ex.printStackTrace();
                    }
                }
            }.start();
        } else {
            selected = -1;
        }


    }

    public void clicarAdicionar(){
        Intent intent = new Intent( this, ContactActivity.class );
        startActivityForResult( intent, Constants.REQUEST_ADD );
    }

    public void clicarEditar(){

        Intent intent = new Intent( this, ContactActivity.class );
        //Intent intent2 = new Intent( this, "br.ufc.quixada.dadm.variastelas.ContactActivity" );

        Contato contato = listaContatos.get( selected );

        intent.putExtra( "id", contato.getId() );
        intent.putExtra( "nome", contato.getNome() );
        intent.putExtra( "telefone", contato.getTelefone() );
        intent.putExtra( "endereco", contato.getEndereco() );

        startActivityForResult( intent, Constants.REQUEST_EDIT );
    }


    @Override
    protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);

      if( requestCode == Constants.REQUEST_ADD && resultCode == Constants.RESULT_ADD ){
          final String nome = ( String )data.getExtras().get( "nome" );
          final String telefone = ( String )data.getExtras().get( "telefone" );
          final String endereco = ( String )data.getExtras().get( "endereco" );

          new Thread() {
              public void run() {

                  HttpURLConnection urlConnection = null;
                  BufferedReader in = null;

                  String stringURL = Constants.SERVER_PATH+"/add?nome="+nome+"&endereco="+endereco+"&telefone="+telefone;
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

                      runOnUiThread(new Runnable() {

                          @Override
                          public void run() {
                              Contato contato = new Contato( nome, telefone, endereco );

                              listaContatos.add( contato );
                              adapter.notifyDataSetChanged();
                          }
                      });
                  } catch( MalformedURLException ex ){
                      ex.printStackTrace();
                  } catch( IOException ex ){
                      ex.printStackTrace();
                  }
              }
          }.start();
      } else if( requestCode == Constants.REQUEST_EDIT && resultCode == Constants.RESULT_ADD ){

          final int idEditar = (int)data.getExtras().get( "id" );
          final String nome = ( String )data.getExtras().get( "nome" );
          final String telefone = ( String )data.getExtras().get( "telefone" );
          final String endereco = ( String )data.getExtras().get( "endereco" );

          new Thread() {
              public void run() {

                  HttpURLConnection urlConnection = null;
                  BufferedReader in = null;

                  String stringURL = Constants.SERVER_PATH+"/edit?nome="+nome+"&endereco="+endereco+"&telefone="+telefone+"&id="+idEditar;
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

                      runOnUiThread(new Runnable() {

                          @Override
                          public void run() {
                              for( Contato contato: listaContatos ){

                                  if( contato.getId() == idEditar ){
                                      contato.setNome( nome );
                                      contato.setEndereco( endereco );
                                      contato.setTelefone( telefone );
                                  }
                              }

                              adapter.notifyDataSetChanged();
                          }
                      });
                  } catch( MalformedURLException ex ){
                      ex.printStackTrace();
                  } catch( IOException ex ){
                      ex.printStackTrace();
                  }
              }
          }.start();


      } //Retorno da tela de contatos com um conteudo para ser adicionado
        //Na segunda tela, o usuario clicou no botão ADD
      else if( resultCode == Constants.RESULT_CANCEL ){
            Toast.makeText( this,"Cancelado",
                    Toast.LENGTH_SHORT).show();
        }

    }








}

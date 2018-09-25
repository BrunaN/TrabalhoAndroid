package br.ufc.quixada.dadm.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;

import br.ufc.quixada.dadm.server.agenda.Agenda;
import br.ufc.quixada.dadm.server.agenda.Contato;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ContatoServer {

	HttpServer server;
	
	public static void main(String[] args) {
		
		HttpServer server;
		 
		try {
			server = HttpServer.create( new InetSocketAddress( 9000 ), 0 );
			server.createContext("/info", new InfoHandler() );
		    server.createContext("/get", new GetHandler() );
		    
		    server.setExecutor(null); // creates a default executor
		    
		    server.start();
		    
		    System.out.println("The server is running");
		    
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    
	}
	
	// http://localhost:8000/info
	  static class InfoHandler implements HttpHandler {
		  
	    public void handle(HttpExchange httpExchange) throws IOException {
	    	
	      String response = "Use /get?hello=word&foo=bar to see how to handle url parameters";
	      SimpleHttpServer.writeResponse( httpExchange, response );
	      
	    }
	  }
	  
	  //Tratar aqui os parâmetros
	  static class GetHandler implements HttpHandler {
		  
	    public void handle( HttpExchange httpExchange ) throws IOException {
	    	
	    	  System.out.println( httpExchange.toString() );
	      StringBuilder response = new StringBuilder();
	      
	      String query = httpExchange.getRequestURI().getQuery();
	      	  	 // Map <String,String>parms = SimpleHttpServer.queryToMap( query );
    	  	  
//      	  response.append("<html><body>");
//          response.append("hello resposta : " + parms.get("param1") + "<br/>");
//          response.append("foo resposta: " + parms.get("param2") + "<br/>");         
//          response.append("</body></html>");
          
          Contato contato1 = new Contato( "Nome1", "telefone1", "endereco1" );
	      Contato contato2 = new Contato( "Nome2", "telefone2", "endereco2" );
	      Contato contato3 = new Contato( "Nome3", "telefone3", "endereco3" );
	      Contato contato4 = new Contato( "Nome4", "telefone4", "endereco4" );
	      //Contato contato5 = new Contato( "Nome5", "telefone5", "endereco5" );
	      //Contato contato6 = new Contato( "Nome6", "telefone6", "endereco6" );
	      
	      Contato[] listaTelefone = {contato1,
	    		  contato2,
	    		  contato3,
	    		  contato4};
	      
	      Agenda agenda = new Agenda( "Agenda", 0, listaTelefone, 30 );
	      
	      Gson gson = new Gson();
	      String agendaToString = gson.toJson( agenda ) ;
	      response.append( agendaToString );
	      
	      SimpleHttpServer.writeResponse(httpExchange, response.toString() );
	      
	      
	    }
	    
	  }
}

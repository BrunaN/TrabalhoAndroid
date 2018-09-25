package br.ufc.quixada.dadm.server;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import br.ufc.quixada.dadm.server.agenda.Agenda;
import br.ufc.quixada.dadm.server.agenda.Telefone;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class SimpleHttpServer {

  private static HttpServer server;
  
  public static void main(String[] args) throws Exception {
	  
	//Servidor htpp esperando respostas na porta 8000
    HttpServer server = HttpServer.create( new InetSocketAddress( 8000 ), 0 );
    
    //1. Criar os contextos de aplicacao
    server.createContext("/info", new InfoHandler() );
    server.createContext("/get", new GetHandler() );
    //server.createContext("/agenda", new AgendaHandler() );
    
    server.setExecutor(null); // creates a default executor
    
    server.start();
    
    System.out.println("The server is running");
    
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
      
      if( query != null ){
    	  
    	  	  Map <String,String>parms = SimpleHttpServer.queryToMap( query );
    	  	  
      	  response.append("<html><body>");
          response.append("hello resposta : " + parms.get("hello") + "<br/>");
          response.append("foo resposta: " + parms.get("foo") + "<br/>");
          response.append("teste resposta: " + parms.get("teste") + "<br/>");
          response.append("teste2 resposta: " + parms.get("teste2") + "<br/>");
          response.append("</body></html>");
          
      } else {
    	      response.append("<html><body>");
          response.append("Empty response");
          response.append("</body></html>");
      }
      
      SimpleHttpServer.writeResponse(httpExchange, response.toString() );
    }
  }
  
//  static class AgendaHandler implements HttpHandler {
//	  
//	    public void handle(HttpExchange httpExchange) throws IOException {
//	      
//	      //Escrever aqui o código que vai no banco e busca os itens da agenda
//	      //Gerar JSON e retornar para o cliente
//	        Telefone telefone1 = new Telefone( "Nom1", 0000000 );
//			Telefone telefone2 = new Telefone( "Nom2", 1111111 );
//			Telefone telefone3 = new Telefone( "Nom3", 2222222 );
//			Telefone telefone4 = new Telefone( "Nom4", 3333333 );
//			Telefone telefone5 = new Telefone( "Nom4", 444 );
//			Telefone telefone6 = new Telefone( "Nom5", 555555 );
//			Telefone telefone7 = new Telefone( "Nom6", 666666 );
//			
//			//String query = httpExchange.getRequestURI().getQuery();
//			//Map <String,String>parms = SimpleHttpServer.queryToMap( query );
//			//String agendaJson = parms.get( "teste" );
//			//System.out.println( agendaJson );
//			
//			Telefone[] listaTelefones = { telefone1, telefone2, telefone3, telefone4, telefone5, telefone6, telefone7 };
//			Agenda mensagem = new Agenda( "NomeDaMensagem", 000, listaTelefones, 0000 );
//			
//			Gson gson = new Gson();
//			
//			String mensagemSerializada = gson.toJson( mensagem );
//			System.out.println( mensagemSerializada );
//			
//	      SimpleHttpServer.writeResponse(httpExchange, mensagemSerializada );
//	      
//	    }
//	  }

  public static void writeResponse(HttpExchange httpExchange, String response) throws IOException {
    httpExchange.sendResponseHeaders( 200, response.length() );
    OutputStream os = httpExchange.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }


  /**
   * returns the url parameters in a map
   * @param query
   * @return map
   */
  public static Map<String, String> queryToMap(String query){
	  System.out.println( "Query: " + query );
      Map<String, String> result = new HashMap<String, String>();
    
    for (String param : query.split("&")) {
    	
        String pair[] = param.split("=");
        
        if (pair.length>1) {
            result.put(pair[0], pair[1]);
        }else{
            result.put(pair[0], "");
        }
    }
    return result;
  }

}

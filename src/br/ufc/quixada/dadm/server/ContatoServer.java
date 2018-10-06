package br.ufc.quixada.dadm.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufc.quixada.dadm.server.agenda.Agenda;
import br.ufc.quixada.dadm.server.agenda.Contato;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class ContatoServer {

	HttpServer server;
	public static Agenda agenda;
	
	public static void main(String[] args) {
		
		HttpServer server;
		
		 List<Contato> lista = new ArrayList<Contato>();
	      
         Contato contato1 = new Contato( "Nome1", "telefone1", "endereco1" );
	     Contato contato2 = new Contato( "Nome2", "telefone2", "endereco2" );
	     Contato contato3 = new Contato( "Nome3", "telefone3", "endereco3" );
	     Contato contato4 = new Contato( "Nome4", "telefone4", "endereco4" );
	     //Contato contato5 = new Contato( "Nome5", "telefone5", "endereco5" );
	     //Contato contato6 = new Contato( "Nome6", "telefone6", "endereco6" );
	      

	     lista.add(contato1);
	     lista.add(contato2);
	     lista.add(contato3);
	     lista.add(contato4);
	     
	     agenda = new Agenda( "Agenda", 0, lista, 30 );
	    
		 
		try {
			server = HttpServer.create( new InetSocketAddress( 9000 ), 0 );
			server.createContext("/info", new InfoHandler() );
		    server.createContext("/get", new GetHandler() );
		    server.createContext("/add", new AddHandler() );
		    server.createContext("/remove", new RemoveHandler() );
		    server.createContext("/edit", new EditHandler() );
		    
		    server.setExecutor(null); // creates a default executor
		    
		    server.start();
		    
		    System.out.println("The server is running");
		    
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    
	}
	
	  // http://localhost:9000/info
	  static class InfoHandler implements HttpHandler {
		  
	    public void handle(HttpExchange httpExchange) throws IOException {
	    	
	      String response = "Use /get?hello=word&foo=bar to see how to handle url parameters";
	      SimpleHttpServer.writeResponse( httpExchange, response );
	      
	    }
	  }
	  
	  // http://localhost:9000/add
	  static class AddHandler implements HttpHandler {
		  
		    public void handle(HttpExchange he) throws IOException {
		    	
		    	// parse request
                Map<String, Object> parameters = new HashMap<String, Object>();
                URI requestedUri = he.getRequestURI();
                String query = requestedUri.getRawQuery();
                parseQuery(query, parameters);
                
                String response = "";
                
    		    System.out.println("Removing");

                try {
               	 String nome = parameters.get("nome").toString();
               	 String endereco = parameters.get("endereco").toString();
               	 String telefone = parameters.get("telefone").toString();

               	 Contato contato = new Contato(nome, telefone, endereco);
               	 
               	 agenda.adicionarContato(contato);
                
               	 response = "contato adicionado";
               	 he.sendResponseHeaders(200, response.length());
               	 OutputStream os = he.getResponseBody();
               	 os.write(response.toString().getBytes());

               	 os.close();
                
                } catch (Exception e) {
               	 response = "parâmetros inválidos";
               	 he.sendResponseHeaders(401, response.length());
	                 OutputStream os = he.getResponseBody();
	                 os.write(response.toString().getBytes());

	                 os.close();
                }
		      
		    }
	  }
	  
	  // http://localhost:9000/edit
	  static class EditHandler implements HttpHandler {
		  
		    public void handle(HttpExchange he) throws IOException {
		    	
		    	// parse request
                Map<String, Object> parameters = new HashMap<String, Object>();
                URI requestedUri = he.getRequestURI();
                String query = requestedUri.getRawQuery();
                parseQuery(query, parameters);
                
                String response = "";
                
    		    System.out.println("Removing");

                try {
               	 int id = Integer.parseInt(parameters.get("id").toString());
               	 String nome = parameters.get("nome").toString();
               	 String endereco = parameters.get("endereco").toString();
               	 String telefone = parameters.get("telefone").toString();
               	 
               	 agenda.editContato(id, nome, telefone, endereco);
                
               	 response = "contato editado";
               	 he.sendResponseHeaders(200, response.length());
               	 OutputStream os = he.getResponseBody();
               	 os.write(response.toString().getBytes());

               	 os.close();
                
                } catch (Exception e) {
               	 response = "parâmetros inválidos";
               	 he.sendResponseHeaders(401, response.length());
	                 OutputStream os = he.getResponseBody();
	                 os.write(response.toString().getBytes());

	                 os.close();
                }
		      
		    }
	  }
	  
	  // http://localhost:9000/remove
	  static class RemoveHandler implements HttpHandler {

	         public void handle(HttpExchange he) throws IOException {
	                 // parse request
	                 Map<String, Object> parameters = new HashMap<String, Object>();
	                 URI requestedUri = he.getRequestURI();
	                 String query = requestedUri.getRawQuery();
	                 parseQuery(query, parameters);
	                 
	                 String response = "";
	                 
	     		    System.out.println("Removing");

	                 try {
	                	 int id = Integer.parseInt(parameters.get("id").toString());
	                	 
	                	 agenda.removeContatoId(id);
	                 
	                	 response = "contato removido";
	                	 he.sendResponseHeaders(200, response.length());
	                	 OutputStream os = he.getResponseBody();
	                	 os.write(response.toString().getBytes());

	                	 os.close();
	                 
	                 } catch (Exception e) {
	                	 response = "parâmetros inválidos";
	                	 he.sendResponseHeaders(401, response.length());
		                 OutputStream os = he.getResponseBody();
		                 os.write(response.toString().getBytes());

		                 os.close();
	                 }
	         }
	  }
	  
	  public static void parseQuery(String query, Map<String, 
				Object> parameters) throws UnsupportedEncodingException {

			         if (query != null) {
			                 String pairs[] = query.split("[&]");
			                 for (String pair : pairs) {
			                          String param[] = pair.split("[=]");
			                          String key = null;
			                          String value = null;
			                          if (param.length > 0) {
			                          key = URLDecoder.decode(param[0], 
			                          	System.getProperty("file.encoding"));
			                          }

			                          if (param.length > 1) {
			                                   value = URLDecoder.decode(param[1], 
			                                   System.getProperty("file.encoding"));
			                          }

			                          if (parameters.containsKey(key)) {
			                                   Object obj = parameters.get(key);
			                                   if (obj instanceof List<?>) {
			                                            List<String> values = (List<String>) obj;
			                                            values.add(value);

			                                   } else if (obj instanceof String) {
			                                            List<String> values = new ArrayList<String>();
			                                            values.add((String) obj);
			                                            values.add(value);
			                                            parameters.put(key, values);
			                                   }
			                          } else {
			                                   parameters.put(key, value);
			                          }
			                 }
			         }
			}
	  
	  // http://localhost:9000/get
	  static class GetHandler implements HttpHandler {
		  
	    public void handle( HttpExchange httpExchange ) throws IOException {
	    	
	      System.out.println( httpExchange.toString() );
	      StringBuilder response = new StringBuilder();
	      
	      String query = httpExchange.getRequestURI().getQuery();
	      
	      
	      Gson gson = new Gson();
	      String agendaToString = gson.toJson( agenda ) ;
	      response.append( agendaToString );
	      
	      SimpleHttpServer.writeResponse(httpExchange, response.toString() );
	      
	      
	    }
	    
	  }
}

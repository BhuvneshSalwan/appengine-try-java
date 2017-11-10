package com.googleapis.adyogi.main;

import java.io.PrintWriter;
import java.util.Arrays;

import org.json.JSONObject;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.tagmanager.TagManager;
import com.google.api.services.tagmanager.model.Account;
import com.google.api.services.tagmanager.model.Container;
import com.google.api.services.tagmanager.model.ListAccountsResponse;

public class GAuthenticate {
	
	public static final HttpTransport TRANSPORT = new NetHttpTransport();
	public static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	public static TagManager getAuthenticated(PrintWriter out){
		
		TagManager manager = null;
		
		if((manager = createAuthorizedClient(out)) != null){
			return manager;
		}
		else{
			return null;
		}
	
	}
	
	public static TagManager createAuthorizedClient(PrintWriter out){

		GoogleCredential credential = null;
		
		if((credential = authorize(out)) != null){
			out.println(credential);
			return new TagManager.Builder(TRANSPORT, JSON_FACTORY, credential).build();
		}
		else{
			return null;
		}
		
	}
	
	public static GoogleCredential authorize(PrintWriter out){
	
		try{

			GoogleCredential credential = GoogleCredential.getApplicationDefault();
			return credential;

		}
		catch(Exception e){
			out.println("Exception : Authenticate - Authorize Method" );
			out.println(e);
			return null;
		}
	
	}
	
	public static JSONObject createGTM(String hostname, String accountName, PrintWriter out) throws Exception{
		  
		  JSONObject responseData = new JSONObject();
		  
		  try{
			  TagManager manager = getAuthenticated(out);
			  
			  out.println("Inside the Create GTM with manager as : " + manager);
			  
			  ListAccountsResponse accounts = manager.accounts().list().execute();
	    	
			  String accountPath = null;
			  
	    	  for (Account account : accounts.getAccount()) {
	    	    
	    		out.println(account.getName());  
	    		  
	    	    if(account.getName().equalsIgnoreCase(accountName)){
	    	    	accountPath = account.getPath();
	    	    }
	    	    
	    	  }
	    	  
	    	  if(null == accountPath){
	    		  accountPath = "accounts/1346491955";
	    	  }
	
	    	  Container container = new Container();
	      	  container.setName(hostname);
	          container.setUsageContext(Arrays.asList("web"));
	      	  container.setPath(accountPath);
	      	
	      	  Container containerResponse = null;
	      	  
	      	  containerResponse = manager.accounts().containers().create(accountPath, container).execute();

	      	  if(null != containerResponse){
	      	      
	      	      responseData.put("success", true);
		      	  responseData.put("gtm_id", containerResponse.getPublicId());
		      	  responseData.put("message", "Created Successfully.");
		      	  responseData.put("gtm_path", containerResponse.getPath());
	      	  
	      	  }else{
	      		
	      		  responseData.put("success", false);
				  responseData.put("gtm_id", "NA");
				  responseData.put("message", "Some Internal Error");
				  responseData.put("gtm_path", "NA");
	      		  
	      	  }
	      	  return responseData;
	      	  
		  }catch(Exception e){
			  
			  responseData.put("success", false);
			  responseData.put("gtm_id", "NA");
			  responseData.put("message", e.toString());
			  responseData.put("gtm_path", "NA");
			  
			  return responseData;
		  }
		  
	  }

}
/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package myapp;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.googleapis.adyogi.main.GAuthenticate;

public class DemoServlet extends HttpServlet {
	
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
	  try{
			String testString = request.getQueryString();
			response.getWriter().print("Version Final 9: "+testString);
			response.setStatus(HttpServletResponse.SC_CREATED);
		}catch(Exception e){
			response.getWriter().print("Exception : " + e);
			response.setStatus(HttpServletResponse.SC_CREATED);
		}  
  }
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		try{		
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = request.getReader();
			String line;
		    while ((line = reader.readLine()) != null) {
		        buffer.append(line);
		    }  
		    String query = buffer.toString();
		    JSONObject jsonData = new JSONObject(query);
		    if(jsonData.has("hostname")){	
		    	String hostname = jsonData.getString("hostname");	    	
		    	String accountName;
		    	if(jsonData.has("accountName")){
		    		accountName = jsonData.getString("accountName");
		    	}
		    	else{
		    		accountName = "AdYogi";
		    	}
		    	JSONObject responseJSON = GAuthenticate.createGTM(hostname, accountName, response.getWriter());
		    	response.getWriter().print(responseJSON.toString());
		    	response.setStatus(HttpServletResponse.SC_CREATED);
		    }
		    else{
		    	response.getWriter().println(new JSONObject().put("success", false).put("gtm_id", "NA").put("message", "Only Hostname (hostname) as a Parameter is accepted.").put("gtm_path", "NA").toString());
		    	response.setStatus(HttpServletResponse.SC_CREATED);
		    }
		}catch(Exception e){
			response.getWriter().print("{\"success\":false,\"gtm_id\":\"NA\",\"message\":\"Please Contact the Support Team. Exception : " + e.toString() + "\",\"gtm_path\":\"NA\"}");
			response.setStatus(HttpServletResponse.SC_CREATED);
		}
  	}
}
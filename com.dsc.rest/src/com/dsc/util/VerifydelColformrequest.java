package com.dsc.util;
 

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

 
public class VerifydelColformrequest  {
	 
	
	public Response verifydelColformrequest(JSONObject inputJsonObj) throws JSONException {
		 Response rb = null;
		 StringBuffer sb = new StringBuffer();
		 // verify if both key values are present in json input  and also should have values
		  if(inputJsonObj.has("reasonCode") 	&&
	          (    inputJsonObj.has("OBSInstID")))
		 {
			   if(( inputJsonObj.get("reasonCode").toString().length() <= 0 ) ||
		     	      (inputJsonObj.get("OBSInstID").toString().length() <= 0 ))
		        {
		          	 
		          	String msg="OBSInstID and/or reasonCode cannot be blank or null)";
				    sb.append("{\"result\":\"FAILED\",\"resultCode\":110,\"message\":\"" +msg +"\"}");
				    rb=Response.ok(sb.toString()).build();
				     return rb;
		        }			 
			 
		 }
		 else
		 {
			   
			    String msg="OBSInstID and/or reasonCode JSON Key is required  for this API)";
			    sb.append("{\"result\":\"FAILED\",\"resultCode\":100,\"message\":\"" +msg +"\"}");
			    rb=Response.ok(sb.toString()).build();
			     return rb;			 
		 
		 }
 
		 // check to see if the OBSInstID is integer
       	try
	        {  
       		  Integer selid = (int) inputJsonObj.get("OBSInstID");
  	           rb=null;
	        }
	        catch( Exception e )
	        {
	        	  
	        	String msg="OBSInstID is not Numeric";
	   		  sb.append("{\"result\":\"FAILED\",\"resultCode\":120,\"message\":\"" +msg +"\"}");
	   		  rb=Response.ok(sb.toString()).build();
	   		  return rb;
	        }
      
       	return rb;
 
	}      
	
}
	
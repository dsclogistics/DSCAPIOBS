package com.dsc.util;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

 
public class verifyNewColrequest {
	 
	
	public Response verifyNewColrequest(JSONObject inputJsonObj) throws JSONException {
		 Response rb = null;
		 StringBuffer sb = new StringBuffer();
		 // verify if both key values are present in json input  and also should have values
		//  if(inputJsonObj.has("FunctionName") 	&&
	         if(    inputJsonObj.has("OBSColFormID"))
		 {
			 //  if(( inputJsonObj.get("FunctionName").toString().length() <= 0 ) ||
		     	   if  (inputJsonObj.get("OBSColFormID").toString().length() <= 0 )
		        {
		          	//  String msg="FunctionName and/or OBSColFormID cannot be blank or null)";
		          	String msg="OBSColFormID cannot be blank or null";
				    sb.append("{\"result\":\"FAILED\",\"resultCode\":110,\"message\":\"" +msg +"\"}");
				    rb=Response.ok(sb.toString()).build();
				     return rb;
		        }			 
			 
		 }
		 else
		 {
			   // String msg="FunctionName and/or OBSColFormID JSON Key is requred for this API)";
			    String msg="OBSColFormID JSON Key is required for this API)";
			    sb.append("{\"result\":\"FAILED\",\"resultCode\":100,\"message\":\"" +msg +"\"}");
			    rb=Response.ok(sb.toString()).build();
			     return rb;			 
		 
		 }
 
		 // check to see if the obscolformid is integer
       	try
	        {  
       		  Integer selid = (int) inputJsonObj.get("OBSColFormID");
  	           rb=null;
	        }
	        catch( Exception e )
	        {
	        	  
	        	String msg="OBSColFormID is not Numeric";
	   		  sb.append("{\"result\":\"FAILED\",\"resultCode\":120,\"message\":\"" +msg +"\"}");
	   		  rb=Response.ok(sb.toString()).build();
	   		  return rb;
	        }
      
       	return rb;
 
	}      
	
}
	
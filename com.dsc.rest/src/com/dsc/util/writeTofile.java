package com.dsc.util;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class writeTofile {
	public  Response   writeTofile(JSONObject jsonObject) throws JSONException {
		  Response rb=null;
		 SimpleDateFormat simpleDateFormat =
	                new SimpleDateFormat("YYYYMMddhhmmss");
	        String dateAsString = simpleDateFormat.format(new Date());
	 //       System.out.println(dateAsString); 

	        //write to file
	        String fname="/var/log/OBS"+dateAsString +".txt";
	      //  System.out.println("Writing file as:"+fname);
	        Writer writer = null;	        
	        try {
	            writer = new BufferedWriter(new OutputStreamWriter(
	                  new FileOutputStream(fname), "utf-8"));
	            writer.write(jsonObject.toString());
	            rb=Response.ok(jsonObject.toString()).build();	             
	        } catch (IOException ex) {
	          // report
	        	// System.out.println("Writing failed for file as:"+ex.getMessage());
	        	 rb=Response.ok("Error. Could not save file").build();
	        } finally {
	           try {writer.close();} catch (Exception ex) {/*ignore*/}
	        }
	       
			  return rb;   
	 
   		
}
}

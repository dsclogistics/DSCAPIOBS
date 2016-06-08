
package com.dsc.util;
 
import java.sql.Statement;
 
import org.codehaus.jettison.json.JSONObject;
 

public class savecollform   {
	
	public String [] savecollform(JSONObject jsonObject) throws Exception {
 	
		    String [] msg = new String[2];
		    String instid=null;
		    String colformid=null;
 
            // Get observationsColFormData
        	JSONObject s1 =  (JSONObject) jsonObject.get("observationsColFormData"); 
            if (s1.get("DBColFormStatus").toString().equals("COLLECTED")) 
            {
        	
           		msg[0]="-1";
        		msg[1]= "{\"result\":\"FAILED\",\"resultCode\":280,\"message\":\""  +
     		       			"Cannot save previously Submitted Form"  +"\"}";
        	    return msg;
      
            }
     	
        	Statement stmt =null;
            // The data should have OBSColFormID. If it does Get OBSInstID and OBSColFormID values
        	if(s1.has("OBSColFormID")) 
        	 {
        		 if (s1.has("OBSInstID")){instid=s1.getString("OBSInstID").trim().toString();}
        		 if (s1.has("OBSColFormID")){colformid=s1.getString("OBSColFormID").trim().toString();}
        	 }
        	else
        	{
        		msg[0]="-1";
    			msg[1]= "{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""  +
 		       			"Invalid Json OBJECT requried OBSInstID value:"+instid +" and OBSColFormID  value:"+colformid+ "not found "  +"\"}";
    			return msg;
        	}
        	
        	 if (instid.trim().length() >0)
        	 {     		 
        		// System.out.println("** SAVECOLLFORM OBSINSTID NOT NULL > 0 length received is:"+instid +" Collection FORM ID received is:"+colformid);
        	 }
        	 else
        	 {
        		 // System.out.println("** SAVECOLLFORM OBSINSTID IS NULL received is:"+instid +" Collection FORM ID received is:"+colformid);	 
        	 }
        	 // if you have a valid OBSInstID then this is an existing Collection Instance else New Collection
        	
        	 if (instid.trim().length() >0)
        	 {
        		 // inistantiate update collection form and send s1 Json Object
        		 updatecollinstance updinst = new updatecollinstance();
        		 msg=updinst.updatecollinstance(s1);
        	 }
        	 else
        	 {
        		 // instanticate insert collection form and send s1 Json Object
        		 insertnewcollinstance newinst = new insertnewcollinstance();
        		 msg=newinst.insertnewcollinstance(s1);
        	 }
        	 
        	 // if the return values are good a string array. Array 1 status, Array 2 message to send back
        	 
        	 return msg;
	}
  
}
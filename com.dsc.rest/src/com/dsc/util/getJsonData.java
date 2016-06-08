package com.dsc.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.dsc.dao.obs;

public class getJsonData   {
	
	public String  getJsonData(JSONObject jsonObject) throws Exception {
// public String  getJsonData(JSONObject jsonObject) throws JSONException {		
		    String xyz=""; 
		    int [] obsids= new int[25];
	      //  JSONObject rs =  (JSONObject) jsonObject.get("result");
	      //  System.out.println(" Result element has:"+rs.names().toString()); 
            // System.out.println(jsonObject.names().toString()); 
        	JSONObject s1 =  (JSONObject) jsonObject.get("observationsColFormData"); 
        	// System.out.println(s1.names().toString()); 
        	Statement stmt =null;
        	
        	 if(s1.has("OBSColFormID")) 
        	 {
        		 String instid=""; 
        		 if (s1.has("OBSInstID")){instid=s1.getString("OBSInstID").toString();}
        		 System.out.println("OBSINST ID BEFORE CALLING is:"+instid);
        		 verifyCFTID vcft = new verifyCFTID();
        		 try {
					  obsids=vcft.vCFTID(s1.getString("OBSColFormID").toString(),s1.getString("OBSInstID").toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	 }
        
        	 System.out.println( " $$$$ obsids11 is:"+obsids[11] +" obsids 4 is:"+obsids[4]); 
        	 // if obsids array 11 < 0 then new form else update existing form 
        	// if (obsids[11] < 0)
        	// {
        	   insertOBSInst obsins = new insertOBSInst();
       	  
        	    
        	 try {
				  obsids= obsins.insOBSInst(s1,obsids);
				 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	// }
        	  
 	 
        	JSONArray jsonArr = (JSONArray) s1.getJSONArray("questions");
        	
        	System.out.println( " json Array length:"+jsonArr.length());  
        	
        	
        							// parseJson obsqarr = new parseJson(jsonArr);
        	parseQuestions q = new parseQuestions();
       	
       	     obsids = q.parseQuestions(jsonArr,obsids);
         	System.out.println( " DID PARSE ANSERS/QUESTION FAIL:"+obsids[20] );      
           	 xyz= "{\"result\":\"SUCCESS\",\"resultCode\":0,\"message\":\""  +
            			"Updates Successful"  +"\"}";
           	 if (obsids[20] == -200)
    			xyz= "{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""  +
 		       			"Connection Failed while starting to parse Questions"  +"\"}";
           	 if (obsids[20] == -160)
			  xyz= "{\"result\":\"FAILED\",\"resultCode\":160,\"message\":\""  +
		       			"Failed to get obs_cfi_id. Invalid observerid/observedid "  +"\"}";      
          	 if (obsids[20] == -150)
			 xyz= "{\"result\":\"FAILED\",\"resultCode\":150,\"message\":\""  +
		       			"Failed to Insert/update instance/collection form inst object"  +"\"}";
          	 
             // obsids array 20 has value > 140 then delete the obs_insts also so no ready is there
          	 /*
             if (obsids[20] == -150 || obsids[20] == -160 || obsids[20] == -200)
             {
            	 Connection conn = null;
 
         		 try {
         			conn= obs.obsConn().getConnection();
         		 	} catch (SQLException e) {
         			// TODO Auto-generated catch block
         			 xyz= "{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""  +
         	       			"Connection Failed while starting to parse Questions"  +"\"}";
              		e.printStackTrace();
 
         			//  return msg;
         		 	} catch (Exception e) {
         			// TODO Auto-generated catch block
         			e.printStackTrace();
         			 xyz= "{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""  +
         		       			"Connection Failed while starting to parse Questions"  +"\"}";
   
         			 // return msg;
         		 		}      
         		 try
         		 {
         		   String query="delete from obs_collect_form_inst where obs_inst_id=" +obsids[4] +
         				        "; delete from obs_inst where obs_inst_id=" +obsids[4];
         		//   System.out.println( " Deleting inst since error in saving:"+query);  
         		//   System.out.println( "Error Message is:"+xyz);  
         		     stmt = conn.createStatement();
         		  //  stmt.executeUpdate(query);
         		     stmt.close();
         		   if (conn != null) { conn.close();}
         		 } 
                 catch (Exception e) 
         		 {
                	 e.printStackTrace();		 
         		 }
         		finally {
 
	    	        if (stmt != null) { stmt.close(); }
	    	        
	    	        if (conn != null) { conn.close();}
	    	     }	
             } // if errors delete inst and form inst
             
          	 */ 
          	 
          	 
	   		return xyz;
	}

}
 
package com.dsc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.dsc.dao.obs;

  public class DeleteColform {
	 
	
	public Response deleteColform(JSONObject inputJsonObj) throws JSONException {
		
		 Response rb = null;
		StringBuffer sb = new StringBuffer();
		StringBuffer sbn = new StringBuffer();
		int req_obs_inst_id=0;
		int rows=0;
			 Connection conn = null;
		 int rsCount=0;
		 
	 
				try {
					conn= obs.obsConn().getConnection();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					 String msg="Connection Failed while deleting Colforminstance ";
	                sb.append("{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""+msg+"\"");
	                rb=Response.ok(sb.toString()).build();
	                return rb;
				//	e.printStackTrace();
				}
		 
		 
	 
		 try {
 
			     req_obs_inst_id=(int) inputJsonObj.get("OBSInstID");
			 	String req_reasoncode= inputJsonObj.get("reasonCode").toString();
 
			 	String SQL = "  update obs_inst set obs_inst_del_date=getdate(),obs_inst_del_yn='Y', "+ 
    		               " obs_inst_del_reason='"+req_reasoncode +"' where obs_inst_id " +
    				       " = "+req_obs_inst_id;
     
			 	Statement stmt = conn.createStatement();
			 	 rows =stmt.executeUpdate(SQL);
 
			     stmt.close();
			       if (conn != null) { conn.close();} 
				}
				 catch (SQLException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					 String msg="Update Failed while deleting OBS Instance ID " +req_obs_inst_id;
		                sb.append("{\"result\":\"FAILED\",\"resultCode\":180,\"message\":\""+msg+"\"");
		                rb=Response.ok(sb.toString()).build();
		                return rb;
			   // while loop for questions
				   }
		       if (rows > 0) 
		       {
		 		String msg="Update Successful for OBS Instance ID " +req_obs_inst_id;
		 		sb.append("{\"result\":\"SUCCESS\",\"resultCode\":0,\"message\":\""+msg+"\"");
		       }
		       else
		       {
			 		String msg="Invalid  OBS Instance ID " +req_obs_inst_id;
			 		 sb.append("{\"result\":\"FAILED\",\"resultCode\":190,\"message\":\""+msg+"\"");	    	   
		       }
		        
		 		
     
	         sb.append("}"); // end start of result 
 
	           if (conn != null) 
	           {
	        	   try{
	        		   conn.close();
	        		  } catch(SQLException e)
	        	      {e.printStackTrace(); }
	           }	
	         rb=Response.ok(sb.toString()).build();
	       //  System.out.println("JSON Message sent is"+sb.toString());
 
         return rb;
	}
}

 

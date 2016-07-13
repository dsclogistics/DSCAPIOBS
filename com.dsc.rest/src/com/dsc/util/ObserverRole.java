package com.dsc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.dsc.dao.obs;

public class ObserverRole {
 
public Response   ObserverRole (JSONObject jsonObject) throws JSONException {
	
	 Response rb = null;
	StringBuffer sb = new StringBuffer();
	StringBuffer sbn = new StringBuffer();
	String msg="";
	 Connection conn = null;	    
   JSONArray json = new JSONArray();
   JSONObject obj1 = new JSONObject();
	
   if (jsonObject.has("name"))
   {
   	msg="";
   }
   else
   {
		msg="Json elements name is required for this API ";
		sb.append("{\"result\":\"FAILED\",\"resultCode\":300,\"message\":\"" +msg +"\"}");		
	 	rb=Response.ok(sb.toString()).build();
		System.out.println(sb.toString());
		return rb;
   }
   
   if ((jsonObject.get("name").toString().length()  > 0 ))
   {
     String name=jsonObject.get("name").toString();


			try {
				conn= obs.obsConn().getConnection();
				// conn.setTransactionIsolation(conn.TRANSACTION_REPEATABLE_READ);
				 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
                msg="ObserverRole Connection Failed.";
               sb.append("{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""+msg+"\"");
  	          rb=Response.ok(obj1.toString()).build();
  	          return rb;
			}

	 try {

		  String SQL = " SELECT  c.*   FROM [dbo].[OBS_USER_AUTH] a, [dbo].[OBS_USER_ROLE] b, "+
		               " [dbo].[OBS_ROLE] c where a.obs_user_auth_id = b.obs_user_auth_id and b.obs_role_id=c.obs_role_id "+
		               " and obs_user_auth_dsc_ad_name='" +name +"'";
        
         
       
         Statement stmt = conn.createStatement();
       //     System.out.println("statement connect done" );
		      // do starts here
		        ResultSet rs = stmt.executeQuery(SQL);
		        ResultSetMetaData rsmd = rs.getMetaData();
		//        System.out.println("result set created" );

				int numColumns = rsmd.getColumnCount(); 
				while (rs.next()) {

				JSONObject obj = new JSONObject();

				for (int i=1; i<numColumns+1; i++) {
			        String column_name = rsmd.getColumnName(i);
 
			          obj.put(column_name, rs.getString(i));
 
			        
				} // for numcolumns
				 json.put(obj);
				} // while loop

		              rs.close();
		             stmt.close();
		             if (conn != null) { conn.close();} 
			     obj1.put("resource",(Object)json);      
			  }
			   catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
                 msg="ObserverRole DB Query Failed.";
               sb.append("{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""+msg+"\"");
  	            rb=Response.ok(obj1.toString()).build();
  	            return rb;
			   }
   } // if non blank input values
        rb=Response.ok(obj1.toString()).build();
        if (conn != null) 
        {
     	   try{
     		   conn.close();
     		  } catch(SQLException e)
     	      {e.printStackTrace(); }
        } 
        return rb;
 }
}

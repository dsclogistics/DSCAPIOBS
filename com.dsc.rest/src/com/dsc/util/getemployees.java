 
package com.dsc.util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONArray;
 


import com.dsc.dao.obs;

public class   getemployees  {
	 
	
	public Response   getemployees (JSONObject jsonObject) throws JSONException {
		
		 Response rb = null;
		StringBuffer sb = new StringBuffer();
		StringBuffer sbn = new StringBuffer();
		String msg="";
		 Connection conn = null;	    
        JSONArray json = new JSONArray();
        JSONObject obj1 = new JSONObject();
		
	    if (jsonObject.has("ObserverLCid"))
	    {
	    	msg="";
	    }
	    else
	    {
			msg="Json elements ObserverLCid is required for this API ";
			sb.append("{\"result\":\"FAILED\",\"resultCode\":300,\"message\":\"" +msg +"\"}");		
		 	rb=Response.ok(sb.toString()).build();
			System.out.println(sb.toString());
			return rb;
	    }
	    
	    if ((jsonObject.get("ObserverLCid").toString().length()  > 0 ))
	    {
	    String lcid=jsonObject.get("ObserverLCid").toString();
 

 				try {
					conn= obs.obsConn().getConnection();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
	                 msg="obs_getemployees Connection Failed.";
	                sb.append("{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""+msg+"\"");
	   	          rb=Response.ok(obj1.toString()).build();
	   	          return rb;
				}
  
		 try {
 
    		  String SQL = " select * from [dbo].[obs_getEmployees] WITH (NOLOCK) where  " +
    		  "ObserverLCid='"+lcid +"'";
	         
	          
		  //   System.out.println("First SQL:" + SQL);
	        
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
				        if (i<3)
				        {
				        obj.put(column_name, rs.getInt(i));
				        }
				        else
				        {
				          obj.put(column_name, rs.getString(i));
				        }
				       
				        
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
	                  msg="obs_getemployees DB Query Failed.";
	                sb.append("{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""+msg+"\"");
	   	            rb=Response.ok(obj1.toString()).build();
	   	            return rb;
				   }
	    } // if non blank input values
	    
        if (conn != null) 
        {
     	   try{
     		   conn.close();
     		  } catch(SQLException e)
     	      {e.printStackTrace(); }
        }	    
	         rb=Response.ok(obj1.toString()).build();
	      //   System.out.println(" Employee JSON Message sent is"+obj1.toString());
             return rb;
	}
}
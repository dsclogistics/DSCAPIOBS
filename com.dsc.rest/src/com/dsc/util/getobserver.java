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

public class getobserver {
	 
	
	public Response getobserver(JSONObject jsonObject) throws JSONException {
		
		 Response rb = null;
		StringBuffer sb = new StringBuffer();
		StringBuffer sbn = new StringBuffer();
		String msg="";

		 Connection conn = null;	    
        JSONArray json = new JSONArray();
        JSONObject obj1 = new JSONObject();
		
	    if ((jsonObject.has("dsc_observer_emp_first_name"))  && (jsonObject.has("dsc_observer_emp_last_name")) &&
	    		(jsonObject.has("dsc_observer_emp_email_addr"))	    		)
	    {
	    	msg="";
	    }
	    else
	    {
			msg="Json elements dsc_observer_emp_first_name,dsc_observer_emp_last_name, dsc_observer_emp_email_addr is required for this API ";
			sb.append("{\"result\":\"FAILED\",\"resultCode\":300,\"message\":\"" +msg +"\"}");		
		 	rb=Response.ok(sb.toString()).build();
			System.out.println(sb.toString());
			return rb;
	    }
	    
	    if ((jsonObject.get("dsc_observer_emp_first_name").toString().length()  > 0 ) &&
	    	(jsonObject.get("dsc_observer_emp_last_name").toString().length()  > 0 ) &&
	    	(jsonObject.get("dsc_observer_emp_email_addr").toString().length()  > 0 ))
	    {
	    String fname=jsonObject.get("dsc_observer_emp_first_name").toString();
	    String lname=jsonObject.get("dsc_observer_emp_last_name").toString();
	    String email=jsonObject.get("dsc_observer_emp_email_addr").toString();
	    int sc = lname.indexOf("'");
	    lname=lname.replaceAll("'","%''%");
	 


 				try {
					conn= obs.obsConn().getConnection();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
	                 msg="obs_getLC Connection Failed.";
	                sb.append("{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""+msg+"\"");
	   	          rb=Response.ok(obj1.toString()).build();
	 	         if (conn != null) 
		         {
		      	   try{
		      		   conn.close();
		      		  } catch(SQLException e1)
		      	      {e1.printStackTrace(); }
		         }
	   	          return rb;
				}
  
		 try {
 
    		  String SQL = " select * from [dbo].[obs_getObserver] WITH (NOLOCK) where  " +
    		//  "dsc_observer_emp_first_name='"+fname +"'" +
    		  "   dsc_observer_emp_email_addr='"+email +"'" ;
 	          /*   				  
    		  if (sc > 0)
    		  {
    			  SQL=SQL +" and dsc_observer_emp_last_name like '"+lname +"'";
    		  }
    		  else
    		  {
    			  SQL=SQL +" and dsc_observer_emp_last_name='"+lname +"'";
    		  }
    		 */
	          
		 //    System.out.println("First SQL:" + SQL);
	        
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
				        if (i==1)
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
	                  msg="obs_getObserver DB Query Failed.";
	                sb.append("{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""+msg+"\"");
	   	            rb=Response.ok(obj1.toString()).build();
	   	            if (conn != null) 
	   	            {
	   	            	try{
		      			  conn.close();
	   	            	} catch(SQLException e2)
	   	            	{e2.printStackTrace(); }
	   	            }	   	            
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

 

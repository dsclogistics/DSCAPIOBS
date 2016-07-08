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

public class OpenReadybyid {
	public Response  OpenReadybyid (JSONObject jsonObject) throws JSONException {
		
		 Response rb = null;
		StringBuffer sb = new StringBuffer();
		StringBuffer sbn = new StringBuffer();
		String msg="";
		 Connection conn = null;	    
       JSONArray json = new JSONArray();
       JSONObject obj1 = new JSONObject();
		/*
	    if ((jsonObject.has("emp_id")) && ((jsonObject.has("observer")) || (jsonObject.has("observed"))))
	    {
	    	msg="";
	    }
	    else
	    {
			msg="Json elements empid and (observer or observed) is required for this API ";
			sb.append("{\"result\":\"FAILED\",\"resultCode\":300,\"message\":\"" +msg +"\"}");		
		 	rb=Response.ok(sb.toString()).build();
			System.out.println(sb.toString());
			return rb;
	    }
	    */
	    String empid="";
	   // if ((jsonObject.get("emp_id").toString().length()  > 0 ))
	   // {
	            if (jsonObject.has("emp_id")) empid=jsonObject.get("emp_id").toString();
	            String observer="N";
	            String observed="N";
	            String searchby="";
	            String filterby="";
	            if (jsonObject.has("observer")) observer="Y"; 
	            if (jsonObject.has("observed")) observed="Y"; 
	            if (jsonObject.has("searchby")) searchby=jsonObject.get("searchby").toString();
	            if (jsonObject.has("filterby")) filterby=jsonObject.get("filterby").toString();
	            
	            if (filterby.equals("READY")) filterby="COLLECTING";
	            if (filterby.equals("READY TO VERIFY")) filterby="COLLECTED";
	            
	            
	            


				try {
					conn= obs.obsConn().getConnection();
					// conn.setTransactionIsolation(conn.TRANSACTION_REPEATABLE_READ);
					 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
	                 msg="openreadybyid Connection Failed.";
	                sb.append("{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""+msg+"\"");
	   	          rb=Response.ok(obj1.toString()).build();
	   	          return rb;
				}
 
		 try {
		
		 String jsql="";
		 String wsql="";
		 
		 
		// if (observed.equals("Y")) 	 jsql= " right join dsc_employee d on a.dsc_observed_emp_id = d.dsc_emp_id ";
		// if (observer.equals("Y"))
		// jsql= " right join dsc_employee d on b.dsc_observer_emp_id = d.dsc_emp_id ";	
		 jsql= " right join dsc_employee d on dsc_observed_emp_id= d.dsc_emp_id ";
		  		 
         if (jsonObject.has("searchby")) 
         {	
        	 if (searchby.length() > 0)
        	 { wsql=" and (";
        	   String [] searcharray=searchby.split(" ");
        	   for (int i=0; i < searcharray.length;i++)
        	   {
        		   if (i > 0) wsql=wsql +" and ";
        		   wsql=wsql +" (d.[dsc_emp_first_name] + d.[dsc_emp_last_name] + obs_cft_title + lc.dsc_lc_name + o.[dsc_emp_first_name] + o.[dsc_emp_last_name] "+
        		         " like '%"+searcharray[i] +"%') ";	
        	   }
        	   wsql=wsql +")";
        	 }
         }
         if (jsonObject.has("filterby")) wsql=wsql +" and obs_inst_status='"+filterby +"' ";
        
         String wempid="";
         if ((jsonObject.get("emp_id").toString().length()  > 0 )) wempid=" and  o.dsc_emp_id="+empid ;

   		  String SQL = " select obs_cfi_id as ObsColFormInstID, dsc_observed_emp_id ,dsc_observer_emp_id," +
		 			   " case  when obs_inst_status = 'COLLECTING' then 'OPEN' when obs_inst_status ='COLLECTED' then 'READY TO VERIFY' "+
   				       " else obs_inst_status end as obs_inst_status,d.[dsc_emp_first_name] as Observedfirst_name," + 
		               " d.[dsc_emp_last_name] as Observedlast_name, o.[dsc_emp_first_name] as Observerfirst_name, o.[dsc_emp_last_name] as Observerlast_name,"+
		               " d.dsc_emp_adp_id as ObservedADPID,obs_cft_title as  ColFormTitle ,"+
		               " obs_cfi_start_dt as ColFormStartDateTime,  lc.dsc_lc_name as lcname,b.obs_cfi_comp_date as completeddate "+
		               " from obs_inst a "+
		               " right join obs_collect_form_inst b on a.obs_inst_id = b.obs_inst_id "+
			           " right join dsc_employee o on   b.dsc_observer_emp_id = o.dsc_emp_id  "+		               
		               " right join obs_type c on a.obs_type_id = c.obs_type_id "+ jsql +
		            //   " "-- right join dsc_employee d on a.dsc_observed_emp_id = d.dsc_emp_id "+
		            //   " right join dsc_employee d on b.dsc_observer_emp_id = d.dsc_emp_id " +
		               " right join obs_collect_form_tmplt q on q.obs_cft_id = b.obs_cft_id "+
		               " right join dsc_lc lc on lc.dsc_lc_id = a.dsc_lc_id "+
		               " where   obs_inst_del_yn ='N'  " + wempid +wsql +
		            //   -- and  d.dsc_emp_id="+empid + wsql +
		               "  order by b.obs_cfi_comp_date desc , a.dsc_lc_id";		  		
   	         
	          
  // System.out.println(">>> OPEN READYL:" + SQL);
	        
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
	                  msg="openreadybyid DB Query Failed.";
	                sb.append("{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""+msg+"\"");
	   	            rb=Response.ok(obj1.toString()).build();
	   	            return rb;
				   }
	    // } // if non blank input values
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

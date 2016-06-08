package com.dsc.util;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dsc.dao.obs;
 
public class verifyCFTID {
 
	// in this class do 2 things.
	// 1) Go to obs_collect_form_tmplt and get
	// obs_type_id, obs_cust_id, obs_lc_id
 
	// 2) verify if obs_collect_form_inst has the CFT ID. 
	//    if exists then it's an exisiting form get CFI, inst, observerid
	
	     public int [] vCFTID(String obscftid, String obsintid) throws Exception {
	    	 
 
 
	    	 
	    	 int obsids[] = new int [25];
	    	 for (int x=0; x < 24;x++)
	    	 {
	    		 
	    		 obsids[x]=-99;
	    	 }
   
	    	 Statement stmt = null;
 
	     	 
			 Connection conn = null;
 
 
			 try {
				conn= obs.obsConn().getConnection();
				conn.setTransactionIsolation(conn.TRANSACTION_READ_UNCOMMITTED);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    	
	      	 
	    	 
	    	 // first get OBS_COLLECT_FORM_TMPLT
	    	 
           
			 String  query= " select  obs_type_id,dsc_cust_id,dsc_lc_id from obs_collect_form_tmplt a " + 
						  " WITH (NOLOCK) where a.obs_cft_id="+obscftid ;
		//	 System.out.println("sql is" + query);
	    	    try {
	    	    	stmt = conn.createStatement();
	    	        ResultSet rs = stmt.executeQuery(query);
	    	        while (rs.next()) {
	    	        	obsids[0] = rs.getInt(1);
	    	        	obsids[1] = rs.getInt(2);
	    	        	obsids[2] = rs.getInt(3);
	    	        	obsids[6] = Integer.parseInt(obscftid);	    	        	
 
	    	//      System.out.println("OBSIDS:"+obsids[0] + "\t" + obsids[1] +
	    	//                              "\t" + obsids[2]);
	    	        }
	    	        rs.close();
	    	    } catch (SQLException e ) {
	    	    	e.printStackTrace();
	    	    } finally {
	    	        if (stmt != null) { stmt.close(); }
	    	        //if (conn != null) { conn.close();}
	    	    }
	 
	    	  //  if (obsintid.trim() != null)
	    	    if (obsintid.length() > 1)
	    	   {
	    	    // Verify if new or existing form. If obsinst id found then an update 
		    	  query = " select  obs_cfi_id,obs_inst_id,dsc_observer_emp_id  from obs_collect_form_inst a "+
						  " WITH (READPAST) where a.obs_inst_id="+obsintid ;
		   // 	  System.out.println("sql is" + query);
		    	    try {
		    	    	stmt = conn.createStatement();
		    	        ResultSet rs = stmt.executeQuery(query);
		    	        while (rs.next()) {
		    	        	obsids[3] = rs.getInt(1);
		    	        	obsids[4] = rs.getInt(2);
		    	        	obsids[5] = rs.getInt(3);
		    	        	obsids[11] = 0;

	 
		 //   	            System.out.println(obsids[3] + "\t" + obsids[4] +
		 //   	                               "\t" + obsids[5]);
		    	        }
		    	        rs.close();
		    	        stmt.close();
		    	        conn.close();
		    	    } catch (SQLException e ) {
		    	    	e.printStackTrace();
		    	    } finally {
		    	        if (stmt != null) { stmt.close(); }
		    	        if (conn != null) { conn.close();}
		    	    }
	    	   } // not null obsinitd
	    	   
		         if (conn != null) 
		         {
		      	   try{
		      		   conn.close();
		      		  } catch(SQLException e)
		      	      {e.printStackTrace(); }
		         } 
		         return obsids;
          
	   		
	}
}

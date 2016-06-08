package com.dsc.util;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.codehaus.jettison.json.JSONObject;

import com.dsc.dao.obs;
 
public class insertOBSInst {
 
	// in this class do 2 things.
	// 1) Go to obs_collect_form_tmplt and get
	// obs_type_id, obs_cust_id, obs_lc_id
 
	// 2) verify if obs_collect_form_inst has the CFT ID. 
	//    if exists then it's an exisiting form get CFI, inst, observerid
	
	// input to this function will be JSON object of observation and an arry of ids
	
	     public    int []  insOBSInst(JSONObject jsonObject, int [] obsids) throws Exception {
	    	 	 
	    	 int instid=0;
	    	 int cfiid=0;
	    	 
   
	    	 Statement stmt = null; 
			 Connection conn = null;
			 Connection conn1=null;
			 Statement stmt1 = null; 
	      	 String formstatus="COLLECTING";
 
			 try {
				conn= obs.obsConn().getConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    	

	     // 	System.out.println("formstatus sent:"+jsonObject.get("ColFormStatus").toString());
	      	 if (jsonObject.get("ColFormStatus").toString().equals("SAVE")) 
	      		 { formstatus="COLLECTING";
	      		 }
	      	 if (jsonObject.get("ColFormStatus").toString().equals("SUBMIT")) 
      		 { formstatus="COLLECTED";
      		 }
	      	 if (jsonObject.get("ColFormStatus").toString().equals("REVIEWED")) 
      		 { formstatus="COMPLETED";
      		 }

	    //  	System.out.println("formstatus after veriy is:"+formstatus);	      	 
	      	 int obdid=Integer.parseInt(jsonObject.get("ObservedEmployeeId").toString()); 
	      	 int obvid=Integer.parseInt(jsonObject.get("ObserverEmployeeID").toString());
	      	 
	          conn.setAutoCommit(false);
	    	  boolean committed = false;

             // finally {
             //  if (!committed) conn.rollback();
             // }	      	 
	      	  
	    //	System.out.println("$$$$ Existing OBSids 4 value is:"+obsids[4]); 
	    	 // array 3 and 4 have -99 then new form so goahead and add it
           if (obsids[4] < -1)
           {   
	        String insert="insert into obs_inst values(" +  
	              obsids[0]+","+obsids[2]+","+obsids[1] +
	              ","+obdid+",GETDATE(),'" +formstatus +"','N',null,'')";
		   	    //  System.out.println( " Insert is:"+insert); 

		    	    try {
		    	    	stmt = conn.createStatement();
		    	    	PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
		    	        ps.executeUpdate();
		    	        ResultSet rs = ps.getGeneratedKeys();
		    	        rs.next();
		    	        instid = rs.getInt(1);	
		    	        obsids[4]=instid;
		    	         rs.close();
		    	         ps.close();
    	        //    System.out.println("**** HELLO INSERTED KEY Inserted Key is:"+instid);
    	            
    	            // insert into obs_collect_form_inst. update last column only if the status is completed
    	              insert="insert into obs_collect_form_inst values(" +  
    	            		  instid+","+obsids[6]+","+ obvid+",GETDATE() " ;
    	              if (!formstatus.equals("COLLECTING")) 
    	              {
    	            	   insert =insert +", getdate() ";
    	              }
    	              else
    	              {
    	            	  insert =insert +", null ";
    	              }
    	            	  
    	  	           //   instid+","+obsids[6]+",2110,GETDATE(),GETDATE(),GETDATE())";
    	        //      System.out.println("**** HELLO INSERTED KEY Inserted Key is:"+instid);  	              
    	              if (formstatus.equals("SUBMIT"))
    	              {
    	            	  insert=insert+",GETDATE())";
    	              }
    	              else
    	              {
    	            	  insert=insert+",null)";
    	              }
    			  // 	     System.out.println( " Insert is:"+insert); 
    	              ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
    	              ps.executeUpdate();
    	              rs = ps.getGeneratedKeys();
		    	        rs.next();
		    	         cfiid = rs.getInt(1);	
		    	         obsids[3]=cfiid;
		    	     //    System.out.println("**** HELLO INSERTED KEY for cfiids:"+cfiid);
		    	         rs.close();
		    	         ps.close();
                        conn.commit();
		    	        committed = true;

		    	    } catch (SQLException e ) {
		    	    	e.printStackTrace();
		    	    } finally {
		    	    	 if (!committed) {conn.rollback(); 
		    	    //	 System.out.println(" Rolling back since there was no cOMMIT");
		    	    	 }
		    	        if (stmt != null) { stmt.close(); }
		    	        
		    	        if (conn != null) { conn.close();}
		    	    }	              
           
	 
			         if (conn != null) 
			         {
			      	   try{
			      		   conn.close();
			      		  } catch(SQLException e)
			      	      {e.printStackTrace(); }
			         } 					  
			  
		 
				  
           }
           else
        	   
           {
        	   try {
   				conn1= obs.obsConn().getConnection();
   				} catch (SQLException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   				} catch (Exception e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   				}    	
        	   // update the collection form if alredy exists
           	   String insert= "update obs_inst set obs_inst_status ='"+ formstatus +
           			   "' where   obs_inst_id="+ obsids[4] ;
    //        	System.out.println("$$$$ Existing OBS_INST. So Update form status as:"+insert);
         	   try
         	   {
   	    		stmt1=conn1.createStatement();
   	    		 stmt1.executeUpdate(insert);   
                 if (stmt1 != null) { stmt1.close(); }
                 if (conn1 != null) { conn1.close();}
         	   }
         	   catch (Exception ee)
         	   {
                   if (stmt1 != null) { stmt1.close(); }
                   if (conn1 != null) { conn1.close();}
         	   }
           }
           if (stmt != null) { stmt.close(); }
           if (conn != null) { conn.close();}
           if (stmt1 != null) { stmt1.close(); }
           if (conn1 != null) { conn1.close();}
		        // return cfiid;
               return obsids;
          
	   		
	}
}

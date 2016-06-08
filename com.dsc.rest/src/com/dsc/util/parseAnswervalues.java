
package com.dsc.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.json.simple.JSONObject;

import com.dsc.dao.obs;

public class parseAnswervalues   {
	
	// public void parseAnswervalue(JSONArray jsonArr, int [] obsids, Connection conn1) throws JSONException {
		public void parseAnswervalue(JSONArray jsonArr, int [] obsids) throws JSONException {	 
		
   	 Statement stmt = null; 
		  Connection conn = null;
 

		 try {
			 if (conn == null)
			conn= obs.obsConn().getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated constructor stub
	   //	System.out.println( " json Array length:"+jsonArr.length()); 
        for(int i=0; i<jsonArr.length(); i++)
        { 
            org.codehaus.jettison.json.JSONObject ansobj = jsonArr.getJSONObject(i);
         //  System.out.println("Answervalues " + i + " for question  is: "+jsonArr.get(i));
        	 // insert into obs_collect_form_inst
           if(ansobj.has("answerText") && ansobj.has("obscolanswgt"))
           {
        	 try
        	 {
        	   String insert= "delete from obs_col_form_inst_ans where obs_cfiq_id="+obsids[8]  ;
        	   // remove the line below since we need to delete all answers for this id and insert again all answers
        	   // assuming the user will send all answers back.
        	//		   " and obs_cfia_ans_val='"+ansobj.getString("answerText").toString() +"'";
        	//   System.out.println("Delete form obs_col_form_inst_ans as: "+insert);
        	// 20160419  PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        	// 20160419  int  rows =ps.executeUpdate();
        	 
   	    		stmt=conn.createStatement();
   	    		int rows=stmt.executeUpdate(insert);  
   	    		System.out.println("Deleted "+rows +" form obs_col_form_inst_ans as: "+insert);
   	    		
        	  //  ps.close();
        	   
        	     String anstxt=ansobj.getString("answerText").toString();
        	     anstxt.replace("'","\"");
        	     insert="insert into obs_col_form_inst_ans values(" +  
        	    		  obsids[8]+",'" +anstxt+"'," +
        			  // obsids[8]+",'" + ansobj.getString("answerText").toString() +"'," +
        			   ansobj.getString("obscolanswgt") +","+ ansobj.getString("answerOrder")+")";
        	     
        	      rows=stmt.executeUpdate(insert);  
        	//     System.out.println("Insert into form obs_col_form_inst_ans as: "+insert);
        	   //  ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
               //  ps.executeUpdate();
               //  ps.close();
	            // 20160419   ResultSet rs = ps.getGeneratedKeys(); 
	            // 20160419   rs = ps.getGeneratedKeys();
	    	     // 20160419  rs.next();    
	    	       
	    	      //   System.out.println("**** HELLO INSERTED KEY for cfiids:"+rs.getInt(1));
	    	     // 20160419    rs.close();
        	 }
        	 catch  (SQLException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        	 }
        	 
           } // end of if 
        }

        if (conn != null) 
        {
     	   try{
     		   conn.close();
     		  } catch(SQLException e)
     	      {e.printStackTrace(); }
        } 
         
	}
        
	   		
	}

 
 
package com.dsc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.codehaus.jettison.json.JSONObject;

import com.dsc.dao.obs;
 
public class insupdOBSQuest {
 
 // This Class update/inserts questions form submitted by the users.
 
	    	 	 
	   public    int[] insupdOBSQuests(JSONObject jsonObject, int [] obsids) throws Exception {
	    	 int cfiid=0;
	    	 Connection conn = null;
   
	    	 Statement stmt = null; 
	    	 ResultSet rs =null;
 
 
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
  
			  // obsids 12 0 if the answertyype is a yes_no
			 obsids[12]=-99;
			   if ( jsonObject.getString("AnswerType").equals("YES_NO"))
			   {
				   obsids[12]=0;
			   }
			    
 		   
	      	 // do an update if the question exists else do an insert
			 
			 String query="update obs_col_form_inst_quest set obs_cfiq_comment='" +
					 jsonObject.getString("comments").toString() +"',obs_cfiq_version=obs_cfiq_version + 1," +
			         " obs_cfiq_upd_dtm=GETDATE()  where " +
			         "obs_cfi_id="+obsids[3] + " and obs_question_id=" +obsids[10];
 

		   	 //    System.out.println( " From Save update OBS_COL_FORM_INST_QUEST  is:"+query); 
 
		   	   PreparedStatement ps=null;
		    	    try {
		    	    	stmt=conn.createStatement();
		    	    	int rows=stmt.executeUpdate(query);
    	                 if (rows <= 0)
    	                 {
    	                	 String cmnts=  jsonObject.getString("comments").toString();
    	                	 String qtxt= jsonObject.getString("QuestionText").toString();
    	                	 String showna=jsonObject.getString("showsNA").toString();
    	                	 System.out.println("**** SHOWNA BEFORE:"+showna);  
    	                	 if (showna.equals("false"))
    	                	 {
    	                		 showna="N";
    	                	 }
    	                	 else
    	                	 {
    	                		 showna="Y";
    	                	 }
    	                	 System.out.println("**** SHOWNA AFTER:"+showna);  
    	                	 qtxt.replace("'", "\"");
    	                	 cmnts=cmnts.replaceAll("'", "''");
    	                	 cmnts=cmnts.replaceAll("\"", " ");
    	                	 cmnts=cmnts.replaceAll("\\\\",""); 	                	 
    	            
    	                	 // insert into obs_collect_form_inst
    	                	 String insert="insert into obs_col_form_inst_quest("+
    	                	 "obs_cfi_id,obs_question_id,obs_cfiq_quest_full_text,obs_cfiq_quest_wgt,obs_cfiq_comment,"+
    	                	 "obs_cfiq_quest_order,obs_cfiq_form_sect_name,obs_cfiq_form_sub_sect_name," +
    	                	 "obs_cfiq_comment_mand_yn,obs_cfiq_na_yn,obs_cfiq_mult_ans_yn,obs_cfiq_init_ans_dtm,"+
    	                	 "obs_cfiq_version,obs_cfiq_upd_dtm)" +
    	                	 "values(" +  
    	  	              obsids[3]+","+obsids[10]+ ",'"+qtxt +"'," +
    	                	 jsonObject.getString("obscolformquestwgt") +",'" + cmnts.trim() +
    	  	          //  jsonObject.getString("comments").toString() +"',"	+jsonObject.getString("QuestionOrder") +
    	                  "',"	+jsonObject.getString("QuestionOrder") +	 
    	  	            ",'"+jsonObject.getString("SectionName") +"','" +jsonObject.getString("SubSectionName") +
    	  	            "','N','" +showna +
    	  	            // 2016/05/25 N
    	  	            " ','N',GETDATE(),0,GETDATE())";
    	              	 System.out.println("**** no record found so do insert:"+insert);   
    	                 ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
                         ps.executeUpdate();
                     
    	               //  ResultSet rs = ps.getGeneratedKeys();
    	                rs = ps.getGeneratedKeys();
		    	        rs.next();    
		    	         obsids[8]= rs.getInt(1);	;
		    	   //      System.out.println("**** INSERTED  New Row KEY OBSIDS array 8 is:"+obsids[8]);
		    	         ps.close();
		    	         rs.close();
    	                 }
    	                 else
    	                 {
    	                	  if (ps != null) {ps.close();}
    	                	 // get cfq and ukpdate obsids[8]
    	                	 query="select distinct obs_cfiq_id  from  obs_col_form_inst_quest WITH (NOLOCK) where " +
    	        					 "obs_cfi_id="+obsids[3] + " and obs_question_id=" +obsids[10]  ;
    	          //      	 System.out.println("****Get cfq id as:"+query);   
    	                	 stmt = conn.createStatement();   	 	    	        
    	                    rs = stmt.executeQuery(query);
    	 	    	        while (rs.next()) {
    	 	    	        	obsids[8] = rs.getInt(1); 
    	 	    	        }
    	 	    	        rs.close();
    	 	    	        stmt.close();
    	  
    	                	 
    	                 }
 
		    	    } catch (SQLException e ) {
		    	    	e.printStackTrace();
		    	    	if (rs != null) {rs.close();}
		    	    } finally {
		    	        if (stmt != null) { stmt.close(); }
		    	       //  if (conn != null) { conn.close();}
		    	        if (rs != null) {rs.close();} 
		    	        
		    	       
		    	    }	              
           
	 
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


package com.dsc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.dsc.dao.obs;
 
public class insertnewcollinstance {
	
	     public    String []  insertnewcollinstance(JSONObject jsonObject) throws Exception {
	    	 
	    	 // *********** ****************************************************************************
	    	 //             INITIALIZE VARIABLES NEEDED WITH NULL OR FROM JSON DATA	    
	    	 String [] msg= new String[2];
	    	 String formstatus="COLLECTING";    	 	 
	    	 if (jsonObject.get("ColFormStatus").toString().equals("SAVE")) 
      		 { formstatus="COLLECTING";
      		 }
	    	 if (jsonObject.get("ColFormStatus").toString().equals("SUBMIT")) 
      	 	{ formstatus="COLLECTED";
      	 	}
	    	 if (jsonObject.get("ColFormStatus").toString().equals("REVIEWED")) 
      	 	{ formstatus="COMPLETED";
      	 	}
       	 
	    	 int obdid=Integer.parseInt(jsonObject.get("ObservedEmployeeId").toString()); 
	    	 int obvid=Integer.parseInt(jsonObject.get("ObserverEmployeeID").toString());
	    	 int obscftid=Integer.parseInt(jsonObject.get("OBSColFormID").toString());	 
	    	 int lcid=Integer.parseInt(jsonObject.get("DSC_LC_ID").toString());	
	    	 int custid=Integer.parseInt(jsonObject.get("customer").toString());
             int obstypeid=-1;
             int instid=0;
             int cfiid=0;
	    	 // ******************************************************************************************************
	         //            SELECT obs_type_id  FROM OBS_COLELCT_FORM_TMPLT FOR THIS CFTID
			  String  query= " select  obs_type_id  from obs_collect_form_tmplt a  where a.obs_cft_id="+obscftid ;
        	  Connection conna = null;
        	  Statement stmta = null; 
 			 try 
 			 {
 				conna= obs.obsConn().getConnection();
 			 } 
             catch (Exception e) 
 			 {
 				// TODO Auto-generated catch block  RETURN ERROR ??????????
 				// e.printStackTrace();
              msg[0] ="-1";
   			  msg[1] = "{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""  +
		       			"Cannot acquire connection to the Database "  +"\"}";   
   			  return msg;
 			 }
 			 
	          try
	          {
    	      	  //   System.out.println( "**IN INSERTNEWCOLLINSTANCE: New Instance so get OBS TYPE Insert is:"+query);
	    	    	stmta = conna.createStatement();
	    	        ResultSet rsa = stmta.executeQuery(query);
	    	        
	    	        while (rsa.next()) 
	    	        {
	    	        	obstypeid=rsa.getInt(1);
	    	        }
	    	        rsa.close();
	    	        stmta.close();
	    	        conna.close();
	    	        if (obstypeid < 0)
	    	        {
	                    msg[0] ="-1";
	         		    msg[1] = "{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""  +
	      		       			"Cannot fine obs typ id for Collection form: "+obscftid  +"\"}";   
	         		    return msg;
	    	        }
     	  
	          }
	          catch (Exception e)
	          {
	    	        if (stmta != null) { stmta.close(); }    	        
	    	        if (conna != null) { conna.close();}
	        	  // return error now
	                msg[0] ="-1";
	     		    msg[1] = "{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""  +
	  		       			"Cannot acquire connection to the Database "  +"\"}";   
	     			  return msg;
	          }
	          finally 
	          {
	    	        if (stmta != null) { stmta.close(); }    	        
	    	        if (conna != null) { conna.close();}

	    	    }	              
             // ************************************************************************************
	         //   DO ALL INSERTS NOW FOR THIS NEW COLLETION FORM INSTANCE USERS WANTS TO SAVE FOR
	         //   OBSINST, OBSCOLLINSTFOR, COLLQUEST, COLLANS IN ONE COMMIT
    
	    	 Statement stmt = null; 
			 Connection conn = null;
			 Connection conn1=null;
			 Statement stmt1 = null; 
			 ResultSet rs = null;
 
 
			 try
			 {
				conn= obs.obsConn().getConnection();
			 } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
                msg[0] ="-1";
     		    msg[1] = "{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""  +
  		       			"Cannot acquire connection to the Database "  +"\"}";   
     		    return msg;
			 }    	
       	 
	          conn.setAutoCommit(false);
	    	  boolean committed = false;
 
	    	    // INSERT INTO OBS_INST TABLE ********************************************
	       
	             String insert="insert into obs_inst values(" +  
	            		 obstypeid+","+lcid+","+custid  +
	              ","+obdid+",GETDATE(),'" +formstatus +"','N',null,'')";
	         //    System.out.println( "**IN INSERTNEWCOLLINSTANCE: INSERT INTO OBS_INST:"+insert); 

		   	      // Insert Row into OBS_INST  1st step
		    	    try 
		    	    {
		    	    	 
		    	    	PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
		    	        ps.executeUpdate();
		    	        rs = ps.getGeneratedKeys();
		    	        rs.next();
		    	        instid = rs.getInt(1);	
		    	        rs.close();
		    	        ps.close();
  
		    	         //    System.out.println("****   INSERTED KEY OBSINST is:"+instid);
    	            
    	                 // INSERT INTO OBS_COLLECT_FORM  ***************************************************
    	               insert="insert into obs_collect_form_inst values(" +  
    	            		    instid+","+obscftid+","+ obvid+",'"+jsonObject.get("ColFormStartDateTime").toString().trim() +"',null ";
    	            //		  instid+","+obscftid+","+ obvid+",GETDATE(),null ";
    	            //   System.out.println( "**IN INSERTNEWCOLLINSTANCE: INSERT INTO OBS_INST:"+insert);  	              
    	                if (formstatus.equals("SUBMIT"))
    	                {
    	            	  insert=insert+",GETDATE())";
    	                }
    	                else
    	                {
    	            	  insert=insert+",null)";
    	                 }
    	            //    System.out.println( "**IN INSERTNEWCOLLINSTANCE:INSERT New COLL FORM Instance:"+insert);
    	                ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
    	                ps.executeUpdate();
    	                rs = ps.getGeneratedKeys();
		    	        rs.next();
		    	        cfiid = rs.getInt(1);	
		    	         rs.close();
		    	         ps.close();
		    	         
		    	      // ************************************************************************************** 
		    	      // INSERT INTO OBS_COL_FORM_INST_QUEST FOR EACH QUESTION IN JSON ARRAY 
		    	         
		    	         JSONArray jsonArr = (JSONArray) jsonObject.getJSONArray("questions");
		    	//         System.out.println( " Number of Questions:"+jsonArr.length()); 
		    	         
		    	         // Loop thru all questions and answers within each of these questions 
		    	         int qid=0;
		    		     for(int i=0; i< jsonArr.length(); i++)
		    		        { 
		    		        	 System.out.println("The " + i + " element of the array: "+jsonArr.get(i));
		    		        	JSONObject s1 =  (JSONObject) jsonArr.get(i);
	    	                	 String cmnts=  s1.getString("comments").toString();
	   
	    	                	 cmnts=cmnts.replaceAll("'", "''");
	    	                	 cmnts=cmnts.replaceAll("\"", " ");
	    	                	 cmnts=cmnts.replaceAll("\\\\","");
	    	                	 
	    	                	  System.out.println("T&&&&&&&&& ###### comment is "+cmnts);
	    	                	 String qtxt= s1.getString("QuestionText").toString();
	    	                	 qtxt=qtxt.replace("'", "''");
	    						 qtxt=qtxt.replaceAll("\\r\\n","");
	    						 
	    	                	 String showna= s1.getString("showsNA").toString();
	    	                //	 System.out.println("**** SHOWNA BEFORE:"+showna);  
	    	                	 if (showna.equals("false"))
	    	                	 {
	    	                		 showna="N";
	    	                	 }
	    	                	 else
	    	                	 {
	    	                		 showna="Y";
	    	                	 }
	    	               // 	 System.out.println("**** SHOWNA AFTER:"+showna);
		 	                	 insert="insert into obs_col_form_inst_quest " +
		 	                		"	       ([obs_col_form_quest_id] "+
		 	                		 "          ,[obs_cfi_id] "+
		 	                		 "          ,[obs_question_id] "+
		 	                		 "          ,[obs_cfiq_quest_full_text] "+
		 	                		 "          ,[obs_cfiq_quest_wgt] "+
		 	                		 "          ,[obs_cfiq_comment] "+
		 	                		 "          ,[obs_cfiq_quest_order] "+
		 	                		 "          ,[obs_cfiq_form_sect_name] "+
		 	                		 "          ,[obs_cfiq_form_sub_sect_name] "+
		 	                		 "          ,[obs_cfiq_comment_mand_yn] "+
		 	                		 "          ,[obs_cfiq_na_yn] "+
		 	                		 "          ,[obs_cfiq_mult_ans_yn] "+
		 	                		 "          ,[obs_cfiq_init_ans_dtm] "+
		 	                		 "          ,[obs_cfiq_version] "+
		 	                		 "          ,[obs_cfiq_upd_dtm]) "+
	    						   " values ("+  (int) s1.get("UniqueQuestionId") +","+
		 	                			cfiid+","+(int) s1.get("QuestionId")+ ",'"+qtxt +"'," +
		 	    	                	 s1.getString("obscolformquestwgt") +",'" + cmnts +
		 	    	                  "',"	+s1.getString("QuestionOrder") +	 
		 	    	  	            ",'"+s1.getString("SectionName") +"','" +s1.getString("SubSectionName") +
		 	    	  	            "','N','"+showna +
		 	    	  	            // N'
		 	    	  	            "','N',GETDATE(),0,GETDATE())";
		 	              //  	 System.out.println( "**IN INSERTNEWCOLLINSTANCE: New  QUESTION  Insert is:"+insert);		 	                	 
		     	                 ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
		    	                 ps.executeUpdate();
		    	                 rs = ps.getGeneratedKeys();
				    	         rs.next();
				    	         qid = rs.getInt(1);	
				    	         rs.close();
				    	         ps.close();
				    	         
				    	         
				    	        // INSERT INTO OBS_COL_FORM_INST_ANS FOR EACH ANSWER FOR THE QUESTION ABOVE  
				    	       	 int aid=0;
				            	 if(s1.has("answervalues") ) 
				            	 {
				            		 JSONArray ansval = (JSONArray) s1.getJSONArray("answervalues");	
				            		 
				            		  for(int av=0; av< ansval.length(); av++)
				            	        { 
				            	            JSONObject ansobj = ansval.getJSONObject(av);
				            	            
				            	            if(ansobj.has("answerText") || ansobj.has("obscolanswgt"))
				            	            {	
				            	            	
				            	                String anstxt="";
				            	                String wgt="";
				            	                String ord="";
				            	                
				            	                if(ansobj.has("answerText")) {anstxt=ansobj.getString("answerText").toString();}
				            	                if(ansobj.has("obscolanswgt")) {wgt=ansobj.getString("obscolanswgt").toString();}
				            	                if(ansobj.has("answerOrder")) {ord=ansobj.getString("answerOrder").toString();}
				            	            	
				            	            	  insert="insert into obs_col_form_inst_ans values(" +  
				            	            			  qid+",'" +anstxt+"','" +wgt+"','"+ord+"')";
				            	            /*
				            	            if(ansobj.has("answerText") || ansobj.has("obscolanswgt"))
				            	            {		            	       
				            	                String anstxt=ansobj.getString("answerText").toString();
				            	            	insert="insert into obs_col_form_inst_ans values(" +  
				            	            			qid+",'" +anstxt+"'," +
				            	            			ansobj.getString("obscolanswgt") +","+ ansobj.getString("answerOrder")+")";
				            	            */
				            	            //	 System.out.println( "**INsert answervalues for Question:"+s1.get("QuestionId")+" as "+insert );	
				            	            	 ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
				            	            	 ps.executeUpdate();
				            	            	 rs = ps.getGeneratedKeys();
				            	            	 rs.next();
				            	            	 aid = rs.getInt(1);	
				            	            	 rs.close();
				            	            	 ps.close();
				            	            	 
				            	             }   			            	            
				            	        } // for each answer value
				            	 } // has answervalues
		    		        }  // for each question
		    		     // NOW COMMIT THE ENTIRE TRANSACTION SET IF NOT ROLL BACK
                         conn.commit();
		    	         committed = true;
			    	     if (stmt != null) { stmt.close(); }
			    	     if (conn != null) { conn.close();}
			    	     if (rs != null)  { rs.close(); }
		    	    }
		    	    catch (SQLException e )
		    	    {
		    	    	 if (!committed) {conn.rollback(); 
		    	    	 committed = true;
		                 msg[0] ="-1";
		      		     msg[1] = "{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""  +
		   		       			"Could not commit updates to Database for CFTID:" +obscftid +"\"}";
		    	    	 System.out.println(" Rolling back since there was no cOMMIT FROM SQL EXCPTION");}
		    	    	e.printStackTrace();
		    	    	return msg;
		    	    } finally {
		    	    	 if (!committed) {conn.rollback(); 
		    	    	 System.out.println(" Rolling back since there was no cOMMIT");}
		    	        if (stmt != null) { stmt.close(); }
		    	        if (conn != null) { conn.close();}
		    	        if (rs != null)  { rs.close(); }
		    	    }	              
           
	                 msg[0] ="0";
	      		     msg[1] = "{\"result\":\"SUCCESS\",\"resultCode\":100,\"message\":\""  +
	   		       			"Added CFTID:" +obscftid +" to the Collection with instid as:"+ instid +" OBS_CFI_ID is:"+cfiid+"\"}";
	    	    	 
		        
             return msg;
          
	   		
	} // end of class instance
} // end of class

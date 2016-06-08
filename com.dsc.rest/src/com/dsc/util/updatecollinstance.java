
package com.dsc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.dsc.dao.obs;
 
public class updatecollinstance {
	
	     public    String []  updatecollinstance(JSONObject jsonObject) throws Exception {
	    	 
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
	    	 int instid=Integer.parseInt(jsonObject.get("OBSInstID").toString());
	    	 int obscfiid=Integer.parseInt(jsonObject.get("OBSColFormInstID").toString());
	    	 
             int obstypeid=0;
             int obscfiq=0;
             int cfiid=0;
             
               
             // ************************************************************************************
	         //   DO ALL UPDATES NOW FOR THIS NEW COLLETION FORM INSTANCE USERS WANTS TO SAVE FOR
	         //   OBSINST, OBSCOLLINSTFOR, COLLQUEST, COLLANS IN ONE COMMIT
    
	    	 Statement stmt = null; 
			 Connection conn = null; 
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
  		       			"Cannot acquire connection to the Database when updating collection instance "  +"\"}";   
	    	     if (conn != null) { conn.close();}
     		    return msg;
			 }    	
       	 
	         // conn.setAutoCommit(false);
	    	//  boolean committed = false;
 
	    	    // UPDATE  OBS_INST TABLE with comments ********************************************
	       
          	      String update= "update obs_inst set obs_inst_status ='"+ formstatus +
           			   "' where   obs_inst_id="+ instid ;
          	       update=update +"; update obs_collect_form_inst set obs_cfi_last_upd_dt =getdate() ";                  
          	       if (!formstatus.equals("COLLECTING")) {update =update +", obs_cfi_comp_date =getdate() ";}
					update=update +" where obs_cfi_id="+obscfiid;
		   	    //  System.out.println( " Update as:"+update); 

		   	      // ************  START COMMITMENT CONTROL NOW **************************
	    	         conn.setAutoCommit(false);
	    	    	 boolean committed = false;
		    	    try 
		    	    {
   	            	 //   System.out.println( "**IN UPDATECOLLINSTANCE: Update OBS_INST as:"+update);
		    	    	stmt = conn.createStatement();
		    	    	stmt.executeUpdate(update);
		    	        // stmt.close();
    	            
		    	      // ************************************************************************************** 
		    	      // INSERT INTO OBS_COL_FORM_INST_QUEST FOR EACH QUESTION IN JSON ARRAY 
		    	         
		    	         JSONArray jsonArr = (JSONArray) jsonObject.getJSONArray("questions");
		    	     //    System.out.println( " Number of Questions:"+jsonArr.length()); 
		    	         
		    	         // Loop thru all questions and answers within each of these questions 
		    	         int qid=0;
		    	       	 
		    		     for(int i=0; i<jsonArr.length(); i++)
		    		        { 
		    		        	// System.out.println("The " + i + " element of the array: "+jsonArr.get(i));
		    		        	JSONObject s1 =  (JSONObject) jsonArr.get(i);
	    	                	 String cmnts=  s1.getString("comments").toString();
	    	                	 cmnts=cmnts.replaceAll("'", "''");
	    	                	 cmnts=cmnts.replaceAll("\"", " ");
	    	                	 cmnts=cmnts.replaceAll("\\\\","");
	    	                	 //cmnts=cmnts.replaceAll("\\\\\\"," ");
	    	                	// System.out.println("T&&&&&&&&& ###### comment in update is "+cmnts);    	     
	    	                	// cmnts=cmnts.replaceAll("\\", " ");
	    	                	 String qtxt= s1.getString("QuestionText").toString();
	    	                	 qtxt=qtxt.replaceAll("\\r\\n","");
	    	                	 qtxt=qtxt.replace("'", "''");
	                             qid=Integer.parseInt(s1.getString("QuestionId").toString());
	                              
	    	        			 update ="update obs_col_form_inst_quest set obs_cfiq_comment='" +
	    	        					// s1.getString("comments").toString() +"',obs_cfiq_version=obs_cfiq_version + 1," +
	    	        					  cmnts.trim() +"',obs_cfiq_version=obs_cfiq_version + 1," +
	    	        			         " obs_cfiq_upd_dtm=GETDATE()  where " +
	    	        			         "obs_cfi_id="+obscfiid + " and obs_cfiq_id="+ s1.getString("CFIQID") + " and "+
	    	        			         " obs_question_id=" +qid;	
	    	        		//	 System.out.println( "**IN UPDATECOLLINSTANCE: Update OBS_COL_FORM_INST_QUEST as:"+update);
	    			    	     stmt.executeUpdate(update);
	    			    	    // stmt.close();
	    			    	    
	    			    	    // NOW GO GET THE OBS_CFIQ_ID FROM THE INST QUEST TO DELETE AND INSERT NEW ANSWER
	     	                	 String query="select distinct obs_cfiq_id  from  obs_col_form_inst_quest  where " +
	    	        					 "obs_cfi_id="+obscfiid + " and "+
	     	                			 " obs_question_id=" +qid  ;
	     	                	 
	     	              //  	System.out.println( "**IN UPDATECOLLINSTANCE: GET CFIQ ID FROM UPDATE from OBS_COL_FORM_INST_QUEST   as:"+query);    	 	    	        
	    	                     rs = stmt.executeQuery(query);
	    	 	    	         while (rs.next()) {
	    	 	    	        	 obscfiq  = rs.getInt(1); 
	    	 	    	         }
	    	 	    	        rs.close();
	    	 	    	        // stmt.close();
	    	 	    	        
	    	 	    	        // DELETE THE ANSWER VALUE FOR THIS CFIQ ID FROM ANSWERS
	    	 	    	        
	    	 	    	        query ="delete from obs_col_form_inst_ans where obs_cfiq_id="+s1.getString("CFIQID") ;
	    	 	    	  //     System.out.println( "**IN UPDATECOLLINSTANCE: Delete OBS_COL_FORM_INST_ANS as:"+query);
	    	 	    	        stmt.executeUpdate(query);

				    	        // INSERT INTO OBS_COL_FORM_INST_ANS FOR EACH ANSWER FOR THE QUESTION ABOVE  
				    	       	 int aid=0;
				            	 if(s1.has("answervalues") ) 
				            	 {
				            		 JSONArray ansval = (JSONArray) s1.getJSONArray("answervalues");	
				            //		 System.out.println( " Number of Answers:"+ansval.length() +" For questionid:"+qid); 
				            		  for(int av=0; av < ansval.length(); av++)
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
				            	                
				            	            //	String insert="insert into obs_col_form_inst_ans values(" +  
				            	            //			obscfiq+",'" +anstxt+"'," +
				            	           // 			ansobj.getString("obscolanswgt") +","+ ansobj.getString("answerOrder")+")";
				            	            	
				            	            	String insert="insert into obs_col_form_inst_ans values(" +  
				            	            			s1.getString("CFIQID")+",'" +anstxt+"','" +wgt+"','"+ord+"')";
				            	            		//	obscfiq+",'" +anstxt+"','" +wgt+"','"+ord+"')";
				            	            			 
				            	            	
				            	      //      	System.out.println( "**IN UPDATECOLLINSTANCE: INSERT ANSWER as:"+insert);
				            	            	stmt.executeUpdate(insert);
							    	          
				            	             }   
				            	            else
				            	            {
				            	            	// System.out.println( " Questionid:"+qid +" does not have answer text or obscolanswgt");
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
		    	    catch (Exception e )
		    	    {
		    	    	 if (!committed) {conn.rollback(); 
		    	    	 committed = true;
		                 msg[0] ="-1";
		      		     msg[1] = "{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""  +
		   		       			"Could not commit answer updates to Database for INSTID:" +instid +"\"}";
		    	    	// System.out.println(" Rolling back since there was no COMMIT FROM SQL EXCPTION"+e.getMessage());
		      		     }
		    	    	e.printStackTrace();
			    	     if (stmt != null) { stmt.close(); }
			    	     if (conn != null) { conn.close();}
			    	     if (rs != null)  { rs.close(); }
		    	    	return msg;
		    	    } finally {
		    	    	 if (!committed) {conn.rollback(); 
		    	    //	 System.out.println(" Rolling back since there was no cOMMIT");
		    	    	 }
		    	        if (stmt != null) { stmt.close(); }
		    	        if (conn != null) { conn.close();}
		    	        if (rs != null)  { rs.close(); }
		    	    }	              
           
	                 msg[0] ="0";
	      		     msg[1] = "{\"result\":\"SUCCESS\",\"resultCode\":100,\"message\":\""  +
	   		       			"Added CFTID:" +obscftid +" to the Collection with instid as:"+ instid +" OBS_CFI_ID is:"+cfiid+"\"}";
	    	    
		    	     if (stmt != null) { stmt.close(); }
		    	     if (conn != null) { conn.close();}
		    	     if (rs != null)  { rs.close(); }
		        
             return msg;
          
	   		
	} // end of class instance
} // end of class
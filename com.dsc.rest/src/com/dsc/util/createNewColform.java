package com.dsc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.dsc.dao.obs;

public class createNewColform {
	 
	
	public Response createNewColform(JSONObject inputJsonObj) throws JSONException {
		
		 Response rb = null;
		StringBuffer sb = new StringBuffer();
		StringBuffer sbn = new StringBuffer();

			 Connection conn = null;
		 int rsCount=0;
		 
	 
				try {
					conn= obs.obsConn().getConnection();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 
		 
	 
		 try {
  	    	
	        String testyn="N";
	        String obdid=" ";
	        String obvid=" ";
	    	int req_obs_cft_id=(int) inputJsonObj.get("OBSColFormID");
	    	if(inputJsonObj.has("Test"))
	    		{testyn="Y";
	    		   obdid="2110";
	    		   obvid="92110";
	    		}
 			 int rsCounta=0;
 
    		  String SQL = " select distinct  i.obs_sub_type_name as FunctionName, " +
					  " g.obs_cft_title as formTitle, '' as FormSubTitle, '1' as FormVersion, " +
					  " e.obs_question_id as QuestionId, a.obs_col_form_quest_order as QuestionOrder," +
					  " f.obs_form_section_name as SectionName,  '' as SubSectionName, " +
					  " e.obs_question_full_text as QuestionText, c.obs_ans_type_api_tag_val as  AnswerType,"+
					  "case when  [obs_col_form_quest_na_yn] = 'Y' then 'true' else 'false' end as showsNA," +
					  " 'true' as canAddComment,a.obs_qat_id as qatid, "+
					  " a.obs_col_form_quest_order,a.obs_col_form_quest_wgt as obscolformwgt,c.obs_ans_type_category as AnswerCategroy, "+
					  " a.obs_col_form_quest_id ,getdate() as startdate from [dbo].[OBS_COL_FORM_QUESTIONS]  a WITH (NOLOCK) "+
					  " left join [dbo].[OBS_QUEST_ANS_TYPES] b WITH (NOLOCK) on a.obs_qat_id = b.obs_qat_id "+
					  " left join [dbo].[OBS_ANS_TYPE] c WITH (NOLOCK) on b.obs_ans_type_id = c.obs_ans_type_id "+
					  " left join [dbo].[OBS_QUESTION] e WITH (NOLOCK) on b.obs_question_id = e.obs_question_id "+
					  " left join [dbo].[OBS_FORM_SECTION] f WITH (NOLOCK) on f.obs_form_section_id = a.obs_form_section_id "+
					  " left join [dbo].[OBS_COLLECT_FORM_TMPLT] g WITH (NOLOCK) on g.obs_cft_id = " +req_obs_cft_id +   
					  " left join [dbo].[OBS_TYPE_SUB_TYPES]  h WITH (NOLOCK) on g.obs_type_id = h.obs_type_id "+
					  " left join [dbo].[OBS_SUB_TYPE] i WITH (NOLOCK) on i.obs_sub_type_id = h.obs_sub_type_id "+
					  "   where a.obs_cft_id="+req_obs_cft_id +
					  " and i.obs_sub_type_group='FUNCTION'" +
				//	   " and  i.obs_sub_type_name='"+req_FunctionName +"'" +
					  " order by a.obs_col_form_quest_order ";
	         
	          
		//   System.out.println("New Collection Form First SQL:" + SQL);
	        
	          Statement stmt = conn.createStatement();
	        //     System.out.println("statement connect done" );
 
			      // do starts here
			        ResultSet rs = stmt.executeQuery(SQL);
			//        System.out.println("result set created" );
	                String msg="OBSColFormID " +req_obs_cft_id +" Not found.";
	                sb.append("{\"result\":\"FAILED\",\"resultCode\":170,\"message\":\""+msg+"\"");
	              
	              //Show data from the result set.
	               if (obvid.isEmpty()) obvid=" ";
	               if (obdid.isEmpty()) obdid=" ";
					while (rs.next()) {
						  rsCount++;
					    if (rsCount == 1 )
					    {
			                sb=sbn;
					    	  msg="";
			                sb.append("{\"result\":\"SUCCESS\",\"resultCode\":0,\"message\":\""+msg+"\"");
					    	// System.out.println("in rsCount of 1 check:" + rsCount);
					    	sb.append(",\"observationsColFormData\":");
					    //	sb.append("{\"FunctionName\":\"" +rs.getString(1)+"\",");
					    	
					    	sb.append("{\"ObservedEmployeeId\":\"" +obvid +"\",");	
					    	sb.append("\"ObserverEmployeeID\":\""+obdid +"\",");					    	
					    	sb.append("\"hiredDate\":\""+obdid +"\" ,");	
					    	sb.append("\"DSC_LC_ID\":0" +",");						    	
					    	sb.append("\"customer\":\"0\",");	
					    	
					    	
					    	sb.append("\"OBSColFormID\":" +req_obs_cft_id+",");					    	
					    	sb.append("\"ColFormTitle\":\"" +rs.getString(2)+"\",");
					    	sb.append("\"ColFormSubTitle\":\"\",");
					    	sb.append("\"ColFormVersion\":\"1\",");  
					    	sb.append("\"OBSInstID\":\"\",");  	
					    	sb.append("\"OBSColFormInstID\":\"\",");  	
					    	// sb.append("\"ColFormStartDateTime\":\"\",");   +rs.getString(17)
					    	sb.append("\"ColFormStartDateTime\":\"" +rs.getString(18) +"\",");
					    	sb.append("\"DBColFormStatus\":\"\","); 
					    	sb.append("\"ColFormStatus\":\"NEW\"");  
					        sb.append(",\"questions\":[");
					    }
  	 
					    // create question Array Start
					    if (rsCount > 1 ) {sb.append(",");}
					 //  System.out.println("Starting questions:" +rs.getString(6));
					  //  System.out.println("Qtxt before" +rs.getString(9));
					    String qtext=rs.getString(9).replace("\"", "\\\"");  	
					    qtext=qtext.replaceAll("\\r\\n","");
					    qtext=qtext.replace("'", "''");
					  //  qtext.replace("\r", "");
					//    System.out.println("Qtxt after" +qtext);
					   // System.out.println("Before:" +rs.getString(9) +" After:"+qtext);  
					    sb.append("{\""+"UniqueQuestionId"+"\":"+rs.getString(17)+",");
					    sb.append("\""+"QuestionId"+"\":"+rs.getString(5)+",");
					    sb.append("\""+"QuestionOrder"+"\":"+rs.getString(6)+",");
					  //  System.out.println("questions getstring now for:" + sb.toString());    
					    sb.append("\""+"SectionName"+"\":"+"\""+rs.getString(7)+"\",");
					    sb.append("\""+"SubSectionName"+"\":"+"\""+rs.getString(8)+"\",");
					    sb.append("\""+"QuestionText"+"\":"+"\""+qtext+"\",");   		    
					    sb.append("\""+"AnswerType"+"\":"+"\""+rs.getString(10)+"\",");
					    sb.append("\""+"showsNA"+"\":"+"\""+rs.getString(11)+"\","); 
					    sb.append("\""+"canAddComment"+"\":"+"\""+rs.getString(12)+"\",");    
					    sb.append("\""+"MustAddComment"+"\":"+"\"\",");
					    sb.append("\""+"answerChanged"+"\":"+"\"false\",");
					    sb.append("\""+"obscolformquestwgt"+"\":"+rs.getString(15)+","); 	
					    sb.append("\""+"comments"+"\":\"\","); 
					  //  System.out.println("Done with questions::" + sb.toString());      		
					    // now do all answers for this question
					    sb.append("\"answers\":[");
					    rsCounta=0;
						SQL = " select d.obs_qsa_order as answerOrder,  d.obs_qsa_text as answerText, " +
							 " d.obs_qsa_wt as obscolanswgt,d.obs_qsa_id as obsansid " +
							  "  from [dbo].[OBS_COL_FORM_QUESTIONS]  a WITH (NOLOCK) " +
							  " left join [dbo].[OBS_QUEST_ANS_TYPES] b WITH (NOLOCK) on a.obs_qat_id = b.obs_qat_id "+
							  " left join [dbo].[OBS_QUEST_SLCT_ANS] d WITH (NOLOCK) on b.obs_qat_id = d.obs_qat_id "+
							  "	left join [dbo].[OBS_QUESTION] e WITH (NOLOCK) on b.obs_question_id = e.obs_question_id "+
							  " left join [dbo].[OBS_FORM_SECTION] f WITH (NOLOCK) on f.obs_form_section_id = a.obs_form_section_id " +
							  "  where a.obs_cft_id="+req_obs_cft_id +" and a.obs_qat_id ="+ rs.getString(13) +
							  "  and  a.obs_col_form_quest_order="+rs.getString(14)   +
							  "  and  b.obs_question_id =  "  +rs.getString(5) +
							  "  and ((d.obs_qsa_text   is not null) and (d.obs_qsa_text is not null))" +
							  " order by  a.obs_col_form_quest_order  " ;	
						        // 5 and 100
						
					//	if (rs.getString(5).equals("96"))
					//		   System.out.println("Second SQL for question:"+rs.getString(5) +"SQL is:" + SQL);
						// writer.write(  SQL.toString());
				   // System.out.println("New Collection Form Second SQL for question:"+rs.getString(5) +"SQL is:" + SQL);
						Statement stmta = conn.createStatement();
 			          ResultSet rsa = stmta.executeQuery(SQL);

					          while (rsa.next()) {
					              rsCounta++;
					        	//  System.out.println("in answer loop for question:" + rsCount +" answer:" +rsCounta);
					        	  if (rsCounta > 1) {sb.append(",");}
							    sb.append("{\""+"answerId"+"\":"+rsa.getString(4)+",");  
					        	sb.append("\""+"obscolanswgt"+"\":"+rsa.getString(3)+",");  
							    sb.append("\""+"answerOrder"+"\":"+rsa.getString(1)+",");
							    sb.append("\""+"answerText"+"\":"+"\""+rsa.getString(2)+"\"}");  	            	  
					          }
					          rsa.close();
					          stmta.close();
 					         // if there are now answers and if AnswerType was yes-no then give 2 loops with y and n
					          if (rsCounta == 0 && rs.getString(16).equals("Yes No") )
					          {
					        	   // sb.append("{\""+"answerId"+"\":\"\",");  
						        	sb.append("{\""+"obscolanswgt"+"\":1,");  
								    sb.append("\""+"answerOrder"+"\":1,");
								    sb.append("\""+"answerText"+"\":"+"\"YES\"},");
								    
					        	 //   sb.append("{\""+"answerId"+"\": \"\",");  
						        	sb.append("{\""+"obscolanswgt"+"\":1,");  
								    sb.append("\""+"answerOrder"+"\":2,");
								    sb.append("\""+"answerText"+"\":"+"\"NO\"}");  	
					        	  
					          }
					          sb.append("]");
                             // if test indicator is on then provide a answervalue
					          if (testyn.equals("Y"))
					          {
					        	  sb.append(",\"answervalues\":[");
					        	  sb.append("{");
					        	  sb.append("\"obscolanswgt\":1,");
					        	  sb.append("\"answerOrder\":1,");
					        	  sb.append("\"answerText\":\"TEST\"}]");					        	  
					          }
					          
	                          sb.append("}");
                         }
			              rs.close();
			             stmt.close();
			             if (conn != null) { conn.close();} 
					  }
				   catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			   // while loop for questions
				   }
		    
	         if (rsCount > 0 )
	         {
	        	  sb.append("]"); // question array
	              sb.append("}"); // observation end tag
	         }

	         sb.append("}"); // end start of result 
 
	           if (conn != null) 
	           {
	        	   try{
	        		   conn.close();
	        		  } catch(SQLException e)
	        	      {e.printStackTrace(); }
	           }	
	         rb=Response.ok(sb.toString()).build();
	      //  System.out.println("NEW COllection Form JSON Message sent is"+sb.toString());
 
         return rb;
	}
}

 

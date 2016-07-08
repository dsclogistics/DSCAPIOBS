 
package com.dsc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.dsc.dao.obs;

public class viewcollform {
	 
	
	public Response viewcollform(JSONObject inputJsonObj) throws JSONException {
		
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
	    	int req_obs_cfi_id=(int) inputJsonObj.get("ObsColFormInstID");
 

			 int rsCounta=0;

			 
			 
              String nsql="select de.dsc_emp_first_name +' '+de.dsc_emp_last_name as Observed_FullName, de.dsc_emp_adp_id,"+
            		     " ocfi.dsc_observer_emp_id as Observer, oi.dsc_observed_emp_id as Observed,dl.dsc_lc_name as  LocationName," +
            		     " ocfi.obs_cfi_comp_date as compdate from "+
            		     "[dbo].[OBS_COLLECT_FORM_INST] ocfi "+
            		     " left join [dbo].[OBS_INST] oi on  oi.obs_inst_id = ocfi.obs_inst_id "+
            		     " left join [dbo].[DSC_EMPLOYEE] de  on oi.dsc_observed_emp_id= de.dsc_emp_id "+
            		     " left join [dbo].[DSC_LC] dl on dl.dsc_lc_id = oi.dsc_lc_id  "+
            		     " where  ocfi.obs_cfi_id="+req_obs_cfi_id +
            		     " union all( "+
            		     " select de.dsc_emp_first_name +' ' +de.dsc_emp_last_name as Observer_FullName,de.dsc_emp_adp_id,"+
            		     " ocfi.dsc_observer_emp_id as Observer, oi.dsc_observed_emp_id as Observed,'' as LocationName ," +
            		     " '' as compdate from "+ 
            		     " [dbo].[OBS_COLLECT_FORM_INST] ocfi" +
            		     " left join [dbo].[OBS_INST] oi on oi.obs_inst_id = ocfi.obs_inst_id "+
            		     " left join [DSC_EMPLOYEE] de on ocfi.dsc_observer_emp_id= de.dsc_emp_id "+
            		     " where  ocfi.obs_cfi_id="+req_obs_cfi_id +")"		;
              
              nsql= "select de.dsc_emp_first_name +' '+de.dsc_emp_last_name as Observed_FullName, de.dsc_emp_adp_id," +
                    " ocfi.dsc_observer_emp_id as Observer, oi.dsc_observed_emp_id as Observed,dl.dsc_lc_name as  LocationName, "+
            	    " ocfi.obs_cfi_comp_date as compdate, de1.dsc_emp_first_name +' '+de1.dsc_emp_last_name as Observer_FullName,"+
            		"de1.dsc_emp_adp_id   from [dbo].[OBS_COLLECT_FORM_INST] ocfi "+
            		" left join [dbo].[OBS_INST] oi on  oi.obs_inst_id = ocfi.obs_inst_id "+  
            		" left join [dbo].[DSC_EMPLOYEE] de  on oi.dsc_observed_emp_id= de.dsc_emp_id "+  
            		" left join [dbo].[DSC_EMPLOYEE] de1  on ocfi.dsc_observer_emp_id= de1.dsc_emp_id "+  
            		" left join [dbo].[DSC_LC] dl on dl.dsc_lc_id = oi.dsc_lc_id "+   
            		" where  ocfi.obs_cfi_id="+req_obs_cfi_id;

    		  String SQL1 = "  select distinct   k.dsc_observed_emp_id as '1', "+
    				  	   " j.dsc_observer_emp_id as '2',  m.[dsc_emp_hire_dt] as '3' ,  "+
    				  	 " k.dsc_lc_id as '4',k.dsc_cust_id as '5',  j.obs_cft_id as '6', g.obs_cft_title   as '7',  "+
    				  	" '' as '8', '1' as '9',    j.obs_inst_id as '10',  "+
    				  	" j.obs_cfi_id as '11', j.obs_cfi_start_dt as '12', "+
    				  	"  case when k.obs_inst_status = 'COLLECTING' then 'Open' else 'Ready' end as '13' , "+
    				  	" e.obs_question_full_text as '14', l.obs_question_id as '15', "+
    				  	" l.obs_cfiq_quest_order as '16',l.obs_cfiq_form_sect_name as '17', "+
    				  	" l.obs_cfiq_form_sub_sect_name as '18' ,p.obs_ans_type_category as '19', "+
    				  	" l.obs_cfiq_na_yn as '20',l.obs_cfiq_quest_wgt as '21',l.obs_cfiq_comment as '22', "+
    				  	"  o.obs_qat_id as '23', p.obs_ans_type_api_tag_val as '24',n.obs_qat_id as '25' " +
    				  	" from [dbo].obs_collect_form_inst j  WITH (NOLOCK)  "+ 
    				  	" left join [dbo].[OBS_COLLECT_FORM_TMPLT] g WITH (NOLOCK) on g.obs_cft_id = j.obs_cft_id   "+      
    				  	" left join dbo.obs_inst k WITH (NOLOCK) on k.obs_inst_id = j.obs_inst_id    "+
    				  	" left join dbo.obs_col_form_inst_quest l WITH (NOLOCK) on l.obs_cfi_id = j.obs_cfi_id   "+  
    				  	" left join [dbo].[OBS_QUESTION] e WITH (NOLOCK) on l.obs_question_id = e.obs_question_id  "+
    				  	" left join dbo.dsc_employee m WITH (NOLOCK) on m.dsc_emp_id = k.dsc_observed_emp_id   "+
    				  	" left join dbo.obs_quest_ans_types n WITH (NOLOCK) on n.obs_question_id = l.obs_question_id "+
    				  	" left join dbo.obs_quest_slct_ans o WITH (NOLOCK) on o.obs_qat_id = n.obs_qat_id " +
    				  	" left join dbo.obs_ans_type p WITH (NOLOCK) on p.obs_ans_type_id = n.obs_ans_type_id " +
    				  	"  where j.obs_cfi_id=" +req_obs_cfi_id +
    				  	" and p.obs_ans_type_id = n.obs_ans_type_id " +
    				  	" order by  l.obs_cfiq_quest_order ";
    		  
  			String SQL=" select distinct   "+
					" k.dsc_observed_emp_id as '1', "+
	    			" j.dsc_observer_emp_id as '2',  m.[dsc_emp_hire_dt] as '3' ,  "+
	    			" k.dsc_lc_id as '4',k.dsc_cust_id as '5',  j.obs_cft_id as '6', g.obs_cft_title   as '7',  "+
	    			" '' as '8', '1' as '9',    j.obs_inst_id as '10',   "+
	    		    " j.obs_cfi_id as '11', j.obs_cfi_start_dt as '12',  "+
	    		    " case when k.obs_inst_status = 'COLLECTING' then 'Open' else 'Ready' end as '13', "+
					" e.obs_question_full_text as '14', l.obs_question_id as '15',  "+
	    		    " l.obs_cfiq_quest_order as '16',l.obs_cfiq_form_sect_name as '17', "+ 
	    		    " l.obs_cfiq_form_sub_sect_name as '18' ,   "+
	    	       "  case when l.obs_cfiq_na_yn = 'Y' then 'true' else 'false' end as '19', "+	    
	    	// 5/25/2015	    " l.obs_cfiq_na_yn as '19', "+
	    		    " l.obs_cfiq_quest_wgt as '20',l.obs_cfiq_comment as '21' "+		   
			//				--   o.obs_qat_id as '23', p.obs_ans_type_api_tag_val as '24',n.obs_qat_id as '25'
	    		    " , l.obs_cfiq_id as '22',l.obs_col_form_quest_id as '23',k.obs_inst_status as '24' " +
			       " from [dbo].obs_collect_form_inst j  WITH (NOLOCK)  "+
				   " left  join dbo.obs_col_form_inst_quest l WITH (NOLOCK) on l.obs_cfi_id = j.obs_cfi_id  "+
				   " left join [dbo].[OBS_COLLECT_FORM_TMPLT] g WITH (NOLOCK) on g.obs_cft_id = j.obs_cft_id "+   
			       " left join dbo.obs_inst k WITH (NOLOCK) on k.obs_inst_id = j.obs_inst_id  "+   
			       " left join [dbo].[OBS_QUESTION] e WITH (NOLOCK) on l.obs_question_id = e.obs_question_id "+   
			       " left join dbo.dsc_employee m WITH (NOLOCK) on m.dsc_emp_id = k.dsc_observed_emp_id "+
  			     //  " left join dbo.obs_quest_ans_types n WITH (NOLOCK) on n.obs_question_id = l.obs_question_id  "+
			     //  " left join dbo.obs_ans_type p WITH (NOLOCK) on p.obs_ans_type_id = n.obs_ans_type_id  "+  
				  	"  where j.obs_cfi_id=" +req_obs_cfi_id +
				  	" order by  l.obs_cfiq_quest_order ";	         
	          
	   // System.out.println("Show collection First SQL:" + nsql);
	
		      String fullname1=null;
		      String adpid1=null;
		      String locname1=null;
		      String fullname2=null;
		      String adpid2=null;
		      String compdte="";
		      
	          Statement stmt = conn.createStatement();
         // First get adp if for observer/obbserved and location/completion dates for this form id
	          
		        ResultSet rs = stmt.executeQuery(nsql);
		        rsCount=0;
				while (rs.next()) 
				{
 
						  fullname1=rs.getString(1) ;
						  adpid1=rs.getString(2);
						  locname1=rs.getString(5) ;
 
						  fullname2=rs.getString(7) ;
						  adpid2=rs.getString(8);
						  compdte=rs.getString(6) ;
 
 
					
				}
				
				rs.close();
				
				 
 
			      // do starts here
			          rs = stmt.executeQuery(SQL);
	                String msg="ObsColFormInstID " +req_obs_cfi_id +" Not found.";
	                sb.append("{\"result\":\"FAILED\",\"resultCode\":170,\"message\":\""+msg+"\"}");
	              rsCount=0;
	              //Show data from the result set.
	               if (obvid.isEmpty()) obvid=" ";
	               if (obdid.isEmpty()) obdid=" ";
					while (rs.next()) 
					{
						  rsCount++;
					    if (rsCount == 1 )
					    {
					    	 sb=sbn;
						     msg="";
					    	sb.append("{\"result\":\"SUCCESS\",\"resultCode\":0,\"message\":\""+msg+"\"");
					    	// System.out.println("in rsCount of 1 check:" + rsCount);
					    	sb.append(",\"observationsColFormData\":");
					    //	sb.append("{\"FunctionName\":\"" +rs.getString(1)+"\",");
					    	
					    	sb.append("{\"ObservedEmployeeId\":\"" +rs.getString(1) +"\",");	
					    	sb.append("\"ObserverEmployeeID\":\""+rs.getString(2) +"\",");					    	
					    	sb.append("\"hiredDate\":\""+rs.getString(3) +"\" ,");	
					    	sb.append("\"DSC_LC_ID\":\""+rs.getString(4) +"\",");		
					    	sb.append("\"customer\":\""+rs.getString(5) +"\",");	
					    	 
					    	sb.append("\"ObservedFullName\":\""+fullname1 +"\",");	
					    	sb.append("\"ObservedADPID\":\""+adpid1 +"\",");	
					    	sb.append("\"ObservedLocation\":\""+locname1 +"\",");						    	
					    	
					    	sb.append("\"ObserverFullName\":\""+fullname2 +"\",");	
					    	sb.append("\"ObserverADPID\":\""+adpid2 +"\",");	
					    	sb.append("\"ObservationCompDate\":\""+compdte +"\",");	
					    	
					    	
					    	sb.append("\"OBSColFormID\":" +rs.getString(6)+",");					    	
					    	sb.append("\"ColFormTitle\":\"" +rs.getString(7)+"\",");
					    	sb.append("\"ColFormSubTitle\":\""+rs.getString(8)+"\",");
					    	sb.append("\"ColFormVersion\":\"" +rs.getString(9) +"\",");  
					    	sb.append("\"OBSInstID\":\""+rs.getString(10) +"\",");  	
					    	sb.append("\"OBSColFormInstID\":\""+ rs.getString(11)+"\",");  	
					    	sb.append("\"ColFormStartDateTime\":\"" +rs.getString(12) +"\","); 
					    	sb.append("\"DBColFormStatus\":\"" +rs.getString(24) +"\",");  
					    	sb.append("\"ColFormStatus\":\""+ rs.getString(13) +"\"");  
					        sb.append(",\"questions\":[");
					    }
  
                     // you need answer type api tag and qat id so get another cursor for each question in a cft id
					String ans25="";
					String ans24="";
					String ans24a="";
					String wcomments="";
					String sqlans=" select distinct c.obs_ans_type_api_tag_val as '25',  a.obs_qat_id as '24', " +
					              " c.obs_ans_type_api_tag_val as '25' "+
					              " from obs_quest_ans_types  a "+
					              " left join obs_col_form_questions b on b.obs_col_form_quest_id ="+ rs.getString(23) +
					              " left join obs_quest_slct_ans d on d.obs_qat_id  = b.obs_qat_id "+
					           //   "  left join obs_quest_slct_ans b on a.obs_qat_id  = b.obs_qat_id "+
					              " left join obs_ans_type c on c.obs_ans_type_id = a.obs_ans_type_id "+
					              " where obs_question_id ="+rs.getString(15)+"  and a.obs_qat_id in " +
					              " (select obs_qat_id from obs_col_form_questions where  obs_col_form_quest_id ="+rs.getString(23)+")";
					//  System.out.println("*** GET ANSWER TYPES VALUES  :" + sqlans);					    
					  Statement stmtans = conn.createStatement();
				        ResultSet rsans = stmtans.executeQuery(sqlans);				    
						while (rsans.next()) 
						{
							ans25=rsans.getString(1);
							ans24=rsans.getString(2);
							ans24a=rsans.getString(3);
						}
						rsans.close();
						stmtans.close();
					    // create question Array Start
					    if (rsCount > 1 ) {sb.append(",");}
					 //  System.out.println("Starting questions:" +rs.getString(6));

					    String qtext=rs.getString(14).replace("\"", "\\\"");  	
					    qtext=qtext.replaceAll("\\r\\n","");
					    qtext=qtext.replace("'", "''");
					    sb.append("{\""+"UniqueQuestionId"+"\":"+rs.getString(23)+",");
					    sb.append("\""+"QuestionId"+"\":"+rs.getString(15)+",");
					    sb.append("\""+"CFIQID"+"\":"+rs.getString(22)+",");
					    sb.append("\""+"QuestionOrder"+"\":"+rs.getString(16)+",");
   		    
					    sb.append("\""+"SectionName"+"\":"+"\""+rs.getString(17)+"\",");
					    sb.append("\""+"SubSectionName"+"\":"+"\""+rs.getString(18)+"\",");
					    sb.append("\""+"QuestionText"+"\":"+"\""+qtext+"\",");   		    
					    //sb.append("\""+"AnswerType"+"\":"+"\""+rs.getString(24)+"\",");  //
					    sb.append("\""+"AnswerType"+"\":"+"\""+ans24a+"\",");   
					    sb.append("\""+"showsNA"+"\":"+"\""+rs.getString(19)+"\","); 
					    sb.append("\""+"canAddComment"+"\":"+"\""+rs.getString(20)+"\",");    
					    sb.append("\""+"MustAddComment"+"\":"+"\"\",");
					    sb.append("\""+"answerChanged"+"\":"+"\"false\",");
					    sb.append("\""+"obscolformquestwgt"+"\":"+rs.getString(20)+","); 	
					    wcomments=rs.getString(21).replaceAll("[\\t\\n\\r]+"," ");						     
					    wcomments=wcomments.replaceAll("\"",""); 
					    sb.append("\""+"comments"+"\":\"" +wcomments +"\","); 
					   // sb.append("\""+"comments"+"\":\"" +rs.getString(21).replaceAll("[\\t\\n\\r]+"," ") +"\","); 
					  //  System.out.println("Done with questions::" + sb.toString());      		
					    // now do all answers for this question
					    sb.append("\"answers\":[");
					    rsCounta=0;
						SQL = " select d.obs_qsa_order as answerOrder,  d.obs_qsa_text as answerText, " +
							 " d.obs_qsa_wt as obscolanswgt,d.obs_qsa_id as obsansid " +
							  "  from [dbo].[OBS_COL_FORM_QUESTIONS]  a  WITH (NOLOCK) " +
							  " left join [dbo].[OBS_QUEST_ANS_TYPES] b WITH (NOLOCK) on a.obs_qat_id = b.obs_qat_id "+
							  " left join [dbo].[OBS_QUEST_SLCT_ANS] d WITH (NOLOCK) on b.obs_qat_id = d.obs_qat_id "+
							  "	left join [dbo].[OBS_QUESTION] e WITH (NOLOCK) on b.obs_question_id = e.obs_question_id "+
							  " left join [dbo].[OBS_FORM_SECTION] f WITH (NOLOCK) on f.obs_form_section_id = a.obs_form_section_id " +
							  "   where a.obs_cft_id="+rs.getString(6) +
							  " and e.obs_question_id ="+ rs.getString(15) +
							//  " and a.obs_qat_id =" +rs.getString(25) +
							  " and a.obs_qat_id =" +ans24 +
							  " and ((d.obs_qsa_text   is not null) and (d.obs_qsa_text is not null))" +
							//  "  and  a.obs_col_form_quest_order="+rs.getString(15)   +
							//  "  and  b.obs_question_id =  "  +rs.getString(15) +
							//  "  and ((d.obs_qsa_text   is not null) and (d.obs_qsa_text is not null))" +
							  " and a. obs_col_form_quest_id ="  +rs.getString(23) +
							  "  order by  a.obs_col_form_quest_order  " ;	
						        // 5 and 100
						 
				//	if (rs.getString(15).equals("96"))
			 //	   System.out.println("Second SQL for question:"+rs.getString(15) +" $$$$ 16 is:"+rs.getString(19)+" category is:"+rs.getString(19)+". SQL is:" + SQL);
						Statement stmta = conn.createStatement();
 			          ResultSet rsa = stmta.executeQuery(SQL);
                      int rsav=0;
					          while (rsa.next()) {
					              rsCounta++;
					              rsav++;
					        	//  System.out.println("in answer loop for question:" + rsCount +" answer:" +rsCounta);
					        	  if (rsCounta > 1) {sb.append(",");}
							    sb.append("{\""+"answerId"+"\":"+rsa.getString(4)+",");  
							    sb.append("\""+"CFIQID"+"\":"+rs.getString(22)+",");
					        	sb.append("\""+"obscolanswgt"+"\":"+rsa.getString(3)+",");  
							    sb.append("\""+"answerOrder"+"\":"+rsa.getString(1)+",");
							    sb.append("\""+"answerText"+"\":"+"\""+rsa.getString(2)+"\"}");  	            	  
					          }
					          rsa.close();
					          stmta.close();
					          
 					         // if there are now answers and if AnswerType was yes-no then give 2 loops with y and n
					          
					         // if (rsav == 0 && rs.getString(19).equals("Yes No") )
					       //   if (rsav == 0 && ( rs.getString(19).equals("Y") ||  rs.getString(19).equals("N")) )
					         
					          if (ans24a.trim().equals("FREE_TXT")) {rsav=99;}
					          if (rsav == 0) 
					          {
					        	 // System.out.println("&&&&&& F R E E  T E X T :"+ans24a +" rsav is:"+rsav);
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
					          
					          
					          SQL="select a.* from dbo.obs_col_form_inst_ans a WITH (NOLOCK) " +
 					              " left join dbo.obs_col_form_inst_quest b WITH (NOLOCK) on a.obs_cfiq_id = b.obs_cfiq_id" +
					        	  " and b.obs_question_id = " +rs.getString(15) +
					        	  "    where a.obs_cfiq_id = b.obs_cfiq_id " +
					        	  "  and b.obs_cfi_id="+rs.getString(11) +
					        	  " and a.obs_cfiq_id="+rs.getString(22) +
					        	  " order by obs_cfiq_id ";
					          
		//	   		         System.out.println("Get  answer values:"+SQL);
					          rsCounta=0;
							  stmta = conn.createStatement();
			 			          rsa = stmta.executeQuery(SQL);

								          while (rsa.next()) {
		
								              if (rsCounta == 0)
											        sb.append(",\"answervalues\":[");
								              rsCounta++;
								        	//  System.out.println("in answer loop for question:" + rsCount +" answer:" +rsCounta);
								        	  if (rsCounta > 1) {sb.append(",");}
 
										      //  sb.append(",\"answervalues\":[");
					        	  				sb.append("{");
					        	  				sb.append("\"obscolanswgt\":"+rsa.getString(4) +",");
					        	  				sb.append("\"answerOrder\":" +rsa.getString(5)+",");
					        	  				sb.append("\"answerText\":\"" +rsa.getString(3) +"\"}");	
					        	  				rsCounta++;
       					                  }
								          if (rsCounta > 0) sb.append("]");
								          rsa.close();
								          stmta.close();
							 
					        				          
					          if (testyn.equals("Y"))
					          {
					        	  sb.append(",\"answervalues\":[");
					        	  sb.append("{");
					        	  sb.append("\"obscolanswgt\":1,");
					        	  sb.append("\"answerOrder\":1,");
					        	  sb.append("\"answerText\":\"TEST\"}]");	
				//	        	  System.out.println("Test for answer values:"+testyn);
					        	  
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
	             sb.append("}"); // end start of result  // changed on 20160418
	         }

	          // sb.append("}"); // end start of result 
 
	
	         rb=Response.ok(sb.toString()).build();
	 //        System.out.println(" Collectio Form sent JSON Message sent is"+sb.toString());
 
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

 


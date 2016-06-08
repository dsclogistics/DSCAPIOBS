package com.dsc.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.dsc.dao.obs;
 

public class parseQuestions   {
	
	public int [] parseQuestions(JSONArray jsonArr, int obsids[]) throws JSONException {
		String msg="";
		// TODO Auto-generated constructor stub
		 Connection conn = null;
        //   obsids[8] instance id from obs_inst is set in insupdOBSQuest only if this is a new else it should
		 //  be found in the json payload
		 try {
			conn= obs.obsConn().getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			 msg= "{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""  +
	       			"Connection Failed while starting to parse Questions"  +"\"}";
     		e.printStackTrace();
     		 obsids[20]=200*-1;
     		return obsids;
			//  return msg;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 msg= "{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""  +
		       			"Connection Failed while starting to parse Questions"  +"\"}";
			 obsids[20]=200*-1;
			 return obsids;
			 // return msg;
		}

		//  org.apache.tomcat.dbcp.dbcp.BasicDataSource
		 
	    //	System.out.println( " Number of Questions  Array length:"+jsonArr.length()); 
        for(int i=0; i<jsonArr.length(); i++)
        { 
        	// System.out.println("The " + i + " element of the array: "+jsonArr.get(i));
        	JSONObject s1 =  (JSONObject) jsonArr.get(i);
         //	System.out.println("**** OBS Array 8 have value of:"+obsids[8]  );
        	
        	obsids[10]=(int) s1.get("QuestionId") ;
        //  	System.out.println("**** Json Question Array "+i +" question id:"+obsids[10]+"  Object is: "+s1.toString());
        	insupdOBSQuest iuq= new insupdOBSQuest();
        	try {
				// obsids = iuq.insupdOBSQuests(s1, obsids,conn);
				obsids = iuq.insupdOBSQuests(s1, obsids);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 msg= "{\"result\":\"FAILED\",\"resultCode\":150,\"message\":\""  +
			       			"Failed to Insert/update instance/collection form inst object"  +"\"}";
				 obsids[20]=150*-1;
				 return obsids;
				// return msg;
			}
        	
        	if (obsids[3] < -1)
        	{
				 msg= "{\"result\":\"FAILED\",\"resultCode\":160,\"message\":\""  +
			       			"Failed to get obs_cfi_id. Invalid observerid/observedid "  +"\"}";
				 obsids[20]=160*-1;
				 return obsids;
				 //return msg;
     		
        		
        	}
        	
 
        	
        	// call parseAnswervalues to parse answer values array
        	 if(s1.has("answervalues") && obsids[8] > -1) 
        	 {
        		 JSONArray ansval = (JSONArray) s1.getJSONArray("answervalues");
        	//	 System.out.println( " ###### json Array for answervalues  length:"+ansval.length());  
        		 parseAnswervalues ansv = new parseAnswervalues();
        		 // ansv.parseAnswervalue(ansval,obsids,conn);
        		 ansv.parseAnswervalue(ansval,obsids);
        	 }
	}
        
     if (conn != null)
     {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 msg= "{\"result\":\"FAILED\",\"resultCode\":200,\"message\":\""  +
		       			"Connection Failed while starting to parse Questions"  +"\"}";
			 obsids[20]=200*-1;
			// return msg;
			 return obsids;
		}
     }
       	 msg= "{\"result\":\"SUCCESS\",\"resultCode\":0,\"message\":\""  +
       			"Updates Successful"  +"\"}";
       	// return msg;
       	return obsids;

	}

}

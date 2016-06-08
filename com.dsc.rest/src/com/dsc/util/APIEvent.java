package com.dsc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.dsc.dao.obs;

//class RunnableDemo
public class APIEvent   implements Runnable {
	   private Thread t;
	   private String threadName;
	   private JSONObject jsonObject;
	   
	   public APIEvent(JSONObject jsonObject1){
	       threadName = "APIEvent";
	       jsonObject=jsonObject1;
	       //System.out.println("Creating " +  threadName );
	   }
	   public void run() {
	     // System.out.println("Running " +  threadName );
	  //    System.out.println("JSON OBJECT " +  jsonObject );
		
    	  Connection conn = null;
    	  Statement stmt = null; 
		  ResultSet rs = null;
		  String query="";
		  String query2="";
		  String insert="";
		  int apiraid=0;
		  int apiappid=0;
		  int apieventid=0;
		  String OBSColFormID="NULL";
		  String OBSInstID="NULL";
		  String  OBSColFormInstID="NULL";
		  String ObserverEmployeeID="NULL";
		  String jsoninput="";
		  String jsonoutput="";
		  String resname="";
		  String status="";
		  String continueflag="N";
		  String appname="";
		  
		  java.util.Date rdate= new java.util.Date();
   	   	  java.util.Date sdate= new java.util.Date();
   	   	  try
   	   	  {
   	   		  if (jsonObject.has("result")) status=jsonObject.get("result").toString();
   	          if (jsonObject.has("resource"))
   	          {
   	        	  if (jsonObject.get("resource").toString().length() > 15)
   	        	  {
   	        		  status="SUCCESS";
   	        	  }
   	        	  else
   	        	  {
   	        		status="FAILED";
   	        		  
   	        	  }
   	          }
   	   		  rdate=(Date) jsonObject.get("apircvddtm");
   	   		  sdate=(Date) jsonObject.get("apisentdtm");
   	   		  resname=jsonObject.get("resname").toString();
   	   		  jsoninput=jsonObject.get("jsoninput").toString();
   	   		 
   	   		  // INPUT as a JSON OBJECT
   	   		  
   	   		  if (jsonObject.has("jsoninput"))
   	   		  {	  
   	   			  JSONObject ip =  (JSONObject) jsonObject.get("jsoninput");
   	   			  
   	   			  if (ip.has("appid"))
   	   			  {
   	   				  appname=ip.getString("appid"); 
   	   			  }
   	   			  else
   	   			  {
   	   				  appname="OBS_PRO_MOBILE";  
   	   	    	  
   	   			  }
   	   		      
   	   	   		  if (resname.equals("DSCAuthenticationSrv"))
   	   	   		  {
   	   	   			  if (jsoninput.contains(","))
   	   	   			  {
   	   	   			     // System.out.println(" Before fix :"+jsoninput);
   	   	   			     ip.remove("password");
   	   	   			     jsoninput=ip.toString();
   	   	   			     
   	   	   				// String [] output=jsoninput.split(",");
   	   	   				// if (output[0].indexOf("user") > 0) { jsoninput=output[0] +"}";}
   	   	   				// if (output[1].indexOf("user") > 0) { jsoninput=output[1] +"}";	} 
   	   	   			//	 System.out.println(" After fix :"+jsoninput);
   	   	   				 // jsoninput=output[0] +"}";
   	   	   			  }
   	   	   			  
   	   	   		  }
   	   		  }
   	   		  jsonoutput=jsonObject.toString();
   	   		  resname=jsonObject.get("resname").toString();
   	   	
   	   	
 

   	   		  query2="";
   	   		  if ( jsonObject.has("observationsColFormData"))
   	   		  {
   	   			  JSONObject jsondata = (JSONObject) jsonObject.getJSONObject("observationsColFormData");
   	   			  OBSColFormID=jsondata.get("OBSColFormID").toString();
   	   			  OBSInstID=jsondata.get("OBSInstID").toString();
   	   			  OBSColFormInstID=jsondata.get("OBSColFormInstID").toString();
   	   		      ObserverEmployeeID=jsondata.get("ObserverEmployeeID").toString();
   	   		      
   	   		      if (resname.equals("obs_Save"))
   	   		      {
   	   		    	  
   	   		        if (jsondata.get("ColFormStatus").toString().equals("SAVE"))
   	   		        {
   	   		        	query2="  and obs_api_ra_action like 'Save%'";
   	   		        }
   	   		        else
   	   		        {
   	   		            query2="  and obs_api_ra_action like 'Submit%'";
   	   		        }
   	   		      }
   	   		//	  System.out.println(" OBS COLLECTION FORM ID " +  OBSColFormID);
   	   			 // status="Success";
   	   		  }
   	   	  }
   	   	  catch(Exception j)
   	   	  {
   	   	       j.printStackTrace();
   	   		  
   	   	  }
   	  
			 try 
			 {
				conn= obs.obsConn().getConnection();
			 } 
         catch (Exception e) 
			 {
				// TODO Auto-generated catch block  RETURN ERROR ??????????
				// e.printStackTrace();
				 try
				 {
				     if (conn != null) { conn.close();}
				  }
				  catch(Exception c)
				  {
				  
				  }
        	   System.exit(0);
			 }
			  // get api_ra_id and app_id first
			  query=" SELECT obs_api_ra_id,obs_app_id FROM [dbo].[OBS_API_RSRC_ACTION],[dbo].[OBS_APPLICATION] "+
  				  " where obs_app_name_token ='"+appname +"' and obs_api_ra_rsrc_name='"+ resname +"'" +query2;
 	    	   try 
 	    	   {
				stmt = conn.createStatement();	 
 	    	 //   System.out.println("Thread get RSRC-app id" +  query);
	 
					rs = stmt.executeQuery(query);
				
	    	     while (rs.next()) 
	    	        {
	    	    	    continueflag="Y";
	    	        	apiraid=rs.getInt(1);
	    	        	apiappid=rs.getInt(2);
	    	        }
	    	     
	    	        rs.close();
	    	        
	    	        // if did not find try with unknow appname and try
	    	        if (continueflag.equals("N"))
	    	        {
	    				  query=" SELECT obs_api_ra_id,obs_app_id FROM [dbo].[OBS_API_RSRC_ACTION],[dbo].[OBS_APPLICATION] "+
	    		  				  " where obs_app_name_token ='UNKNOWN' and obs_api_ra_rsrc_name='"+ resname +"'";	
	    				  
	    					rs = stmt.executeQuery(query);
	    					
	    		    	     while (rs.next()) 
	    		    	        {
	    		    	    	    continueflag="Y";
	    		    	        	apiraid=rs.getInt(1);
	    		    	        	apiappid=rs.getInt(2);
	    		    	        }
	    		    	     
	    		    	        rs.close();	    				  
	    	        }
	    	        stmt.close();
	    	    } catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}    	            

	    	   
             if (continueflag.equals("Y"))
                		  
             {
            	 boolean committed = false;   
            	 // START COMMIT NOW
	    	    try 
	    	    {
	    	    	conn.setAutoCommit(false);
	    	    	
	    	    	int instid=0;
	    	    	insert="insert into [OBS_API_EVENT_LOG] " +
	    	    	"	       ([obs_API_RA_ID] "+
      				"			,[obs_app_id]   "+
      				"			,[obs_inst_id]  "+
      				"			,[obs_cfi_id]   "+
      				"			,[obs_cft_id]   "+
      				"			,[dsc_emp_id]   "+
      				"			,[api_call_rcvd_dtm] "+
      				"			,[api_call_sent_dtm] "+
      				"			,[api_call_status]  "+
      				"			,[obs_inst_event_text]) "+
	    			" values ("+apiraid+","+apiappid     +
	    			","+OBSInstID+","+OBSColFormInstID+","+OBSColFormID+","+ObserverEmployeeID+",'"+rdate+"','"+sdate+"','"+status+"','')";
	    			
 
 					//System.out.println("Thread insert to EVENT LOG" +  insert);
		   	      // Insert Row into OBS_INST  1st step

		    	    	 
		    	    	PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
		    	        ps.executeUpdate();
		    	        rs = ps.getGeneratedKeys();
		    	        rs.next();
		    	        apieventid = rs.getInt(1);	
		    	        rs.close();
		    	        ps.close();
		    	        
		    	      //  System.out.println("Thread API EVENT ID " +  apieventid);
		    	        
		    	        
		    	        int index=0;
		    	        int seq=1;
		    	        String data="";
		    	        while (index < jsoninput.length())
		    	        {
		    	        	data=(jsoninput.substring(index, Math.min(index + 7500,jsoninput.length())));
                        // data.replace('"', '\"');
                         data.replaceAll("'", "\'");
 	    	        	 insert="insert into [OBS_API_JSON] "+
		    	         			" 	    ([obs_api_event_log_id] "+
		    	        			"		,[obs_api_json_seq]     "+
		    	        			"		,[obs_api_json_rcvd_sent] "+
		    	        			"		,[obs_api_json_text])     "+
		    	        			" values("+apieventid+","+seq +",'R','"+data+"')";
		    	        //	System.out.println("Thread insert to API JSON  seq 1" +  insert);
		    	        	 ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
			    	        ps.executeUpdate();
			    	        rs = ps.getGeneratedKeys();
			    	        rs.next();
			    	        // apieventid = rs.getInt(1);	
		    	        	//stmt.executeUpdate(insert);
		    	        	index+=7500;
		    	        	seq++;
 
	    	          }    
		    	        
		    	      index=0;
		    	      data="";
		    	      seq=1;
		    	        while (index < jsonoutput.length())
		    	        {
		    	        	data=(jsonoutput.substring(index, Math.min(index + 7500,jsonoutput.length())));

	  		    	        insert="insert into [OBS_API_JSON] "+
	  				    	        " 	    ([obs_api_event_log_id] "+
	  		      					"		,[obs_api_json_seq]     "+
	  		      					"		,[obs_api_json_rcvd_sent] "+
	  		      					"		,[obs_api_json_text])     "+
	  		      					" values("+apieventid+","+seq+",'S','"+data+"')";
		    	        //	System.out.println("Thread insert to API JSON  seq 1" +  data);
		    	        	 ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
			    	        ps.executeUpdate();
			    	        rs = ps.getGeneratedKeys();
			    	        rs.next();
			    	       // apieventid = rs.getInt(1);	
		    	        	// stmt.executeUpdate(insert);
		    	        	index+=7500;
		    	        	seq++;

	    	          } 
   					    					
		    	        // end commit
		    	        conn.commit();
		    	         committed = true;
			    	     if (stmt != null) { stmt.close(); }
			    	     if (conn != null) { conn.close();}
			    	     if (rs != null)  { rs.close(); }
		    	    }
		    	    
		    	    catch (Exception e)
		    	    {
		    	    	 
		    	    }
	    	    finally {
	    	    	 if (!committed) 
	    	    	 {
	    	    		  try 
	    	    		  {
						    conn.rollback();
				    	     if (stmt != null) { stmt.close(); }
				    	     if (conn != null) { conn.close();}
				    	     if (rs != null)  { rs.close(); }
					      } catch (SQLException e)
	    	    		  {
					     	// TODO Auto-generated catch block
						      e.printStackTrace();
					      }
	    	    	 } 
	    	    }	    	 
	    	    
	         }  // if found a row in rsrc   
             else
             {
            	 System.out.println("No Matching appname found for:"+appname);
             }
             
	    	/*  
	      try {
	         for(int i = 4; i > 0; i--) {
	            System.out.println("Thread: " + threadName + ", " + i);
	            // Let the thread sleep for a while.
	            Thread.sleep(50);
	         }
	     } catch (InterruptedException e) {
	         System.out.println("Thread " +  threadName + " interrupted.");
	     }
	     	java.util.Date date= new java.util.Date();
	   	 	System.out.println(" DSCAutheication "+new Timestamp(date.getTime()));
	   	 	
	   	 */
	     // System.out.println("Thread " +  threadName + " exiting.");
	   }
	   
	   public void start ()
	   {
	   //   System.out.println("Starting " +  threadName );
	      if (t == null)
	      {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	   }

	}

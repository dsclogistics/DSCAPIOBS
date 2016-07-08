package com.dsc.rest.getobsemp;

// here is where I added ldap stuff
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.servlet.ServletContext;
import javax.naming.NamingEnumeration;
 import javax.naming.AuthenticationException;
 import javax.naming.AuthenticationNotSupportedException;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import java.sql.Timestamp;
import java.util.Hashtable; 
// ending lDAP stuff

// new import for json

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
import org.codehaus.jettison.json.JSONObject;


// import com.dsc.dao.obs;

// import com.dsc.util.ToJSON;

import com.dsc.util.getJsonData;
import com.dsc.util.getemployees;
import com.dsc.util.getformslist;
import com.dsc.util.getfunctions;
import com.dsc.util.getlc;
import com.dsc.util.getobserver;
import com.dsc.util.getopenready;
import com.dsc.util.ldap;
import com.dsc.util.savecollform;
import com.dsc.util.verifyNewColrequest;
import com.dsc.util.viewcollform;
import com.dsc.util.writeTofile;
import com.dsc.util.createNewColform;
import com.dsc.util.APIEvent;
import com.dsc.util.DeleteColform;
import com.dsc.util.ObserverRole;
import com.dsc.util.OpenReadybyid;
import com.dsc.util.VerifydelColformrequest;
import com.dsc.util.createColform;
import com.dsc.util.verifyColformrequest;
 

@Path("/v1/getobsemp")
public class V1_getobsemp   {
 
    @GET
	@Produces(MediaType.TEXT_HTML)
	public String returnTitle()
	{
    	//ServletContext sc = getServletContext();
    	//String testNameValue = sc.getInitParameter("testName");
    	java.util.Date date= new java.util.Date();
    	/* 
    	APIEvent R1 = new APIEvent( "Thread-1");
        R1.start();
       	java.util.Date date= new java.util.Date();
   	 	System.out.println(" Return back to user at "+new Timestamp(date.getTime()));
   	 	 */
		return "<p>Default Service</p>"+new Timestamp(date.getTime());
	}
 
//  **************** DSC Authenication Service
    @Path("/DSCAuthenticationSrv")
    @POST
   	@Consumes(MediaType.APPLICATION_JSON)
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response V1_dscldap(JSONObject inputJsonObj) throws Exception {
    	//  System.out.println("Json Object sent by user is "+ inputJsonObj);
    	java.util.Date date= new java.util.Date();
    	java.util.Date sdate=new Timestamp(date.getTime());
   	 //	System.out.println(" DSCAutheication "+new Timestamp(date.getTime()));
    	 Response rb = null;
    	 ldap vr = new ldap();
    	 rb=vr.ldap(inputJsonObj);

       	// API EVENT LOG Start Here 
   	    java.util.Date xdate= new java.util.Date(); 
  	    java.util.Date edate=new Timestamp(xdate.getTime());
  	     JSONObject x=new JSONObject(rb.getEntity().toString());
  		 JSONObject result =  (JSONObject) x; 
  		 result.put("apircvddtm",sdate);
  		 result.put("apisentdtm",edate);
  		 result.put("jsoninput",inputJsonObj);
  		 result.put("resname","DSCAuthenticationSrv");
  	//	 System.out.println("**** NEW ADDED DATA DSCAuthenticationSrv result json:"+result.toString());	
      	APIEvent R1 = new APIEvent(result);
         R1.start();
      // API EVENT LOG Ends Here 
         
     //    System.out.println("**** DSCAuthenticationSrv result json:"+ rb.getEntity().toString());  	 
    	 
    	  return rb;
    	  
  	}

    
//  **************** get observer
    @Path("/obs_getObserver")
    @POST
   	@Consumes(MediaType.APPLICATION_JSON)
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response V1_getobserver(JSONObject inputJsonObj) throws Exception {
    	java.util.Date date= new java.util.Date();
    	java.util.Date sdate=new Timestamp(date.getTime());
   	 //	System.out.println(" getObserver "+new Timestamp(date.getTime()));
   //   System.out.println("**** OBS getObserver Sent by user:"+inputJsonObj);	
    	 Response rb = null;
    	 getobserver vr = new getobserver();
    	 rb=vr.getobserver(inputJsonObj);
  
    	 
      	// API EVENT LOG Start Here 
    	    java.util.Date xdate= new java.util.Date(); 
   	    java.util.Date edate=new Timestamp(xdate.getTime());
   	     JSONObject x=new JSONObject(rb.getEntity().toString());
   		 JSONObject result =  (JSONObject) x; 
   		 result.put("apircvddtm",sdate);
   		 result.put("apisentdtm",edate);
   		 result.put("jsoninput",inputJsonObj);
   		 result.put("resname","obs_getObserver");
   		// System.out.println("**** NEW ADDED DATA DSCAuthenticationSrv result json:"+result.toString());	
       	APIEvent R1 = new APIEvent(result);
          R1.start();
       // API EVENT LOG Ends Here     	
      //    System.out.println("**** obs_getObserver result json:"+ rb.getEntity().toString());                  
    	  return rb;
    	  
  	}    
     
//  **************** get open ready
    @Path("/obs_getOpenReady")
    @POST
   	@Consumes(MediaType.APPLICATION_JSON)
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response V1_getopenready(JSONObject inputJsonObj) throws Exception {
    	java.util.Date date= new java.util.Date();
    	java.util.Date sdate=new Timestamp(date.getTime());
   	// 	System.out.println(" getOpenReady "+new Timestamp(date.getTime()));
 //       System.out.println("**** OBS getOpenReady Sent by user:"+inputJsonObj);	
    	 Response rb = null;
    	 getopenready vr = new getopenready();
    	 rb=vr.getopenready(inputJsonObj);
    	 
    	 
       	// API EVENT LOG Start Here 

     	    java.util.Date xdate= new java.util.Date(); 
    	    java.util.Date edate=new Timestamp(xdate.getTime());
    	     JSONObject x=new JSONObject(rb.getEntity().toString());
    		 JSONObject result =  (JSONObject) x; 
    		 result.put("apircvddtm",sdate);
    		 result.put("apisentdtm",edate);
    		 result.put("jsoninput",inputJsonObj);
    		 result.put("resname","obs_getOpenReady");
    	//	 System.out.println("**** NEW ADDED DATA DSCAuthenticationSrv result json:"+result.toString());	
        	APIEvent R1 = new APIEvent(result);
           R1.start();
        // API EVENT LOG Ends Here 
      //     System.out.println("**** obs_getOpenReady result json:"+ rb.getEntity().toString());            
    	  return rb;
    	  
  	} 
    
//  **************** get lc
    @Path("/obs_getLC")
    @GET
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response V1_getlc() throws Exception {
    	java.util.Date date= new java.util.Date();
 	    java.util.Date sdate=new Timestamp(date.getTime());
   	 //	System.out.println(" Get LC @ "+new Timestamp(date.getTime()));
    	 Response rb = null;
    	 getlc vr = new getlc();
    	 rb=vr.getlc();
    //	 System.out.println(" Return Get LC @"+new Timestamp(date.getTime()));
    //     System.out.println("**** getlc result json:"+ rb.getEntity().toString());
 
    	 
        	// API EVENT LOG Start Here 

      	    java.util.Date xdate= new java.util.Date(); 
     	    java.util.Date edate=new Timestamp(xdate.getTime());
     	     JSONObject x=new JSONObject(rb.getEntity().toString());
     		 JSONObject result =  (JSONObject) x; 
     		 result.put("apircvddtm",sdate);
     		 result.put("apisentdtm",edate);
     		 result.put("jsoninput","");
     		 result.put("resname","obs_getLC");
     	//	 System.out.println("**** NEW ADDED DATA DSCAuthenticationSrv result json:"+result.toString());	
         	APIEvent R1 = new APIEvent(result);
            R1.start();
         // API EVENT LOG Ends Here 
         //   System.out.println("**** obs_getLC result json:"+ rb.getEntity().toString());               
    	  return rb;
    	  
  	}     
//  **************** get open ready
    @Path("/obs_getEmployees")
    @POST
   	@Consumes(MediaType.APPLICATION_JSON)
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response V1_getemployees(JSONObject inputJsonObj) throws Exception {
    	java.util.Date date= new java.util.Date();
 	    java.util.Date sdate=new Timestamp(date.getTime());
   	 //	System.out.println(" getEmmployees "+new Timestamp(date.getTime()));
    //   System.out.println("**** OBS getEmployees Sent by user:"+inputJsonObj);	
    	 Response rb = null;
    	 getemployees vr = new getemployees();
    	 rb=vr.getemployees(inputJsonObj);
   // 	 System.out.println("**** getemployees result json:"+rb.getEntity().toString());	
    	 
    	 
     	// API EVENT LOG Start Here 

   	    java.util.Date xdate= new java.util.Date(); 
  	    java.util.Date edate=new Timestamp(xdate.getTime());
  	     JSONObject x=new JSONObject(rb.getEntity().toString());
  		 JSONObject result =  (JSONObject) x; 
  		 result.put("apircvddtm",sdate);
  		 result.put("apisentdtm",edate);
  		 result.put("jsoninput",inputJsonObj);
  		 result.put("resname","obs_getEmployees");
  	//	 System.out.println("**** NEW ADDED DATA DSCAuthenticationSrv result json:"+result.toString());	
      	APIEvent R1 = new APIEvent(result);
         R1.start();
      // API EVENT LOG Ends Here 
    //     System.out.println("**** obs_getEmployees result json:"+ rb.getEntity().toString());           
    	  return rb;
    	  
  	}  
 

//  **************** get functions
    @Path("/obs_getFunctions")
    @GET
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response V1_getfunctions() throws Exception {
    	java.util.Date date= new java.util.Date();
 	    java.util.Date sdate=new Timestamp(date.getTime());
   //	 	System.out.println(" Get Functions "+new Timestamp(date.getTime()));
    	 Response rb = null;
    	 getfunctions vr = new getfunctions();
    	 rb=vr.getfunctions();
    //	 System.out.println("**** getfunctions result json:"+rb.toString());	
    	 
    	 
     	// API EVENT LOG Start Here 

   	    java.util.Date xdate= new java.util.Date(); 
  	    java.util.Date edate=new Timestamp(xdate.getTime());
  	     JSONObject x=new JSONObject(rb.getEntity().toString());
  		 JSONObject result =  (JSONObject) x; 
  		 result.put("apircvddtm",sdate);
  		 result.put("apisentdtm",edate);
  		 result.put("jsoninput","");
  		 result.put("resname","obs_getFunctions");
  	//	 System.out.println("**** NEW ADDED DATA DSCAuthenticationSrv result json:"+result.toString());	
      	APIEvent R1 = new APIEvent(result);
         R1.start();
      // API EVENT LOG Ends Here 
      //   System.out.println("**** obs_getFunctions result json:"+ rb.getEntity().toString());          
    	  return rb;
    	  
  	}    


    

    
//  **************** get Forms list
    @Path("/obs_getformslist")
    @POST
   	@Consumes(MediaType.APPLICATION_JSON)
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response V1_getformslist(JSONObject inputJsonObj) throws Exception {
    	java.util.Date date= new java.util.Date();
 	    java.util.Date sdate=new Timestamp(date.getTime());
   //	 	System.out.println(" getformslist "+new Timestamp(date.getTime()));
   //    System.out.println("**** OBS getformslist Sent by user:"+inputJsonObj);	
    	 Response rb = null;
    	 getformslist vr = new getformslist();
    	 rb=vr.getformslist(inputJsonObj);
   // 	 System.out.println("**** getformslist result json:"+rb.toString());	
    	 
    	 
     	// API EVENT LOG Start Here 

   	    java.util.Date xdate= new java.util.Date(); 
  	    java.util.Date edate=new Timestamp(xdate.getTime());
  	     JSONObject x=new JSONObject(rb.getEntity().toString());
  		 JSONObject result =  (JSONObject) x; 
  		 result.put("apircvddtm",sdate);
  		 result.put("apisentdtm",edate);
  		 result.put("jsoninput",inputJsonObj);
  		 result.put("resname","obs_getformslist");
  //		 System.out.println("**** NEW ADDED DATA DSCAuthenticationSrv result json:"+result.toString());	
      	APIEvent R1 = new APIEvent(result);
         R1.start();
      // API EVENT LOG Ends Here 
 //        System.out.println("**** obs_getformslist result json:"+ rb.getEntity().toString());        
    	  return rb;
    	  
  	}   
    
//  **************** Here Starts OBSEND POINT
    @Path("/obs_NewCollform")
    @POST
   	@Consumes(MediaType.APPLICATION_JSON)
  	@Produces(MediaType.APPLICATION_JSON)
  	  public Response V1_obsNewform(JSONObject inputJsonObj) throws Exception {
    	java.util.Date date= new java.util.Date();
 	    java.util.Date sdate=new Timestamp(date.getTime());
   //	 	System.out.println(" NewCollform "+new Timestamp(date.getTime()));
    	 Response rb = null;
    //      System.out.println("**** OBS NewCollfrom Sent by user:"+inputJsonObj);	
    	 verifyNewColrequest vr = new verifyNewColrequest();
    	 rb=vr.verifyNewColrequest(inputJsonObj);
    	 if ( rb != null) 
    		 return rb;
 
         createNewColform cf = new createNewColform();
         rb=cf.createNewColform(inputJsonObj);
    //     System.out.println("**** getnewcollform result json:"+rb.toString());	
         
    	 
     	// API EVENT LOG Start Here 

   	    java.util.Date xdate= new java.util.Date(); 
  	    java.util.Date edate=new Timestamp(xdate.getTime());
  	     JSONObject x=new JSONObject(rb.getEntity().toString());
  		 JSONObject result =  (JSONObject) x; 
  		 result.put("apircvddtm",sdate);
  		 result.put("apisentdtm",edate);
  		 result.put("jsoninput",inputJsonObj);
  		 result.put("resname","obs_NewCollform ");
     //	 System.out.println("**** NEW ADDED DATA DSCAuthenticationSrv result json:"+result.toString());	
      	APIEvent R1 = new APIEvent(result);
         R1.start();
      // API EVENT LOG Ends Here 
   //      System.out.println("**** obs_NewCollform result json:"+ rb.getEntity().toString());             
    	  return rb;
    	  
    	}
//  **************** Here Starts OBSEND POINT  
    @Path("/obsgetNewColform")
    @POST
   	@Consumes(MediaType.APPLICATION_JSON)
  	@Produces(MediaType.APPLICATION_JSON)
  	  public Response V1_obsNewformx(JSONObject inputJsonObj) throws Exception {
    	java.util.Date date= new java.util.Date();
 	    java.util.Date sdate=new Timestamp(date.getTime());
   	// 	System.out.println(" NewCollForm "+new Timestamp(date.getTime()));
    	 Response rb = null;
    //     System.out.println("**** OBS getNewColform Sent by user:"+inputJsonObj);	
    	 verifyNewColrequest vr = new verifyNewColrequest();
    	 rb=vr.verifyNewColrequest(inputJsonObj);
    	 if ( rb != null) 
    		 return rb;
 
         createNewColform cf = new createNewColform();
         rb=cf.createNewColform(inputJsonObj);
  //       System.out.println("**** getnewcolform result json:"+rb.toString());	
         
           	 
     	// API EVENT LOG Start Here 

   	    java.util.Date xdate= new java.util.Date(); 
  	    java.util.Date edate=new Timestamp(xdate.getTime());
  	     JSONObject x=new JSONObject(rb.getEntity().toString());
  		 JSONObject result =  (JSONObject) x; 
  		 result.put("apircvddtm",sdate);
  		 result.put("apisentdtm",edate);
  		 result.put("jsoninput",inputJsonObj);
  		 result.put("resname","obs_NewCollform ");
  		// System.out.println("**** NEW ADDED DATA obsgetNewColform result json:"+result.toString());	
      	APIEvent R1 = new APIEvent(result);
         R1.start();
      // API EVENT LOG Ends Here     
     //    System.out.println("**** obsgetNewColform result json:"+ rb.getEntity().toString());       
    	  return rb;
    	  
    	}     
//  **************** Here Starts Process OBS Form Sent Back
    @Path("/obs_Save")
    @POST
   	@Consumes(MediaType.APPLICATION_JSON)
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response V1_obssave(JSONObject jsonObject) throws Exception {
    	java.util.Date date= new java.util.Date();
 	    java.util.Date sdate=new Timestamp(date.getTime());
  // 	 	System.out.println(" Save "+new Timestamp(date.getTime()));
      System.out.println("**** OBS SAVE Sent by user for save:"+jsonObject);		
        Response rb  = null;
        writeTofile w2f = new writeTofile(); 
        rb=w2f.writeTofile(jsonObject); 
        String r="";
       // getJsonData obsjson = new getJsonData();
   	   // String r = obsjson.getJsonData(jsonObject);
        
        	savecollform savejson = new savecollform();
        	String [] msg=savejson.savecollform(jsonObject);
        	r=msg[1];        
        
     	 rb=Response.ok(r.toString()).build();
 //    	 System.out.println("Return JSON OBJECT:"+r);
         
    	 
     	// API EVENT LOG Start Here 

   	    java.util.Date xdate= new java.util.Date(); 
  	    java.util.Date edate=new Timestamp(xdate.getTime());
  	     JSONObject x=new JSONObject(rb.getEntity().toString());
  		 JSONObject result =  (JSONObject) x; 
  		 result.put("apircvddtm",sdate);
  		 result.put("apisentdtm",edate);
  		 result.put("jsoninput",jsonObject);
  		 result.put("resname","obs_Save");
  	//	 System.out.println("**** NEW ADDED DATA DSCAuthenticationSrv result json:"+result.toString());	
      	APIEvent R1 = new APIEvent(result);
         R1.start();
      // API EVENT LOG Ends Here      	 
    //     System.out.println("**** obs_Save result json:"+ rb.getEntity().toString());     	 
  	  return rb;
  	}    
//  **************** Here Starts Process OBS Form Sent Back
    @Path("/obsSave")
    @POST
   	@Consumes(MediaType.APPLICATION_JSON)
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response V1_obssavex(JSONObject jsonObject) throws Exception {
    	java.util.Date date= new java.util.Date();
 	    java.util.Date sdate=new Timestamp(date.getTime());
   //	 	System.out.println(" Save "+new Timestamp(date.getTime()));
  //    System.out.println("**** OBS SAVE Sent by user for save:"+jsonObject);	
        Response rb  = null;
        writeTofile w2f = new writeTofile(); 
        rb=w2f.writeTofile(jsonObject); 
        String r="";
       // getJsonData obsjson = new getJsonData();
   	   // String r = obsjson.getJsonData(jsonObject);

        savecollform savejson = new savecollform();
        String [] msg=savejson.savecollform(jsonObject);
          r=msg[1];
        
     	 rb=Response.ok(r.toString()).build();
 //    	 System.out.println("Return JSON OBJECT:"+r);
    	 
     	// API EVENT LOG Start Here 

   	    java.util.Date xdate= new java.util.Date(); 
  	    java.util.Date edate=new Timestamp(xdate.getTime());
  	     JSONObject x=new JSONObject(rb.getEntity().toString());
  		 JSONObject result =  (JSONObject) x; 
  		 result.put("apircvddtm",sdate);
  		 result.put("apisentdtm",edate);
  		 result.put("jsoninput",jsonObject);
  		 result.put("resname","obs_Save");
  //		 System.out.println("**** NEW ADDED DATA DSCAuthenticationSrv result json:"+result.toString());	
      	APIEvent R1 = new APIEvent(result);
         R1.start();
      // API EVENT LOG Ends Here      	 
     	      	 
     //    System.out.println("**** obs_Save result json:"+ rb.getEntity().toString());     	      	 
  	  return rb;
  	}    


//  **************** Here Starts Get OBS Collection Form
	@Path("/obsgetCollform")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response V1_obsgcf(JSONObject inputJsonObj) throws Exception {
    	java.util.Date date= new java.util.Date();
	    java.util.Date sdate=new Timestamp(date.getTime());
  // 	 	System.out.println(" GetCollection Form "+new Timestamp(date.getTime()));
		 Response rb = null;
	//      System.out.println("**** OBS getgetCollform Sent by user:"+inputJsonObj);			 
		 verifyColformrequest vr = new verifyColformrequest();
		 rb=vr.verifyColformrequest(inputJsonObj);
		 if ( rb != null) 
			 return rb;
	
	    // createColform cf = new createColform();
	    // rb=cf.createColform(inputJsonObj);
	     
	  	 
         viewcollform cf = new viewcollform();
          rb=cf.viewcollform(inputJsonObj);   
       //   System.out.println("**** getcollform result json:"+rb.toString());	
 //      	 System.out.println("**** getcollform result json:"+rb.getEntity().toString());	
       	 
    	 
     	// API EVENT LOG Start Here 

   	    java.util.Date xdate= new java.util.Date(); 
  	    java.util.Date edate=new Timestamp(xdate.getTime());
  	     JSONObject x=new JSONObject(rb.getEntity().toString());
  		 JSONObject result =  (JSONObject) x; 
  		 result.put("apircvddtm",sdate);
  		 result.put("apisentdtm",edate);
  		 result.put("jsoninput",inputJsonObj);
  		 result.put("resname","obs_getCollform");
 // 		 System.out.println("**** NEW ADDED DATA DSCAuthenticationSrv result json:"+result.toString());	
      	APIEvent R1 = new APIEvent(result);
         R1.start();
      // API EVENT LOG Ends Here      	 
   //      System.out.println("**** obsgetCollform result json:"+ rb.getEntity().toString());         	        	 
		  return rb;
		  
	}

   
	//  **************** Here Starts Get OBS Collection Form
    @Path("/obs_getCollform")
    @POST
   	@Consumes(MediaType.APPLICATION_JSON)
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response V1_obsgcfx(JSONObject inputJsonObj) throws Exception {
    	java.util.Date date= new java.util.Date();
    	java.util.Date sdate=new Timestamp(date.getTime());
   	 //	System.out.println(" Get Collection Form "+new Timestamp(sdate.getTime()));
    	 Response rb = null;
    //    System.out.println("**** OBS getCollform Sent by user:"+inputJsonObj);		 
    	 verifyColformrequest vr = new verifyColformrequest();
    	 rb=vr.verifyColformrequest(inputJsonObj);
    	 if ( rb != null) 
    		 return rb;
 
        // createColform cf = new createColform();
       //  rb=cf.createColform(inputJsonObj);
    	 
           viewcollform cf = new viewcollform();
            rb=cf.viewcollform(inputJsonObj);    	
           // System.out.println("**** getcolform result json:"+rb.toString());	
  //        	 System.out.println("**** getcollform result json:"+rb.getEntity().toString());	
          	 
          	// API EVENT LOG Start Here 
          	    java.util.Date xdate= new java.util.Date(); 
         	    java.util.Date edate=new Timestamp(xdate.getTime());
         	     JSONObject x=new JSONObject(rb.getEntity().toString());
         		 JSONObject result =  (JSONObject) x; 
         		  result.put("apircvddtm",sdate);
         		 result.put("apisentdtm",edate);
         		 result.put("jsoninput",inputJsonObj);
         		 result.put("resname","obs_getCollform");
 //        		 System.out.println("**** NEW ADDED DATA getcollform result json:"+result.toString());	
             	APIEvent R1 = new APIEvent(result);
                R1.start();
             // API EVENT LOG Ends Here 
 //               System.out.println("**** obsgetCollform result json:"+ rb.getEntity().toString());             		 
    	  return rb;
    	  
  	}  

//  **************** Here Starts Get OBS Collection Form
    @Path("/obs_delCollform")
    @POST
   	@Consumes(MediaType.APPLICATION_JSON)
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response V1_obsdcf(JSONObject inputJsonObj) throws Exception {
    	java.util.Date date= new java.util.Date();
	    java.util.Date sdate=new Timestamp(date.getTime());
  // 	 	System.out.println(" delete Form "+new Timestamp(date.getTime()));
    	 Response rb = null;
 //        System.out.println("**** OBS delCollform Sent by user:"+inputJsonObj);	
    	 VerifydelColformrequest vr = new VerifydelColformrequest();
    	 rb=vr.verifydelColformrequest(inputJsonObj);
    	 if ( rb != null) 
    		 return rb;
 
    	 DeleteColform cf = new DeleteColform();
         rb=cf.deleteColform(inputJsonObj);
//         System.out.println("**** delcollform result json:"+rb.toString());	
         
      	 
      	// API EVENT LOG Start Here 
      	    java.util.Date xdate= new java.util.Date(); 
     	    java.util.Date edate=new Timestamp(xdate.getTime());
     	     JSONObject x=new JSONObject(rb.getEntity().toString());
     		 JSONObject result =  (JSONObject) x; 
     		  result.put("apircvddtm",sdate);
     		 result.put("apisentdtm",edate);
     		 result.put("jsoninput",inputJsonObj);
     		 result.put("resname","obs_getCollform");
   //  		 System.out.println("**** NEW ADDED DATA getcollform result json:"+result.toString());	
         	APIEvent R1 = new APIEvent(result);
            R1.start();
         // API EVENT LOG Ends Here 
   //         System.out.println("**** obs_delCollform result json:"+ rb.getEntity().toString());        	
    	  return rb;
    	  
  	}   
//  **************** Here Starts Get OBS Collection Form
    @Path("/obsdelCollform")
    @POST
   	@Consumes(MediaType.APPLICATION_JSON)
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response V1_obsdcfx(JSONObject inputJsonObj) throws Exception {
    	java.util.Date date= new java.util.Date();
	    java.util.Date sdate=new Timestamp(date.getTime());
 //  	 	System.out.println(" Delete Form "+new Timestamp(date.getTime()));
    	 Response rb = null;
 //    	  System.out.println("**** OBS delCollform Sent by user:"+inputJsonObj);	
    	 VerifydelColformrequest vr = new VerifydelColformrequest();
    	 rb=vr.verifydelColformrequest(inputJsonObj);
    	 if ( rb != null) 
    		 return rb;
 
    	 DeleteColform cf = new DeleteColform();
         rb=cf.deleteColform(inputJsonObj);
 //        System.out.println("**** delcollform result json:"+rb.toString());	
         
      	 
      	// API EVENT LOG Start Here 
      	    java.util.Date xdate= new java.util.Date(); 
     	    java.util.Date edate=new Timestamp(xdate.getTime());
     	     JSONObject x=new JSONObject(rb.getEntity().toString());
     		 JSONObject result =  (JSONObject) x; 
     		  result.put("apircvddtm",sdate);
     		 result.put("apisentdtm",edate);
     		 result.put("jsoninput",inputJsonObj);
     		 result.put("resname","obs_getCollform");
 //    		 System.out.println("**** NEW ADDED DATA getcollform result json:"+result.toString());	
         	APIEvent R1 = new APIEvent(result);
            R1.start();
         // API EVENT LOG Ends Here 
  //          System.out.println("**** obs_delCollform result json:"+ rb.getEntity().toString());       	
    	  return rb;
    	  
  	}

     // openreadybyid
    
//  **************** Here Starts Get OBS Collection Form
    @Path("/openreadybyid")
    @POST
   	@Consumes(MediaType.APPLICATION_JSON)
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response V1_openreadybyid(JSONObject inputJsonObj) throws Exception {
    	java.util.Date date= new java.util.Date();
	    java.util.Date sdate=new Timestamp(date.getTime());
    	 Response rb = null;	
    	 OpenReadybyid or = new OpenReadybyid();
    	 rb=or.OpenReadybyid(inputJsonObj);
 	 
      	// API EVENT LOG Start Here 
      	    java.util.Date xdate= new java.util.Date(); 
     	    java.util.Date edate=new Timestamp(xdate.getTime());
     	     JSONObject x=new JSONObject(rb.getEntity().toString());
     		 JSONObject result =  (JSONObject) x; 
     		  result.put("apircvddtm",sdate);
     		 result.put("apisentdtm",edate);
     		 result.put("jsoninput",inputJsonObj);
     		 result.put("resname","openreadybyid");
 //    		 System.out.println("**** NEW ADDED DATA getcollform result json:"+result.toString());	
  //       	APIEvent R1 = new APIEvent(result);
  //          R1.start();
         // API EVENT LOG Ends Here 
 //           System.out.println("**** openreadybyid result json:"+ rb.getEntity().toString());       	
    	  return rb;
    	  
  	}

    //  get observerrole
//  **************** Here Starts Get OBS Collection Form
    @Path("/observerrole/")
    @POST
   	@Consumes(MediaType.APPLICATION_JSON)
  	@Produces(MediaType.APPLICATION_JSON)
  	public Response V1_observerrole(JSONObject inputJsonObj) throws Exception {
    	java.util.Date date= new java.util.Date();
	    java.util.Date sdate=new Timestamp(date.getTime());
    	 Response rb = null;	
    	 ObserverRole or = new ObserverRole();
    	 rb=or.ObserverRole(inputJsonObj);
 	 
      	// API EVENT LOG Start Here 
      	    java.util.Date xdate= new java.util.Date(); 
     	    java.util.Date edate=new Timestamp(xdate.getTime());
     	     JSONObject x=new JSONObject(rb.getEntity().toString());
     		 JSONObject result =  (JSONObject) x; 
     		  result.put("apircvddtm",sdate);
     		 result.put("apisentdtm",edate);
     		 result.put("jsoninput",inputJsonObj);
     		 result.put("resname","openreadybyid");
 //    		 System.out.println("**** NEW ADDED DATA getcollform result json:"+result.toString());	
  //       	APIEvent R1 = new APIEvent(result);
  //          R1.start();
         // API EVENT LOG Ends Here 
 //           System.out.println("**** openreadybyid result json:"+ rb.getEntity().toString());       	
    	  return rb;
    	  
  	}
    
} // end of main class
package com.dsc.dao;
import javax.naming.*;
import javax.sql.*;

public class obs {
	
	private static DataSource obs = null;
	private static Context  context = null;
 
	
	public static DataSource obsConn() throws Exception
	{
		if (obs != null){
			return obs;
		}
		try{
			
			if (context == null){
				context = new InitialContext();
			}
		  obs = (DataSource) context.lookup("java:/comp/env/OBS");
		}
		catch( Exception e) {
			e.printStackTrace();
		}
		return obs;
	}

}

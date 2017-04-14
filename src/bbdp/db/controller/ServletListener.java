package bbdp.db.controller;
 
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import bbdp.db.model.DBConnection;
 

public class ServletListener implements ServletContextListener {
 

    public void contextInitialized(ServletContextEvent event) {
 
    	ServletContext sc = event.getServletContext();		//取得Context裡的資料
 
    	String url = sc.getInitParameter("url");
    	String user_name = sc.getInitParameter("user_name");
    	String password = sc.getInitParameter("password");
    	String database = sc.getInitParameter("database");
    	
    	DBConnection db = new DBConnection(url + database, user_name, password);
    	sc.setAttribute("db", db);
 
    }
 

    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
 
}
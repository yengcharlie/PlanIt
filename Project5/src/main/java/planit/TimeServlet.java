package planit;



import com.google.appengine.api.datastore.DatastoreService;

import com.google.appengine.api.datastore.DatastoreServiceFactory;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;

import com.google.appengine.api.users.UserService;

import com.google.appengine.api.users.UserServiceFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

 

import java.io.IOException;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

 

public class TimeServlet extends HttpServlet {
	 
	private static final Logger _logger = Logger.getLogger(TimeServlet.class.getName());
    public void doPost(HttpServletRequest req, HttpServletResponse resp)

                throws IOException {

        UserService userService = UserServiceFactory.getUserService();

        User user = userService.getCurrentUser();
        String email = user.getEmail();
        _logger.info(email);
 
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        String start[] = req.getParameterValues("start[]");
        String end[] = req.getParameterValues("end[]");
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String date = req.getParameter("datepicker");
        Date d = new Date();
        
        SimpleDateFormat dateF = new SimpleDateFormat("yyyy-MM-dd");
        try {
			d = dateF.parse(date); 
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//resp.sendRedirect("/invalid.jsp");
			//d = new Date();
			//e1.printStackTrace();
			resp.sendRedirect("/invalid.jsp");
			
			
		}
        
        for(int i = 0; i<start.length;i++) {
        try {
			Date dt = d;
			Date dt1 = d;
			dt= sdf.parse(start[i]);
			dt1= sdf.parse(end[i]);
			Calendar cal = Calendar.getInstance();
			    cal.setTime(d);
			    Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
			    calendar.setTime(dt);   // assigns calendar to given date 
			    int hour = calendar.get(Calendar.HOUR_OF_DAY);
			    if(hour==0) {
			    	hour=12;
			    }
			    int minute = calendar.get(Calendar.MINUTE);
			    Calendar calendar1 = Calendar.getInstance(); // creates a new calendar instance
			    calendar.setTime(dt1);   // assigns calendar to given date 
			    int hour1 = calendar.get(Calendar.HOUR_OF_DAY);
			    if(hour1==0) {
			    	hour1=12;
			    }
			    int minute1 = calendar.get(Calendar.MINUTE);
			    // Set time fields to zero
			    cal.set(Calendar.HOUR_OF_DAY,hour);
			    cal.set(Calendar.MINUTE, minute);
			    cal.set(Calendar.SECOND, 0);
			    dt = cal.getTime();
			    cal.set(Calendar.HOUR_OF_DAY,hour1);
			    cal.set(Calendar.MINUTE, minute1);
			    cal.set(Calendar.SECOND, 0);
			    dt1 = cal.getTime();
			    
			
			

	        Key eKey = KeyFactory.createKey("Stamp", email);
	        
	        Entity em = new Entity("st",eKey);
	        em.setProperty("starttime", dt);
	        em.setProperty("endtime", dt1);
	        em.setProperty("email", email);
	        _logger.info(dt.toString());
	        _logger.info(dt1.toString());
	        
	        datastore.put(em);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			resp.sendRedirect("/errors.jsp");
			//e.printStackTrace();
		}
        }
        Key blogKey = KeyFactory.createKey("Stamp", email);

        // Run an ancestor query to ensure we see the most up-to-date

        // view of the Greetings belonging to the selected Guestbook.
 
        Query query = new Query("st",blogKey);
        //Query query = new Query("st", blogKey).addSort("starttime", Query.SortDirection.ASCENDING);

        List<Entity> greetings = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(2000));
        
        for(Entity gre : greetings) {
        	System.out.println(gre.getProperty("starttime").toString());
        	System.out.println(gre.getProperty("endtime").toString());
        	System.out.println(gre.getProperty("email").toString());
        }

        resp.sendRedirect("/index.jsp");

    }

}



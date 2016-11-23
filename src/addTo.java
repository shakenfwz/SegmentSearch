/* A servlet to display the contents of the MySQL segmentdb database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class addTo extends HttpServlet
{
    public String getServletInfo()
    {
       return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    // Use http GET

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();


        try
           {
        	
        	 Class.forName("com.mysql.jdbc.Driver").newInstance();
        		
             // Connect to MySQL as root
            Connection connection = DriverManager.getConnection("jdbc:mysql://","root", "changeme");
           
            // Create and execute an SQL statement to get all the database names
            Statement myDBStm = connection.createStatement();
           
           
            String uname = request.getParameter("user");
            String password =request.getParameter("password");
            HttpSession session = request.getSession(false);
            
            session.setAttribute("sysUser", uname);
            
            session.setAttribute("sysPass", password);
            
        
            
         
            
      
            response.sendRedirect("http://localhost:8080/SegmentSearch/showPrivileges.html");
            	
            
           
              myDBStm.close();
              connection.close();
            
           }
        catch (SQLException ex) {
              while (ex != null) {
                    System.out.println ("SQL Exception:  " + ex.getMessage ());
                    ex = ex.getNextException ();
                }  // end while
            }  // end catch SQLException

        catch(java.lang.Exception ex)
            {
                out.println("<HTML>" +
                            "<HEAD><TITLE>" +
                            "segmentdb: Error" +
                            "</TITLE></HEAD>\n<BODY>" +
                            "<P>SQL error in doGet: " +
                            ex.getMessage() + "</P></BODY></HTML>");
                return;
            }
         out.close();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
	doGet(request, response);
    }
}


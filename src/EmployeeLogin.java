/* A servlet to display the contents of the MySQL segmentdb database */

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;

import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class EmployeeLogin extends HttpServlet
{
    public String getServletInfo()
    {
       return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    // Use http GET

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
 //       String loginUser = "root";
 //       String loginPasswd = "changeme";
 //       String loginUrl = "jdbc:mysql://localhost:3306/segmentdb";

        response.setContentType("text/html");    // Response mime type

        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

        out.println("<HTML><HEAD><TITLE>Login</TITLE></HEAD>");
        out.println("<BODY><H1>Login</H1>");
        
        Connection dbcon = null;
        Statement statement = null;
        ResultSet rs =null;
        try
           {
              //Class.forName("org.gjt.mm.mysql.Driver");
              //Class.forName("com.mysql.jdbc.Driver").newInstance();
        	  InitialContext contxt = new InitialContext();
    	      DataSource source = (DataSource)contxt.lookup("java:comp/env/jdbc/segmentdb");
    	      dbcon = source.getConnection();
              //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
              // Declare our statement
               statement = dbcon.createStatement();

	      String email = request.getParameter("email");
	      String pass = request.getParameter("pass");
              String query = "SELECT * from employees where emp_name = '" + email + "' and " +
              		"password = '" + pass + "'";

              // Perform the query
              rs = statement.executeQuery(query);

              if (rs.first()) 
              {
            	  request.getSession(true);
            	  response.sendRedirect("http://localhost:8080/SegmentSearch/EmployeeHome.html");
              }
              else
              {
            	  response.sendRedirect("http://localhost:8080/SegmentSearch/EmployeeLogin.html");
              }
              
              rs.close();
              statement.close();
              dbcon.close();
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

}
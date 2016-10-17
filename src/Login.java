/* A servlet to display the contents of the MySQL segmentdb database */

import java.io.*;

import java.sql.*;



import javax.naming.InitialContext;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class Login extends HttpServlet
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
              // Declare our statement
          statement = dbcon.createStatement();
        
	      String email = request.getParameter("email");
	      String pass = request.getParameter("pass");
	      
	         /*
              String query = "SELECT * from users where email = '" + email + "' and " +
              		"password = '" + pass + "'";
              */
	      String query = "SELECT * from users ";
              // Perform the query
             rs = statement.executeQuery(query);

              if (rs.first()) 
              {	
            	  HttpSession session = request.getSession(true);
            	  session.setMaxInactiveInterval(30*60);
            	  session.setAttribute("email", email);
        	      session.setAttribute("pass",pass);
            	  response.sendRedirect("http://localhost:8080/SegmentSearch/Home.jsp");
              }
              else
              {
            	  response.sendRedirect("http://localhost:8080/SegmentSearch/login.html");
              }
              
              rs.close();
              statement.close();
              dbcon.close();
              statement = null;
              dbcon = null;
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
                            "<P>SQL error in doPost: " +
                            ex.getMessage() + "</P></BODY></HTML>");
                return;
            }finally {
                try { if(null!=rs)rs.close();} 
                catch (SQLException e) 
                {e.printStackTrace();}
                try { if(null!=statement)statement.close();}
                catch (SQLException e) 
                {e.printStackTrace();}
                try { if(null!=dbcon)dbcon.close();} 
                catch (SQLException e) 
                {e.printStackTrace();}
            }
        
         out.close();
       
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
        {
    	doGet(request, response);
        }
}
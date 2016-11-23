/* A servlet to display the contents of the MySQL segmentdb database */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class BrowseByTitle extends HttpServlet
{
    public String getServletInfo()
    {
       return "Servlet connects to MySQL database and displays result of a SELECT";
    }

    // Use http GET

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
    	if (request.getSession(false) == null)
    	{
    		response.sendRedirect("http://localhost:8080/SegmentSearch/Login.html");
    	}
       

        response.setContentType("text/html");    // Response mime type

        InitialContext contxt = null;
		try {
			contxt = new InitialContext();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataSource source = null;
		try {
			source = (DataSource)contxt.lookup("java:comp/env/jdbc/segmentdb");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
      
        // Output stream to STDOUT
        PrintWriter out = response.getWriter();

       
        Connection dbcon = null;
        Statement statement = null;
        ResultSet rs =null;


        out.println("<HTML><HEAD><TITLE>Browse By Title</TITLE>"+  "<script type='text/javascript'></script>" +"</HEAD>");
        out.println("<BODY  BGCOLOR=' #42a2ce'><H1>Browse By Title</H1>");


        try
           {
        	   dbcon = source.getConnection();
               // Declare our statement
                statement = dbcon.createStatement();
              String character = request.getParameter("letter");

          String query = "SELECT * from movies where title like '"+character+"%' order by title asc";

       // Perform the query
         rs = statement.executeQuery(query);
         out.println("<div ALIGN='CENTER'> <a href='javascript:history.back()'>" + "Back " + "</a>|<a href='http://localhost:8080/SegmentSearch/Home.jsp'> Home </a>|<a href='logout'> Logout </a>| <a href='http://localhost:8080/SegmentSearch/Checkout.jsp'> Checkout </a></div>");
         out.println("<div ALIGN='CENTER'>");
         out.println("<TABLE border>");
         out.println("<tr>");
         out.println("<th>Photo</th>");
         out.println("<th>Movie id</th>");
         out.println("<th>Title</th>");
         out.println("<th>Director</th>");
         out.println("<th>Year</th>");
         out.println("<th>Trailer</th>");
         out.println("</tr>");
          
          // Iterate through each row of rs
          while (rs.next())
          {
              String title = rs.getString("title");
              title = title.replace("'", "&#39");
              int movieID = rs.getInt("id");
              String movieDirector = rs.getString("director");
              String year = rs.getString("year");
              String trailer = rs.getString("trailer_url");
              String photo = rs.getString("banner_url");
              
              out.println("<tr>" + 
            		  "<td><img src='"+photo+ "' height='50' width='50'></td>" + 
                      "<td>" + movieID + "</td>" +
                      "<td><b><a href='SingleMovie?title="+title+"&genre=&sort=&sortby=&number=&numOfTimes=0'>"+title+"</a></b></br><a href='cart?movieid="+movieID+"&qty=1'><img src='http://gateway.hopto.org:9000/fabflix/images/buttonOff.png'></a></td>" +
                      "<td>" + movieDirector + "</td>" +
                      "<td>" + year + "</td>" +
                      "<td><a href ='"+trailer +"'>"+trailer +"</a></td>" +
                      "</tr>");
       
          }
          out.println("</table>");
          out.println("</div>");
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
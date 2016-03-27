/* A servlet to display the contents of the MySQL segmentdb database */

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class advancedSearch extends HttpServlet
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

        response.setContentType("text/html");    // Response mime type

       

        out.println("<HTML><HEAD><TITLE>Browse By Title</TITLE>"+  "<script type='text/javascript'></script>" +"</HEAD>");
        out.println("<BODY BGCOLOR=' #42a2ce'><H1>Browse By Title</H1>");


        try
           {
              //Class.forName("org.gjt.mm.mysql.Driver");
             
              dbcon = source.getConnection();
              // Declare our statement
               statement = dbcon.createStatement();
              String number = request.getParameter("number");
              String temp = request.getParameter("numOfTimes");
              int numOfTimes = Integer.parseInt(temp);
          String sort = request.getParameter("sort");
          String sortBy = request.getParameter("sortby");
          String title = request.getParameter("title");
          String year = request.getParameter("year");
          String director = request.getParameter("director");
          String fname = request.getParameter("fname");
          String lname = request.getParameter("lname");
          String genre = request.getParameter("genre");
          String query =   "select distinct banner_url, movies.id, title, year, director from movies inner " +
          		"join stars_in_movies on movies.id = stars_in_movies.movie_id inner " +
          		"join stars on stars.id = stars_in_movies.star_id where title " +
          		"like '%" + title + "%' and year like '%" + year + "%' and director like" +
          				" '%" + director + "%' and first_name like '%" + fname + "%' and last_name " +
          						"like '%" + lname + "%' "+sort+" "+sortBy+"";

          
          
          
       // Perform the query
           rs = statement.executeQuery(query);
          
          if (sortBy.equals(""))
          {
        	  sortBy = "asc";
          }
          else if (sortBy.equals("asc"))
          {
        	  sortBy = "desc";
          }
          else if (sortBy.equals("desc"))
          {
        	  sortBy = "asc";
          }
          
          out.println("<form action='advancedSearch' method = 'get'>");
          out.println("Number of movies per page: <input type='text' name='number'>");
          out.println("<input type='hidden' name='title' value='"+title+"'>");
          out.println("<input type='hidden' name='genre' value=''>");
          out.println("<input type='hidden' name='sort' value=''>");
          out.println("<input type='hidden' name='sortby' value=''>");
          out.println("<input type='hidden' name='numOfTimes' value='0'>");
          out.println("<input type='hidden' name='director' value='"+director+"'>");
          out.println("<input type='hidden' name='year' value='"+year+"'>");
          out.println("<input type='hidden' name='fname' value='"+fname+"'>");
          out.println("<input type='hidden' name='lname' value='"+lname+"'>");
          out.println("<input type='submit'></form>");
          

          // Iterate through each row of rs
        
            	  
          if (number.equals(""))
          { 
        	  out.println("<div ALIGN='CENTER'> <a href='javascript:history.back()'>" +" Back " + "</a>|<a href='http://localhost:8080/SegmentSearch/Home.jsp'> Home </a>|<a href='logout'> Logout </a>| <a href='http://localhost:8080/SegmentSearch/Checkout.jsp'> Checkout </a></div>");
              out.println("<div ALIGN='CENTER'>");
        	   out.println("<TABLE border>");
               out.println("<tr>");
               out.println("<th>Photo</th>");
               out.println("<th>Movie ID</th>");
               out.println("<th><b><a href='advancedSearch?title="+title+"&genre=&sort=" +
               		"order by title&sortby="+sortBy+"&number=&numOfTimes="+temp+"&director="+director+"" +
               				"&year="+year+"&fname="+fname+"&lname="+lname+"'>title</a></b></br></th>");
               out.println("<th>Director</th>");
               out.println("<th><b><a href='advancedSearch?title="+title+"&genre=&sort=" +
                 		"order by year&sortby="+sortBy+"&number=&numOfTimes="+temp+"&director="+director+"" +
           				"&year="+year+"&fname="+fname+"&lname="+lname+"'>year</a></b></br></th>");
               out.println("<th>Director</th>");
               out.println("<th>Stars</th>");
               out.println("</tr>");
            	  while (rs.next())
            	  {
            		  String photo = rs.getString("banner_url");
            		  String movieTitle = rs.getString("movies.title");
            		  movieTitle = movieTitle.replace("'", "''");
            		  Statement statement3 = dbcon.createStatement();
            		  String query3 = "select distinct stars.first_name, stars.last_name from movies inner join stars_in_movies on " +
                    		"movies.id = stars_in_movies.movie_id inner join stars on stars_in_movies.star_id = " +
                    		"stars.id where movies.title = '"+movieTitle+"'";
            		  ResultSet rs3 = statement3.executeQuery(query3);
            		  String stars = "";
            		  while (rs3.next())
            		  {
            			  String firstName = rs3.getString("stars.first_name");
            			  String lastName = rs3.getString("stars.last_name");
            			  stars = "<b><a href='SingleStar?fname=" + firstName +
                			  "&lname="+lastName+"'>"+firstName + " " + lastName +"</a></b></br>" + stars;
            		  }
            		  rs3.close();
            		  statement3.close();
            		  Statement statement2 = dbcon.createStatement();
            		  String query2 = "select genres.name from movies inner join genres_in_movies on movies.id = " +
                    		"genres_in_movies.movie_id inner join genres on genres_in_movies.genre_id = genres.id" +
                    		" where movies.title = '"+movieTitle+"'";
            		  ResultSet rs2 = statement2.executeQuery(query2);
            		  String genres = "";
            		  while (rs2.next())
            		  {
            			  genres = "<b><a href='SelectTitle?genre=" + rs2.getString("genres.name") +
                    			  "&number=&numOfTimes=0&title=&sort=&sortby='>"+rs2.getString("genres.name")+"" +
                    			  		"</a></b></br>" + genres;
            		  }
            		  rs2.close();
            		  statement2.close();
            	  
            		  String movieID = rs.getString("movies.id");
            		  movieTitle = movieTitle.replace("''", "&#39");
            		  String movieDirector = rs.getString("movies.director");
            		  int theYear = rs.getInt("movies.year");
            		  out.println("<tr>" + 
            				  "<td><img src='"+photo+ "' height='50' width='50'></td>" +
                              "<td>" + movieID + "</td>" +
                              "<td><b><a href='SingleMovie?title=" + movieTitle + "'>"+movieTitle+"</a> </b></br><a href='cart?movieid="+movieID+"&qty=1'><img src='http://gateway.hopto.org:9000/fabflix/images/buttonOff.png'></a></td>" +
                              "<td>" + movieDirector + "</td>" +
                              "<td>" + theYear + "</td>" +
                              "<td>" + genres + "</td>" +
                              "<td>" + stars + "</td>" +
                              "</tr>");
            		  
            	  }
          }
          else
          {
        	  out.println("<div ALIGN='CENTER'> <a href='javascript:history.back()'>" + "Back " + "</a>|<a href='http://localhost:8080/SegmentSearch/Home.jsp'> Home </a>|<a href='logout'> Logout </a>| <a href='http://localhost:8080/SegmentSearch/Checkout.jsp'> Checkout </a></div>");
              out.println("<div ALIGN='CENTER'>");
        	  out.println("<TABLE border>");
              out.println("<tr>");
              out.println("<th>Photo</th>");
              out.println("<th>Movie ID</th>");
              out.println("<th><b><a href='advancedSearch?title="+title+"&genre="+genre+"&sort=" +
              		"order by title&sortby="+sortBy+"&number="+number+"&numOfTimes="+temp+"&director=" +
              				""+director+"&year="+year+"&fname="+fname+"&lname="+lname+"'>Title</a></b></br></th>");
              out.println("<th>Director</th>");
              out.println("<th><b><a href='advancedSearch?title="+title+"&genre="+genre+"&sort=" +
              		"order by year&sortby="+sortBy+"&number="+number+"&numOfTimes="+temp+"&director=" +
              				""+director+"&year="+year+"&fname="+fname+"&lname="+lname+"'>Year</a></b></br></th>");
              out.println("<th>Genres</th>");
              out.println("<th>Stars</th>");
              out.println("</tr>");
        	  int num = Integer.parseInt(number);
        	  for (int i = 0; i < num*numOfTimes; i++)
        	  {
        		  rs.next();
        	  }
        	  int index = 0;
        	  while ((rs.next()) & (index < num))
        	  {	  String photo = rs.getString("banner_url");
        		  String movieTitle = rs.getString("movies.title");
        		  movieTitle = movieTitle.replace("'", "''");
        		  Statement statement3 = dbcon.createStatement();
        		  String query3 = "select distinct stars.first_name, stars.last_name from movies inner join stars_in_movies on " +
                		"movies.id = stars_in_movies.movie_id inner join stars on stars_in_movies.star_id = " +
                		"stars.id where movies.title = '"+movieTitle+"'";
        		  ResultSet rs3 = statement3.executeQuery(query3);
        		  String stars = "";
        		  while (rs3.next())
        		  {
        			  String firstName = rs3.getString("stars.first_name");
        			  String lastName = rs3.getString("stars.last_name");
        			  stars = "<b><a href='SingleStar?fname=" + firstName +
            			  "&lname="+lastName+"'>"+firstName + " " + lastName +"</a></b></br>" + stars;
        		  }
        		  rs3.close();
        		  statement3.close();
        		  Statement statement2 = dbcon.createStatement();
        		  String query2 = "select genres.name from movies inner join genres_in_movies on movies.id = " +
                		"genres_in_movies.movie_id inner join genres on genres_in_movies.genre_id = genres.id" +
                		" where movies.title = '"+movieTitle+"'";
        		  ResultSet rs2 = statement2.executeQuery(query2);
        		  String genres = "";
        		  while (rs2.next())
        		  {
        			  genres = "<b><a href='SelectTitle?genre=" + rs2.getString("genres.name") +
                			  "&number=&numOfTimes=0&title=&sort=&sortby='>"+rs2.getString("genres.name")+"" +
                			  		"</a></b></br>" + genres;
        		  }
        		  rs2.close();
        		  statement2.close();
        	  
        		  String movieID = rs.getString("movies.id");
        		  movieTitle = movieTitle.replace("''", "&#39");
        		  String movieDirector = rs.getString("movies.director");
        		  int theYear = rs.getInt("movies.year");
        		  out.println("<tr>" + 
        				  "<td><img src='"+photo+ "' height='50' width='50'></td>" +
                          "<td>" + movieID + "</td>" +
                          "<td><b><a href='SingleMovie?title=" + movieTitle + "'>"+movieTitle+"</a></b></br><a href='cart?movieid="+movieID+"&qty=1'><img src='http://gateway.hopto.org:9000/fabflix/images/buttonOff.png'></a></td>" +
                          "<td>" + movieDirector + "</td>" +
                          "<td>" + theYear + "</td>" +
                          "<td>" + genres + "</td>" +
                          "<td>" + stars + "</td>" +
                          "</tr>");
        		  index++;
        		  
        	  }
        	  int t = Integer.parseInt(temp);
        	  t = t + 1;
        	  temp = Integer.toString(t);
        	  out.println("<b><a href='advancedSearch?title="+title+"&genre="+genre+"&sort=" +
        	  		"&sortby=&number="+number+"&numOfTimes="+temp+"&director="+director+"&year=" +
        	  				""+year+"&fname="+fname+"&lname="+lname+"'>Next</a></b></br>");
        	  t = t-2;
        	  if (t < 0)
        	  {
        		  t = 0;
        	  }
        	  temp = Integer.toString(t);
              out.println("<b><a href='advancedSearch?title="+title+"&genre="+genre+"&sort=" +
        	  		"&sortby=&number="+number+"&numOfTimes="+temp+"&director="+director+"&year=" +
        	  				""+year+"&fname="+fname+"&lname="+lname+"'>Previous</a></b></br>");
          }
           
              
              
        	  
        	  out.println("</TABLE>");
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
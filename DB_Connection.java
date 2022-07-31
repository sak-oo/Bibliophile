package tyitproject.booksonthego;

import java.sql.*;

public class DB_Connection
{
    public static String SENDERS_EMAILID="manasvidesai2411@gmail.com";
    public static String SENDERS_PASSWORD="manasvi1998";
    public static Connection get_DBConnection()
    {
        Connection conn=null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2:3306/book_db", "root", "abc");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return conn;
    }

}





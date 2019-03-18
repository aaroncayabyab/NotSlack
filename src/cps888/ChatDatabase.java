package cps888;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatDatabase {
    private Connection conn;
    
    // local database credentials and setup
    public final String DATABASE_NAME = "chat_db";
    public final String USERNAME = "root";
    public final String PASSWORD = "rootpassword";  
    public final String MYSQL_URL = "jdbc:mysql://localhost:3306/"+DATABASE_NAME;
    
    // user and userchathistory table in database
    public final String USER_TABLE = "user";
    public final String USERCHAT_TABLE = "userchathistory";
    
    // jdbc query objects
    private PreparedStatement pst;
    private ResultSet rs;
    private Statement st;
    
    // establish connection to local mysql database given credentials
    public Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(MYSQL_URL, USERNAME, PASSWORD);
            System.out.println("DB Connection Successful");
        }
        catch (SQLException e) {
            System.out.println(e);
            System.out.println("DB Connection Error");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
    
    // insert user's username into user table when a new user has logged in
    public void insertUser(String u) {
        try {
            pst = conn.prepareCall("INSERT INTO "+USER_TABLE+"(username) VALUES('"+u+"')");
            // execute INSERT statement
            int exec = pst.executeUpdate();
            if(exec > 0) System.out.println("User Insert Successfull");        
        }
        catch (SQLException e) {
            Logger.getLogger(ChatDatabase.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    // check if a user already exists in the user table
    public int checkUser(String u) {
        try {
            pst = conn.prepareStatement("SELECT * FROM "+USER_TABLE+" WHERE username = '"+u+"'");
            // execute SELECT statement 
            rs = pst.executeQuery();       
            // result of execution is ResultSet
            // rs.first() is used to obtain first row of query result
            if (rs.first()) {
                // if rs.first() is true, then user already exists in database
                return 1;
            }
        }
        catch (SQLException e) {
            Logger.getLogger(ChatDatabase.class.getName()).log(Level.SEVERE, null, e);
        }
        return 0;
    }
    
    // retreived id of record in user table given username exists in the table
    public int getUserRow(String u) {
        try {
            pst = conn.prepareStatement("SELECT id FROM "+USER_TABLE+" WHERE username = '"+u+"'");
            //execute SELECT statement
            rs = pst.executeQuery();
            
            if (rs.first()) {
                // if rs.first is true, id of user is returned
                return rs.getInt("id");
            }
        }
        catch (SQLException e) {
            Logger.getLogger(ChatDatabase.class.getName()).log(Level.SEVERE, null, e);
        }
        return 0;
    }
    
    // insert chat history and time of messages between two users
    public void insertChatMessage(int s, int r, String m, String d) {
        try {
            pst = conn.prepareCall("INSERT INTO "+USERCHAT_TABLE+"(useridsender, useridreceiver, message, datetime) VALUES('"+s+"', '"+r+"', '"+m+"', '"+d+"')");
            int kq = pst.executeUpdate();
            // execute INSERT statement
            if(kq > 0) System.out.println("Chat History Insert Successfull");
        }
        catch (SQLException e) {
            Logger.getLogger(ChatDatabase.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    // terminate database connection
    public void closeConnection() {
        try {
            if(rs!=null) rs.close();
            if(pst!=null) pst.close();
            if(st!=null) st.close();
            if(conn!=null) conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(ChatDatabase.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Terminate DB Connection");
        }
    }
}

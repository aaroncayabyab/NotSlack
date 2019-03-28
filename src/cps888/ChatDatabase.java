package cps888;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatDatabase {
    private Connection conn;
    
    // local database credentials and setup
    public final String DATABASE_NAME = "chat_db";
    public final String USERNAME = "root";
    public final String PASSWORD = "rootpassword";  
    public final String MYSQL_URL = "jdbc:mysql://localhost:3306/"+DATABASE_NAME+"?serverTimezone=EST5EDT";
    
    // user and userchathistory table in database
    public final String USER_TABLE = "user";
    public final String GROUP_TABLE = "group";
    public final String USERCHAT_TABLE = "userchathistory";
    public final String USERGROUP_TABLE = "usergroup";
    
    // jdbc query objects
    private PreparedStatement pst;
    private ResultSet rs;
    private Statement st;
    
    ArrayList <Integer> userId;
    
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
    public void insertUser(String u, String s) {
        try {
            pst = conn.prepareCall("INSERT INTO "+USER_TABLE+"(username, status) VALUES('"+u+"', '"+s+"')");
            // execute INSERT statement
            int exec = pst.executeUpdate();
            if(exec > 0) System.out.println("User Insert Successfull");        
        }
        catch (SQLException e) {
            Logger.getLogger(ChatDatabase.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    // update user's stattus in user table when user logs out
    public void updateUser(String u, String s) {
        try {
            pst = conn.prepareCall("UPDATE "+USER_TABLE+" SET status = '"+s+"' WHERE username = '"+u+"'");
            // execute INSERT statement
            int exec = pst.executeUpdate();
            if(exec > 0) System.out.println("User Status Update Successfull");        
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
    
    // retrieve id of record in user table given username exists in the table
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
            int exec = pst.executeUpdate();
            // execute INSERT statement
            if(exec > 0) System.out.println("Chat History Insert Successfull");
        }
        catch (SQLException e) {
            Logger.getLogger(ChatDatabase.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    // insert chat room name 
    public void insertRoom(String n) {
        try {
            pst = conn.prepareCall("INSERT INTO chat_db."+GROUP_TABLE+" (name) VALUES ('"+n+"')");
            int exec = pst.executeUpdate();
            // execute INSERT statement
            if(exec > 0) System.out.println("Chat Room Insert Successfull");
        }
        catch (SQLException e) {
            Logger.getLogger(ChatDatabase.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    // retreived id of record in user table given username exists in the table
    public int getRoomRow(String n) {
        try {
            pst = conn.prepareStatement("SELECT id FROM chat_db."+GROUP_TABLE+" WHERE name = '"+n+"'");
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
    
    // check if a chat room already exists in the group table
    public int checkRoom(String n) {
        try {
            pst = conn.prepareStatement("SELECT * FROM chat_db."+GROUP_TABLE+" WHERE name = '"+n+"'");
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
    
    // store user ids for users in a chat room 
    public void insertUserRoom(int u, int g) {
        try {
            pst = conn.prepareCall("INSERT INTO "+USERGROUP_TABLE+"(userid, groupid) VALUES('"+u+"', '"+g+"')");
            int exec = pst.executeUpdate();
            // execute INSERT statement
            if(exec > 0) System.out.println("Users in a Chat Room Insert Successfull");
        }
        catch (SQLException e) {
            Logger.getLogger(ChatDatabase.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    // retrieve all online users in a given chat room
    public Integer getUsersInRoomCount(int g, String s) {
        try {
            pst = conn.prepareCall("SELECT count(distinct(userid)) as count FROM usergroup INNER JOIN user ON usergroup.userid = user.id "
                    + "INNER JOIN chat_db.group ON usergroup.groupid = chat_db.group.id "
                    + "WHERE chat_db.group.id = '"+g+"' and user.status = '"+s+"'");
            rs = pst.executeQuery();
            
            if (rs.first()) {
                return rs.getInt("count");
            }
        }
        catch (SQLException e) {
            Logger.getLogger(ChatDatabase.class.getName()).log(Level.SEVERE, null, e);
        }
        return 0;
    }
    
    // retrieve all online users in a given chat room
    public int[] getUsersInRoom(int g, String s) {
        int i = getUsersInRoomCount(g,s);
        int[] users = new int[i];
        try {
            pst = conn.prepareCall("SELECT distinct(userid) FROM usergroup INNER JOIN user ON usergroup.userid = user.id "
                    + "INNER JOIN chat_db.group ON usergroup.groupid = chat_db.group.id "
                    + "WHERE chat_db.group.id = '"+g+"' and user.status = '"+s+"'");
            rs = pst.executeQuery();  
            for (int j=0; j<users.length; j++) {
                rs.next();
                users[j] = rs.getInt("userid");
            }
        }
        catch (SQLException e) {
            Logger.getLogger(ChatDatabase.class.getName()).log(Level.SEVERE, null, e);
        }
        return users;
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

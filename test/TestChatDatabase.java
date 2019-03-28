/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import cps888.ChatDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author jagmeetcheema
 */
public class TestChatDatabase {
    
    private ChatDatabase cdb;
    private Connection connect;
    private PreparedStatement pst;
    
    @BeforeMethod
    public void beforeMethod() throws SQLException{
        cdb = new ChatDatabase();
        connect = cdb.connect();
        
        pst = connect.prepareCall("DELETE FROM userchathistory");           
        pst.executeUpdate();   
        pst = connect.prepareCall("DELETE FROM user");           
        pst.executeUpdate(); 
    }
    
    @Test
    public void testOpenConnection() throws SQLException{
        Assert.assertNotNull(connect, "Connect should not be null");
        Assert.assertFalse(connect.isClosed(), "Connect should be open now");
    }
    
    @Test
    public void testCloseConnection() throws SQLException{
        cdb.closeConnection();
        Assert.assertTrue(connect.isClosed(), "Connect should be closed now");
    }
    
    @Test
    public void testInsertAndCheckUser()
    {
        final String userName = "Jagmeet";
              
        Assert.assertEquals(cdb.checkUser(userName), 0, "This user should not be in DB!");
        cdb.insertUser(userName, "online");
        Assert.assertEquals(cdb.checkUser(userName), 1, "This user should be in DB!");
    }
    
    @Test
    public void testGetUserRow()
    {   
        final String userName = "Jagmeet";
        cdb.insertUser(userName, "online");
        Assert.assertTrue(cdb.getUserRow(userName) > 0, "This user should be in DB!");
    }
    
    @Test
    public void testUpdateUser(){   
        final String userName = "Jagmeet";
        cdb.insertUser(userName, "online");
        cdb.updateUser(userName, "offline");
        Assert.assertEquals(cdb.checkUser(userName), 1, "This user should be in DB!");
    }
    
    @Test
    public void testCheckroom()
    {
        final String roomName = "CPS Room";
        Assert.assertEquals(cdb.checkRoom(roomName), 0, "There should be no chatrooms!");
    }
   
}

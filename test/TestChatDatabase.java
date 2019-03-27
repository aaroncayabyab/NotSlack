/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import cps888.ChatDatabase;
import java.sql.Connection;
import org.testng.Assert;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author jagmeetcheema
 */
public class TestChatDatabase {
    private ChatDatabase cdb;
    private Connection connect;
    @BeforeMethod
    public void beforeMethod(){
        cdb = new ChatDatabase();
         connect = cdb.connect();
    }
    
    @Test
    public void testConnection(){
        Assert.assertNotNull(connect, "Connect should not be null");
    }
    
    @Test
    public void testInsertAndCheckUser(){
        
        final String userName = "Jagmeet";
        
        Assert.assertEquals(cdb.checkUser(userName), 0, "This user should not be in DB!");
        //cdb.insertUser(userName);
        Assert.assertEquals(cdb.checkUser(userName), 1, "This user should be in DB!");

    }
    @Test
    public void testGetUserRow(){
         final String userName = "Jagmeet";
         Assert.assertEquals(cdb.getUserRow(userName), 0, "This user should not be in DB!");
        cdb.insertUser(userName);
        Assert.assertEquals(((Integer)cdb.getUserRow(userName)) instanceof Integer, "This user should be in DB!");
    }
   
}

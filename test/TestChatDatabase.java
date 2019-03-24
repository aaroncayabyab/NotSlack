/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import cps888.ChatDatabase;
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
    
    @Test
    public void testConnection(){
        Assert.assertNotNull(new ChatDatabase().connect(), "Connect should not be null");
    }
    
    @Test
    public void testInsertAndCheckUser(){
        ChatDatabase cdb = new ChatDatabase();
        final String userName = "Jagmeet";
        
        Assert.assertEquals(cdb.checkUser(userName), 0, "This user should not be in DB!");
        cdb.insertUser(userName);
        Assert.assertEquals(cdb.checkUser(userName), 1, "This user should be in DB!");

    }
}

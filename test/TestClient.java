/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import cps888.Client;
import junit.framework.Assert;
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
public class TestClient {
    
    private Client client;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // what executes before all your tests are done
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // what executes after all your tests are done
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
        client = new Client("Jagmeet", "server", 5000);
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }
    
    @Test
    public void testGetUserName()
    {
        Assert.assertEquals("this failed!", "Jagmeet", client.getUsername());
    }
    
    @Test
    public void testStart()
    {
        // what do you need to thest this?
        // if too much things are being tested, separate into different methods
        // dont duplicate your testing
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import cps888.Client;
import org.testng.Assert;
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

    @BeforeMethod
    public void setUpMethod() {
        client = new Client("Jagmeet", "server", 5000);
    }
    
    @Test
    public void testGetUserName()
    {
        Assert.assertEquals(client.getUsername(), "Jagmeet", "Clients username should be 'Jagmeet'");
    }

    @Test
    public void testReceivedMessage()
    {
        //Check if client got the correct message sent from another user
    }
    
    @Test 
    public void testUserConnection()
    {
        //Check if two users are connected to each other by checking if the other user got the 
        //correct message that was sent
    }
    
    @Test
    public void testCreateGroup()
    {
        //Verify that the user can create a group chat 
    }
    @Test
    public void testLeaveGroup()
    {
        //Check if the user successfully leaves the chat and doesn't receive any messages.
    }
    
    @Test
    public void testValidUserAdded()
    {
        //Check that only the users listed online or in the application are added into the group chat
    }
    
    
}

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
    public void testGetMessage(){
        client.start();
        Assert.assertTrue(client.getMessage()!=null, "Message should have a value");
    }
    
    @Test 
    public void testGetUserList(){
        Assert.assertTrue(client.getUserList()!=null, "User list should be initialized");
       
       client.send("getActiveUsers");
       client.start();
       Assert.assertTrue(client.getUserList().size()==1, "User created, there should be user");
    }
    
    @Test
    public void testGetRoomList(){
        
        Assert.assertTrue(client.getRoomList().isEmpty(), "No chat rooms have been created yet");
        client.send("getChatRooms");
        client.start();
        Assert.assertTrue(client.getRoomList().size()==1, "There should be only one chat room");
    }
  
    

}

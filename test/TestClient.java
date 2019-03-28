/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import cps888.Client;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author jagmeetcheema
 */
public class TestClient {
    
    private Client client;
    private Client client2;

    @BeforeMethod
    public void setUpMethod() {
        client = new Client("Jagmeet", "localhost", 5000);
        client.start();
        
        client2 = new Client("TestUser", "localhost", 5000);
        client2.start();
    }
    
    @Test
    public void testGetUserName()
    {
        Assert.assertEquals(client.getUsername(), "Jagmeet", "Clients username should be 'Jagmeet'");
    }
    @Test
    public void testGetMessage() throws InterruptedException{
        client2.send("@TestUser hello");
        Thread.sleep(2000);
        Assert.assertTrue(client2.getMessage().contains("hello"), "Message should have the message");
    }
    
    @Test
    public void testGetMessageList() throws InterruptedException
    {
        Assert.assertNull(client.getMessageList("chatroom"), "There should be no messages yet!");
    }
    
    @Test 
    public void testGetUserList() throws InterruptedException{
        client.send("getActiveUsers");
        Thread.sleep(500);
        Assert.assertTrue(client.getUserList().size() > 0, "Message should have a value");
    }
    
    @Test
    public void testGetRoomList() throws InterruptedException{
        Assert.assertTrue(client.getRoomList().isEmpty(), "No chat rooms have been created yet");
        client.getRoomList().add("CPSroom");
        Thread.sleep(1000);
        Assert.assertTrue(client.getRoomList().size()==1, "There should be only one chat room");
    }

    @Test 
    public void testGetChatRooms() throws InterruptedException{
        Assert.assertTrue(client.getRoomList().isEmpty(), "Size of room should be size 0");
        client.send("getChatRooms");
        Thread.sleep(1000);
        Assert.assertTrue(client.getRoomList().size() > 0, "Size of room should be greater than 0");
    }
}

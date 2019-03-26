/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import cps888.Server;
import java.util.Scanner;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author jagmeetcheema
 */
public class TestServer {
    private Server server;
    
    @BeforeMethod
    public void beforeMethod(){
         server = new Server(5000);
    
}
    /**
     * No assertions because testing will be conducted through interaction with console.
     */
    @Test
    public void testStart(){
        
        server.start();
        
        Scanner scan = new Scanner(System.in);
        System.out.println("Did this test pass? enter 'Y' or'N'.");
        Assert.assertTrue(scan.nextLine().toLowerCase().equals("Y"), "Server started correctly!");
    }
    
    @Test
    public void testChatRoom(){
        Assert.assertTrue(server.getChatRooms().isEmpty(), "Chat rooms should be initalized to 0");
        String chatroom = "cps chat";
        server.addChatRoom(chatroom);
        Assert.assertTrue(server.getChatRooms().size()==1,"There should only be one chat room");
        Assert.assertEquals(server.getChatRooms().get(0), chatroom, "Has one chat room but is the wrong one");
    }
    
   
    
    @Test 
    public void testGetWorkers(){
        Assert.assertTrue(server.getWorkers().isEmpty(), "Connection has not started, there should be no workers");
        server.start();
        Assert.assertTrue(server.getWorkers().size()>=1, "Started connection but got no workers");
        
    }
}

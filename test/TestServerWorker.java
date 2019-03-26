/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import cps888.Server;
import cps888.ServerWorker;
import java.util.List;
import java.util.Scanner;
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
public class TestServerWorker {
     private Server server;
    @BeforeMethod
    public void beforeMethod() {
        server = new Server(5000);
        
    }
@Test 
public void testSendMessage(){
    server.start();
     List<ServerWorker> workers =  server.getWorkers();
     workers.get(0).send("Hello");
      Scanner scan = new Scanner(System.in);
        System.out.println("Did this test pass? enter 'Y' or'N'.");
        Assert.assertTrue(scan.nextLine().toLowerCase().equals("Y"), "Message was not successful");
    
}
 @Test
 public void testConnect(){
     server.start();
     List<ServerWorker> workers =  server.getWorkers();
     
     Assert.assertTrue(workers.get(0).isConnected(), "Client socket wasn't connected");

 }
 
    
}

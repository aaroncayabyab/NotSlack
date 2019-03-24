/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import cps888.Server;
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
public class TestServer {
    
    /**
     * No assertions because testing will be conducted through interaction with console.
     */
    @Test
    public void testStart(){
        Server server = new Server(5000);
        server.start();
        
        Scanner scan = new Scanner(System.in);
        System.out.println("Did this test pass? enter 'Y' or'N'.");
        Assert.assertTrue(scan.nextLine().toLowerCase().equals("Y"), "Server started correctly!");
    }
    
}

package cps888;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import javafx.collections.*;

public class Client {
    private final String username;
    private final String server;
    private final int port;
    private Socket socket;
    
    private PrintWriter output;
    private BufferedReader input;
    
    //Frontend integration
    private static String sentMsg;
    private String bcMsg;
    private static ObservableList<String> users;
    private static ObservableList<String> rooms;
    private static Map<String, ObservableList<String>> messages;
    
    public Client(String username, String server, int port) {
        this.username = username;
        this.server = server;
        this.port = port;
        users = FXCollections.observableArrayList();
        rooms = FXCollections.observableArrayList();
        messages = new HashMap<>();
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getMessage() {
        return bcMsg;
    }
    //----------------------------------------------------------------------------------------------------
    //User List methods
    public ObservableList<String> getUserList() {
        return users;
    }
    
    //Room List methods  
    public ObservableList<String> getRoomList() {
        return rooms;
    }
    
    //Messages methods
    public ObservableList<String> getMessageList(String chatName) {
        return messages.get(chatName);
    }
    
    
    //--------------------------------------------------------------------------------------------------------------------------------
    
    //intializing sockets and streams
    public boolean start() {
        try {
            socket = new Socket(server, port);
            System.out.println("Connection complete");
            
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            
            //thread to listen from incoming responses from server
            new RetrieveFromServer().start();
            output.println(username);
            return true;
            
        } catch(IOException e) {
            System.out.println("Error:" + e);
            disconnect();
            return false;
        }
        
    }
    
    //closes input, output streams and sockets
    public void disconnect() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch(Exception e) {
            System.out.println("Error closing streams: " + e);
        }
    }
    
    // send message to server to relay to intended users
    public void send(String message) {
        try {
            sentMsg = message;
            output.println(message);
        } catch(Exception e) {
            System.out.println("Failed to send message: " + e);
        }
    }
    
    public static void main(String[] args) {
        try {
            Scanner scan = new Scanner(System.in);
            String username;
            
            System.out.println("Enter your guest username: ");
            username = scan.nextLine();
            
            Client client = new Client(username, "localhost", 5000);
            
//            ChatDatabase cb = new ChatDatabase();
//            // establish database connection
//            cb.connect();
//            int userCount = cb.checkUser(username);
//            
//            // if user does not exist in database, insert user information into database
//            if (userCount == 0) {
//                cb.insertUser(username);
//            }
            
            if(!client.start())
                return;
            
            System.out.println("\nYou have entered the chatroom");
            System.out.println("Options:");
            System.out.println("1. Type in a message to broadcast to all online users");
            System.out.println("2. Type '@username message' to direct message a particular user");
            System.out.println("3. Type 'getActiveUsers' to see all online users");
            System.out.println("4. Type 'join #groupName in order to join a group");
            System.out.println("5. Type #groupName msg in order to message a group");
            System.out.println("6. Type 'getChatRooms' to see all rooms created");
            System.out.println("7. Type 'logout' in order to leave chatroom");
            
            //continually reads in input entered by the client
            while(true) {
                //change to system.out.println?
                System.out.print("->"); 
                String msg = scan.nextLine();
                sentMsg = msg;
                
                if(msg.equalsIgnoreCase("logout")) {
                    client.send("logout");
                    break;
                    
                } else {
                    client.send(msg);
                }
            }
            scan.close();
            client.disconnect();
            //cb.closeConnection();
        } catch(Exception e) {
            System.out.println(e);
        }
        
    }
    // reads in messages that are forwarded from the server
    class RetrieveFromServer extends Thread {

        @Override
        public void run() {
            try {
                String serverMsg;
                while ((serverMsg = input.readLine()) != null) {
                    bcMsg = serverMsg;
                      if(sentMsg!=null) {
                        if(sentMsg.equals("logout")) {
                            users.remove(username);
                        }
                        else if(sentMsg.equals("getActiveUsers")) {
                            if(users.contains(bcMsg) || username.equals(bcMsg)) {
                                continue;
                            }
                            users.add(bcMsg);
                        }
                        else if(sentMsg.equals("getChatRooms")) {
                            if(rooms.contains(bcMsg)){
                                continue;
                            }
                            rooms.add(bcMsg);
                        }
                        else if(sentMsg.indexOf("@") == 0) {
                            //directmessage
                            String chatID = sentMsg.split(" ")[0].substring(1);
                            messages.putIfAbsent(chatID, FXCollections.observableArrayList());
                            messages.get(chatID).add(bcMsg);
                        }
                        else if(sentMsg.indexOf("#") == 0) {
                            //group message
                            String chatID = sentMsg.split(" ")[0].substring(1);
                            messages.putIfAbsent(chatID, FXCollections.observableArrayList());
                            messages.get(chatID).add(bcMsg);
                                                       
                        }
                      }
                 
                    System.out.println(bcMsg);
                }
            } catch (IOException e) {
                System.out.println("Server connection closed:" + e);
            } 
        }
    }  
}

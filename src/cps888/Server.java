package cps888;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private final ArrayList<ServerWorker> workers;
    private final int port;
    private ServerSocket serverSocket;
    
    //------------------------------------------------------------------------
    private static ArrayList<String> users;
    private static ArrayList<String> rooms;
    private static HashMap<String, ArrayList<String>> messages;
    //------------------------------------------------------------------------
    
    //Singleton
    private static Server instance = null;
    public static Server getInstance() {
        if(instance == null) {
            instance = new Server(5000);
        }
        
        return instance;
    }
    
    private Server(int port) {
        instance = this;
        this.port = port;
        workers = new ArrayList<>();
        users = new ArrayList<>();
        
    }
    //----------------------------------------------------------------------------------------------------
    //User List methods
    public ArrayList<String> getUserList() {
        return users;
    }
    
    public void addUserToList(String user) {
        users.add(user);
        System.out.println(user +" added to userlist.");
    }
    
    public void removeUserFromList(String user) {
        users.remove(user);
        System.out.println(user +" removed from userlist.");
    }
    
    //Room List methods  
    public ArrayList<String> getRoomList() {
        return Server.rooms;
    }
    
    public void addRoomToList(String room) {
        rooms.add(room);
        System.out.println(room +" added to roomlist.");
    }
    
    public void removeRoomFromList(String room) {
        rooms.remove(room);
        System.out.println(room +" removed from roomlist.");
    }
    
    //Messages methods
    public ArrayList<String> getMessageList(String chatName) {
        return messages.get(chatName);
    }
    
    public void addMessageToList(String chatName, String msg) {
        if(messages.get(chatName) == null) {
            messages.put(chatName, new ArrayList<>());
        }
        messages.get(chatName).add(msg);
    }
    //--------------------------------------------------------------------------------------------------------------------------------
    
    public ArrayList<ServerWorker> getWorkers() {
        return this.workers;
    }
    
    //starts server and creates workers for each client connection
    public void start() {
        try {
            this.serverSocket = new ServerSocket(this.port);
            while(true) {
                System.out.println("Server waiting for client connections:");              
                Socket socket = this.serverSocket.accept();           
                ServerWorker worker = new ServerWorker(this, socket);
                this.workers.add(worker);
                worker.start();
                System.out.println(getUserList());
            }     
        } catch(IOException e) {
            System.out.println("Error starting server: " + e);
        }
    }

    //removes worker from list
    public void removeWorker(ServerWorker worker) {
        this.workers.remove(worker);
    }
    
    public static void main(String [] args) {
        Server server = new Server(5000);
        server.start();
        System.out.println("Server started");
        
    }
}

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

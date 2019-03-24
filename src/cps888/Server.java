package cps888;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Ragulan
 */

public class Server {
    private final ArrayList<ServerWorker> workers;
    private final ArrayList<String> chatRooms;
    private final int port;
    private ServerSocket serverSocket;
    
    
    public Server(int port) {
        this.port = port;
        workers = new ArrayList<>();
        chatRooms = new ArrayList<>();
    }
    
    public ArrayList<String> getChatRooms() {
        return this.chatRooms;
    }
    
    public void addChatRoom(String room) {
        this.chatRooms.add(room);
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

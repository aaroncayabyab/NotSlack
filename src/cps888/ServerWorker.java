package cps888;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class ServerWorker extends Thread {
    private Server server;
    private Socket clientSocket;
    private String username;
    private HashSet<String> groups = new HashSet<>();

    private PrintWriter output;
    private BufferedReader input;
    private final SimpleDateFormat dateFormatter;
    
    private ChatDatabase cd = new ChatDatabase();
    
    
    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        // date format follows pattern of yyyy-MM-dd HH:mm:ss
        this.dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            this.output = new PrintWriter(this.clientSocket.getOutputStream(), true);
            this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.username = input.readLine();
            broadcast("", "online");
            System.out.println(this.username + " is now online");            
        } catch(IOException e) {
            System.out.println("Error setting up streams: " + e);
        }
    }
    
    public Socket getSocket(){
        return this.clientSocket;
    }
    
    public PrintWriter getOutputStream() {
        return this.output;
    }
    
    public BufferedReader getInputStream() {
        return this.input;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public HashSet<String> getGroups() {
        return groups;
    }
    
    public boolean isConnected() {
        if(!this.clientSocket.isConnected()) {
            close();
            return false;
        }
        return true;
    }
    public void send(String message) {
        try {
            this.output.println(message);
        } catch(Exception e) {
            System.out.println("Error sending message: " + e);
        }
            
    }
    
    private void close() {
        try {
            this.clientSocket.close();
            this.output.close();
            this.input.close();
        } catch(IOException e) {
            System.out.println("Problem closing streams and socket: " + e);
        }
    }
    
    private boolean isMemberOfGroup(String group) {
        return this.groups.contains(group);
    }
    
    //sending either direct message or message to all, or a group
    private synchronized void broadcast(String from, String message) {
        String datetime = this.dateFormatter.format(new Date());
        String tokens [] = message.split(" ", 2);
        boolean isDirectMessage = false;
        boolean isGroup = false;
        String sendTo = "";
        String msg = datetime  + " " + from + ": "  + message;
        String group = "";
        
        //is a direct message if the text contains @username
        if(tokens[0].charAt(0) == '@') {
            isDirectMessage = true;
            sendTo = tokens[0].substring(1);
            msg = from + " " + datetime + " " + from + ": " + tokens[1]; 
        }
        
        //messaging a group case
        if(tokens[0].charAt(0) == '#') {
            isGroup = true;
            group = tokens[0].substring(1);
            msg = group + " " + datetime + " " + from + ": " + tokens[1];
        }
        if(from.equals("")) msg = message;
        
        //iterates through all workers and finds worker whose username matches
        for(ServerWorker worker: this.server.getWorkers()) {
            //checks if worker is still connected otherwise removes
            if(!worker.isConnected()) {
                this.server.getWorkers().remove(worker);
                System.out.println(worker.getUsername() + " has disconnected");
            } else if(isDirectMessage && (worker.getUsername().equals(sendTo) || worker.getUsername().equals(from))) { // direct message case
                worker.send(msg);
            } else if(!isGroup || (isGroup && worker.isMemberOfGroup(group))) { // group or all case
                worker.send(msg);
            }
        }
    }
    
    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch(IOException e) {
            System.out.println(e);
        }
    }
    
    private void handleClientSocket() throws IOException {
        cd.connect();
        int userId;
        int roomId;
        boolean connected = true;
        while(connected) {
            String message = this.input.readLine();
            String tokens [] = message.split(" ", 2);
            
            if(tokens[0].equalsIgnoreCase("logout")) {
                broadcast("", "offline " + this.username);
                System.out.println(this.username + " has left the chat room and is offline");
                
                connected = false;
            } else if(tokens[0].equalsIgnoreCase("getActiveUsers")) {
                String res = "";
                for(ServerWorker worker: this.server.getWorkers()) {
                    res += worker.getUsername() + ":";
                }
                res = res.substring(0, res.length() -1);
                send(res);
                
            } else if(tokens[0].equalsIgnoreCase("getChatRooms")) {
                this.server.getChatRooms().stream().forEach((room) -> {
                    send(room);
                });
            } else if(tokens[0].equals("join")) { //joining a group case
                this.groups.add(tokens[1].substring(1));
                userId = cd.getUserRow(this.username);
                roomId = cd.getRoomRow(tokens[1].substring(1));
                cd.insertUserRoom(userId, roomId);
            } else if(tokens[0].charAt(0) == '#') { //messaging a group case
                broadcast(this.username, message);
            } else if(tokens[0].charAt(0) == '@'){ //direct messaging case
                boolean found = false;
                String user = tokens[0].substring(1);
                for(ServerWorker worker: this.server.getWorkers()) {
                    if(user.equals(worker.getUsername()))
                        found = true;
                }
                if(!found) {
                    send("The user was not found.");
                }
                broadcast(this.username, message);
            } else {
                broadcast(this.username, message);
            }
        }
        this.server.removeWorker(this);
        cd.closeConnection();
        close();
    }   
}

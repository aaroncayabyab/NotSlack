/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cps888;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

/**
 *
 * @author Ragulan
 */

public class ServerWorker extends Thread {
    private Server server;
    private Socket clientSocket;
    private String username;
    private HashSet<String> groups = new HashSet<>();
  
    private PrintWriter output;
    private BufferedReader input;
    private final SimpleDateFormat dateFormatter;
    
    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.dateFormatter = new SimpleDateFormat("HH:mm:ss");

        try {
            this.output = new PrintWriter(this.clientSocket.getOutputStream(), true);
            this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.username = input.readLine();
            broadcast(this.username, "online");
            
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
        String time = this.dateFormatter.format(new Date());
        String tokens [] = message.split(" ", 2);
        boolean isDirectMessage = false;
        boolean isGroup = false;
        String sendTo = "";
        String msg = time  + " " + from + ": "  + message;
        String group = "";
        
        //is a direct message if the text contains @username
        if(tokens[0].charAt(0) == '@') {
            isDirectMessage = true;
            sendTo = tokens[0].substring(1);
            msg = time + " " + from + ": " + tokens[1];
            
        }
        //messaging a group case
        if(tokens[0].charAt(0) == '#') {
            isGroup = true;
            group = tokens[0].substring(1);
            msg = group + " " + time + " " + from + ": " + tokens[1];
        }
        
        //iterates through all workers and finds worker whose username matches
        for(ServerWorker worker: this.server.getWorkers()) {
            //checks if worker is still connected otherwise removes
            if(!worker.isConnected()) {
                this.server.getWorkers().remove(worker);
                System.out.println(worker.getUsername() + " has disconnected");
            } else if(isDirectMessage && worker.getUsername().equals(sendTo)) { // direct message case
                 worker.send(msg);
                break;
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
        boolean connected = true;
        while(connected) {
            String message = this.input.readLine();
            String tokens [] = message.split(" ", 2);
            
            if(tokens[0].equalsIgnoreCase("logout")) {
                System.out.println(this.username + " has left the chat room");
                connected = false;
            } else if(tokens[0].equalsIgnoreCase("getActiveUsers")) {
                this.server.getWorkers().stream().forEach((worker) -> {
                    send(worker.getUsername());
                });
            } else if(tokens[0].equals("join")) { //joining a group case
                this.groups.add(tokens[1].substring(1));
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
        close();
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import cps888.Client;
import chat.Chat;
import java.io.IOException;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author aacay
 */
public class MenuController implements Initializable {
    //General variables
    private Client client;
    ArrayList<String> users;

    @FXML
    private Text errMsg; 
    
    @FXML
    private Text errMsgRoom;
    //Login Pane
    @FXML
    private TextField userField;  
    @FXML
    private Pane loginPane;  
    
    //Menu Pane
    @FXML
    private Pane menuPane;  
    @FXML
    private Text welcomeText;
    @FXML
    private ListView<String> userList;
    @FXML
    private ListView<String> roomList;
    //Create Room Pane
    @FXML
    private Pane createRoomPane; 
    @FXML
    private TextField roomField;
    
    //Methods
    @FXML
    public void onConnect(ActionEvent event) throws InterruptedException {
        String user = userField.getText();    
        if(user.equals("") || user == null) {
            errMsg.setText("Please enter valid username.");
            errMsg.setVisible(true);            
        }
        else {
            client = new Client(user, "localhost", 5000);
            if(!client.start()) {          
                errMsg.setText("Unable to connect to server");
                errMsg.setVisible(true);
            }
            else {
                //Switch to menu displaying rooms and whatnot
                errMsg.setVisible(false);                
                loginPane.setVisible(false);    
                menuPane.setVisible(true);
                welcomeText.setText("Hi, " + user +"!");
                
                client.send("getActiveUsers");
                Thread.sleep(500);
                userList.setItems(client.getUserList()); 
                
                //TODO: also load getchatrooms
                
            }
        }

    }
    
    @FXML
    public void onRefresh(Event event) throws InterruptedException {                       
        client.send("getActiveUsers");            
    }

    @FXML
    public void onSelectCreate(Event event) {
        menuPane.setVisible(false);
        createRoomPane.setVisible(true);
    }
    
    @FXML
    public void onCreateRoom(Event event) {
        //Add room to list
        String roomName = roomField.getText();
        
        if(client.getRoomList().contains(roomName)) {
            errMsgRoom.setVisible(true);
            return;
        }
        
        errMsgRoom.setVisible(false);
        client.getRoomList().add(roomName);
        
        ObservableList rooms = client.getRoomList();
        roomList.setItems(rooms);
        
        createRoomPane.setVisible(false);
        menuPane.setVisible(true);
        
        System.out.println(roomName);
    }  
    
    @FXML
    public void onJoin(Event event) throws InterruptedException {
        String room = roomList.getSelectionModel().getSelectedItem();
        
        if(room == null || room.equals(""))
            return;
        
        client.send("join #" +room);
        try {
            Chat chat = new Chat(room, client, true);
            Stage stage = new Stage();
            chat.start(stage);
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void onMessageUser(Event event) throws InterruptedException {
         String dm = userList.getSelectionModel().getSelectedItem();
                         
         if(dm == null || dm.equals(""))
            return;
         try {
            Chat chat = new Chat(dm, client, false);
            Stage stage = new Stage();
            chat.start(stage);
        } catch (IOException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }       
    }
    
     public Client getClient() {
        return this.client;
    }   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }  
    

    
}

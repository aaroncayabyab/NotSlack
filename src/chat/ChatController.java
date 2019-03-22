/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;
import menu.MenuController;
import cps888.Client;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author aacay
 */
public class ChatController implements Initializable {
    private Client client;
    private boolean isChatRoom;
    private String name;
    
    @FXML
    Text roomName;
    @FXML
    ListView<String> activeList;
    @FXML
    ListView<String> messageList;
    @FXML
    TextField messageField;
    
    @FXML
    public void onSendMessage(Event event) throws InterruptedException {
        //TODO display messages in listview messageList
        String msg = "";
        if(!isChatRoom) {
            msg = "@"+name+" "+messageField.getText();
        }
        else {
           msg = "#"+name+" "+messageField.getText(); 
        }
        
        client.send(msg);
        //get value from message field
        Thread.sleep(100);
        messageList.setItems(client.getMessageList(name));
        
        messageField.clear();
        //create and add value to arraylist, value = name, date, message
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    String msg = "";
                    if(!isChatRoom) {
                        msg = "@"+name+" has joined the chat.";
                    }
                    else {
                        msg = "#"+name+" has joined the chat.";
                    }
                    client.send(msg);

                    Thread.sleep(100);
                    messageList.setItems(client.getMessageList(name));
                } catch (InterruptedException ex) {
                    Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
    } 
    
    public void setClient(Client client) {
        this.client = client;
        
        //get active users
        activeList.setItems(client.getUserList());
        
       
    }
    
    public void setRoomName(String name) {
        this.name = name;
        roomName.setText(name);
    }
    
    public void setChatConfig(boolean isChatRoom) {
        this.isChatRoom = isChatRoom;
    }
    
    
}

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
    private MenuController mc;
    private String name;
    private
    
    @FXML
    Text roomName;
    @FXML
    ListView<String> activeList;
    @FXML
    ListView<String> messageList;
    @FXML
    TextField messageField;
    
    @FXML
    public void onSendMessage(Event event) {
        String msg = messageField.getText();
        client.send(name+" "+msg);
        //get value from message field
        //create and add value to arraylist, value = name, date, message
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    public void setMenuController(MenuController mc) {
        this.mc = mc;
        client = mc.getClient();
        
    }
    
    public void setRoomName(String name) {
        this.name = name;
        roomName.setText(name);
    }
    
    
}

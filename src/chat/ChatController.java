/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author aacay
 */
public class ChatController implements Initializable {
    
    @FXML
    ListView<String> activeList;
    @FXML
    ListView<String> messageList;
    @FXML
    TextField messageField;
    
    @FXML
    public void onSendMessage(Event event) {
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
    
}

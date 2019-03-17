/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import cps888.Client;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author aacay
 */
public class MenuController implements Initializable {
    
    //Client variable
    Client client;
    
    @FXML
    private TextField userField;
    
    @FXML
    private Button connectBtn;
    
    @FXML
    private Text errMsg;
    
    @FXML
    private Pane loginPane;
    
    @FXML
    public void onConnect(ActionEvent event) {
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
                loginPane.setDisable(true);
                loginPane.setVisible(false);                
            }
        }

    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    public Client getClient() {
        return client;
    }
    
}

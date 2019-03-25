/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;
import cps888.Client;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    ListView<String> roomList;
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
        
        messageField.clear();
        //create and add value to arraylist, value = name, date, message
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        activeList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if(click.getClickCount() == 2) {
                    try {
                        String selected = activeList.getSelectionModel().getSelectedItem();
                        if(selected.equals("") || selected == null || selected.equals(name))
                            return;
                        
                        Chat chat = new Chat(selected, client, false);
                        Stage stage = new Stage();
                        chat.start(stage);
                    } catch (IOException ex) {
                        Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        roomList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if(click.getClickCount() == 2) {
                    try {
                        String selected = roomList.getSelectionModel().getSelectedItem();
                                                
                        if(selected.equals("") || selected == null || selected.equals(name))
                            return;
                        Chat chat = new Chat(selected, client, true);
                        Stage stage = new Stage();
                        chat.start(stage);
                    } catch (IOException ex) {
                        Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
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
        activeList.setCellFactory(cell -> new ActiveCell());
        
        //get rooms
        roomList.setItems(client.getRoomList());
        
       
    }
    
    public void setRoomName(String name) {
        this.name = name;
        roomName.setText(name);
    }
    
    public void setChatConfig(boolean isChatRoom) {
        this.isChatRoom = isChatRoom;
    }
   
    private class ActiveCell extends ListCell<String> {
        ImageView imageView = new ImageView();
        Image image = new Image("resources/online.png", 16, 16, false, false);
        
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                imageView.setImage(null);

                setGraphic(null);
                setText(null);
            } else {
                imageView.setImage(image);
                setText(item);
                setGraphic(imageView);
            }
        }
    }
}

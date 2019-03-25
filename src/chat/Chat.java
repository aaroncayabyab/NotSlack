/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import cps888.Client;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author aacay
 */
public class Chat extends Application {
    String chatName;
    Client client;
    boolean isChatRoom;
    ChatController controller;
    
    public Chat(String chatName, Client client, boolean isChatRoom) {
        this.chatName = chatName;
        this.client = client;
        this.isChatRoom = isChatRoom;
    }
    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChatView.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        controller = fxmlLoader.<ChatController>getController();
        controller.setClient(client);
        controller.setRoomName(chatName);
        controller.setChatConfig(isChatRoom);
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("chat/Chat.css");
        stage.setTitle("NotSlack");
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

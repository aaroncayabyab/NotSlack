/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import menu.MenuController;
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
    MenuController clientMC;
    boolean isChatRoom;
    
    public Chat(String chatName, MenuController mc, boolean isChatRoom) {
        this.chatName = chatName;
        this.clientMC = mc;
        this.isChatRoom = isChatRoom;
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ChatView.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        ChatController controller = fxmlLoader.<ChatController>getController();
        controller.setMenuController(clientMC);
        controller.setRoomName(chatName);
        controller.setChatConfig(isChatRoom);
        
        Scene scene = new Scene(root);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;
import cps888.ChatDatabase;
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
public class Menu extends Application {
    private MenuController mc;
    private ChatDatabase cd = new ChatDatabase();
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MenuView.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        mc = fxmlLoader.<MenuController>getController();
        
        Scene scene = new Scene(root);
        
        stage.setTitle("NotSlack");
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void stop() throws Exception {
        if(mc.getClient() != null) {
            mc.getClient().send("logout");
            String u = mc.getClient().getUsername();
            cd.connect();
            // set user status to offline in db
            cd.updateUser(u, "offline");
            cd.closeConnection();
            mc.getClient().disconnect();
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

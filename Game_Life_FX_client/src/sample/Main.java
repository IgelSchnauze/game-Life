package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Game Life");
        primaryStage.setScene(new Scene(root, 510, 530));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> Controller.client.close_primary_stage());
    }

    public static void main(String[] args) { launch(args); }
}

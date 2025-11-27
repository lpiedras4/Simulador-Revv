package com.tuempresa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/IUGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Simulador-Revv");
        stage.setScene(scene);
        stage.show();
    }
}

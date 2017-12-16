package ru.soloyes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainClient extends Application {

    @Override
    public void start(Stage primaryStage){

        RootsController roots = new RootsController();

        ClassLoader classLoader = getClass().getClassLoader();

        roots.addRoot("client", classLoader.getResource("client.fxml"));
        roots.addRoot("login", classLoader.getResource("login.fxml"));

        Scene scene = new Scene(RootsController.getScenes().get("login"),
                GetProperty.getInt("setWidth", 500),
                GetProperty.getInt("setHeight", 600));

        RootsController.mainScene = scene;

        primaryStage.setTitle("JavaMesa");

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(GetProperty.getInt("setMinWidth", 300));
        primaryStage.setMinHeight(GetProperty.getInt("setMinHeight", 600));

        Connect.connect();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
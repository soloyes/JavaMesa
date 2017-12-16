package ru.soloyes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

class RootsController {

    static Scene mainScene;
    
    private static HashMap<String, Parent> scenes = new HashMap<>();

    static HashMap<String, Parent> getScenes() {
        return scenes;
    }

    void addRoot(String name, URL location){

        FXMLLoader loader = new FXMLLoader(location);

        Parent root = null;

        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        scenes.put(name, root);
    }
}

package ru.soloyes;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class LoginController {

    @FXML
    TextField loginField;
    @FXML
    PasswordField passwordField;
    @FXML
    TextArea logArea;

    public void loginButton(MouseEvent mouseEvent) {
        if (Connect.isIsConnected()) {
            if (loginField.getText().length() == 0 || passwordField.getText().length() == 0) {
                logArea.appendText("Incorrect Password/Login.\n");
            } else if(!ClientHandler.isInList(loginField.getText())){
                logArea.appendText("Incorrect Login, already online.\n");
            }
            else {

                FadeTransition fo = new FadeTransition(
                        Duration.millis(
                                GetProperty.getInt("fadeOut", 400)),
                        RootsController.mainScene.getRoot()
                );
                fo.setFromValue(1.0);
                fo.setToValue(0.0);
                fo.play();
                fo.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        RootsController.mainScene.setRoot(RootsController.getScenes().get("client"));
                        new ClientHandler(loginField.getText(), passwordField.getText());
                    }
                });
            }
        }
        else {
            logArea.appendText("Disconnected!\n");
        }
    }

    public void regButton(MouseEvent mouseEvent) {}

    public void connButton(MouseEvent mouseEvent) {
        Connect.connect();
    }
}

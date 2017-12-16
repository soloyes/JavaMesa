package ru.geekbrains;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientController implements Initializable{

    @FXML
    TextArea outArea;
    @FXML
    TextArea inArea;
    @FXML
    Button commitButton;
    @FXML
    ListView<String> currentUsers = new ListView<>();

    static ObservableList<String> observableList = FXCollections.observableArrayList();

    private Pattern pattern = Pattern.compile("\n*");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUsers.setItems(observableList);
    }

    public void commitField(KeyEvent keyEvent) {
        if (keyEvent.isControlDown() && keyEvent.getCode().equals(KeyCode.ENTER)){
            commit();
        }
    }

    public void commitButton(MouseEvent mouseEvent) {
        commit();
        inArea.requestFocus();
    }

    private void commit(){
        DataOutputStream out = Connect.getOut();

        Matcher matcher = pattern.matcher(inArea.getText());

        if (!matcher.matches()){
            try {
                out.writeUTF(inArea.getText());
            } catch (IOException e) {
                System.out.println("Commit in trouble");
                e.printStackTrace();
            }
            inArea.clear();
        }
    }

    public void clickOnList(MouseEvent mouseEvent) {
        if  (mouseEvent.getClickCount() == 2){
            inArea.appendText("/w /"+ currentUsers.getSelectionModel().getSelectedItem() + " ");
            inArea.requestFocus();
        }
    }
}
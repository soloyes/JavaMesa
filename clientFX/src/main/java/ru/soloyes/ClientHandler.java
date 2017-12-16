package ru.soloyes;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import java.io.IOException;

class ClientHandler {

    ClientHandler(String name, String password){

        Thread t = new Thread(() -> {
            try {
                String msg;
                Connect.getOut().writeUTF("/login" + " " +  name + " " +  password);
                for(;;) {
                    msg = Connect.getIn().readUTF();
                    if (msg.startsWith("/clientlist")) {
                        msg = msg.replace("/clientlist ", "");
                        String[] users = msg.split(" ");
                        Platform.runLater(() -> {
                            ClientController.observableList.clear();
                            ClientController.observableList.addAll(users);
                        });
                        continue;
                    }

                    //For future auth flag, for example greeting message
                    if (msg.startsWith("/loginok")) {
                        continue;
                    }

                    TextArea outArea = (TextArea) RootsController.mainScene.lookup("#outArea");
                    outArea.appendText(msg);
                    outArea.appendText("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                //Something to do need to be here
                try {
                    Connect.getSocket().close();
                } catch (IOException e) {
                    System.out.println("Socket close in trouble");
                    e.printStackTrace();
                }
                try {
                    Connect.getIn().close();
                } catch (IOException e) {
                    System.out.println("in close in trouble");
                    e.printStackTrace();
                }
                try {
                    Connect.getOut().close();
                } catch (IOException e) {
                    System.out.println("out close in trouble");
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    static boolean isInList(String login){
        try {
            Connect.getOut().writeUTF("/inlist " + login);
            if (Connect.getIn().readUTF().equals("/e_ok")) {
                return true;
            }
        } catch (IOException e) {
            System.out.println("isInList in trouble");
            e.printStackTrace();
        }
        return false;
    }
}

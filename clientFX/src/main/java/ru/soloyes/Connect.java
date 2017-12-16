package ru.soloyes;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class Connect {

    private static DataInputStream in;

    private static DataOutputStream out;

    private static Socket socket;

    private static boolean isConnected = false;

    static DataInputStream getIn() {
        return in;
    }

    static DataOutputStream getOut() {
        return out;
    }

    static Socket getSocket() {
        return socket;
    }

    static boolean isIsConnected() {
        return isConnected;
    }

    static void connect() {

        TextArea logArea = (TextArea) RootsController.mainScene.lookup("#logArea");
        Button connButton = (Button) RootsController.mainScene.lookup("#connButton");

        try {
            socket = new Socket("localhost", 8888);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            connButton.setVisible(false);
            isConnected = true;
            logArea.appendText("Connected! Disconnect after 120s\n");

            //Disconnect every 120 seconds while not login
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted (120s)");
                        e.printStackTrace();
                    }
                    if (RootsController.mainScene.getRoot().
                            equals(RootsController.getScenes().get("login"))){
                        if (isConnected){
                            isConnected = false;
                            logArea.appendText("Disconnected!\n");
                            connButton.setVisible(true);
                            try {
                                socket.close();
                                in.close();
                                out.close();
                            } catch (IOException e) {
                                System.out.println("Connection close in trouble (120s)");
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            t.setDaemon(true);
            t.start();
            //

        } catch (IOException e) {
            connButton.setVisible(true);
            isConnected = false;
            logArea.appendText("Disconnected!\n");
            e.printStackTrace();
        }
    }
}

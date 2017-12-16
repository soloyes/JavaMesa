package ru.geekbrains;

import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;

public class Server {

    private final int PORT = 8888;

    private Vector<ServerClientHandler> clients = new Vector<>();

    public Vector<ServerClientHandler> getClients() {
        return clients;
    }

    Server(){
        try{
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            for(;;){
                Socket socket = serverSocket.accept();
                System.out.println("Incoming connection!\nFrom " + socket.getInetAddress() + " port " +
                socket.getPort());
                new ServerClientHandler(this, socket);
            }

        }catch (IOException e){
            System.out.println("Server socket in trouble");
            e.printStackTrace();
        }
    }

    void subscribe(ServerClientHandler client){
        clients.add(client);
        System.out.println("Subscribe " + client.getName());
        refreshList();
    }

    void unsubscribe(ServerClientHandler client){
        clients.remove(client);
        System.out.println("Unsubscribe " + client.getName());
        refreshList();
    }

    private void refreshList(){
        StringBuilder stringBuilder = new StringBuilder("/clientlist");
        for (ServerClientHandler s : clients) {
            stringBuilder.append(" ").append(s.getName());
        }
        for (ServerClientHandler s : clients) {
            s.sendMsg(stringBuilder.toString());
        }
    }

    void broadcast(ServerClientHandler client, String msg){
        for (ServerClientHandler s : clients) {
            s.sendMsg(client.getName() + ": " + msg);
        }
    }

    void unicast(ServerClientHandler client, String msg){
        for (ServerClientHandler s : clients) {
            String[] umsg = msg.split(" ",2);
            String[] rmsg = umsg[1].split(" ",2);
            if (rmsg[0].replaceAll("/", "").equals(s.getName())) {
                s.sendMsg(client.getName() + ": " + rmsg[1]);
                if (!(rmsg[0].replaceAll("/", "").equals(client.getName()))) {
                    client.sendMsg(client.getName() + ": " + rmsg[1]);
                }
            }
        }
    }
}


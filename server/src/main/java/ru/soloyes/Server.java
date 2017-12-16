package ru.soloyes;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
        StringBuilder clientlist = new StringBuilder("/clientlist");
        for (ServerClientHandler s : clients) {
            clientlist.append(" ").append(s.getName());
        }
        for (ServerClientHandler s : clients) {
            s.sendMsg(clientlist.toString());
        }
    }

    void broadcast(ServerClientHandler client, String msg){
        StringBuilder tail_msg = new StringBuilder(": ").append(msg);
        for (ServerClientHandler s : clients) {
            s.sendMsg(client.getName() + tail_msg);
        }
    }

    void unicast(ServerClientHandler client, String msg){
        String[] split_msg = msg.split(" ",2);
        String final_msg = "";
        if (split_msg.length > 1) {
            final_msg = split_msg[1];
        }
        String target = split_msg[0].replaceFirst("@","");
        StringBuilder tail_msg = new StringBuilder(": ").append(final_msg);

        for (ServerClientHandler s : clients) {
            if (target.equals(s.getName())) {
                s.sendMsg(client.getName() + tail_msg);
                if (!(target.equals(client.getName()))) {
                    client.sendMsg(client.getName() + tail_msg);
                }
                break;
            }
        }
    }
}


package ru.soloyes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerClientHandler {

    private Server server;

    private Socket socket;

    private DataInputStream in;

    private DataOutputStream out;

    private String name;

    String getName() {
        return name;
    }

    ServerClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            Thread t = new Thread(() -> {
                try {
                    for (;;) {
                        String msg = in.readUTF();
                        if (msg.startsWith("/inlist")){
                            String exists = "/existsok";
                            if (server.getClients().size() != 0){
                                for (ServerClientHandler s:server.getClients()) {
                                    if (s.getName().equals(msg.split(" ")[1])){
                                        exists = "/existsnok";
                                        break;
                                    }
                                }
                            }
                            out.writeUTF(exists);
                            continue;
                        }

                        if (msg.startsWith("/login")){
                            this.name = msg.split(" ")[1];
                            server.subscribe(ServerClientHandler.this);
                            out.writeUTF("/loginok");
                            continue;
                        }

                        if (msg.startsWith("@")){
                            server.unicast(ServerClientHandler.this, msg);
                        }
                        else {
                            server.broadcast(ServerClientHandler.this, msg);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("readUTF in trouble");
                    e.printStackTrace();
                } finally {
                    server.unsubscribe(ServerClientHandler.this);
                    try {
                        socket.close();
                    } catch (IOException e) {
                        System.out.println("Socket close in trouble");
                        e.printStackTrace();
                    }
                    try {
                        in.close();
                    } catch (IOException e) {
                        System.out.println("in close in trouble");
                        e.printStackTrace();
                    }
                    try {
                        out.close();
                    } catch (IOException e) {
                        System.out.println("out close in trouble");
                        e.printStackTrace();
                    }
                }
            });
            t.start();

        } catch (IOException e) {
            System.out.println("in/out in trouble");
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg){
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            System.out.println("Send message in trouble");
            e.printStackTrace();
        }
    }

}

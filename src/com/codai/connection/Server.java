package com.codai.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {

        final int port = 3322;
        final String host = "127.0.0.1";


        try {

            ServerSocket server = new ServerSocket(port);
            InetAddress inet = server.getInetAddress();

            if (!server.isBound()){
                System.out.println("Bounding...");
                server.bind(new InetSocketAddress("127.0.0.1", server.getLocalPort()));
            }

            System.out.println("HostPort = "+server.getLocalPort());
            System.out.println("HostAddress = "+inet.getHostAddress());
            System.out.println("HostName = "+inet.getHostName());

            Socket cliente;

            while (true){

                cliente = server.accept(); // colocar dentro de while para varios clientes

                System.out.println("Client ip: "+cliente.getInetAddress().getHostAddress());
                Scanner inFromClient = new Scanner(cliente.getInputStream());
                while ( inFromClient.hasNextLine() ) {

                    System.out.println( inFromClient.nextLine() );
                }
                inFromClient.close();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

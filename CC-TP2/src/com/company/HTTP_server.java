package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTP_server implements Runnable{

    //Our server socket which will listen to clients
    private ServerSocket http_server;

    //The folder we want to sync
    private String folder;

    //The host(s) which we want to sync with
    private String hosts[];

    //Our http port will be 8080
    private static final int port= 8080;



    //Construtor creates a socket at port 80 to handle TCP connection
    public HTTP_server(String folder, String hosts[]) throws IOException {
        try {
            this.http_server = new ServerSocket(port);
            this.hosts = hosts;
            this.folder = folder;
        }
        catch (IOException e){ e.printStackTrace(); }
    }


    /**
     * Gets the port number
     * @return port
     */
    public int getPort(){
        return this.port;
    }

    /**
     * Gest the http server socket
     * @return http server socket
     */
    public ServerSocket getHTTPSocket(){
        return this.http_server;
    }


    //TCP socket is listenning...it will debug once we open a browser and put something like http://<host>:8080/
    public void run(){
        try {
            System.out.println("HTTP Server socket created at port: " + getPort());
            System.out.println("\nHTTP Server socket listening for connection on port 8080...\n");
            while (true) {
                //HTTP socket is listening for clients
                Socket client = this.http_server.accept();
                Thread dedicated_thread= new Thread(new Client_HTTP(client, this.folder, this.hosts));
                dedicated_thread.start();
            }
        }
        catch(Exception e) {}
    }
}

package com.company;

import java.io.IOException;

//This Class is just for debug
public class ControlManager{

    //The host(s) which we want to sync with
    private String hosts[];

    //The folder we want to sync
    private String folder;

    //Our object which will be the http server
    private HTTP_server http_server;

    //Dedicated thread to manage HTTP request
    private static Thread http_thread;

    /*
    Control Manager constructor
     */
    public ControlManager(String args[]) throws IOException {
        this.hosts= new String[args.length- 1];
        this.folder= args[0];
        for(int i= 1; i< args.length; i++) this.hosts[i- 1]= args[i];
        this.http_thread= new Thread(new HTTP_server(this.folder, this.hosts));
    }

    /*
    Method to start the control manager. This method is responsible to call the dedicated thread for http request and to call methods to sync the host(s) with the folder
     */
    public void name() throws InterruptedException {

        this.http_thread.start();
        while (true) {
        }
    }

    /*
    Method to print debug stuff
     */
    public String toString(){
        StringBuilder s= new StringBuilder();
        s.append("--------------------Control Manager debug--------------------");
        s.append("\nHosts: ");
        for(int i= 0; i< this.hosts.length; i++) s.append(this.hosts[i]).append(" ");
        s.append("\nFolder: ").append(this.folder);

        return s.toString();
    }
}

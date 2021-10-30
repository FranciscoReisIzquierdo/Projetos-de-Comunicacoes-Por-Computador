package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Client_HTTP implements Runnable{
    private Socket client;
    //The folder we want to sync
    private String folder;

    private String hosts[];

    public Client_HTTP(Socket client, String folder, String hosts[]){
        this.client= client;
        this.folder= folder;
        this.hosts= hosts;
    }

    /**
     * Gets the folder
     * @return folder
     */
    public String getFolder(){
        return this.folder;
    }

    /**
     * Gets the hosts list
     * @return hosts list
     */
    public String[] getHosts(){
        return this.hosts;
    }

    //Client Handler
    private void handleClient(Socket client) throws IOException {
        System.out.println("Client connected at port 8080 " + client.toString());
        //Read the client request
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));

        //We are parsing the client request
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while (!(line = br.readLine()).isBlank()) {
            requestBuilder.append(line + "\r\n");
        }

        String request = requestBuilder.toString();
        String[] requestsLines = request.split("\r\n");
        String[] requestLine = requestsLines[0].split(" ");
        String method = requestLine[0];
        String path = requestLine[1];
        String version = requestLine[2];
        String host = requestsLines[1].split(" ")[1];

        List<String> headers = new ArrayList<>();
        for (int h = 2; h < requestsLines.length; h++) {
            String header = requestsLines[h];
            headers.add(header);
        }

        String accessLog = String.format("Client %s, method %s, path %s, version %s, host %s, headers %s",
                client.toString(), method, path, version, host, headers.toString());
        System.out.println("--------------------Client Request--------------------");
        System.out.println(accessLog);

        Path filePath = getFilePath(path);
        if (Files.exists(filePath)) {
            // file exist
            String contentType = guessContentType(filePath);
            showToClientResponse(client, "200 OK", contentType, Files.readAllBytes(filePath));
        } else {
            // 404
            byte[] notFoundContent = "<h1>Not found :(</h1>".getBytes();
            showToClientResponse(client, "404 Not Found", "text/html", notFoundContent);
        }
    }

    private void showToClientResponse(Socket client, String status, String contentType, byte[] content) throws IOException{
        OutputStream clientOutput = client.getOutputStream();
        clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
        clientOutput.write(("ContentType: text/html\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
        clientOutput.write("<title>FolderFastSync</title>".getBytes());
        clientOutput.write("<h1>FolderFastSync</h1>".getBytes());
        clientOutput.write("\r\n\r\n".getBytes());
        clientOutput.write("<h2>Welcome to FolderFastSync status manager</h2>".getBytes());
        clientOutput.write("\r\n\r\n".getBytes());
        clientOutput.write("<h3>Folder path: ".concat(getFolder()).concat("</h3>").getBytes());
        clientOutput.write("\r\n\r\n".getBytes());
        clientOutput.write("<h3>Hosts: ".concat(getHosts()[0]).concat("</h3>").getBytes());
        clientOutput.write("\r\n\r\n".getBytes());
        clientOutput.flush();
        client.close();
    }

    private static Path getFilePath(String path) {
        if ("/".equals(path)) {
            path = "/index.html";
        }

        return Paths.get("/tmp/www", path);
    }

    private static String guessContentType(Path filePath) throws IOException {
        return Files.probeContentType(filePath);
    }

    @Override
    public void run() {
        try {
            handleClient(this.client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

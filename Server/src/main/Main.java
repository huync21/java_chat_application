/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.net.ServerSocket;
import service.ServerProcess;

/**
 *
 * @author LENOVO
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        ServerProcess serverProcess = new ServerProcess(serverSocket);
        serverProcess.startServer();
        
    }
}

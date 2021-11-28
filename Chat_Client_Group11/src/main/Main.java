/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import service.ClientProcess;
import views.SignInFrm;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author LENOVO
 */
public class Main {
    public static void main(String[] args) throws IOException {

         Socket socket = new Socket("10.243.237.131", 1234);
//        Socket socket = new Socket("localhost", 1234);
        ClientProcess clientProcess = new ClientProcess(socket);
        SignInFrm signInFrm = new SignInFrm(clientProcess);
        signInFrm.setVisible(true);
    }
}

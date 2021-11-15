/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.Serializable;

/**
 *
 * @author LENOVO
 */
public class DataPackage implements Serializable{
    private static final long serialVersionUID = 13L;
    public static final int LOGIN = 1;
    public static final int SIGNUP = 2;
    public static final int GET_SINGLE_CHAT_ROOMS = 3;
    public static final int GET_MESSAGES_FROM_DB = 4;
    public static final int SET_CURRENT_ROOM = 5;
    public static final int SEND_MESSAGE = 6; 
    public static final int RECEIVE_MESSAGE = 7;
    public static final int SEND_ONLINE_STATUS_BROADCAST = 8;
    public static final int RECEIVE_ONLINE_STATUS_BROADCAST = 9;
    private int operation;
    private Object data;
    private String statusMessage;

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public DataPackage() {
    }
    
    
    public DataPackage(int operation, Object data) {
        this.operation = operation;
        this.data = data;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.*;
/**
 *
 * @author LENOVO
 */
public class Message implements Serializable{
    final long serialVersionUID = 2L;
    private int id;
    private String textContent;
    private Date time;
    private UserInARoom userInARoom;

    public Message() {
    }
    
    public Message(String textContent, Date time, UserInARoom userInARoom) {
        this.textContent = textContent;
        this.time = time;
        this.userInARoom = userInARoom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public UserInARoom getUserInARoom() {
        return userInARoom;
    }

    public void setUserInARoom(UserInARoom userInARoom) {
        this.userInARoom = userInARoom;
    }
    
    
    
}

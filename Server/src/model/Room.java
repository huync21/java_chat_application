/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author LENOVO
 */
public class Room implements Serializable{
    final long serialVersionUID = 1L;
    private int id;
    private String name;
    private ArrayList<UserInARoom> listUserInARoom = new ArrayList<>();

    public ArrayList<UserInARoom> getListUserInARoom() {
        return listUserInARoom;
    }

    public void setListUserInARoom(ArrayList<UserInARoom> listUserInARoom) {
        this.listUserInARoom = listUserInARoom;
    }
    
    public Room() {
    }

    public Room(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}

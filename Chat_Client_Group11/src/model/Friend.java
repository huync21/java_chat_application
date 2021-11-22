/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author QUANG
 */
public class Friend implements Serializable{
    final long serialVersionUID = 5L;
    private int id;
    private User user1;
    private User user2;
    private int friendStatus;

    public Friend() {
    }

    public Friend(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public Friend(int id, User user1, User user2, int friendStatus) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.friendStatus = friendStatus;
    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public int getFriendStatus() {
        return friendStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public void setFriendStatus(int friendStatus) {
        this.friendStatus = friendStatus;
    }

    
}

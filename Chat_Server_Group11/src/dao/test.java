/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import model.Friend;
import model.User;

/**
 *
 * @author QUANG
 */
public class test {
    public static void main(String[] args) {
        FriendDAO frienddao = new FriendDAO();
        User user = new User(1);
        ArrayList<Friend> list = frienddao.getAllInfoFriend(user);
        Friend fr = new Friend(new User(4), new User(7));
        frienddao.requestFriend(fr);
//        for( Friend f:list){
//            System.out.println(f.getId() + " " + f.getUser1().getFullName()+ " " + f.getUser2().getFullName() + "\n");
//        }
    }
}

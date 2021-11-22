/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Friend;
import model.User;

/**
 *
 * @author QUANG
 */
public class FriendDAO extends DAO{
    public void requestFriend(Friend friend){  // Gửi kết bạn
        try {
            String sql = "Insert into tblfriend(UserID1,UserID2,friendStatus) values(?,?,0);";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,friend.getUser1().getId());
            ps.setInt(2,friend.getUser2().getId());
            ps.executeUpdate();
        } catch (Exception e) {
        }
    }
    
    public void acceptFriend(Friend friend){  // Đồng ý kết bạn
        try {
            String sql = "update tblfriend set friendstatus = 1 where (UserID1 = ? and UserID2 = ?) or (UserID1 = ? and UserID2 = ?);";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, friend.getUser1().getId());
            ps.setInt(2, friend.getUser2().getId());
            ps.setInt(3, friend.getUser2().getId());
            ps.setInt(4, friend.getUser1().getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void deleteFriend(Friend friend){ // Xóa bạn bè
        try {
            String sql = "delete from tblfriend where (UserID1 = ? and UserID2 = ?) or (UserID1 = ? and UserID2 = ?);";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, friend.getUser1().getId());
            ps.setInt(2, friend.getUser2().getId());
            ps.setInt(3, friend.getUser2().getId());
            ps.setInt(4, friend.getUser1().getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public ArrayList<Friend> getAllInfoFriend(User user){  // Lấy tất cả thông tin về friend của 1 user
        ArrayList<Friend> list = new ArrayList<>();
        UserDAO userdao = new UserDAO();
        try {
            String sql ="select  *\n" +
                        "from  tblfriend\n" +
                        "where UserID1 = ? or UserID2 = ?;";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, user.getId());
            ps.setInt(2, user.getId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Friend fr = new Friend(rs.getInt("id"), userdao.getUserByID(rs.getInt("UserID1")),
                                userdao.getUserByID(rs.getInt("UserID2")),rs.getInt("friendStatus"));
                list.add(fr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
//    public ArrayList<User> getFriendRequest(User user){ // Lấy danh sách yêu cầu kết bạn
//        ArrayList<User> list = new ArrayList<>();
//        try {
//            String sql = "select DISTINCT tbluser.id , tbluser.fullName, tbluser.email, tbluser.phoneno, tbluser.onlinestatus\n"
//                        + "from  tbluser, tblfriend\n"
//                        + "where tblfriend.tblfriendid = ?  and tblfriend.friendstatus = 0 and  tblUser.id = tblfriend.tbluserid ;";
//            PreparedStatement ps = con.prepareStatement(sql);
//            ps.setInt(1, user.getId());
//            ResultSet rs = ps.executeQuery();
//            while(rs.next()){
//                User us = new User(rs.getInt("id"), rs.getString("fullName"), 
//                                    rs.getString("email"), rs.getString("phoneno"), rs.getInt("onlinestatus"));
//                list.add(us);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }
}

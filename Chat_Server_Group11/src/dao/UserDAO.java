/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.User;
//import com.sun.corba.se.spi.activation.Server;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Room;
import model.UserInARoom;
import service.ClientHandler;
import service.ServerProcess;

/**
 *
 * @author LENOVO
 */
public class UserDAO extends DAO{
    
    public boolean isExistedUser(User user){
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tblUser WHERE userName = ?");
            String userName = user.getUserName();
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public User signUp(User user){
        User result = null;
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO tblUser(userName,password,fullName,email,phoneNo,onlineStatus) VALUES(?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhoneNo());
            ps.setInt(6, 1);
            ps.executeUpdate();
            
            //Lay ra id auto increment
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if(generatedKeys.next()){
                user.setId(generatedKeys.getInt(1));
            }
            user.setOnlineStatus(1);
            result = user;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
    
    public User signIn(User user){
        User result = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tblUser WHERE userName = ? AND password = ?");
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                result = new User();
                result.setId(rs.getInt("id"));
                result.setUserName(rs.getString("userName"));
                result.setFullName(rs.getString("fullName"));
                result.setEmail(rs.getString("email"));
                result.setPhoneNo(rs.getString("phoneNo"));
                ps = con.prepareStatement("UPDATE tblUser SET onlineStatus = 1 WHERE id = ?");
                 ps.setInt(1, result.getId());
                ps.executeUpdate(); 
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
    
    public void setUserOffline(User user){
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE tblUser SET onlineStatus = 0 WHERE id = ?");
            ps.setInt(1, user.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    // L???y t???t c??? danh s??ch user ra tr??? user c???a m??y kh??ch ????? tr??? v???
    public ArrayList<User> getAllUsers(int exceptUser){
        ArrayList<User> listUsers = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tblUser WHERE id <> ?");
            ps.setInt(1, exceptUser);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("userName"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNo(rs.getString("phoneno"));
//                user.setOnlineStatus(rs.getInt("onlineStatus"));
                
                int userId = user.getId();
                int isOnline = 0;
                for(ClientHandler ch: ServerProcess.listClientHandler){
                    if(ch.getUser().getId() == userId){
                        isOnline = 1;
                        break;
                    }
                }
                
                user.setOnlineStatus(isOnline);
                listUsers.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listUsers;
    }

    public ArrayList<User> getUsersByName(String keyWord, User exceptUser) {
        ArrayList<User> result = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tbluser WHERE id <> ? AND (fullName LIKE ? OR userName LIKE ?)");
            ps.setInt(1, exceptUser.getId());
            ps.setString(2, "%"+keyWord+"%");
            ps.setString(3, "%"+keyWord+"%");
            
            result = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUserName(rs.getString("userName"));
                user.setFullName(rs.getString("fullName"));
//                user.setOnlineStatus(rs.getInt("onlineStatus"));
                
                int userId = user.getId();
                int isOnline = 0;
                for(ClientHandler ch: ServerProcess.listClientHandler){
                    if(ch.getUser().getId() == userId){
                        isOnline = 1;
                        break;
                    }
                }
                
                user.setOnlineStatus(isOnline);
                result.add(user);
            }
        } catch (SQLException ex) {
          return null;
        }
        return result;
    }
    
    public User getUserByID(int id){
        User result = null;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM tblUser WHERE id = ?");
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                result = new User();
                result.setId(rs.getInt("id"));
                result.setUserName(rs.getString("userName"));
                result.setFullName(rs.getString("fullName"));
                result.setEmail(rs.getString("email"));
                result.setPhoneNo(rs.getString("phoneNo"));
                
                int userId = result.getId();
                int isOnline = 0;
                for(ClientHandler ch: ServerProcess.listClientHandler){
                    if(ch.getUser().getId() == userId){
                        isOnline = 1;
                        break;
                    }
                }
                result.setOnlineStatus(isOnline);
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
    
    public Room getRoomChatById(int roomId){
        Room room = new Room();
        try {
        String sql = "SELECT id, tblUserId  FROM chat_application.tbluserinaroom where tblRoomId = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, roomId);
        ResultSet rs = ps.executeQuery();
        ArrayList<UserInARoom> listUserInARoomK = new ArrayList<>();
        while(rs.next()){
            UserInARoom userInARoom = new UserInARoom();
            userInARoom.setId(rs.getInt("id"));
            userInARoom.setUser(getUserByID(rs.getInt("tblUserId")));
            listUserInARoomK.add(userInARoom);
        }
            System.out.println(listUserInARoomK.size());
            System.out.println(roomId);
        room.setId(roomId);
        room.setListUserInARoom(listUserInARoomK);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
       
        return room;
    }
    
    public boolean insertUserToGroupChat(UserInARoom us){
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO tbluserinaroom(tblUserId,tblRoomId) VALUES(?,?)",Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,us.getUser().getId());
            ps.setInt(2, us.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    

}

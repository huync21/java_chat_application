/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseAcess;

import Model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            }
            ps = con.prepareStatement("UPDATE tblUser SET onlineStatus = 1 WHERE id = ?");
            ps.setInt(1, result.getId());
            ps.executeUpdate();
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
}
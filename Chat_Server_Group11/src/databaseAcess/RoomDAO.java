/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseAcess;

import Model.Room;
import Model.User;
import Model.UserInARoom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import service.ClientHandler;
import service.ServerProcess;

/**
 *
 * @author LENOVO
 */
public class RoomDAO extends DAO {

    public ArrayList<Room> getSingleChatRooms(int userId) { // lấy ra những phòng có 2 người
        ArrayList<Room> result = null;
        try {
            result = new ArrayList<>();
            String getRoomsSQL = "SELECT DISTINCT tblroom.id as roomId, tblroom.name as roomName\n"
                    + "FROM tblRoom,tblUserInARoom,tbluser\n"
                    + "WHERE tblroom.id = tbluserinaroom.tblRoomId AND tbluserinaroom.tblUserId = tbluser.id AND tbluser.id = ?";
            PreparedStatement ps = con.prepareStatement(getRoomsSQL);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt(1));
                room.setName(rs.getString(2));
                String getUsersSQL = "SELECT tbluser.id, tbluser.userName, tbluser.fullName, tbluser.onlineStatus,tblUserInARoom.id as tblUserInARoomId\n"
                        + "FROM tblRoom,tblUserInARoom,tbluser\n"
                        + "WHERE tblroom.id = tbluserinaroom.tblRoomId AND tbluserinaroom.tblUserId = tbluser.id AND tblroom.id = ?;";
                ps = con.prepareStatement(getUsersSQL);
                ps.setInt(1, room.getId());
                ResultSet rs1 = ps.executeQuery();
                int countUserInRoom = 0;
                while (rs1.next()) {
                    countUserInRoom++;
                    UserInARoom userInARoom = new UserInARoom();
                    User user = new User();
                    user.setId(rs1.getInt(1));
                    user.setUserName(rs1.getString(2));
                    user.setFullName(rs1.getString(3));
//                    user.setOnlineStatus(rs1.getInt(4));

                    int isOnline = 0;
                    for (ClientHandler ch : ServerProcess.listClientHandler) {
                        if (ch.getUser().getId() == user.getId()) {
                            isOnline = 1;
                            break;
                        }
                    }

                    user.setOnlineStatus(isOnline);
                    userInARoom.setId(rs1.getInt("tblUserInARoomId"));
                    userInARoom.setUser(user);
                    room.getListUserInARoom().add(userInARoom);
                }
                if (countUserInRoom == 2) {
                    result.add(room);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public ArrayList<Room> getGroupChatRooms(int userid) {
        ArrayList<Room> result = null;
        try {
            result = new ArrayList<>();
            String getRoomsSQL = "SELECT DISTINCT tblroom.id as roomId, tblroom.name as roomName\n"
                    + "FROM tblRoom,tblUserInARoom,tbluser\n"
                    + "WHERE tblroom.id = tbluserinaroom.tblRoomId AND tbluserinaroom.tblUserId = tbluser.id AND tbluser.id = ?";
            PreparedStatement ps = con.prepareStatement(getRoomsSQL);
            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Room room = new Room();
                room.setId(rs.getInt(1));
                room.setName(rs.getString(2));
                String getUsersSQL = "SELECT tbluser.id, tbluser.userName, tbluser.fullName, tbluser.onlineStatus,tblUserInARoom.id as tblUserInARoomId\n"
                        + "FROM tblRoom,tblUserInARoom,tbluser\n"
                        + "WHERE tblroom.id = tbluserinaroom.tblRoomId AND tbluserinaroom.tblUserId = tbluser.id AND tblroom.id = ?;";
                ps = con.prepareStatement(getUsersSQL);
                ps.setInt(1, room.getId());
                ResultSet rs1 = ps.executeQuery();
                int countUserInRoom = 0;
                while (rs1.next()) {
                    countUserInRoom++;
                    UserInARoom userInARoom = new UserInARoom();
                    User user = new User();
                    user.setId(rs1.getInt(1));
                    user.setUserName(rs1.getString(2));
                    user.setFullName(rs1.getString(3));
//                    user.setOnlineStatus(rs1.getInt(4));

                    int isOnline = 0;
                    for (ClientHandler ch : ServerProcess.listClientHandler) {
                        if (ch.getUser().getId() == user.getId()) {
                            isOnline = 1;
                            break;
                        }
                    }

                    user.setOnlineStatus(isOnline);
                    userInARoom.setId(rs1.getInt("tblUserInARoomId"));
                    userInARoom.setUser(user);
                    room.getListUserInARoom().add(userInARoom);
                }
               
                result.add(room);
                
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}

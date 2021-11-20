/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.Room;
import model.User;
import model.UserInARoom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            String getRoomsSQL = "SELECT tblRoom.id, tblRoom.name FROM tblRoom, tbluserinaroom, (SELECT tbluserinaroom.tblRoomId as id  From tbluserinaroom GROUP BY tbluserinaroom.tblRoomId HAVING COUNT(tbluserinaroom.tblUserId) > 2) as groupChatId where tblRoom.id = tbluserinaroom.tblRoomId and groupChatId.id = tblRoom.id and tblUserId = ?";
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
                while (rs1.next()) {
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
        System.out.println(result.toString());
        return result;
    }

    public Room createRoom(Room room) {
        String insertRoom = "INSERT INTO tblRoom(name) values(?)";
        String insertUserInRoom = "INSERT INTO tbluserinaroom(tblUserId, tblRoomId) values(?,?)";
        boolean result = true;
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(insertRoom,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, room.getName());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                room.setId(generatedKeys.getInt(1));

                for (UserInARoom us : room.getListUserInARoom()) {
                    PreparedStatement ps1 = con.prepareStatement(insertUserInRoom,
                            Statement.RETURN_GENERATED_KEYS);
                    ps1.setInt(1, us.getUser().getId());
                    ps1.setInt(2, room.getId());

                    int rs = ps1.executeUpdate();

                    ResultSet generatedKeys1 = ps1.getGeneratedKeys();
                    if (generatedKeys1.next()) {
                        int userInAroom = generatedKeys1.getInt(1);
                        us.setId(userInAroom);
                    }
                    if (rs == 0) {//unavailable
                        con.rollback();
                        con.setAutoCommit(true);
                        return null;
                    }
                }
                con.commit();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return room;
    }

    public Room getExistedSingleChatRoom(User user1, User user2) {
        Room result = null;
        try {
            String getRoomSQL = "SELECT tblroom.id as roomId,tbluserinaroom.id as userInARoomId, tblUser.id as userId,tblUser.userName,tblUser.fullName,tblUser.email,tblUser.phoneNo \n"
                    + "FROM tblroom\n"
                    + ",(SELECT id,tblUserId,tblRoomId FROM tblUserInARoom WHERE tblUserId = ? OR tblUserId = ? GROUP BY tblRoomId HAVING COUNT(tblRoomId) = 2) as A\n"
                    + ",tbluserinaroom,tbluser \n"
                    + "WHERE tblroom.id = A.tblRoomId AND tblroom.id = tbluserinaroom.tblRoomId AND tbluserinaroom.tblUserId = tbluser.id";

            PreparedStatement ps = con.prepareStatement(getRoomSQL);
            ps.setInt(1, user1.getId());
            ps.setInt(2, user2.getId());
            ResultSet rs = ps.executeQuery();
            result = new Room();
            ArrayList<UserInARoom> listUserInARooms = new ArrayList<>();
            UserInARoom user1st = new UserInARoom();
            user1st.setUser(new User());
            UserInARoom user2nd = new UserInARoom();
            user2nd.setUser(new User());

            listUserInARooms.add(user1st);
            listUserInARooms.add(user2nd);
            result.setListUserInARoom(listUserInARooms);
            int count = 0;
            while (rs.next()) {

                result.setId(rs.getInt("roomId"));
                UserInARoom userInARoom = result.getListUserInARoom().get(count);
                userInARoom.setId(rs.getInt("userInARoomId"));
                User u = result.getListUserInARoom().get(count).getUser();
                u.setId(rs.getInt("userId"));
                u.setUserName(rs.getString("userName"));
                u.setFullName(rs.getString("fullName"));
                u.setEmail(rs.getString("email"));
                u.setPhoneNo(rs.getString("phoneNo"));
                userInARoom.setUser(u);
                result.getListUserInARoom().add(userInARoom);
                count++;

            }
            if (count != 2 || result.getId() == 0) {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }

        return result;
    }

}

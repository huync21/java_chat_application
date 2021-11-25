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
                    userInARoom.setId(rs1.getInt(5));
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
        String insertRoom = "INSERT INTO tblRoom(name,isSingleChat) values(?,?)";
        String insertUserInRoom = "INSERT INTO tbluserinaroom(tblUserId, tblRoomId) values(?,?)";
        boolean result = true;
        try {
            con.setAutoCommit(false);
            PreparedStatement ps = con.prepareStatement(insertRoom,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, room.getName());
            ps.setInt(2, room.getIsSingleChat());
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
        ArrayList<Room> listRoomOfUser1 = new RoomDAO().getSingleChatRooms(user1.getId());
        
        //Lay ra room chat cua 2 nguoi
        for(Room r: listRoomOfUser1){
            //Lay ra user con lai
            User u2 = new User();
            //Kiem tra xem nguoi con lai trong phong co phai user2 ko, neu co thi lay ra phong gan vao result
            for(UserInARoom uiar: r.getListUserInARoom()){
                if(uiar.getUser().getId() == user2.getId()){
                    result = r;
                    u2 = uiar.getUser();
                    break;
                }
            }
            // Lay ra duoc phong roi thi break vong for thoi
            if(u2.getId() == user2.getId()){
                break;
            }
            
        }
        return result;
    }
}

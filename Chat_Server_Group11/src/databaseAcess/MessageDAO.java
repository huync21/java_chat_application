/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseAcess;

import Model.Message;
import Model.Room;
import Model.User;
import Model.UserInARoom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author LENOVO
 */
public class MessageDAO extends DAO {

    public ArrayList<Message> getListMessages(Room room) {
        ArrayList<Message> result = new ArrayList<>();

        try {
            String sql = "SELECT tblmessage.id as messageId, tblmessage.textContent, tblmessage.time, tbluserinaroom.id as userInARoomId, tbluser.id as userID, tbluser.userName as userName\n"
                    + "FROM tblmessage, tbluserinaroom,tbluser,tblroom\n"
                    + "WHERE tblmessage.tbluserinaroomid = tbluserinaroom.id AND tbluserinaroom.tbluserid = tbluser.id AND tbluserinaroom.tblroomid = tblroom.id AND tblroom.id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, room.getId());
            ResultSet rs = ps.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            while (rs.next()) {
                Message message = new Message();
                message.setId(rs.getInt("messageId"));
                message.setTextContent(rs.getString("textContent"));
                String timeString = rs.getString("time");
                if (timeString != null) {
                    message.setTime(sdf.parse(timeString));
                }
                UserInARoom userInARoom = new UserInARoom();
                userInARoom.setId(rs.getInt("userInARoomId"));
                User user = new User();
                user.setId(rs.getInt("userID"));
                user.setUserName(rs.getString("userName"));
                
                userInARoom.setUser(user);
                message.setUserInARoom(userInARoom);
                
                result.add(message);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        return result;
    }
}

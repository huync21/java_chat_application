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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LENOVO
 */
public class MessageDAO extends DAO {

    public ArrayList<Message> getListMessages(Room room) { // lấy tất cả message trong phòng này
        ArrayList<Message> result = new ArrayList<>();

        try {
            String sql = "SELECT tblmessage.id as messageId, tblmessage.textContent, tblmessage.time, tbluserinaroom.id as userInARoomId, tbluser.id as userID, tbluser.userName as userName\n"
                    + "FROM tblmessage, tbluserinaroom,tbluser,tblroom\n"
                    + "WHERE tblmessage.tbluserinaroomid = tbluserinaroom.id AND tbluserinaroom.tbluserid = tbluser.id AND tbluserinaroom.tblroomid = tblroom.id AND tblroom.id = ?\n"
                    + "ORDER BY time ASC";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, room.getId());
            ResultSet rs = ps.executeQuery();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
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

    public void saveMessage(Message message) { // cứ gửi tin nhắn đi thì phải lưu vào db
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            PreparedStatement ps = con.prepareStatement("INSERT INTO tblmessage(textContent,time,tblUserInARoomId) VALUES (?,?,?)");
            ps.setString(1, message.getTextContent());
            ps.setString(2, sdf.format(message.getTime()));
            ps.setInt(3, message.getUserInARoom().getId());
            System.out.println(message.getUserInARoom().getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.groupChat;

import java.awt.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import model.Room;
import model.User;
import model.UserInARoom;
import service.ClientProcess;

/**
 *
 * @author Do Dinh
 */
public class AddFriendToGroupChat extends javax.swing.JFrame {

    /**
     * Creates new form AddFriendToGroupChat
     */
    private JFrame mainFrame = this;
    private ClientProcess clientProcess;
    private ArrayList<User> listUserSelectGroup = new ArrayList<>();
    private ArrayList<User> listUsers = new ArrayList<>();
    private Room currentRoom;
    public AddFriendToGroupChat(ClientProcess clientProcess) {
        initComponents();

        this.clientProcess = clientProcess;
        clientProcess.setCurrentFrame(mainFrame);
        clientProcess.getAllUsers();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblUserFriend = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblUserFriend.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "user Id", "user name", "full name", "online status"
            }
        ));
        tblUserFriend.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUserFriendMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUserFriend);

        jLabel1.setText("Danh sách bạn bè");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 20, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(121, 121, 121))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(188, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblUserFriendMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUserFriendMouseClicked
        // TODO add your handling code here:

        int column = tblUserFriend.getColumnModel().getColumnIndexAtX(evt.getX()); // 
        int row = evt.getY() / tblUserFriend.getRowHeight();

        if (row < tblUserFriend.getRowCount() && row >= 0
                && column < tblUserFriend.getColumnCount() && column >= 0) {

            //listUserSelectGroup.add();
            this.currentRoom = clientProcess.getRoom();
            User us = listUsers.get(row);
            UserInARoom fr = new UserInARoom();
            fr.setId(this.currentRoom.getId());
            fr.setUser(us);
            clientProcess.addUserToGroupChat(fr);
            
        }
    }//GEN-LAST:event_tblUserFriendMouseClicked

    public void backFrGroupChat(Room room){
        clientProcess.setRoom(room);
        new GroupChatFrm(clientProcess).setVisible(true);
        mainFrame.dispose();
    }
    public boolean checkUserOutRoom(ArrayList<UserInARoom> listUserInARoom, User us) {
        for (UserInARoom userIAR : listUserInARoom) {
            if (userIAR.getUser().getId() == us.getId()) {
                return false;
            }
        }
        return true;
    }

    public void receiveAllUsersAndDisplay(ArrayList<User> listUsers) {
        ArrayList<UserInARoom> listUserInARoom = clientProcess.getRoom().getListUserInARoom();
        ArrayList<User> listUserIAR = new ArrayList<>(listUsers.stream().filter(e -> checkUserOutRoom(listUserInARoom, e)).collect(Collectors.toList()));
        this.listUsers = listUserIAR;
        // hien thi
        String[][] data = new String[listUserIAR.size()][4];
        String[] columnNames = {"id", "user name", "full name", "online status"};
        for (int i = 0; i < listUserIAR.size(); i++) {
            User user = listUserIAR.get(i);
            data[i][0] = user.getId() + "";
            data[i][1] = user.getUserName();
            data[i][2] = user.getFullName();
            data[i][3] = user.getOnlineStatus() + "";
        }
        DefaultTableModel dtm = new DefaultTableModel(data, columnNames);
        tblUserFriend.setModel(dtm);

    }

    private void tblUsersMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUserFriend;
    // End of variables declaration//GEN-END:variables
}
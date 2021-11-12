/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.groupChat;

import Model.Room;
import Model.User;
import Model.UserInARoom;
import Service.ClientProcess;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LENOVO
 */
public class CreateNewGroupFrm extends javax.swing.JFrame {

    /**
     * Creates new form CreateNewGroupFrm
     */
    private JFrame mainFrame = this;
    private ClientProcess clientProcess;
    private ArrayList<User> listUserSelectGroup = new ArrayList<>();
    private ArrayList<User> listUsers = new ArrayList<>();
    public CreateNewGroupFrm(ClientProcess clientProcess) {
        initComponents();
        
        this.clientProcess = clientProcess;
        
        listUsers = clientProcess.getAllUsers(clientProcess.getUser().getId());
        
        // hien thi
        String[][] data = new String[listUsers.size()][4];
        String[] columnNames = {"id","user name","full name","online status"};
        for(int i=0;i<listUsers.size();i++){
            User user = listUsers.get(i);
            data[i][0] = user.getId()+"";
            data[i][1] = user.getUserName();
            data[i][2] = user.getFullName();
            data[i][3] = user.getOnlineStatus()+"";
        }
        DefaultTableModel dtm = new DefaultTableModel(data, columnNames);
        tblUsers.setModel(dtm);
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        labelTheRestUserName = new javax.swing.JLabel();
        txtTenPhong = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsers = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAddedUsers = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Ten phong:");

        labelTheRestUserName.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        labelTheRestUserName.setText("Tao moi 1 phong chat:");

        txtTenPhong.setText("Phan tich thiet ke");

        tblUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "user Id", "user name", "full name", "online status"
            }
        ));
        tblUsers.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tblUsersAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tblUsers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUsers);

        tblAddedUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "user Id", "user name", "full name", "online status"
            }
        ));
        jScrollPane2.setViewportView(tblAddedUsers);

        jLabel2.setText("danh sach user");

        jLabel3.setText("danh sach user da them:");

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(27, 27, 27)
                            .addComponent(jLabel1)
                            .addGap(50, 50, 50)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labelTheRestUserName)
                                .addComponent(txtTenPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(53, 53, 53)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(59, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(254, 254, 254))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(labelTheRestUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtTenPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jButton1)
                .addContainerGap(75, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblUsersAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblUsersAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tblUsersAncestorAdded

    private void tblUsersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsersMouseClicked
        // TODO add your handling code here:
        int column = tblUsers.getColumnModel().
                        getColumnIndexAtX(evt.getX()); // 
                int row = evt.getY()/tblUsers.getRowHeight(); 
 
                if (row < tblUsers.getRowCount() && row >= 0 && 
                            column < tblUsers.getColumnCount() && column >= 0) {
                  
                   listUserSelectGroup.add(listUsers.get(row));
                   String[][] data = new String[listUsers.size()][4];
                    String[] columnNames = {"id","user name","full name","online status"};
                    for(int i=0;i<listUserSelectGroup.size();i++){
                        User user = listUserSelectGroup.get(i);
                        data[i][0] = user.getId()+"";
                        data[i][1] = user.getUserName();
                        data[i][2] = user.getFullName();
                        data[i][3] = user.getOnlineStatus()+"";
                    }
                    DefaultTableModel dtm = new DefaultTableModel(data, columnNames);
                    tblAddedUsers.setModel(dtm);
               }
    }//GEN-LAST:event_tblUsersMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Room room = new Room();
        room.setName(txtTenPhong.getText());
        ArrayList<UserInARoom> listUserInAroom = new ArrayList<>();
        for(int i=0; i<listUserSelectGroup.size(); i++){
            UserInARoom userInARoom = new UserInARoom();
            userInARoom.setUser(listUserSelectGroup.get(i));
            listUserInAroom.add(userInARoom);
        }
        room.setListUserInARoom(listUserInAroom);
        
        try {
            if(clientProcess.creatRoom(room) != null){
                clientProcess.setRoom(room);
                new GroupChatFrm(clientProcess).setVisible(true);
            }
            else{
                JOptionPane.showMessageDialog(rootPane, "Error Create Room");
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(rootPane, "Error Create Room");
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelTheRestUserName;
    private javax.swing.JTable tblAddedUsers;
    private javax.swing.JTable tblUsers;
    private javax.swing.JTextField txtTenPhong;
    // End of variables declaration//GEN-END:variables
}

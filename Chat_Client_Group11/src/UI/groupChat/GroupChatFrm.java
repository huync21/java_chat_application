/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.groupChat;

import Model.Message;
import Model.Room;
import Model.User;
import Model.UserInARoom;
import service.ClientProcess;
import UI.SingleChatRoomsFrm;
import java.awt.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author LENOVO
 */
public class GroupChatFrm extends javax.swing.JFrame {

    /**
     * Creates new form GroupChatFrm
     */
    private ClientProcess clientProcess;
    String messagesOnTheScreen = "";
    public GroupChatFrm(ClientProcess clientProcess) {
        initComponents();
        
        this.clientProcess = clientProcess;
        
        clientProcess.setgroupRoomFrame(this);
        
        
        
        System.out.println(clientProcess.getRoom().getName());
        
        
        btnBack.addActionListener((e) -> {
            
            new SingleChatRoomsFrm(clientProcess).setVisible(true);
            this.dispose();
        });
        
        labelTheRestUserName.setText(clientProcess.getRoom().getName());
        // Lấy ra user còn lại trong phòng
        User currentUser = clientProcess.getUser();
        ArrayList<User> theRestUserInTheRoom = new ArrayList<>();
        Room currentRoom = clientProcess.getRoom();
        ArrayList<UserInARoom> listUserInCurrentRoom = currentRoom.getListUserInARoom();
        
        for (UserInARoom u : listUserInCurrentRoom) {
                theRestUserInTheRoom.add(u.getUser());
        }
        
         //Lúc mới mở màn hình chat của phòng này thì lấy ra danh sách các tin nhắn cũ trong phòng này từ database
//        ArrayList<Message> listMessagesInRoom = clientProcess.getMessagesFromDatabase(currentRoom);
        clientProcess.setListMessagesInARoom(listMessagesInRoom);
        for (Message m : clientProcess.getListMessagesInARoom()) {
            System.out.println(m.getTextContent() + " " + m.getId());
            updateChatScreen(m);
        }
        
        clientProcess.listenToMessage();
        
        btnSend.addActionListener((e) -> {
            String input = txtMessage.getText();

            Message message = new Message();
            message.setTextContent(input);
            message.setTime(new Date());

            // lấy ra id của user in a room của user
            UserInARoom userInARoom = new UserInARoom();
            for (UserInARoom u : currentRoom.getListUserInARoom()) {
                if (currentUser.getId() == u.getUser().getId()) {
                    userInARoom = u;
                    break;
                }
            }
            userInARoom.setUser(currentUser);
            message.setUserInARoom(userInARoom);

            // gửi đi 
            this.clientProcess.sendMessage(message);

            // xóa đi text đã nhập trong input và hiện trong screen chat
            txtMessage.setText("");
            clientProcess.getListMessagesInARoom().add(message);
            
        });
        
    }
    
    public void updateChatScreen(Message message) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (message.getTextContent() != null) {
            messagesOnTheScreen += (message.getUserInARoom().getUser().getUserName()+" ("+sdf.format(message.getTime()) +")" + ":\n" + message.getTextContent() + "\n\n");
            txtChatScreen.setText(messagesOnTheScreen);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelTheRestUserName = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtChatScreen = new javax.swing.JTextArea();
        txtMessage = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelTheRestUserName.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        labelTheRestUserName.setText("@Name Of The Room");

        txtChatScreen.setEditable(false);
        txtChatScreen.setColumns(20);
        txtChatScreen.setRows(5);
        txtChatScreen.setText("huy2110(ngay gio): hello\n\ndo123(ngay gio): hihi\n\ncuong345(ngay gio): hehe\n\n");
        jScrollPane1.setViewportView(txtChatScreen);

        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(54, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSend))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 649, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(217, 217, 217))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(159, 159, 159)
                        .addComponent(labelTheRestUserName))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelTheRestUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSend))
                .addContainerGap(135, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_btnSendActionPerformed

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnSend;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelTheRestUserName;
    private javax.swing.JTextArea txtChatScreen;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables
}

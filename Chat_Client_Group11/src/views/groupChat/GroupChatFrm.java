/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.groupChat;

import java.awt.Color;
import java.awt.Dimension;
import model.Message;
import model.Room;
import model.User;
import model.UserInARoom;
import service.ClientProcess;
import views.SingleChatRoomsFrm;
import java.awt.List;
import java.awt.Toolkit;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import utils.EmojiUtils;

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
        System.out.println("Ok");
        initComponents();
         HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        jEditorPane2.setEditable(false);
        jEditorPane2.setContentType("text/html");
        jEditorPane2.setEditorKit(htmlEditorKit);
        jEditorPane2.setText("<html><body id='body'></body></html>");
        this.clientProcess = clientProcess;
        
        clientProcess.setCurrentFrame(this);
        
        //UI
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.getContentPane().setBackground(Color.getHSBColor(106, 52, 50));
        setTitle("Phòng chat nhóm của "+clientProcess.getUser().getUserName());
        
        System.out.println(clientProcess.getRoom().getName());
        
        
        labelTheRestUserName.setText(clientProcess.getRoom().getName());
        // Lấy ra user còn lại trong phòng
        User currentUser = clientProcess.getUser();
        ArrayList<User> theRestUserInTheRoom = new ArrayList<>();
        Room currentRoom = clientProcess.getRoom();
        ArrayList<UserInARoom> listUserInCurrentRoom = currentRoom.getListUserInARoom();
        
        for (UserInARoom u : listUserInCurrentRoom) {
                theRestUserInTheRoom.add(u.getUser());
        }
        

        this.clientProcess.getMessagesFromDatabase(currentRoom);
        btnSend.addActionListener((e) -> {
            String input = txtMessage.getText().trim();
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
        
          String[][] data = new String[currentRoom.getListUserInARoom().size()][4];
                    String[] columnNames = {"id","user name","full name","online status"};
                    for(int i=0;i<currentRoom.getListUserInARoom().size();i++){
                        User user = currentRoom.getListUserInARoom().get(i).getUser();
                        data[i][0] = user.getId()+"";
                        data[i][1] = user.getUserName();
                        data[i][2] = user.getFullName();
                        data[i][3] = user.getOnlineStatus()+"";
                    }
                    DefaultTableModel dtm = new DefaultTableModel(data, columnNames);
                    tblUsersInRoom.setModel(dtm);
    }
    public void updateChatScreen(Message message) {
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        if (message.getTextContent() != null) {
            try {
                String userText = message.getUserInARoom().getUser().getUserName();
                String userAndDateText = new String();
                if(message.getUserInARoom().getUser().getId() == (clientProcess.getUser().getId())){
                    userText = "Bạn";
                    userAndDateText=  "<b><i style = \"color:green;\">"+userText + " (" + sdf.format(message.getTime()) + ")" + ":</i></b>";
                }else{
                    userAndDateText=  "<b><i style = \"color:blue;\">"+userText + " (" + sdf.format(message.getTime()) + ")" + ":</i></b>";
                };
                HTMLDocument doc = (HTMLDocument) jEditorPane2.getDocument();
                Element elem = doc.getElement("body");
                String htmlText = String.format("<p>%s</p>", userAndDateText);
                doc.insertBeforeEnd(elem, htmlText);
                String textContent = message.getTextContent();

                Map<String,String> map = new EmojiUtils().getMap();
                
                String imageURL = null;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (textContent.equals(entry.getKey())) {
                        imageURL = entry.getValue();
                    }
                }
                if (imageURL != null) {
                    doc.insertBeforeEnd(elem, String.format("<img src=\"" + imageURL + "\">"));
                } else {
                    doc.insertBeforeEnd(elem, String.format("<p>%s</p>", textContent));// ko phai emoji thi in ra text thường
                }

            } catch (BadLocationException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getMaximum());
        jEditorPane2.validate();
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
        txtMessage = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane2 = new javax.swing.JEditorPane();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblUsersInRoom = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelTheRestUserName.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        labelTheRestUserName.setText("@Name Of The Room");

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

        jScrollPane1.setViewportView(jEditorPane2);

        jButton2.setText("Thêm thành viên");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        tblUsersInRoom.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "user Id", "user name", "full name", "online status"
            }
        ));
        tblUsersInRoom.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tblUsersInRoomAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tblUsersInRoom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsersInRoomMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblUsersInRoom);

        jLabel2.setText("Danh sách user trong phòng chat nhóm:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(196, 196, 196)
                        .addComponent(labelTheRestUserName)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(61, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(332, 332, 332))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGap(57, 57, 57)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(74, 74, 74))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(83, 83, 83)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel1))
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(jLabel1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(btnBack)
                        .addGap(6, 6, 6)
                        .addComponent(labelTheRestUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jButton2)
                        .addGap(35, 35, 35)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSend)
                    .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        new GroupChatRoomsFrm(clientProcess).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_btnSendActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        new AddFriendToGroupChat(clientProcess).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tblUsersInRoomAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblUsersInRoomAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tblUsersInRoomAncestorAdded

    private void tblUsersInRoomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsersInRoomMouseClicked
  
    }//GEN-LAST:event_tblUsersInRoomMouseClicked

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnSend;
    private javax.swing.JButton jButton2;
    private javax.swing.JEditorPane jEditorPane2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelTheRestUserName;
    private javax.swing.JTable tblUsersInRoom;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables
}

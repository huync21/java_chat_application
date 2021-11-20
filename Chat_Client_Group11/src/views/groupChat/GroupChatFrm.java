/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.groupChat;

import model.Message;
import model.Room;
import model.User;
import model.UserInARoom;
import service.ClientProcess;
import views.SingleChatRoomsFrm;
import java.awt.List;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
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
        initComponents();
         HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        jEditorPane2.setEditable(false);
        jEditorPane2.setContentType("text/html");
        jEditorPane2.setEditorKit(htmlEditorKit);
        jEditorPane2.setText("<html><body id='body'></body></html>");
        this.clientProcess = clientProcess;
        
        clientProcess.setCurrentFrame(this);
        
        
        
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
        
    }
    public void updateChatScreen(Message message) {
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        if (message.getTextContent() != null) {
            try {
                String userText = message.getUserInARoom().getUser().getUserName();
                if(message.getUserInARoom().getUser().getId() == (clientProcess.getUser().getId())){
                    userText = "Bạn";
                };
                String userAndDateText =  userText + " (" + sdf.format(message.getTime()) + ")" + ":";
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(159, 159, 159)
                        .addComponent(labelTheRestUserName))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(119, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(txtMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE))
                .addGap(63, 63, 63)
                .addComponent(btnSend)
                .addGap(217, 217, 217))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelTheRestUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSend))
                .addContainerGap(135, Short.MAX_VALUE))
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

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnSend;
    private javax.swing.JEditorPane jEditorPane2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelTheRestUserName;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables
}

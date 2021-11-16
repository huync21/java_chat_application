/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Model.Message;
import Model.Room;
import Model.User;
import Model.UserInARoom;
import service.ClientProcess;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 *
 * @author LENOVO
 */
public class SingleChatFrm extends javax.swing.JFrame {

    /**
     * Creates new form SingleChatFrm
     */
    private ClientProcess clientProcess;
    String messagesOnTheScreen = "";
    private User theRestUserInTheRoom;
    private Room currentRoom;
    private User currentUser;

    public SingleChatFrm(ClientProcess clientProcess) {
        initComponents();
        this.clientProcess = clientProcess;
        clientProcess.setCurrentFrame(this);
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        txtChatScreen.setEditable(false);
        txtChatScreen.setContentType("text/html");
        txtChatScreen.setEditorKit(htmlEditorKit);
        txtChatScreen.setText("<html><body id='body'></body></html>");

        btnBack.addActionListener((e) -> {
            new SingleChatRoomsFrm(clientProcess).setVisible(true);
            this.dispose();
        });

        // Lấy ra user còn lại trong phòng
        currentUser = clientProcess.getUser();
        theRestUserInTheRoom = new User();
        currentRoom = clientProcess.getRoom();
        ArrayList<UserInARoom> listUserInCurrentRoom = currentRoom.getListUserInARoom();
        for (UserInARoom u : listUserInCurrentRoom) {
            if (u.getUser().getId() != currentUser.getId()) {
                theRestUserInTheRoom = u.getUser();
            }
        }

        // In ra tên user còn lại và trạng thái đăng nhập
        labelTheRestUserName.setText(theRestUserInTheRoom.getUserName() + "(" + theRestUserInTheRoom.getFullName() + ")");
        if (theRestUserInTheRoom.getOnlineStatus() == 1) {
            labelStatus.setText("Online");
            labelStatus.setForeground(Color.GREEN);
        } else {
            labelStatus.setText("Offline");
            labelStatus.setForeground(Color.red);
        }

        //in ra tên user hiện tại
        txtUser.setText("User: " + clientProcess.getUser().getFullName());

        // lay messages cu~ tu database
        clientProcess.getMessagesFromDatabase(currentRoom);

        // set room frame cho tiến trình client process để gọi hàm update ui ở bên client process mỗi khi có tin nhắn tới
        clientProcess.setRoomFrame(this);

        // Gửi tin nhắn
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

        //emoji
    }

    // khi nao co tin nhan toi, client process se goi ham nay
    public void updateChatScreen(Message message) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        if (message.getTextContent() != null) {
//            messagesOnTheScreen += (message.getUserInARoom().getUser().getUserName() + " (" + sdf.format(message.getTime()) + ")" + ":\n" + message.getTextContent() + "\n\n");
//            StringTokenizer stk = new StringTokenizer(messagesOnTheScreen);
//            
//            txtChatScreen.setText(messagesOnTheScreen);
            try {
                String userAndDateText = message.getUserInARoom().getUser().getUserName() + " (" + sdf.format(message.getTime()) + ")" + ":";
                HTMLDocument doc = (HTMLDocument) txtChatScreen.getDocument();
                Element elem = doc.getElement("body");
                String htmlText = String.format("<p>%s</p>", userAndDateText);
                doc.insertBeforeEnd(elem, htmlText);
                String textContent = message.getTextContent();
                StringTokenizer stk = new StringTokenizer(textContent);

                //emoji
                Map<String, String> map = new HashMap<>();
                String imageURL = null;
                map.put(":)", "https://static.xx.fbcdn.net/images/emoji.php/v9/t5f/1.5/32/1f642.png");
                map.put("(:", "https://static.xx.fbcdn.net/images/emoji.php/v9/te0/1.5/32/1f643.png");
                map.put("^_^", "https://static.xx.fbcdn.net/images/emoji.php/v9/t92/1.5/32/1f60a.png");
                map.put(":(", "https://static.xx.fbcdn.net/images/emoji.php/v9/tde/1.5/32/1f641.png");
                map.put("B)", "https://static.xx.fbcdn.net/images/emoji.php/v9/t96/1.5/32/1f60e.png");
                map.put(":/", "https://static.xx.fbcdn.net/images/emoji.php/v9/t5/1.5/32/1f615.png");
                map.put(":o", "https://static.xx.fbcdn.net/images/emoji.php/v9/td4/1.5/32/1f62e.png");
                map.put(":D", "https://static.xx.fbcdn.net/images/emoji.php/v9/t64/1.5/32/1f603.png");
                map.put("<(\")", "https://static.xx.fbcdn.net/images/emoji.php/v9/t24/1.5/32/1f427.png");
                map.put(":3", "https://static.xx.fbcdn.net/images/emoji.php/v9/ec7/1.5/32/FACE_WITH_COLON_THREE.png");
                map.put(":v", "https://static.xx.fbcdn.net/images/emoji.php/v9/e7e/1.5/32/PACMAN.png");
                map.put("<3", "https://static.xx.fbcdn.net/images/emoji.php/v9/tf9/1.5/32/2764.png");

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

        jScrollPane4.getVerticalScrollBar().setValue(jScrollPane4.getVerticalScrollBar().getMaximum());
        txtChatScreen.validate();
    }

    public void updateUIOnlineStatus(String status) {
        switch (status) {
            case "ONLINE":
                labelStatus.setText("Online");
                labelStatus.setForeground(Color.GREEN);
                break;
            case "OFFLINE":
                labelStatus.setText("Offline");
                labelStatus.setForeground(Color.RED);
                break;
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
        btnSend = new javax.swing.JButton();
        txtMessage = new javax.swing.JTextField();
        labelStatus = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtChatScreen = new javax.swing.JEditorPane();
        emojiWow = new javax.swing.JLabel();
        emojiCool = new javax.swing.JLabel();
        emojiPacman = new javax.swing.JLabel();
        txtUser = new javax.swing.JLabel();
        emojiConfuse = new javax.swing.JLabel();
        emojiCheer = new javax.swing.JLabel();
        emojiHeart = new javax.swing.JLabel();
        emojiBigSmile = new javax.swing.JLabel();
        emojiReverseSmile = new javax.swing.JLabel();
        emojiSad = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelTheRestUserName.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        labelTheRestUserName.setText("@Name Of The Rest User");

        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        txtMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMessageKeyPressed(evt);
            }
        });

        labelStatus.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelStatus.setText("status: online or offline");

        btnBack.setText("<---");

        jScrollPane4.setViewportView(txtChatScreen);

        emojiWow.setIcon(new javax.swing.ImageIcon("C:\\Users\\LENOVO\\Desktop\\nhom 11 lap trinh mang\\java_chat_application\\Chat_Client_Group11\\wow.png")); // NOI18N
        emojiWow.setText("jLabel1");

        emojiCool.setIcon(new javax.swing.ImageIcon("C:\\Users\\LENOVO\\Desktop\\nhom 11 lap trinh mang\\java_chat_application\\Chat_Client_Group11\\cool.png")); // NOI18N
        emojiCool.setText("jLabel2");

        emojiPacman.setIcon(new javax.swing.ImageIcon("C:\\Users\\LENOVO\\Desktop\\nhom 11 lap trinh mang\\java_chat_application\\Chat_Client_Group11\\PACMAN.png")); // NOI18N

        txtUser.setText("jLabel4");

        emojiConfuse.setIcon(new javax.swing.ImageIcon("C:\\Users\\LENOVO\\Desktop\\nhom 11 lap trinh mang\\java_chat_application\\Chat_Client_Group11\\confuse.png")); // NOI18N

        emojiCheer.setIcon(new javax.swing.ImageIcon("C:\\Users\\LENOVO\\Desktop\\nhom 11 lap trinh mang\\java_chat_application\\Chat_Client_Group11\\cheer.png")); // NOI18N

        emojiHeart.setIcon(new javax.swing.ImageIcon("C:\\Users\\LENOVO\\Desktop\\nhom 11 lap trinh mang\\java_chat_application\\Chat_Client_Group11\\heart.png")); // NOI18N

        emojiBigSmile.setIcon(new javax.swing.ImageIcon("C:\\Users\\LENOVO\\Desktop\\nhom 11 lap trinh mang\\java_chat_application\\Chat_Client_Group11\\bigSmile.png")); // NOI18N

        emojiReverseSmile.setIcon(new javax.swing.ImageIcon("C:\\Users\\LENOVO\\Desktop\\nhom 11 lap trinh mang\\java_chat_application\\Chat_Client_Group11\\reverseSmile.png")); // NOI18N

        emojiSad.setIcon(new javax.swing.ImageIcon("C:\\Users\\LENOVO\\Desktop\\nhom 11 lap trinh mang\\java_chat_application\\Chat_Client_Group11\\sad.png")); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\LENOVO\\Desktop\\nhom 11 lap trinh mang\\java_chat_application\\Chat_Client_Group11\\penguin.png")); // NOI18N
        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSend)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(emojiSad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(emojiHeart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(emojiPacman, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(emojiWow, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emojiConfuse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(emojiBigSmile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(emojiCool, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(emojiCheer)
                    .addComponent(emojiReverseSmile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(btnBack)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(labelTheRestUserName)
                        .addGap(44, 44, 44)
                        .addComponent(labelStatus)
                        .addContainerGap(260, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtUser)
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelTheRestUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelStatus)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(btnBack)))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(emojiWow)
                            .addComponent(emojiCool, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(emojiPacman, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(emojiConfuse, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(emojiCheer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(emojiHeart, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emojiBigSmile, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(emojiReverseSmile, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addComponent(emojiSad, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 85, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSend))
                .addGap(21, 21, 21))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSendActionPerformed

    private void txtMessageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMessageKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
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
        }

    }//GEN-LAST:event_txtMessageKeyPressed

    private void sendMessage(String input) {
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
        clientProcess.getListMessagesInARoom().add(message);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel emojiBigSmile;
    private javax.swing.JLabel emojiCheer;
    private javax.swing.JLabel emojiConfuse;
    private javax.swing.JLabel emojiCool;
    private javax.swing.JLabel emojiHeart;
    private javax.swing.JLabel emojiPacman;
    private javax.swing.JLabel emojiReverseSmile;
    private javax.swing.JLabel emojiSad;
    private javax.swing.JLabel emojiWow;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel labelStatus;
    private javax.swing.JLabel labelTheRestUserName;
    private javax.swing.JEditorPane txtChatScreen;
    private javax.swing.JTextField txtMessage;
    private javax.swing.JLabel txtUser;
    // End of variables declaration//GEN-END:variables
}

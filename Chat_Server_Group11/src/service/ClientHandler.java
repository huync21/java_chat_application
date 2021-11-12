/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Model.Message;
import Model.Room;
import Model.User;
import Model.UserInARoom;
import databaseAcess.MessageDAO;
import databaseAcess.RoomDAO;
import databaseAcess.UserDAO;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LENOVO
 */
// Lớp này làm nhiệm vụ giao tiếp(nhận request, xử lý, thao tác với db và trả về dữ liệu) với một tiến trình ClientProcess bên máy khách
public class ClientHandler implements Runnable {

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private User user;
    private Room currentRoom;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.dis = new DataInputStream(socket.getInputStream());
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String header = dis.readUTF();
                // Check xem header mà ClientProcess gửi là chức năng gì để còn xử lý trả về dữ liệu cho phù hợp
                System.out.println(header);
                switch (header) {
                    case "sign up":
                        User userThatClientWantsToSignUp = (User) ois.readObject();
                        UserDAO userDAO = new UserDAO();
                        // Nếu không bị trùng tên đăng nhập thì đăng ký user đó(thêm user vào db) và trả về cho client
                        if (!userDAO.isExistedUser(userThatClientWantsToSignUp)) {
                            User responseUser = userDAO.signUp(userThatClientWantsToSignUp);
                            if (responseUser != null) {
                                ServerProcess.listClientHandler.add(this);
                                System.out.println("User " + responseUser.getUserName() + " has signed in");
                                this.user = responseUser;  //gán user trả về cho property user của ClientHandler
                                dos.writeUTF("Sign Up Successfully");
                                dos.flush();
                                oos.writeObject(responseUser);
                                oos.flush();
                            } else {
                                dos.writeUTF("Some thing wrong with database access");
                                dos.flush();
                                oos.writeObject(null);
                                oos.flush();
                            }
                        } else {// Nếu bị trùng thì gửi lại message và 1 object user với giá trị null
                            dos.writeUTF("This account name has already existed, please try another");
                            dos.flush();
                            oos.writeObject(null);
                            oos.flush();
                        }
                        break;
                    case "sign in":
                        User userThatContainsUserAndPassword = (User) ois.readObject();
                        User userToReturnToClient = new UserDAO().signIn(userThatContainsUserAndPassword);
                        if (userToReturnToClient != null) {
                            ServerProcess.listClientHandler.add(this);
                            
                            System.out.println("User " + userToReturnToClient.getUserName() + " has sign in");
                            this.user = userToReturnToClient; //gán user trả về cho property user của ClientHandler
                            dos.writeUTF("Sign In Successfully");
                            dos.flush();
                            oos.writeObject(userToReturnToClient);
                            oos.flush();
                        } else {
                            dos.writeUTF("Wrong user name or password");
                            dos.flush();
                            oos.writeObject(null);
                            oos.flush();
                        }
                        break;
                    case "get single chat rooms": // lấy ra tất cả các phòng chat đơn của 1 người dùng
                        int userId = dis.readInt();
                        ArrayList<Room> listSingleChatRooms = new RoomDAO().getSingleChatRooms(userId);
                        for (Room room : listSingleChatRooms) {
                            oos.writeObject(room);
                        }
                        oos.writeObject(null);
                        oos.flush();
                        break;
                    case "get group chat rooms": // lấy ra tất cả các phòng chat đơn của 1 người dùng
                        int userIdk = dis.readInt();
                        System.out.println("userIdk: " + userIdk);
                        ArrayList<Room> listGroupChatRooms = new RoomDAO().getGroupChatRooms(userIdk);
                        for (Room room : listGroupChatRooms) {
                            oos.writeObject(room);
                        }
                        oos.writeObject(null);
                        oos.flush();
                        break;
                    case "get all users": // lấy ra tất cả người dùng trừ người dùng yêu cầu
                        int exceptUser = dis.readInt();
                        ArrayList<User> allUsers = new UserDAO().getAllUsers(exceptUser);
                        for (User user : allUsers) {
                            oos.writeObject(user);
                        }
                        oos.writeObject(null);
                        oos.flush();
                        break;
                    case "set current room": // khi user click vào 1 phòng trong máy khách thì set current room trên ClientHandler trong server là phòng đó luôn để tí nữa phục vụ gửi message
                        Room cuRoom = (Room) ois.readObject();
                        this.currentRoom = cuRoom;
                        break;
                    case "get messages from database": // lấy ra tất cả message trong 1 phòng
                        Room roomToTakeMessagesFrom = (Room) ois.readObject();
                        this.currentRoom = roomToTakeMessagesFrom;

                        //gửi về list cho client:
                        ArrayList<Message> listMessages = new MessageDAO().getListMessages(currentRoom);
                        for (Message m : listMessages) {
                            oos.writeObject(m);
                        }
                        oos.writeObject(null);
                        oos.flush();
                        break;
                    case "send online status": // Cái này cho vui thôi, kiểu update cái trạng thái đăng nhập online offline của user còn lại ở trong phòng chat đơn
                        int onlineStatus = dis.readInt();
                        int roomId = dis.readInt();
                        int userID = dis.readInt();
                        System.out.println(roomId);
                        // Check xem thanh niên nào chung phòng với mình để gửi đi trạng thái online
                        for (ClientHandler c : ServerProcess.listClientHandler) {
                            if (c.getCurrentRoom() != null) {
                                synchronized (c) {
                                    if (c.getCurrentRoom().getId() == roomId && c.getUser().getId() != userID) {
                                        c.getDos().writeInt(1);
                                    }
                                }
                            }
                        }
                        break;
                    case "send message": // Gửi tin nhắn cho những người còn lại trong phòng chat và lưu tin nhắn vào database
                        Message message = (Message) ois.readObject();
                        
                        System.out.println("message: "+message.getTextContent());
                        for (ClientHandler c : ServerProcess.listClientHandler) { //Gửi cho những user khác trong phòng trên ứng dụng
                            if (c.getCurrentRoom() != null) {
                                synchronized (c) { // đồng bộ cái này tránh trường hợp có nhiều luồng tranh luồng gửi của c sẽ dẫn đến chữ bị loạn hết lên
                                    if (c.getCurrentRoom().getId() == currentRoom.getId()) {
                                        c.getOos().writeObject(message);
                                        c.getOos().flush();
                                    }
                                }
                            }
                        }
                        
                        //Lưu message vào database
                        new MessageDAO().saveMessage(message);
                        break;
                    case "creat room":
                        Room room = (Room) ois.readObject();
                        oos.writeObject(new RoomDAO().createRoom(room));
                    case "null object":
                        oos.writeObject(null);
                        break;
                    default:
                        break;

                }
            } catch (SocketException ex) {// Nếu socket bị đóng từ phía client thì sẽ nhảy vào đoạn code này
                System.out.println("User: " + user.getUserName() + " has left!");
                new UserDAO().setUserOffline(user); // Nếu vậy thì cập nhật tình trạng là offline cho user đó trong db 

                // Cập nhật tình trạng offline trên UI của các máy khách đang bấm vào màn hình chat với user này
                for (ClientHandler c : ServerProcess.listClientHandler) {
                    if (c.getCurrentRoom().getId() == this.currentRoom.getId()) {
                        try {
                            synchronized(c){
                                c.getDos().writeInt(0);
                            }
                        } catch (IOException ex1) {

                        }
                    }
                }

                closeEverything();//nếu socket bị đóng từ phía client thì remove cái clientHandler này và đóng các luồng đang mở bên phía clientHandler này
                break;
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    // xóa cái clientHandler trong list và đóng các luồng vào ra và đóng socket
    private void closeEverything() {
        ServerProcess.listClientHandler.remove(this);
        try {
            if (dos != null) {
                dos.close();
            }
            if (dis != null) {
                dis.close();
            }
            if (oos != null) {
                oos.close();
            }
            if (ois != null) {
                ois.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public DataInputStream getDis() {
        return dis;
    }

    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

}

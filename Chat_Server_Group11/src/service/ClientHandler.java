/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.FriendDAO;
import model.Message;
import model.Room;
import model.User;
import model.UserInARoom;
import dao.MessageDAO;
import dao.RoomDAO;
import dao.UserDAO;
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
import model.Friend;

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
                DataPackage dataPackage = (DataPackage) ois.readObject();
                int operation = dataPackage.getOperation();
                DataPackage responsePackage = new DataPackage();
                // Check xem header mà ClientProcess gửi là chức năng gì để còn xử lý trả về dữ liệu cho phù hợp
                switch (operation) {
                    case DataPackage.SIGNUP:
                        responsePackage.setOperation(DataPackage.SIGNUP);

                        User userThatClientWantsToSignUp = (User) dataPackage.getData();
                        UserDAO userDAO = new UserDAO();
                        // Nếu không bị trùng tên đăng nhập thì đăng ký user đó(thêm user vào db) và trả về cho client
                        if (!userDAO.isExistedUser(userThatClientWantsToSignUp)) {
                            User responseUser = userDAO.signUp(userThatClientWantsToSignUp);

                            if (responseUser != null) {
                                ServerProcess.listClientHandler.add(this);
                                System.out.println("User " + responseUser.getUserName() + " has signed in");
                                this.user = responseUser;  //gán user trả về cho property user của ClientHandler

                                responsePackage.setData(responseUser);
                                responsePackage.setStatusMessage("OK");
                            } else {
                                responsePackage.setData(new User());
                                responsePackage.setStatusMessage("Có lỗi truy cập database");
                            }
                        } else {// Nếu bị trùng thì gửi lại message và 1 object user với giá trị null
                            responsePackage.setData(new User());
                            responsePackage.setStatusMessage("Tài khoản này đã có người sử dụng, vui lòng sử dụng tên đăng nhập khác");
                        }
                        oos.writeObject(responsePackage);
                        oos.flush();
                        break;
                    case DataPackage.LOGIN:
                        responsePackage.setOperation(DataPackage.LOGIN);
                        User userThatContainsUserAndPassword = (User) dataPackage.getData();
                        User userToReturnToClient = new UserDAO().signIn(userThatContainsUserAndPassword);
                        if (userToReturnToClient != null) {
                            ServerProcess.listClientHandler.add(this);

                            System.out.println("User " + userToReturnToClient.getUserName() + " đã đăng nhập");
                            this.user = userToReturnToClient; //gán user trả về cho property user của ClientHandler
                            responsePackage.setData(userToReturnToClient);
                            responsePackage.setStatusMessage("OK");
                        } else {
                            responsePackage.setData(new User());
                            responsePackage.setStatusMessage("Nhập sai tên đăng nhập hoặc mật khẩu");
                        }
                        oos.writeObject(responsePackage);
                        oos.flush();
                        break;
                    case DataPackage.GET_SINGLE_CHAT_ROOMS: // lấy ra tất cả các phòng chat đơn của 1 người dùng
                        responsePackage.setOperation(DataPackage.GET_SINGLE_CHAT_ROOMS);
                        int userID = (Integer) dataPackage.getData();
                        ArrayList<Room> listSingleChatRooms = new RoomDAO().getSingleChatRooms(userID);
                        responsePackage.setData(listSingleChatRooms);
                        responsePackage.setStatusMessage("OK");
                        oos.writeObject(responsePackage);
                        oos.flush();
                        break;
                    case DataPackage.GET_ALL_USERS: // lấy ra tất cả người dùng trừ người dùng yêu cầu
                        int exceptUser = (Integer) dataPackage.getData();
                        ArrayList<User> allUsers = new UserDAO().getAllUsers(exceptUser);
                        oos.writeObject(new DataPackage(DataPackage.GET_ALL_USERS, allUsers));
                        oos.flush();
                        break;
                    case DataPackage.SET_CURRENT_ROOM: // khi user click vào 1 phòng trong máy khách thì set current room trên ClientHandler trong server là phòng đó luôn để tí nữa phục vụ gửi message
                        Room cuRoom = (Room) dataPackage.getData();
                        this.currentRoom = cuRoom;
                        break;
                    case DataPackage.GET_MESSAGES_FROM_DB: // lấy ra tất cả message trong 1 phòng
                        responsePackage.setOperation(DataPackage.GET_MESSAGES_FROM_DB);
                        Room roomToTakeMessagesFrom = (Room) dataPackage.getData();
                        this.currentRoom = roomToTakeMessagesFrom;

                        ArrayList<Message> listMessages = new MessageDAO().getListMessages(currentRoom);
                        responsePackage.setData(listMessages);
                        responsePackage.setStatusMessage("OK");
                        oos.writeObject(responsePackage);
                        oos.flush();
                        break;
//                    case "send online status": // Cái này cho vui thôi, kiểu update cái trạng thái đăng nhập online offline của user còn lại ở trong phòng chat đơn
//                        int onlineStatus = dis.readInt();
//                        int roomId = dis.readInt();
//                        int userID = dis.readInt();
//                        System.out.println(roomId);
//                        // Check xem thanh niên nào chung phòng với mình để gửi đi trạng thái online
//                        for (ClientHandler c : ServerProcess.listClientHandler) {
//                            if (c.getCurrentRoom() != null) {
//                                synchronized (c) {
//                                    if (c.getCurrentRoom().getId() == roomId && c.getUser().getId() != userID) {
//                                        c.getDos().writeInt(1);
//                                    }
//                                }
//                            }
//                        }
//                        break;
                    case DataPackage.SEND_MESSAGE: // Gửi tin nhắn cho những người còn lại trong phòng chat và lưu tin nhắn vào database
                        Message message = (Message) dataPackage.getData();
                        responsePackage.setOperation(DataPackage.RECEIVE_MESSAGE);
                        responsePackage.setData(message);
                        responsePackage.setStatusMessage("OK");
                        for (ClientHandler c : ServerProcess.listClientHandler) { //Gửi cho những user khác trong phòng trên ứng dụng
                            if (c.getCurrentRoom() != null) {
                                synchronized (c) { // đồng bộ cái này tránh trường hợp có nhiều luồng tranh luồng gửi của c sẽ dẫn đến chữ bị loạn hết lên
                                    if (c.getCurrentRoom().getId() == currentRoom.getId()) { // dùng các luồng client handler tương ứng để gửi cho những người còn lại trong phòng
                                        c.getOos().writeObject(responsePackage);
                                        c.getOos().flush();
                                    }
                                }
                            }
                        }

                        //Lưu message vào database
                        new MessageDAO().saveMessage(message);

                        break;
//                    case "get group chat rooms": // lấy ra tất cả các phòng chat đơn của 1 người dùng
//                        int userid = dis.readInt();
//                        ArrayList<Room> listGroupChatRooms = new RoomDAO().getGroupChatRooms(userid);
//                        for (Room room : listGroupChatRooms) {
//                            oos.writeObject(room);
//                        }
//                        oos.writeObject(null);
//                        oos.flush();
//                        break;
//                    case "null object":
//                        oos.writeObject(null);
//                        break;
                    case DataPackage.SEND_ONLINE_STATUS_BROADCAST:
                        User onlineUser = (User) dataPackage.getData();
                        this.user = onlineUser;
                        responsePackage = new DataPackage(DataPackage.RECEIVE_ONLINE_STATUS_BROADCAST, onlineUser);
                        responsePackage.setStatusMessage("ONLINE");
                        for (ClientHandler ch : ServerProcess.listClientHandler) {
                            synchronized (ch) {
                                ch.getOos().writeObject(responsePackage);
                            }
                        }
                        break;
                    case DataPackage.CREATE_ROOM:
                        Room room = (Room)dataPackage.getData();
                        room = new RoomDAO().createRoom(room);
                        responsePackage.setOperation(DataPackage.CREATE_ROOM);
                        responsePackage.setData(room);
                        responsePackage.setStatusMessage("OK");
                        oos.writeObject(responsePackage);
                        oos.flush();
                        break;
                    case DataPackage.GET_EXISTED_ROOM:
                        ArrayList<User> listUser = (ArrayList<User>) dataPackage.getData();
                        User user1 = listUser.get(0);
                        User user2 = listUser.get(1);
                        Room existedSingleChatRoom = new RoomDAO().getExistedSingleChatRoom(user1,user2);
                        responsePackage.setOperation(DataPackage.GET_EXISTED_ROOM);
                        responsePackage.setData(existedSingleChatRoom);
                        oos.writeObject(responsePackage);
                        break;
                    case DataPackage.GET_USER_BY_NAME:
                        String keyWord = (String) dataPackage.getData();
                        ArrayList<User> listUsersByName = new UserDAO().getUsersByName(keyWord,this.getUser());
                        responsePackage.setData(listUsersByName);
                        responsePackage.setOperation(DataPackage.GET_USER_BY_NAME);
                        oos.writeObject(responsePackage);
                        break;
                        
//                  Modul Friend   
                    case DataPackage.GeT_LIST_FRIEND:   // Lấy tất cả thông tin friend và request friend của 1 user
                        User user = (User) dataPackage.getData();
                        ArrayList<Friend> listFriend = new FriendDAO().getAllInfoFriend(user);
                        responsePackage.setData(listFriend);
                        responsePackage.setOperation(DataPackage.GeT_LIST_FRIEND);
                        oos.writeObject(responsePackage);
                    case DataPackage.DELETE_FRIEND:
                        Friend friend = (Friend) dataPackage.getData();
                        new FriendDAO().deleteFriend(friend);
                        responsePackage.setOperation(DataPackage.DELETE_FRIEND);
                        responsePackage.setStatusMessage("OK");
                        oos.writeObject(responsePackage);
                        oos.flush();
                    default:
                        break;
//
                }

            } catch (SocketException ex) {// Nếu socket bị đóng từ phía client thì sẽ nhảy vào đoạn code này
                System.out.println("User: " + user.getUserName() + " đã thoát!");
                new UserDAO().setUserOffline(user); // Nếu vậy thì cập nhật tình trạng là offline cho user đó trong db 

                // Cập nhật tình trạng offline trên UI của các máy khách đang bấm vào màn hình chat với user này
                for (ClientHandler c : ServerProcess.listClientHandler) {
                    DataPackage responsePackage = new DataPackage(DataPackage.RECEIVE_ONLINE_STATUS_BROADCAST, this.user);
                    responsePackage.setStatusMessage("OFFLINE");
                    synchronized (c) {
                        try {
                            c.getOos().writeObject(responsePackage);
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

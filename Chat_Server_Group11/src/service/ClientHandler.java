/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import Model.Room;
import Model.User;
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

/**
 *
 * @author LENOVO
 */
// Lớp này làm nhiệm vụ giao tiếp(nhận request, xử lý, thao tác với db và trả về dữ liệu) với một tiến trình ClientProcess bên máy khách
class ClientHandler implements Runnable {

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
                switch (header) {
                    case "sign up":
                        User userThatClientWantsToSignUp = (User) ois.readObject();
                        UserDAO userDAO = new UserDAO();
                        // Nếu không bị trùng tên đăng nhập thì đăng ký user đó(thêm user vào db) và trả về cho client
                        if (!userDAO.isExistedUser(userThatClientWantsToSignUp)) {
                            User responseUser = userDAO.signUp(userThatClientWantsToSignUp);
                            if (responseUser != null) {
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
                        for(Room room : listSingleChatRooms){
                            oos.writeObject(room);
                        }
                        oos.writeObject(null);
                        oos.flush();
                        break;
                    case "get all users": // lấy ra tất cả người dùng trừ người dùng yêu cầu
                        int exceptUser = dis.readInt();
                        ArrayList<User> allUsers = new UserDAO().getAllUsers(exceptUser);
                        for(User user: allUsers){
                            oos.writeObject(user);
                        }
                        oos.writeObject(null);
                        oos.flush();
                        break;
                    case "set current room": // khi user click vào 1 phòng trong máy khách thì set current room trên ClientHandler trong server là phòng đó luôn để tí nữa phục vụ gửi message
                        Room cuRoom = (Room)ois.readObject();
                        this.currentRoom = cuRoom;
                        break;
                    default:
                        break;

                }
            } catch (SocketException ex) {// Nếu socket bị đóng từ phía client thì sẽ nhảy vào đoạn code này
                System.out.println("User: "+user.getUserName()+" has left!");
                new UserDAO().setUserOffline(user); // Nếu vậy thì cập nhật tình trạng là offline cho user đó trong db 
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
}

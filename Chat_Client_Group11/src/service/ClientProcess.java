package service;

import model.Message;
import model.User;
import model.Room;
import model.UserInARoom;
import views.SearchUserForSingleChatFrm;
import views.SignInFrm;
import views.SignUpFrm;
import views.SingleChatFrm;
import views.SingleChatRoomsFrm;
import views.groupChat.GroupChatFrm;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import model.Friend;
import views.friend.FriendFrm;

/**
 *
 * @author LENOVO
 */
public class ClientProcess {

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private User user;
    private Room currentRoom;
    private ArrayList<Message> listMessagesInARoom;
    private SingleChatFrm roomFrame;
    private GroupChatFrm groupRoomFrame;
    private Thread currentThread;
    private JFrame currentFrame;
    private Friend friend;

    public ClientProcess(Socket socket) {
        try {
            this.socket = socket;
            this.dos = new DataOutputStream(socket.getOutputStream());
            this.dis = new DataInputStream(socket.getInputStream());
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Thread nay de lang nghe cac goi tin tra ve tu server
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        DataPackage receivePackage = (DataPackage) ois.readObject();
                        int operation = receivePackage.getOperation();
                        switch (operation) {
                            case DataPackage.LOGIN:
                                if (currentFrame instanceof SignInFrm) {
                                    SignInFrm signInFrm = (SignInFrm) currentFrame;
                                    if (receivePackage.getStatusMessage().equals("OK")) {
                                        User loginUser = (User) receivePackage.getData();
                                        user = loginUser;// set cho property user cua client process user nay
                                        signInFrm.goToChatHomeFrm();
                                    } else {
                                        signInFrm.showMessage(receivePackage.getStatusMessage());
                                    }
                                }
                                break;
                            case DataPackage.SIGNUP:
                                if (currentFrame instanceof SignUpFrm) {
                                    SignUpFrm signUpFrm = (SignUpFrm) currentFrame;
                                    if (receivePackage.getStatusMessage().equals("OK")) {
                                        User signUpUser = (User) receivePackage.getData();
                                        user = signUpUser;
                                        signUpFrm.goToChatHomeFrm();
                                    } else {
                                        signUpFrm.showMessage(receivePackage.getStatusMessage());
                                    }
                                }

                                break;
                            case DataPackage.GET_SINGLE_CHAT_ROOMS:
                                //lay list room tu server roi goi giao dien hien thi

                                if (currentFrame instanceof SingleChatRoomsFrm) {
                                    SingleChatRoomsFrm singleChatRoomsFrm = (SingleChatRoomsFrm) currentFrame;
                                    ArrayList<Room> listSingleChatRooms = (ArrayList<Room>) receivePackage.getData();
                                    singleChatRoomsFrm.receiveRoomsAndDisplayData(listSingleChatRooms);
                                }
                                break;
                            case DataPackage.GET_MESSAGES_FROM_DB:
                                if (currentFrame instanceof SingleChatFrm) {
                                    SingleChatFrm singleChatFrm = (SingleChatFrm) currentFrame;
                                    listMessagesInARoom = (ArrayList<Message>) receivePackage.getData();
                                    for (Message message : listMessagesInARoom) {
                                        singleChatFrm.updateChatScreen(message);
                                    }
                                }
                                break;
                            case DataPackage.RECEIVE_MESSAGE:
                                if (currentFrame instanceof SingleChatFrm) {
                                    SingleChatFrm singleChatFrm = (SingleChatFrm) currentFrame;
                                    Message message = (Message) receivePackage.getData();
                                    listMessagesInARoom.add(message);
                                    singleChatFrm.updateChatScreen(message);
                                }
                                break;
                            case DataPackage.RECEIVE_ONLINE_STATUS_BROADCAST:
                                if (currentFrame instanceof SingleChatFrm) {
                                    SingleChatFrm singleChatFrm = (SingleChatFrm) currentFrame;
                                    User onlineUser = (User) receivePackage.getData();

                                    // Lấy ra user còn lại trong phòng so sánh xem có giống với cái user được gửi broadcast ko, nếu giống thì cập nhật ui thành màu xanh online hoặc đỏ offline 
                                    User theRestUserInTheRoom = new User();
                                    ArrayList<UserInARoom> listUserInCurrentRoom = currentRoom.getListUserInARoom();
                                    for (UserInARoom u : listUserInCurrentRoom) {
                                        if (u.getUser().getId() != user.getId()) {
                                            theRestUserInTheRoom = u.getUser();
                                        }
                                    }

                                    // so sanh, neu giong thi moi cap nhat trang thai online offline
                                    if (theRestUserInTheRoom.getId() == onlineUser.getId()) {
                                        String onlineStatus = receivePackage.getStatusMessage();
                                        singleChatFrm.updateUIOnlineStatus(onlineStatus);
                                    }
                                }
                                break;
                            case DataPackage.GET_ALL_USERS:
                                if (currentFrame instanceof SearchUserForSingleChatFrm) {
                                    SearchUserForSingleChatFrm searchUserForSingleChatFrm = (SearchUserForSingleChatFrm) currentFrame;
                                    ArrayList<User> listAllUsers = (ArrayList<User>) receivePackage.getData();
                                    searchUserForSingleChatFrm.receiveAllUsersAndDisplay(listAllUsers);
                                }
                                break;
                            // xem phòng chat đơn giữa 2 người đã tồn tại chưa, nếu chưa thì tạo mới, nếu rồi thì lấy về thông tin room luôn
                            case DataPackage.GET_EXISTED_ROOM:
                                if (currentFrame instanceof SearchUserForSingleChatFrm) {
                                    SearchUserForSingleChatFrm searchUserForSingleChatFrm = (SearchUserForSingleChatFrm) currentFrame;
                                    if (receivePackage.getData() == null) {
                                        searchUserForSingleChatFrm.createRoom();
                                    } else {
                                        Room receiveRoom = (Room) receivePackage.getData();
                                        searchUserForSingleChatFrm.receiveRoomAndGoChatInThatRoom(receiveRoom);
                                    }
                                }
                                break;
                            case DataPackage.CREATE_ROOM:
                                if (currentFrame instanceof SearchUserForSingleChatFrm) {
                                    SearchUserForSingleChatFrm searchUserForSingleChatFrm = (SearchUserForSingleChatFrm) currentFrame;
                                    Room receiveRoom = (Room) receivePackage.getData();
                                    searchUserForSingleChatFrm.receiveRoomAndGoChatInThatRoom(receiveRoom);
                                }
                                break;
                            case DataPackage.GET_USER_BY_NAME:
                                if (currentFrame instanceof SearchUserForSingleChatFrm) {
                                    SearchUserForSingleChatFrm searchUserForSingleChatFrm = (SearchUserForSingleChatFrm) currentFrame;
                                    ArrayList<User> listUsers = (ArrayList<User>) receivePackage.getData();
                                    searchUserForSingleChatFrm.receiveAllUsersAndDisplay(listUsers);
                                }
//                          Modol friend
                            case DataPackage.GeT_LIST_FRIEND:
                                if(currentFrame instanceof FriendFrm){
                                    FriendFrm friendFrm = (FriendFrm) currentFrame;
                                    ArrayList<Friend> listFriend = (ArrayList<Friend>) receivePackage.getData();
                                    friendFrm.receiveListFriend(listFriend);
                                }
                            
                                break;
                            default:
                                break;
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void signUp(User user) {
        try {
            oos.writeObject(new DataPackage(DataPackage.SIGNUP, user));
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void signIn(User user) {
        try {
            oos.writeObject(new DataPackage(DataPackage.LOGIN, user));
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void getSingleChatRooms(int userId) {
        try {
            oos.writeObject(new DataPackage(DataPackage.GET_SINGLE_CHAT_ROOMS, userId));
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //gui request lay ra tat ca user de chat don tru user nay
    public void getAllUsers() {
        try {
            oos.writeObject(new DataPackage(DataPackage.GET_ALL_USERS, user.getId()));
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // lấy tất cả tin nhắn trong phòng room chỉ định
    public void getMessagesFromDatabase(Room room) {
        try {
            oos.writeObject(new DataPackage(DataPackage.GET_MESSAGES_FROM_DB, room));
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMessage(Message message) { // Gửi tin nhắn
        try {
            oos.writeObject(new DataPackage(DataPackage.SEND_MESSAGE, message));
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void creatRoom(Room room) {
        try {
            oos.writeObject(new DataPackage(DataPackage.CREATE_ROOM, room));
            oos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public ArrayList<Room> getGroupChatRooms(int userId) {
        ArrayList<Room> listRooms = new ArrayList<>();

        try {
            //header
            dos.writeUTF("get group chat rooms");
            dos.flush();

            //body
            dos.writeInt(userId);
            dos.flush();

            //get back result
            Room r = new Room();
            while ((r = (Room) ois.readObject()) != null) {
                listRooms.add(r);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return listRooms;
    }

    public void sendOnlineStatus() {
        try {
            oos.writeObject(new DataPackage(DataPackage.SEND_ONLINE_STATUS_BROADCAST, user));
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ClientProcess() {
    }

    public SingleChatFrm getRoomFrame() {
        return roomFrame;
    }

    public void setRoomFrame(SingleChatFrm roomFrame) {
        this.roomFrame = roomFrame;
    }

    public void setgroupRoomFrame(GroupChatFrm roomFrame) {
        this.groupRoomFrame = roomFrame;
    }

    public ArrayList<Message> getListMessagesInARoom() {
        return listMessagesInARoom;
    }

    public void setListMessagesInARoom(ArrayList<Message> listMessagesInARoom) {
        this.listMessagesInARoom = listMessagesInARoom;
    }

    public Room getRoom() {
        return currentRoom;
    }

    public void setRoom(Room room) {
        this.currentRoom = room;
        try {
            oos.writeObject(new DataPackage(DataPackage.SET_CURRENT_ROOM, room));
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public JFrame getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(JFrame currentFrame) {
        this.currentFrame = currentFrame;
    }

    public void closeEverything() {

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

    public void getExistedSingleChatRoom(User user1, User user2) {
        Room result = null;
        try {
            ArrayList<User> listUser = new ArrayList<>();
            listUser.add(user1);
            listUser.add(user2);
            oos.writeObject(new DataPackage(DataPackage.GET_EXISTED_ROOM, listUser));
            oos.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void getUserByName(String keyWord) {
        try {
            oos.writeObject(new DataPackage(DataPackage.GET_USER_BY_NAME, keyWord));
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
//    Modul friend
    
    public void getListFriend(User user) { // Lấy tất cả thông tin friend và request friend của 1 user
        try {
            oos.writeObject(new DataPackage(DataPackage.GeT_LIST_FRIEND, this.user));
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void deleteFriend(Friend friend){
        try {
            oos.writeObject(new DataPackage(DataPackage.GeT_LIST_FRIEND, this.friend));
            oos.flush();
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
}

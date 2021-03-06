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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.Friend;
import views.friend.AddNewFriendDialog;
import views.friend.FriendFrm;
import views.friend.FriendRequestDialog;
import views.groupChat.AddFriendToGroupChat;
import views.groupChat.CreateNewGroupFrm;
import views.groupChat.GroupChatRoomsFrm;

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
    private JDialog currenDialog;
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
                                if(currentFrame instanceof GroupChatFrm){
                                    GroupChatFrm groupChatFrm = (GroupChatFrm) currentFrame;
                                    listMessagesInARoom = (ArrayList<Message>) receivePackage.getData();
                                     for (Message message : listMessagesInARoom) {
                                        groupChatFrm.updateChatScreen(message);
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
                                if(currentFrame instanceof GroupChatFrm){
                                    GroupChatFrm groupChatFrm =(GroupChatFrm) currentFrame;
                                    Message message = (Message) receivePackage.getData();
                                    listMessagesInARoom.add(message);
                                    groupChatFrm.updateChatScreen(message);
                                }
                                break;
                            case DataPackage.RECEIVE_ONLINE_STATUS_BROADCAST:
                                if (currentFrame instanceof SingleChatFrm) {
                                    SingleChatFrm singleChatFrm = (SingleChatFrm) currentFrame;
                                    User onlineUser = (User) receivePackage.getData();

                                    // L???y ra user c??n l???i trong ph??ng so s??nh xem c?? gi???ng v???i c??i user ???????c g???i broadcast ko, n???u gi???ng th?? c???p nh???t ui th??nh m??u xanh online ho???c ????? offline 
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
                                if(currentFrame instanceof CreateNewGroupFrm){
                                    CreateNewGroupFrm createNewGroupFrm = (CreateNewGroupFrm) currentFrame;
                                    ArrayList<User> listAllUsers = (ArrayList<User>) receivePackage.getData();
                                    createNewGroupFrm.receiveAllUsersAndDisplay(listAllUsers);
                                }
                                if(currentFrame instanceof AddFriendToGroupChat){
                                    AddFriendToGroupChat addFriendToGroupChat = (AddFriendToGroupChat) currentFrame;
                                    ArrayList<User> listAllUsers = (ArrayList<User>) receivePackage.getData();
                                    addFriendToGroupChat.receiveAllUsersAndDisplay(listAllUsers);
                                }
                                break;
                            // xem ph??ng chat ????n gi???a 2 ng?????i ???? t???n t???i ch??a, n???u ch??a th?? t???o m???i, n???u r???i th?? l???y v??? th??ng tin room lu??n
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
                                if (currentFrame instanceof FriendFrm) {
                                    FriendFrm friendFrm = (FriendFrm) currentFrame;
                                    if (receivePackage.getData() == null) {
                                        friendFrm.createRoom();
                                    } else {
                                        Room receiveRoom = (Room) receivePackage.getData();
                                        friendFrm.receiveRoomAndGoChatInThatRoom(receiveRoom);
                                    }
                                }
                                break;
                            case DataPackage.CREATE_ROOM:
                                if (currentFrame instanceof SearchUserForSingleChatFrm) {
                                    SearchUserForSingleChatFrm searchUserForSingleChatFrm = (SearchUserForSingleChatFrm) currentFrame;
                                    Room receiveRoom = (Room) receivePackage.getData();
                                    searchUserForSingleChatFrm.receiveRoomAndGoChatInThatRoom(receiveRoom);
                                }
                                if(currentFrame instanceof CreateNewGroupFrm){
                                    CreateNewGroupFrm createNewGroupFrm = (CreateNewGroupFrm) currentFrame;
                                    Room receiveRoom = (Room) receivePackage.getData();
                  
                                    createNewGroupFrm.receiveRoomAndGoChatInThatRoom(receiveRoom);
                                }
                                if (currentFrame instanceof FriendFrm) {
                                    FriendFrm friendFrm = (FriendFrm) currentFrame;
                                    Room receiveRoom = (Room) receivePackage.getData();
                                    friendFrm.receiveRoomAndGoChatInThatRoom(receiveRoom);
                                }
                                break;
                            case DataPackage.GET_USER_BY_NAME:
                                if (currentFrame instanceof SearchUserForSingleChatFrm) {
                                    SearchUserForSingleChatFrm searchUserForSingleChatFrm = (SearchUserForSingleChatFrm) currentFrame;
                                    ArrayList<User> listUsers = (ArrayList<User>) receivePackage.getData();
                                    searchUserForSingleChatFrm.receiveAllUsersAndDisplay(listUsers);
                                }
                                if(currenDialog instanceof AddNewFriendDialog){
                                    AddNewFriendDialog AddFrDia = (AddNewFriendDialog) currenDialog;
                                    ArrayList<User> listUsers = (ArrayList<User>) receivePackage.getData();
                                    AddFrDia.receiveAllUsersAndDisplay(listUsers);
                                }
                                break;
                            case DataPackage.GET_GROUP_CHAT_ROOMS: 
                                  if (currentFrame instanceof GroupChatRoomsFrm) {
                                    GroupChatRoomsFrm groupChatRoomsFrm = (GroupChatRoomsFrm) currentFrame;
                                    ArrayList<Room> listGroupChatRooms = (ArrayList<Room>) receivePackage.getData();
                                    groupChatRoomsFrm.receiveRoomsAndDisplayData(listGroupChatRooms);
                                }
                                  break;
                            // Modol friend
                            case DataPackage.GeT_LIST_FRIEND:
                                if(currentFrame instanceof FriendFrm){
                                    FriendFrm friendFrm = (FriendFrm) currentFrame;
                                    ArrayList<Friend> listFriend = (ArrayList<Friend>) receivePackage.getData();
                                    friendFrm.receiveListFriend(listFriend);
                                }
                                break;
                            case DataPackage.DELETE_FRIEND:
                                if(currentFrame instanceof FriendFrm){
                                    FriendFrm friendFrm = (FriendFrm) currentFrame;
                                    friendFrm.showMessage(receivePackage.getStatusMessage());
                                }
                                break;
                            case DataPackage.ACCEPT_FRIEND:
                                if(currenDialog instanceof FriendRequestDialog){
                                    FriendRequestDialog frDia = (FriendRequestDialog) currenDialog;
                                    frDia.showMessage(receivePackage.getStatusMessage());
                                }
                                break;
                            case DataPackage.REFUSE_FRIEND:
                                if(currenDialog instanceof FriendRequestDialog){
                                    FriendRequestDialog frDia = (FriendRequestDialog) currenDialog;
                                    frDia.showMessage(receivePackage.getStatusMessage());
                                }
                                break;
                            case DataPackage.LIST_USER_ADD_FRIEND:  // danh s??ch user ????? g???i k???t b???n
                                if(currenDialog instanceof AddNewFriendDialog){
                                    AddNewFriendDialog AddFrDia = (AddNewFriendDialog) currenDialog;
                                    ArrayList<User> allUser = (ArrayList<User>) receivePackage.getData();
                                    AddFrDia.receiveAllUsersAndDisplay(allUser);
                                }
                                break;    
                            case DataPackage.SEND_REQUEST_FRIEND:
                                if(currenDialog instanceof AddNewFriendDialog){
                                    AddNewFriendDialog frDia = (AddNewFriendDialog) currenDialog;
                                    frDia.showMessage(receivePackage.getStatusMessage());
                                }
                                break; 
                            case DataPackage.ADD_USER_TO_GROUP_CHAT:
                                if(currentFrame instanceof AddFriendToGroupChat){
                                    AddFriendToGroupChat addFrindToGroupFrm = (AddFriendToGroupChat) currentFrame;
                                    String response_st =  receivePackage.getStatusMessage();
                                    if(response_st.equals("Ok")){
                                        addFrindToGroupFrm.backFrGroupChat((Room) receivePackage.getData());
                                    }
                                    else{
                                        JOptionPane.showMessageDialog(addFrindToGroupFrm, "Kh??ng th??? th??m");
                                    }
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
        public void getGroupChatRooms(int userId) {
        try {
            oos.writeObject(new DataPackage(DataPackage.GET_GROUP_CHAT_ROOMS, userId));
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

    // l???y t???t c??? tin nh???n trong ph??ng room ch??? ?????nh
    public void getMessagesFromDatabase(Room room) {
        try {
            oos.writeObject(new DataPackage(DataPackage.GET_MESSAGES_FROM_DB, room));
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMessage(Message message) { // G???i tin nh???n
        try {
            oos.writeObject(new DataPackage(DataPackage.SEND_MESSAGE, message));
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public boolean creatRoom(Room room) {
        try {
            oos.writeObject(new DataPackage(DataPackage.CREATE_ROOM, room));
            oos.flush();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
           return false;
        }

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

    public JDialog getCurrenDialog() {
        return currenDialog;
    }

    public void setCurrenDialog(JDialog currenDialog) {
        this.currenDialog = currenDialog;
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
    
    public void getListFriend(User user) { // L???y t???t c??? th??ng tin friend v?? request friend c???a 1 user
        try {
            oos.writeObject(new DataPackage(DataPackage.GeT_LIST_FRIEND, user));
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void deleteFriend(Friend friend){
        try {
            oos.writeObject(new DataPackage(DataPackage.DELETE_FRIEND, friend));
            oos.flush();
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
    
    public void acceptFriend(Friend friend){
        try {
            oos.writeObject(new DataPackage(DataPackage.ACCEPT_FRIEND, friend));
            oos.flush();
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
    
    public void refuseFriend(Friend friend){
        try {
            oos.writeObject(new DataPackage(DataPackage.REFUSE_FRIEND, friend));
            oos.flush();
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
    public void getListUserAddFriend() {
        try {
            oos.writeObject(new DataPackage(DataPackage.LIST_USER_ADD_FRIEND, user.getId()));
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void sendRequestFriend(Friend friend){
        try {
            oos.writeObject(new DataPackage(DataPackage.SEND_REQUEST_FRIEND, friend));
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addUserToGroupChat(UserInARoom us){
        try {
            oos.writeObject(new DataPackage(DataPackage.ADD_USER_TO_GROUP_CHAT, us));
            oos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

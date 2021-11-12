package Service;

import Model.Message;
import Model.User;
import Model.Room;
import UI.SingleChatFrm;
import UI.groupChat.GroupChatFrm;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

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
    private boolean isStop ;
    
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
    }
    
    
    public void outRoom(){
        isStop = true;
    }
    
    
    
    public String signUp(User user) {
        String headerResponse = "Sign Up Failed";
        try {
            // Gửi đi request đăng ký
            // Gửi header cho biết đây là chức năng đăng ký
            dos.writeUTF("sign up");
            dos.flush();
            // Gửi đối tượng user để server xử lý đăng ký
            oos.writeObject(user);
            oos.flush();

            // Nhận lại bản tin:
            // Nhận lại header:
            headerResponse = dis.readUTF();
            // Nhận lại đối tượng user đã đăng ký:
            User signedUpUser = (User) ois.readObject();

            if (signedUpUser != null) {
                this.user = signedUpUser;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return headerResponse;

    }

    public String signIn(User user) {
        String headerResponse = "Sign In Failed";
        try {
            // Gửi đi request đăng ký
            // Gửi header cho biết đây là chức năng đăng nhập
            dos.writeUTF("sign in");
            dos.flush();
            // Gửi đối tượng user để server xử lý đăng nhập
            oos.writeObject(user);
            oos.flush();

            // Nhận lại bản tin:
            // Nhận lại header:
            headerResponse = dis.readUTF();
            // Nhận lại đối tượng returnUser đã đăng nhập thành công:
            User returnUser = (User) ois.readObject();

            // Set đối tượng returnUser trả về cho property user của tiến trình ClientProcess
            if (returnUser != null) {
                this.user = returnUser;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return headerResponse;

    }


    public ArrayList<User> getAllUsers(int exceptThisUserId) {
        ArrayList<User> listUser = new ArrayList<>();
        try {
            //header
            dos.writeUTF("get all users");

            //body: loai tru chinh user nay ra
            dos.writeInt(user.getId());
            dos.flush();

            //get back result
            User u = new User();
            while ((u = (User) ois.readObject()) != null) {
                listUser.add(u);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return listUser;
    }

    // lấy tất cả tin nhắn trong phòng room chỉ định
    public ArrayList<Message> getMessagesFromDatabase(Room room) {
        ArrayList<Message> result = new ArrayList<>();
        try {
            //header
            dos.writeUTF("get messages from database");
            dos.flush();

            //body
            oos.writeObject(room);
            oos.flush();

            //nhận về message
            Message message = null;
            while ((message = (Message) ois.readObject()) != null) {
                result.add(message);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    

    
    public ArrayList<Room> getSingleChatRooms(int userId) {
        ArrayList<Room> listRooms = new ArrayList<>();

        try {
            //header
            dos.writeUTF("get single chat rooms");
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
    
    public Room creatRoom(Room room) throws ClassNotFoundException {
        ArrayList<Room> listRooms = new ArrayList<>();
        Room result = null;
        try {
            //header
            dos.writeUTF("creat room");
            dos.flush();
                
           
            //body
            oos.writeObject(room);
            dos.flush();

            //get back result
            result = (Room) ois.readObject();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
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
    
    public void sendMessage(Message message) { // Gửi tin nhắn
        try {
            //header
            dos.writeUTF("send message");
            dos.flush();

            //body
            oos.writeObject(message);

            oos.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    
    public void listenToMessage() {// Lắng nghe tin nhắn mới đến
        
        this.currentThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop) {
                    try {
                        Message m = (Message) ois.readObject();
                        listMessagesInARoom.add(m);
                        
                        // Gọi UI phòng chat cập nhật màn hình chat có thêm message mới vừa nhận về
                        if(roomFrame != null) roomFrame.updateChatScreen(m);
                        else{
                            groupRoomFrame.updateChatScreen(m);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        break;
                    }
                }
            }
        });
        this.currentThread.start();
    }
    

    public void sendOnlineStatus(int onlineStatus, int roomId, int userId) {
        try {
            dos.writeUTF("send online status");
            dos.writeInt(onlineStatus);
            dos.writeInt(roomId);
            dos.writeInt(userId);
            dos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void listenOnlineStatus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        int onlineStatus = dis.readInt();
                        if (roomFrame != null) {
                            roomFrame.updateOnlineStatus(onlineStatus);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        }).start();
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
            // set current room trên ClientHandler trên server luôn
            //header
            dos.writeUTF("set current room");
            dos.flush();
            oos.writeObject(currentRoom);
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public User getUser() {
        return user;
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

}

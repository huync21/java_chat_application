package Service;

import Model.User;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            User signedUpUser = (User)ois.readObject();
            
            if(signedUpUser!=null){
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
            User returnUser = (User)ois.readObject();
            
            // Set đối tượng returnUser trả về cho property user của tiến trình ClientProcess
            if(returnUser!=null){
                this.user = returnUser;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return headerResponse;
       
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

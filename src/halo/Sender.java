package halo;

import java.io.IOException;
import java.net.Socket;

/**
 * send message to other users
 * @author Phan Hieu
 */
public class Sender extends Thread{
    private String ip;
    private int port;
    private Socket socket;
    
    private String message;

    public void setMessage(String message) {
        this.message = Halo.user.getUserName()+":"+message;
    }
    
    public Sender(){}

    public Sender(String ip, int port){
        this.ip = ip;
        this.port = port;
    }
    
    public Sender(String ip, int port, String message) {
        this.ip = ip;
        this.port = port;
        setMessage(message);
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(ip, port);
            socket.getOutputStream().write(message.getBytes());
            socket.close();
        } catch (IOException ex) {
            System.out.println("Không thể gửi tin.");
        }
    }
    
    
}

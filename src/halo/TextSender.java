package halo;

import halo.models.Packet;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * send message to other users
 * @author Phan Hieu
 */
public class TextSender extends Thread{
    private String ip;
    private int port;
    
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public TextSender(String ip, int port){
        this.ip = ip;
        this.port = port;
    }
    
    public TextSender(String ip, int port, String message) {
        this.ip = ip;
        this.port = port;
        setMessage(message);
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(ip, port);
            DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
            dout.write(Packet.CreateDataPacket(Halo.user.getUserName(), Packet.COMMAND_SEND_TEXT, message.getBytes("UTF8")));
            dout.flush();
        } catch (IOException ex) {
            System.out.println("Không thể gửi tin.");
        }
    }
    
    
}

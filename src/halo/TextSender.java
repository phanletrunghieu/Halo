package halo;

import halo.models.Packet;
import halo.models.User;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * send message to other users
 * @author Phan Hieu
 */
public class TextSender extends Thread{
    private User toUser;
    private String message;
    
    public TextSender(User toUser, String message) {
        this.toUser=toUser;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(toUser.getAddrListening(), toUser.getPortListening());
            DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
            dout.write(Packet.CreateDataPacket(Halo.user.getUserName(), Packet.COMMAND_SEND_TEXT, message.getBytes("UTF8"), toUser.getPublicKeyN(), toUser.getPublicKeyE()));
            dout.flush();
        } catch (IOException ex) {
            System.out.println("Không thể gửi tin.");
        }
    }
    
    
}

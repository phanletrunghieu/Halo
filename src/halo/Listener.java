package halo;

import halo.file.FileReceiver;
import halo.models.Packet;
import halo.models.User;
import halo.ui.ChatForm;
import halo.ui.call.ReceiveCallForm;
import halo.voice.VoiceServer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * accept connect request from other users
 *
 * @author Phan Hieu
 */
public class Listener extends Thread {

    private static ArrayList<ChatForm> usersChatting = new ArrayList<>();

    public static void addUserChatting(ChatForm chatForm) {
        usersChatting.add(chatForm);
    }

    public static void removeUserChatting(ChatForm chatForm) {
        usersChatting.remove(chatForm);
    }

    private InetAddress inetAddress;

    private ServerSocket serverSocket;
    private int port;

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Listener() {
        try {
            inetAddress = Setting.GetIP();

            serverSocket = new ServerSocket(0, 100, inetAddress);
            port = serverSocket.getLocalPort();
        } catch (IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Listener(InetAddress inetAddress) {
        try {
            this.inetAddress = inetAddress;

            serverSocket = new ServerSocket(0, 100, inetAddress);
            System.out.println(serverSocket.getInetAddress().getHostAddress());
            port = serverSocket.getLocalPort();
        } catch (IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        Socket clientSocket;
        try {
            while ((clientSocket = serverSocket.accept()) != null) {
                DataInputStream din = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dout = new DataOutputStream(clientSocket.getOutputStream());

                if (din.readByte() == Packet.INITITALIZE) {
                    int b = 0;
                    String fromUsername = "";
                    while ((b = din.read()) != Packet.SEPARATOR) {
                        fromUsername += (char) b;
                    }

                    byte[] cmd_buff = new byte[3];
                    din.read(cmd_buff, 0, cmd_buff.length);

                    byte[] data = Packet.ReadStream(din);

                    switch (new String(cmd_buff)) {
                        case Packet.COMMAND_SEND_TEXT:
                            try {
                                ChatForm chatForm = ShowChatForm(fromUsername);
                                chatForm.receiveNewMessage(new String(data));
                            } catch (NullPointerException ex) {
                                System.out.println("Message is empty");
                            }
                            break;
                        case Packet.COMMAND_SEND_FILE:
                            ChatForm chatForm = ShowChatForm(fromUsername);
                            chatForm.receiveNewMessage("Nhận file: " + new String(data));
                            new FileReceiver(clientSocket).start();
                            break;
                        case Packet.COMMAND_REQUEST_CALL:
                            if (Halo.isCalling) {
                                dout.write(Packet.CreateDataPacket(Halo.user.getUserName(), Packet.COMMAND_USER_BUSY, "I'm busy".getBytes("UTF8")));
                            } else {
                                Halo.isCalling = true;
                                ReceiveCallForm receiveCallForm = new ReceiveCallForm(new User(fromUsername), clientSocket);
                                receiveCallForm.setVisible(true);
                                new VoiceServer(clientSocket, receiveCallForm).start();
                            }
                            break;
                    }
                }
            }
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ChatForm ShowChatForm(String username) throws SQLException {
        int vt = -1;
        for (int i = 0; i < usersChatting.size(); i++) {
            ChatForm chatForm = usersChatting.get(i);
            if (chatForm.getUser().getUserName().equals(username)) {
                vt = i;
            }
        }

        ChatForm chatForm;
        if (vt == -1) {
            chatForm = new ChatForm(new User(username));
            usersChatting.add(chatForm);
        } else {
            chatForm = usersChatting.get(vt);
        }
        chatForm.setVisible(true);
        return chatForm;
    }
}

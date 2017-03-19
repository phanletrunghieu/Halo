package halo;

import halo.models.User;
import halo.ui.ChatForm;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * accept connect request from others
 * @author Phan Hieu
 */
public class Listener extends Thread {
    
    private static ArrayList<ChatForm> usersChatting=new ArrayList<>();
    public static void addUserChatting(ChatForm chatForm){
        usersChatting.add(chatForm);
    }
    public static void removeUserChatting(ChatForm chatForm){
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
            ArrayList<InetAddress> iPs = getIPs();
            inetAddress=iPs.get(0);
            
            serverSocket=new ServerSocket(0, 100, inetAddress);
            port=serverSocket.getLocalPort();
        } catch (IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Listener(InetAddress inetAddress){
        try {
            this.inetAddress=inetAddress;
            
            serverSocket=new ServerSocket(0, 100, inetAddress);
            System.out.println(serverSocket.getInetAddress().getHostAddress());
            port=serverSocket.getLocalPort();
        } catch (IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private ArrayList<InetAddress> getIPs() throws SocketException{
        ArrayList<InetAddress> ips = new ArrayList<>();
        
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = e.nextElement();
            Enumeration<InetAddress> ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = ee.nextElement();

                if (!i.isLoopbackAddress() && i instanceof Inet4Address) {
                    ips.add(i);
                }
            }
        }
        return ips;
    }

    @Override
    public void run() {
        Socket clientSocket;
        try {
            while ((clientSocket=serverSocket.accept())!=null) {
                InputStream is=clientSocket.getInputStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                String line=br.readLine();
                
                if(line!=null){
                    String[] data = line.split(":");
                    System.out.println(data[0]+"zzz"+data[1]);
                    
                    int vt=-1;
                    for (int i = 0; i < usersChatting.size(); i++) {
                        ChatForm chatForm = usersChatting.get(i);
                        if (chatForm.getUser().getUserName().equals(data[0])) {
                            vt=i;
                        }
                    }
                    
                    ChatForm chatForm;
                    if(vt==-1){
                        chatForm=new ChatForm(new User(data[0]));
                        usersChatting.add(chatForm);
                    } else {
                        chatForm=usersChatting.get(vt);
                    }
                    chatForm.setVisible(true);
                    chatForm.receiveNewMessage(data[1]);
                }
                //String ip=(((InetSocketAddress) clientSocket.getRemoteSocketAddress()).getAddress()).toString().replace("/","");
            }
        } catch (IOException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}
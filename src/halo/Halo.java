package halo;

import halo.models.User;
import halo.ui.LoginForm;

/**
 *
 * @author Phan Hieu
 */
public class Halo {

    public static User user;
    public static boolean isCalling=false;
    
    public Halo() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        new LoginForm().setVisible(true);
        
        /*Scanner scanner=new Scanner(System.in);
        
        System.out.print("username: ");
        String username=scanner.nextLine();
        
        System.out.print("pass: ");
        String password=scanner.nextLine();
        
        try {
            user = new User(username);
            if(user.getHashPassword().equals(MD5Encode(password))){
                Listener listener=new Listener();
                listener.start();
                user.setAddrListening(listener.getInetAddress().getHostAddress());
                user.setPortListening(listener.getPort());
                
                new FriendList(user).setVisible(true);
                
                System.out.print("Chat to: ");
                String chatTo = scanner.nextLine();
                User other=new User(chatTo);
                new ChatForm(other).setVisible(true);
                
            } else {
                System.out.println("Login fail.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Halo.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
}

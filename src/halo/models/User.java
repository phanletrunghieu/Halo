package halo.models;

import halo.dataaccess.SQLDatabaseConnection;
import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Phan Hiếu
 */
public class User {

    private static final String tableName = "users";
    private SQLDatabaseConnection connection;

    private String userName;
    private String hashPassword;

    private byte[] avatar;

    private String addrListening = "";
    private int portListening = -1;

    private String status;
    private boolean isOnline;

    public User() {
    }

    public User(String userName) throws SQLException {
        this.userName = userName;
        connection = new SQLDatabaseConnection();
        refreshData();
    }

    public User(String userName, String hashPassword, byte[] avatar, String status, String addrListening, int portListening) throws SQLException {
        this.userName = userName;
        this.hashPassword = hashPassword;
        this.avatar = avatar;
        this.status = status;
        this.addrListening = addrListening;
        this.portListening = portListening;
        connection = new SQLDatabaseConnection();
    }

    @Override
    protected void finalize() {
        try {
            setAddrListening(null);
            setPortListening(0);
        } catch (SQLException ex) {
            System.out.println("Destroyed.");
        }
    }

    public void refreshData() throws SQLException {
        ResultSet resultSet = connection.Select(tableName, "username='" + userName + "'");
        resultSet.next();

        this.userName = resultSet.getString("username");
        this.hashPassword = resultSet.getString("password");
        this.avatar = resultSet.getBytes("avatar");
        this.addrListening = resultSet.getString("ip");
        this.portListening = resultSet.getInt("port");
        this.status = resultSet.getString("status");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) throws SQLException {
        this.userName = userName.trim();

        Map<String, String> data = new HashMap<>();
        data.put("username", userName);
        connection.Update(tableName, "username='" + userName + "'", data);
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) throws SQLException {
        this.hashPassword = hashPassword.trim();

        Map<String, String> data = new HashMap<>();
        data.put("password", hashPassword);
        connection.Update(tableName, "username='" + userName + "'", data);
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) throws SQLException {
        this.avatar = avatar;
        connection.Update(tableName, "username='" + userName + "'", "avatar", new ByteArrayInputStream(this.avatar));
    }
    
    public void deleteAvatar() throws SQLException{
        Map<String, String> data = new HashMap<>();
        data.put("avatar", null);
        connection.Update(tableName, "username='" + userName + "'", data);
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) throws SQLException {
        this.status = status.trim();

        Map<String, String> data = new HashMap<>();
        data.put("status", status);
        connection.Update(tableName, "username='" + userName + "'", data);
    }

    public String getAddrListening() {
        return addrListening;
    }

    public void setAddrListening(String addrListening) throws SQLException {
        this.addrListening = addrListening.trim();

        Map<String, String> data = new HashMap<>();
        data.put("ip", addrListening);
        connection.Update(tableName, "username='" + userName + "'", data);
    }

    public int getPortListening() {
        return portListening;
    }

    public void setPortListening(int portListening) throws SQLException {
        this.portListening = portListening;

        Map<String, String> data = new HashMap<>();
        data.put("port", String.valueOf(portListening));
        connection.Update(tableName, "username='" + userName + "'", data);
    }

    public boolean isIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) throws SQLException {
        this.isOnline = isOnline;

        Map<String, String> data = new HashMap<>();
        if (isOnline) {
            data.put("isOnline", "1");
        } else {
            data.put("isOnline", "0");
        }
        connection.Update(tableName, "username='" + userName + "'", data);
    }

    public ArrayList<User> getFriends() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = connection.Select("friend", "username1='" + userName + "' OR username2='" + userName + "'");
        ArrayList<User> users = new ArrayList<>();
        while (resultSet.next()) {
            String username1 = resultSet.getString("username1");
            String username2 = resultSet.getString("username2");

            User user;
            if (username1.equals(userName)) {//username 2 is friend
                user = getUser(username2);
            } else {//username 1 is friend
                user = getUser(username1);
            }
            users.add(user);
        }

        return users;
    }

    public static User getUser(String username) throws SQLException {
        SQLDatabaseConnection connection = new SQLDatabaseConnection();
        ResultSet resultSet = connection.Select(tableName, "username='" + username + "'");
        resultSet.next();
        return new User(resultSet.getString("username"), resultSet.getString("password"), resultSet.getBytes("avatar"), resultSet.getString("status"), resultSet.getString("ip"), resultSet.getInt("port"));
    }

    public void addFriend(User friend) throws SQLException{
        Map<String,String> relationship = new HashMap<String,String>();
        relationship.put(this.getUserName(), friend.getUserName());
        connection.Insert("friend", relationship);
    }
    public String toString() {
        return this.userName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }

        User user = (User) obj;
        if (getUserName().equals(user.getUserName())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getUserName().hashCode();
    }
}

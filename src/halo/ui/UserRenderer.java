/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package halo.ui;

import halo.models.User;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Hanh
 */
public class UserRenderer extends JLabel implements ListCellRenderer<User> {

    public UserRenderer() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends User> list, User user, int index, boolean isSelected, boolean cellHasFocus) {
        ImageIcon icon = new ImageIcon((user.getAvatar()));
        this.setIcon(icon);
        String onlineStatus = "";
        if(user.isIsOnline()){
            onlineStatus = "Is online";
        } else {
            onlineStatus = "Is offline";
        }
        this.setText(user.getUserName() + " " + onlineStatus + " " + user.getStatus());
        return this;
    }

}

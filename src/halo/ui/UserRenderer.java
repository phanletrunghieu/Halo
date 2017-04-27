/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package halo.ui;

import halo.models.User;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Hanh
 */
public class UserRenderer extends JLabel implements ListCellRenderer<User> {

    public UserRenderer() {

    }

    @Override
    public Component getListCellRendererComponent(JList<? extends User> list, User user, int index, boolean isSelected, boolean cellHasFocus) {
        //ImageIcon icon = new ImageIcon((user.getAvatar()));
        //this.setIcon(icon);
        if (user.getAvatar() != null && user.getAvatar().length > 0) {
            try {
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(user.getAvatar()));
                Image newimg = bufferedImage.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
                this.setIcon(new ImageIcon(newimg));
            } catch (IOException ex) {
                Logger.getLogger(FriendList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String onlineStatus = "";
        if (user.isIsOnline()) {
            onlineStatus = "Is online";
        } else {
            onlineStatus = "Is offline";
        }
        String status = "";
        if (!"null".equals(user.getStatus())) {
            status = user.getStatus();
            System.out.println(user.getStatus());
        }
        this.setText(user.getUserName() + " " + onlineStatus + " " + status);
        this.setBorder(new EmptyBorder(10,10, 10, 10));
        if (cellHasFocus) { // Change color on focus 
            this.setBackground(Color.CYAN);
        } else {
            this.setBackground(Color.WHITE);
        }
        return this;
    }

}

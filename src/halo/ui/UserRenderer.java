/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package halo.ui;

import halo.models.User;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Hanh
 */
public class UserRenderer extends JLabel implements ListCellRenderer<User> {

    private ImageIcon defaultUserIcon;

    public UserRenderer() {
        try {
            BufferedImage bufferedImage = ImageIO.read(getClass().getResource("/halo/resources/no-user-image-square.jpg"));
            Image newimg = bufferedImage.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
            defaultUserIcon = new ImageIcon(newimg);
        } catch (IOException ex) {
            defaultUserIcon = new ImageIcon();
        }
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends User> list, User user, int index, boolean isSelected, boolean cellHasFocus) {
        if (user.getAvatar() != null && user.getAvatar().length > 0) {
            try {
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(user.getAvatar()));
                Image newimg = bufferedImage.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
                this.setIcon(new ImageIcon(newimg));
            } catch (IOException ex) {
                Logger.getLogger(FriendList.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.setIcon(defaultUserIcon);
        }

        String onlineStatus = "";
        if (user.isIsOnline()) {
            onlineStatus = "Online";
        } else {
            onlineStatus = "Offline";
        }
        String status = "";
        if (user.getStatus() != null) {
            status = user.getStatus();
        }
        this.setText("<html>" + user.getUserName() + "<br/>" + onlineStatus + "<br/>" + status + "</html>");
        return this;
    }

}

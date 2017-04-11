/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package halo.ui;

import halo.models.User;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.ListModel;

/**
 *
 * @author Phan Hieu
 */
public class FriendList extends javax.swing.JFrame {

    private User user;

    /**
     * Creates new form FriendList
     */
    public FriendList() {
        initComponents();
        user = new User();
        updateInfo();
    }

    public FriendList(User user) {
        this.user = user;
        initComponents();
        updateInfo();
    }

    public void updateInfo() {
        setUsername();
        getFriends();
    }

    public void setUsername() {
        userNameLabel.setText(this.user.getUserName());
    }

    public void getFriends() {
        try {
            List<User> friends = this.user.getFriends();
            if (friends != null) {
                friendList.removeAll();
                DefaultListModel<String> defaultListModel=new DefaultListModel<>();
                for (User friend : friends) {
                    defaultListModel.addElement(friend.toString());
                }
                friendList.setModel(defaultListModel);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FriendList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FriendList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        friendList = new javax.swing.JList<>();
        statusTextField = new javax.swing.JTextField();
        avatarPanel = new javax.swing.JPanel();
        avatarLabel = new javax.swing.JLabel();
        userNameLabel = new javax.swing.JLabel();
        onlineComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        friendList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        friendList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        friendList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                friendListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(friendList);

        statusTextField.setText("What's happening today?");
        statusTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusTextFieldActionPerformed(evt);
            }
        });
        statusTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                statusTextFieldKeyPressed(evt);
            }
        });

        avatarPanel.setBackground(new java.awt.Color(0, 0, 0));
        avatarPanel.setPreferredSize(new java.awt.Dimension(80, 80));
        avatarPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                avatarPanelMouseClicked(evt);
            }
        });

        avatarLabel.setText("jLabel1");

        javax.swing.GroupLayout avatarPanelLayout = new javax.swing.GroupLayout(avatarPanel);
        avatarPanel.setLayout(avatarPanelLayout);
        avatarPanelLayout.setHorizontalGroup(
            avatarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(avatarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(avatarLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                .addContainerGap())
        );
        avatarPanelLayout.setVerticalGroup(
            avatarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(avatarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(avatarLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addContainerGap())
        );

        userNameLabel.setText("username");

        onlineComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Online", "Offline" }));
        onlineComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onlineComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(avatarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(userNameLabel)
                            .addComponent(onlineComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 190, Short.MAX_VALUE))
                    .addComponent(statusTextField))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statusTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(avatarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(userNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(onlineComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void statusTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_statusTextFieldKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_statusTextFieldKeyPressed

    private void statusTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusTextFieldActionPerformed
        try {
            // setting status
            this.user.setStatus(statusTextField.getText());
        } catch (SQLException ex) {
            Logger.getLogger(FriendList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_statusTextFieldActionPerformed

    private void onlineComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onlineComboBoxActionPerformed
        // setting online status 
        if ("Online".equals(onlineComboBox.getSelectedItem().toString())) {
            try {
                this.user.setIsOnline(true);
            } catch (SQLException ex) {
                Logger.getLogger(FriendList.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                this.user.setIsOnline(false);
            } catch (SQLException ex) {
                Logger.getLogger(FriendList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_onlineComboBoxActionPerformed

    private void avatarPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_avatarPanelMouseClicked
        // TODO add your handling code here:
        JFileChooser jfc = new JFileChooser();
        int returnVal = jfc.showOpenDialog(jfc);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            avatarLabel.setIcon(new ImageIcon(file.getAbsolutePath()));
        }
    }//GEN-LAST:event_avatarPanelMouseClicked

    private void friendListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_friendListValueChanged
        // TODO add your handling code here:
        if (!evt.getValueIsAdjusting()) {
            try {
                JList source = (JList) evt.getSource();
                String selected = source.getSelectedValue().toString();
                new ChatForm(new User(selected)).setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(FriendList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_friendListValueChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FriendList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FriendList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FriendList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FriendList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FriendList().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel avatarLabel;
    private javax.swing.JPanel avatarPanel;
    private javax.swing.JList<String> friendList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> onlineComboBox;
    private javax.swing.JTextField statusTextField;
    private javax.swing.JLabel userNameLabel;
    // End of variables declaration//GEN-END:variables

}

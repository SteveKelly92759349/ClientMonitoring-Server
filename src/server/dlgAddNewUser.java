package Server;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import Entity.User;
import Service.UserService;
import Util.StringUtil;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

/**
 *
 * @author Men Nguyen
 */
@SuppressWarnings("serial")
public class dlgAddNewUser extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JLabel lblTitle;
    private static JTextField txtUsername;
    private static JTextField txtFullName;

    /**
     * Launch the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            dlgAddNewUser dialog = new dlgAddNewUser();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
        }
    }

    /**
     * Create the dialog.
     */
    public dlgAddNewUser() {
        setBounds(100, 100, 365, 255);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        {
            lblTitle = new JLabel("THÔNG TIN USER");
            lblTitle.setForeground(Color.BLUE);
            lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        }

        JLabel lblUsername = new JLabel("Tên đăng nhập:");

        JLabel lblFullName = new JLabel("Họ tên:");

        txtUsername = new JTextField();
        txtUsername.setColumns(10);

        txtFullName = new JTextField();
        txtFullName.setColumns(10);

        JLabel lblNote = new JLabel("");
        lblNote.setForeground(Color.DARK_GRAY);
        lblNote.setFont(new Font("Arial", Font.PLAIN, 11));
        lblNote.setText("<html>※ Lưu ý: Mật khẩu mặc định là tên đăng nhập.<br> Khi đăng nhập lần đầu, người dùng phải thay đổi mật khẩu.<br></html>");

        GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
        gl_contentPanel.setHorizontalGroup(
                gl_contentPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPanel.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
                                        .addComponent(lblNote, GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                                        .addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
                                                .addGroup(gl_contentPanel.createSequentialGroup()
                                                        .addComponent(lblFullName)
                                                        .addGap(58)
                                                        .addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
                                                                .addGroup(gl_contentPanel.createSequentialGroup()
                                                                        .addComponent(lblTitle)
                                                                        .addGap(7))
                                                                .addComponent(txtFullName, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(gl_contentPanel.createSequentialGroup()
                                                        .addComponent(lblUsername)
                                                        .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txtUsername, 139, 139, 139))))
                                .addContainerGap())
        );
        gl_contentPanel.setVerticalGroup(
                gl_contentPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPanel.createSequentialGroup()
                                .addGap(5)
                                .addComponent(lblTitle)
                                .addGap(18)
                                .addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblFullName)
                                        .addComponent(txtFullName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18)
                                .addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblUsername))
                                .addGap(26)
                                .addComponent(lblNote)
                                .addContainerGap(17, Short.MAX_VALUE))
        );
        contentPanel.setLayout(gl_contentPanel);
        {
            JPanel buttonPane = new JPanel();
            InnerClassActionListener actionListener = new InnerClassActionListener();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    try {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    }
                    break;
                }
            }
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton btnAdd = new JButton("Thêm");
                btnAdd.setActionCommand("add");
                btnAdd.addActionListener(actionListener);
                buttonPane.add(btnAdd);
                getRootPane().setDefaultButton(btnAdd);
            }
            {
                JButton btnCancel = new JButton("Hủy");
                btnCancel.setActionCommand("cancel");
                btnCancel.addActionListener(actionListener);
                buttonPane.add(btnCancel);
            }
        }
    }

    /*
	 * This is inner class listener for action listener
     */
    private class InnerClassActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            String strActionCommand = ae.getActionCommand();
            Component component = (Component) ae.getSource();
            JDialog dlgAddNewUser = (JDialog) SwingUtilities.getRoot(component);
            if (strActionCommand.equals("add")) {
                if (txtUsername.getText().isEmpty() || txtFullName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(dlgAddNewUser, "Vui lòng nhập đầy đủ thông tin!", "Thông báo", JOptionPane.OK_OPTION);
                    return;
                }
                addNewUser();
            }
            if (strActionCommand.equals("cancel")) {
                dlgAddNewUser.dispose();
            }
        }
    }

    /*
	 * Add new user
     */
    private void addNewUser() {
        User newUser = new User();
        // Current only have 1 server
        int serverId = 1;
        // status is "never"
        int status = 2;
        newUser.setUsername(txtUsername.getText());
        // hashing password
        String PasswordHash = StringUtil.hashPassword(txtUsername.getText());
        newUser.setPassword(PasswordHash);
        newUser.setFullName(txtFullName.getText());
        newUser.setStatus(status);

        int isAdded = UserService.addNewUser(newUser, serverId);
        if (isAdded < 1) {
            JOptionPane.showMessageDialog(this, "Thêm User không thành công", "Thông báo", JOptionPane.OK_OPTION);
            return;
        }
        JOptionPane.showMessageDialog(this, "Thêm User thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
    }
}

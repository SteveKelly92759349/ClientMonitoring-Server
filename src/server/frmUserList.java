package Server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Entity.User;
import Service.UserService;

/**
 *
 * @author Men Nguyen
 */
@SuppressWarnings("serial")
public class frmUserList extends JFrame {

    private JPanel contentPane;
    private static JTable tUserList;
    private final JScrollPane spUserList;
    private DefaultTableModel model;

    /**
     * Launch the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                frmUserList frame = new frmUserList();
                frame.setVisible(true);
            } catch (Exception e) {
            }
        });
    }

    /**
     * Create the frame.
     */
    public frmUserList() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 390);
        contentPane = new JPanel(new BorderLayout());
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                }
                break;
            }
        }

        InnerClassActionListener actionListener = new InnerClassActionListener();

        JPanel pTop = new JPanel();
        contentPane.add(pTop, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel("DANH SÁCH USER");
        lblTitle.setForeground(Color.BLUE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 15));
        pTop.add(lblTitle);

        JPanel pBottom = new JPanel();
        contentPane.add(pBottom, BorderLayout.SOUTH);

        JButton btnAdd = new JButton("Thêm");
        pBottom.add(btnAdd);
        btnAdd.setActionCommand("add");
        btnAdd.addActionListener(actionListener);

        JButton btnDelete = new JButton("Xóa");
        pBottom.add(btnDelete);
        btnDelete.setActionCommand("delete");
        btnDelete.addActionListener(actionListener);

        spUserList = new JScrollPane();
        tUserList = new JTable();
        tUserList.setDefaultEditor(Object.class, null);
        tUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        spUserList.setViewportView(tUserList);

        contentPane.add(spUserList, BorderLayout.CENTER);
        getContentPane().add(contentPane);

        // load data in jtable
        loadUserList();

        // auto refresh data in jtable each 3 seconds
        autoRefresh();
    }

    /**
     * load the list of users
     */
    private void loadUserList() {
        String[] columns = new String[]{"ID", "Tài khoản", "Họ tên", "Trạng thái"};
        model = new DefaultTableModel(null, columns);
        ArrayList<User> userList = new ArrayList<>();
        userList = UserService.getUsers();
        for (int i = 0; i < userList.size(); i++) {
            Object[] obj = new Object[]{userList.get(i).getUserId(),
                userList.get(i).getUsername(),
                userList.get(i).getFullName(),
                userList.get(i).getStatus() == 1 ? "Online" : "Offline"};

            model.addRow(obj);
        }
        tUserList.setModel(model);

        // set width column
        tUserList.getColumnModel().getColumn(0).setMaxWidth(50);
        tUserList.getColumnModel().getColumn(1).setMaxWidth(200);
        tUserList.getColumnModel().getColumn(2).setMaxWidth(330);
        tUserList.getColumnModel().getColumn(3).setMaxWidth(70);

        // set center text
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tUserList.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
    }

    /**
     * reload the list of users after 3 seconds
     */
    private void autoRefresh() {
        Timer timer = new Timer(0, (ActionEvent e) -> {
            int row = tUserList.getSelectedRow();
            loadUserList();
            tUserList.getSelectionModel().setSelectionInterval(row, row);
        });

        timer.setDelay(3000); // delay for 3 seconds
        timer.start();
    }

    /**
     * This is inner class listener for action listener
     */
    private class InnerClassActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            String strActionCommand = ae.getActionCommand();
            Component component = (Component) ae.getSource();
            JFrame frmUserList = (JFrame) SwingUtilities.getRoot(component);
            if (strActionCommand.equals("add")) {
                dlgAddNewUser dlgAddNewUser = new dlgAddNewUser();
                dlgAddNewUser.setResizable(false);
                dlgAddNewUser.setModal(true);
                dlgAddNewUser.setLocationRelativeTo(frmUserList);
                dlgAddNewUser.setVisible(true);
            }
            if (strActionCommand.equals("delete")) {
                delete();
            }
        }
    }

    /**
     * delete user
     */
    private void delete() {
        int row = tUserList.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn User cần xóa!", "Thông báo", JOptionPane.OK_OPTION);
        } else {
            int userId = Integer.parseInt(tUserList.getValueAt(row, 0).toString());
            int isDeleted = UserService.deleteUser(userId);
            if (isDeleted < 1) {
                JOptionPane.showMessageDialog(this, "Xóa không thành công", "Thông báo", JOptionPane.OK_OPTION);
            }
            JOptionPane.showMessageDialog(this, "Xóa thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

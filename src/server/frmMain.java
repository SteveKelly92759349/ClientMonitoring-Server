package Server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

import Service.ServerService;

/**
 *
 * @author Men Nguyen
 */
@SuppressWarnings("serial")
public class frmMain extends JFrame {

    private JPanel contentPane;
    private JButton btnStart, btnStop;
    private JTextArea taInfo;
    private final JTextField txtIP;
    private final JTextField txtPort;

    private ServerService server;

    /**
     * Launch the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                frmMain frame = new frmMain();
                frame.setVisible(true);
            } catch (Exception e) {
            }
        });
    }

    /**
     * Create the frame.
     */
    public frmMain() {
        setTitle("Server");
        setResizable(false);
        contentPane = new JPanel(new BorderLayout());
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if (info.getName().equals("Nimbus")) {
                try {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                }
                break;
            }
        }

        InnerClassActionListener actionListener = new InnerClassActionListener();

        taInfo = new JTextArea();
        taInfo.setEditable(false);
        taInfo.setFont(new java.awt.Font("Arial", 0, 16));
        JScrollPane spInfo = new JScrollPane();
        spInfo.setViewportView(taInfo);
        spInfo.setPreferredSize(new Dimension(400, 400));

        btnStart = new JButton("Khởi động");
        btnStart.setEnabled(true);
        btnStart.setActionCommand("Start");
        btnStart.addActionListener(actionListener);

        btnStop = new JButton("Dừng");
        btnStop.setEnabled(false);
        btnStop.setActionCommand("Stop");
        btnStop.addActionListener(actionListener);

        JPanel pBottom = new JPanel();
        pBottom.add(btnStart);
        pBottom.add(btnStop);
        pBottom.setBackground(new java.awt.Color(0, 0, 128));

        JPanel pLeft = new JPanel();
        pLeft.setPreferredSize(new Dimension(30, 30));
        pLeft.setBackground(new java.awt.Color(0, 0, 128));
        JPanel pRight = new JPanel();
        pRight.setBackground(new java.awt.Color(0, 0, 128));
        pRight.setPreferredSize(new Dimension(30, 30));
        contentPane.add(spInfo, BorderLayout.CENTER);
        contentPane.add(pBottom, BorderLayout.SOUTH);
        contentPane.add(pLeft, BorderLayout.WEST);
        contentPane.add(pRight, BorderLayout.EAST);

        getContentPane().add(contentPane);
        contentPane.setBackground(new java.awt.Color(0, 0, 128));

        JPanel pTop = new JPanel();
        contentPane.add(pTop, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel("THÔNG TIN SERVER");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel lblIP = new JLabel("IP:");
        lblIP.setFont(new Font("Arial", Font.BOLD, 13));
        lblIP.setForeground(Color.WHITE);

        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
        }
        txtIP = new JTextField();
        txtIP.setText(inetAddress.getHostAddress());
        txtIP.setEnabled(false);
        txtIP.setEditable(false);
        txtIP.setColumns(10);

        JLabel lblPort = new JLabel("PORT:");
        lblPort.setForeground(Color.WHITE);
        lblPort.setFont(new Font("Arial", Font.BOLD, 11));

        txtPort = new JTextField();
        txtPort.setText("3306");
        txtPort.setColumns(10);

        JButton btnUserList = new JButton("Danh sách user");
        btnUserList.setActionCommand("UserList");
        btnUserList.setSize(35, 25);
        btnUserList.addActionListener(actionListener);

        GroupLayout gl_pTop = new GroupLayout(pTop);
        gl_pTop.setHorizontalGroup(
                gl_pTop.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_pTop.createSequentialGroup()
                                .addGap(32)
                                .addComponent(lblIP)
                                .addGap(18)
                                .addComponent(txtIP, GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                                .addGap(18)
                                .addComponent(lblPort)
                                .addGap(18)
                                .addComponent(txtPort, GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                                .addGap(30)
                                .addComponent(btnUserList, GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                                .addGap(30))
                        .addGroup(gl_pTop.createSequentialGroup()
                                .addGap(157)
                                .addComponent(lblTitle, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(179))
        );
        gl_pTop.setVerticalGroup(
                gl_pTop.createParallelGroup(Alignment.TRAILING)
                        .addGroup(gl_pTop.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblTitle, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_pTop.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(lblIP)
                                        .addComponent(lblPort)
                                        .addComponent(txtPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnUserList, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtIP))
                                .addContainerGap())
        );
        pTop.setLayout(gl_pTop);
        pTop.setBackground(new java.awt.Color(0, 0, 128));
        setSize(500, 484);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void appendMessage(String message) {
        taInfo.append(message);
        taInfo.setCaretPosition(taInfo.getText().length() - 1);
    }

    /**
     * This is inner class listener for action listener
     */
    private class InnerClassActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            String strActionCommand = ae.getActionCommand();
            Component component = (Component) ae.getSource();
            JFrame frmMain = (JFrame) SwingUtilities.getRoot(component);

            if (strActionCommand.equals("Start")) {
                startServer();
            }
            if (strActionCommand.equals("Stop")) {
                stopServer();
            }
            if (strActionCommand.equals("UserList")) {
                frmUserList frmUserList = new frmUserList();
                frmUserList.setResizable(false);
                frmUserList.setLocationRelativeTo(frmMain);
                frmUserList.setVisible(true);
            }
        }
    }

    private void startServer() {
        int port = Integer.parseInt(txtPort.getText().trim());
        server = new ServerService(port);
        server.taInfo = this.taInfo;

        // update the information of server such as: ip, port
        int result = server.updateServerInfo(txtIP.getText().trim(), port);
        if (result < 1) {
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin Server không thành công!", "Thông báo", JOptionPane.OK_OPTION);
            return;
        }

        // start server
        server.start();

        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        txtPort.setEnabled(false);
    }

    private void stopServer() {
        int option
                = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn đóng Server?", "Lưu ý", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            server.stop();
            btnStart.setEnabled(true);
            btnStop.setEnabled(false);
            txtPort.setEnabled(true);
        }
    }
}

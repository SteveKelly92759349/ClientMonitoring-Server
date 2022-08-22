package Service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JTextArea;

import Common.Message;

public class ServerThreadService implements Runnable {

    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    private String username;
    private Message message;

    public static Hashtable<String, ServerThreadService> userList = new Hashtable<>();

    public JTextArea taInfo;
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public ServerThreadService(Socket socket, JTextArea taInfo) throws IOException {
        this.socket = socket;
        this.taInfo = taInfo;
        out = new ObjectOutputStream(this.socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(this.socket.getInputStream());
    }

    public void appendMessage(String message) {
        taInfo.append(message);
        taInfo.setCaretPosition(taInfo.getText().length() - 1);
    }

    public Message recieveFromUser() {
        try {
            return (Message) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
        }
        return null;
    }

    @Override
    public void run() {
        executeRequest();
    }

    private void executeRequest() {
        String action;
        while (true) {
            message = recieveFromUser();
            System.out.println(message.toString());
            action = message.type;
            switch (action) {
                case "ONLINE": {
                    username = message.sender;

                    userList.put(username, this);

                    this.appendMessage("\n[" + sdf.format(new Date()) + "] User \"" + username + "\" đã kết nối");

                    break;
                }
                case "LOGOUT": {
                    username = message.sender;

                    userList.remove(username);

                    this.appendMessage("\n[" + sdf.format(new Date()) + "] User \"" + username + "\" đã đóng kết nối");

                    break;
                }
                case "CHAT": {
                    notifyToAllUsers(message);

                    break;
                }
                case "UPLOAD": {
                    if (message.recipient.equals("All")) {
                        sendToAllUsers(new Message("RECEIVE_FILE", message.sender, message.content, "All"));
                    }

                    break;
                }
                case "RESPONSE": {
                    if (!message.content.equals("NO")) {
                        // get ip of user will receive file
                        String IP = findServerThreadService(message.sender).socket.getInetAddress().getHostAddress();

                        // find ServerThreadService of upload user and send message with sender is "IP Server of receive user"
                        findServerThreadService(message.recipient).sendToSpecificUser(new Message("UPLOAD_FILE", IP, message.content, message.recipient));
                    }

                    break;
                }
            }
        }
    }

    public void notifyToAllUsers(Message message) {
        Enumeration<ServerThreadService> users = userList.elements();
        ServerThreadService st;
        ObjectOutputStream writer;

        while (users.hasMoreElements()) {
            st = users.nextElement();
            writer = st.out;

            try {
                writer.writeObject(message);
                writer.flush();
            } catch (IOException e) {
                System.out.println("Gửi tin đến tất cả User thất bại!");
            }
        }
    }

    /*
	 * find ServerThreadService of user by username in userList
     */
    public ServerThreadService findServerThreadService(String username) {
        Enumeration<ServerThreadService> users = userList.elements();
        ServerThreadService st;
        while (users.hasMoreElements()) {
            st = users.nextElement();
            if (st.username.equals(username)) {
                return st;
            }
        }
        return null;
    }

    public void sendToAllUsers(Message message) {
        Enumeration<ServerThreadService> users = userList.elements();
        ServerThreadService st;
        ObjectOutputStream writer;

        while (users.hasMoreElements()) {
            st = users.nextElement();
            // send to all users except the upload user
            if (!st.username.equals(message.sender)) {
                writer = st.out;

                try {
                    writer.writeObject(message);
                    writer.flush();
                } catch (IOException e) {
                    System.out.println("Gửi tin đến tất cả User thất bại!");
                }
            }
        }
    }

    public void sendToSpecificUser(Message message) {
        Enumeration<ServerThreadService> users = userList.elements();
        ServerThreadService st;
        ObjectOutputStream writer;

        while (users.hasMoreElements()) {
            st = users.nextElement();
            if (st.username.equals(message.recipient)) {
                writer = st.out;

                try {
                    writer.writeObject(message);
                    writer.flush();
                } catch (IOException e) {
                    System.out.println("Gửi tin đến User thất bại!");
                }
            }
        }
    }
}

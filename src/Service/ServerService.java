package Service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JTextArea;

import DAO.ServerDAO;

public class ServerService implements Runnable {
    private ServerSocket server;
    private final int port;
    private Socket socket;
    private final ExecutorService pool;
    private Thread thread;
    
    public JTextArea taInfo;
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public void appendMessage(String message) {
        taInfo.append(message);
        taInfo.setCaretPosition(taInfo.getText().length() - 1);
    }
    
	public ServerService(int port) {
		this.port = port;
        pool = Executors.newFixedThreadPool(100);
    }
    
    public void start() {
 	   if (thread == null) {
 		   thread = new Thread(this);
 	       thread.start();
 	   }
    }

	@Override
	public void run() {
		this.startServer();
	}
	
	public void startServer() {
		try {
			server = new ServerSocket(port);
			appendMessage("\n["+sdf.format(new Date())+"] Server đang lắng nghe kết nối...");
	        while (true) {
	        	socket = server.accept();
	            ServerThreadService runnable = new ServerThreadService(socket, taInfo);
	            pool.execute(runnable);
	        }
		} catch (IOException e) {
			appendMessage("\n["+sdf.format(new Date())+"] Khởi động Server thất bại!");
        }
    }

	public void stop()   {
	   if (thread != null) {
		   thread.stop();
	       thread = null;
	       
	       try {
	    	   server.close();
	       } catch (IOException e) {
	    	   appendMessage("\n["+sdf.format(new Date())+"] Đóng Server thất bại!");
	       }
	       
	       appendMessage("\n["+sdf.format(new Date())+"] Server đã đóng");
	   }
	}
	
	public int updateServerInfo(String ip, int port) {
		return ServerDAO.updateServerInfo(ip, port);
	}
}

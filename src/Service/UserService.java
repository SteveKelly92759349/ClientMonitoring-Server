package Service;

import java.util.ArrayList;

import DAO.UserDAO;
import Entity.User;

public class UserService {
	public static User getUserInfo(int userId) 
	{
		return UserDAO.getUser(userId);
	}
	
	public static ArrayList<User> getUsers() {
		return UserDAO.getUsers();
	}
	
	public static int addNewUser(User newUser, int serverId) {
		return UserDAO.insertNewUser(newUser, serverId);
	}
	
	public static int deleteUser(int userId) {
		return UserDAO.deleteUser(userId);
	}
}

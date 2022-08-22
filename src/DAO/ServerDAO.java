package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServerDAO {
	/**
	 * update the information of server
	 * 
	 * @return either (1) the row count for SQL Data Manipulation Language (DML) statements 
	 * or (2) 0 for SQL statements that return nothing
	 */
	public static int updateServerInfo(String ip, int port) {
		String sql = "UPDATE server SET ip = ?, port = ?";
		Connection conn = DAO.DataProvide.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, ip);
			ps.setInt(2, port);
			int result = ps.executeUpdate();
            conn.close();
            return result;
		} catch (SQLException e) {
			System.out.println("Cập nhật thông tin Server thất bại!");
			e.printStackTrace();
			return 0;
		}
	}
}

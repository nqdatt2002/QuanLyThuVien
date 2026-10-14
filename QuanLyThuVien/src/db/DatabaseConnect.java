package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnect {
	public static Connection getConnect() {
		String url = "jdbc:sqlserver://LAPTOP-2VB93MTI\\SQLEXPRESS:1433;databaseName=QuanLySach";
		String user = "sa";
		String password = "1234$";
		
		Connection con = null;
		
		try {
			con = DriverManager.getConnection(url,user,password);
			return con;
		} catch (SQLException e) {
			System.out.println("Lỗi kết nối: "+ e.getMessage());
			return null;
		}
		
	}
}

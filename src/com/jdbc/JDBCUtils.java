package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class JDBCUtils {

	static final String driver = "com.mysql.jdbc.Driver";  
	static final String url = "jdbc:mysql://localhost/test";
	static final String user = "root";
	static final String pass = "root";
	
	public static ResultSet getJdbc(String sql){
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, user, pass);
			PreparedStatement ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public static void main(String[] args) throws SQLException {
		String sql = "SELECT * FROM test_one";
		ResultSet rs = getJdbc(sql);
		while(rs.next()){
			System.out.println("-----------");
			System.out.println("ID:"+rs.getInt("ID")+";HOSPITAL_ID:"+rs.getInt("HOSPITAL_ID")+";FUNCTION_ID:"+rs.getInt("FUNCTION_ID")+";NAME:"+rs.getString("NAME"));
			System.out.println("-----------");
		}
	}
}

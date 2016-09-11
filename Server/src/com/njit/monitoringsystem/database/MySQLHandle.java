package com.njit.monitoringsystem.database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLHandle {
	private static MySQLHandle mySQLHandle;

	private java.sql.Connection connection;
	private java.sql.Statement statement;

	private String databaseName = "forestmonitoring";
	private String user = "root";
	private String password = "123456";

	private MySQLHandle() {
		// TODO Auto-generated constructor stub
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName + "?user=" + user
					+ "&password=" + password + "&useUnicode=true&characterEncodeing=UTF8&useSSL=false");
			statement = connection.createStatement();

			ResultSet resultSet = connection.getMetaData().getTables(null, null, "tahdata", null);
			if (resultSet.next()) {
				System.out.println("数据库连接成功");
			} else {
				System.out.println("table创建成功");
				statement.executeUpdate(
						"create table tahdata(place int not null, times timestamp not null, tem smallint, hum smallint, primary key(place, times))");
			}		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("数据库操作失败");
			e.printStackTrace();
		}
	}

	public static MySQLHandle getMySQLHandle() {
		if (mySQLHandle == null) {
			// synchronized (mySQLHandle) {
			// if (mySQLHandle == null) {
			mySQLHandle = new MySQLHandle();
			// }
			// }
		}

		return mySQLHandle;
	}

	public ResultSet selectData(int place, String down, String up) {
		try {
			ResultSet resultSet = statement.executeQuery(
					"select * from tahdata where place = " + place + " and times >= " + down + " and times < " + up + ";");
			return resultSet;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void updateData(int place, String times, int tem, int hum) {
		try {
			statement.executeUpdate("insert into tahdata(place, times, tem, hum) values(" + place + ", " + times + ", " + tem + ", " + hum + ");");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void closeConnection() {
		try {
			if (mySQLHandle != null) {
				mySQLHandle.connection.close();
				System.out.println("数据库连接已关闭");
			} else {
				System.out.println("数据库连接已关闭");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

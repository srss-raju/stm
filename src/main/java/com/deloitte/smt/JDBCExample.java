package com.deloitte.smt;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class JDBCExample {

	public static String test() {


		try {

			Class.forName("org.postgresql.Driver");

		} catch (ClassNotFoundException e) {

			return null;

		}


		Connection connection = null;

		try {

			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/smt", "postgres","postgres");

		} catch (SQLException e) {

			e.printStackTrace();
			return null;

		}
		String response = null;
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
			response ="You made it, take control your database now!";
		} else {
			System.out.println("Failed to make connection!");
		}
		return response;
	}

}
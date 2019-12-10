package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javax.net.ssl.SSLException;

public class Main {
	static private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	// update USER, PASS and DB URL according to credentials provided by the website:
	// https://remotemysql.com/
	// in future move these hard coded strings into separated config file or even better env variables
	static private final String DB = "ii9L0m3rmp";
	static private final String DB_URL = "jdbc:mysql://remotemysql.com/"+ DB + "?useSSL=false";
	static private final String USER = "ii9L0m3rmp";
	static private final String PASS = "9nlQJMbwKc";

	public static void main(String[] args) throws SSLException {
		Connection conn = null;
		Statement stmt = null;
		String use="use";
		
		
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			System.out.println("\t============");
      //      stmt.executeUpdate("UPDATE flights  SET price='"+2019+"' WHERE num='"+387+"'");
			String sq2 = "UPDATE flights set price=2019 WHERE num=387";
			stmt.executeUpdate(sq2);
			String sql= "SELECT num,price FROM flights";
		    ResultSet Myrs = stmt.executeQuery(sql);
			while(Myrs.next()){
			if(Myrs.getInt("num")==387){
			System.out.println();
			System.out.println("This is the New Price of Flight num 387: " + Myrs.getInt("price"));
			}

			}
		    String Price_str="price";
			float Myprice=0;
           Statement Mystmt=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
           ResultSet Myrs2=Mystmt.executeQuery("SELECT price,distance,num FROM flights where distance > '"+1000+"'");
			while(Myrs2.next()){
			Myprice=Myrs2.getFloat(Price_str);
			Myprice=Myprice+100;
			Myrs2.updateFloat(Price_str, Myprice);
			Myrs2.updateRow();
			
			}
			Statement Mystmt3=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
           ResultSet Myrs3=Mystmt3.executeQuery("SELECT price,distance,num FROM flights where price < 300 ");
			while(Myrs3.next()){
			 Price_str="price";
			Myprice=Myrs3.getFloat(Price_str);
			Myprice=Myprice-25;
			Myrs3.updateFloat(Price_str, Myrs3.getFloat(Price_str)-25);
			Myrs3.updateRow();
			System.out.println("This is the New Price of Flight num: " + Myrs3.getInt("num") + "Price :" + Myrs3.getInt("price"));
			
			}
			
			PreparedStatement pstmt = conn.prepareStatement("UPDATE flights SET price=price+100 WHERE distance > 1000");
			
			 
			 pstmt.executeUpdate();
			 PreparedStatement pstmt2 = conn.prepareStatement("UPDATE flights SET price=price-25 WHERE price < 300");
			 
			 pstmt2.executeUpdate();
			 sql = "SELECT * FROM flights";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int num = rs.getInt("num");
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				int distance = rs.getInt("distance");
				int price = rs.getInt("price");

				System.out.format("Number %5s Origin %15s destinations %18s Distance %5d Price %5d\n", num, origin, destination, distance, price);
			}
			
						
			

			System.out.println("\t============");

			sql = "SELECT origin, destination, distance, num FROM flights";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String origin = rs.getString("origin");
				String destination = rs.getString("destination");
				int distance = rs.getInt("distance");

				System.out.print("From: " + origin);
				System.out.print(",\tTo: " + destination);
				System.out.println(",\t\tDistance: " + distance);
			}

			System.out.println("\t============");
			
			sql = "SELECT origin, destination FROM flights WHERE distance > ?";
			
			
			PreparedStatement prep_stmt = conn.prepareStatement(sql);
			prep_stmt.setInt(1, 200);
			rs = prep_stmt.executeQuery();
			while (rs.next()) {
				String origin = rs.getString("origin");
				System.out.println("From: " + origin);
			}
		
		
		
			stmt.close();
			conn.close();

		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
            System.out.println("SQLState: " + se.getSQLState());
            System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
}

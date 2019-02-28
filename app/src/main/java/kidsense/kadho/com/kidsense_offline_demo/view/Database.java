package kidsense.kadho.com.kidsense_offline_demo.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Database {

    private static final String username = "root";
    private static final String password = "56723595Angel";

    private Database() {}

    public static Connection establishConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/kidsense", username, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static void main(String[] args) {
        Connection con = establishConnection();

        if(con != null) {
            try {
                Statement stmt = con.createStatement();

                ResultSet set = stmt.executeQuery("SELECT * FROM `User` WHERE `UID` = 1");

                set.next();

                System.out.println(set.getInt("UID"));

                //int id = set.getInt("UID");

                //System.out.println(name);
                System.out.println("HELLO");
                con.close();
            }catch(SQLException e) {
                System.out.println("ERROR");
            }
        }
        System.out.println("HELLO2");
    }
}

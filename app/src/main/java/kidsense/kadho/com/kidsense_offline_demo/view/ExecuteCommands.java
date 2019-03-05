package kidsense.kadho.com.kidsense_offline_demo.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class ExecuteCommands {

    private static final String username = "capadmin";
    private static final String password = ".cuddlefish";

    private ExecuteCommands() {}

    public static Connection establishConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            return DriverManager.getConnection("jdbc:mysql://capstone.cbmzu8sefbol.us-west-1.rd.amazonaws.com:3306/FAKEKIDSENSE", username, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static boolean verifyUser(String username, String password) {
        Connection con = establishConnection();

        String pstmt = "SELECT Username, Password FROM User Where Username = ?;";
        try {
            PreparedStatement update = con.prepareStatement(pstmt);

            update.setString(1, username);

            ResultSet res = update.executeQuery();


            if(res.next() == false) {
                return false;
            }

            return res.getString("password").equals(password);

        }catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static boolean userExists(String username) {
        Connection con = establishConnection();

        String pstmt = "SELECT * FROM User Where Username = ?;";
        try {
            PreparedStatement update = con.prepareStatement(pstmt);

            update.setString(1, username);

            ResultSet res = update.executeQuery();

            if(res.next() == false)
                return false;

            return true;

        }catch(SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean newUser(String ... args) {
        Connection con = establishConnection();

        if(userExists(args[0])) {
            System.out.println("USERNAME IN USE");
            return false;
        }

        String pstmt = "INSERT INTO User (Username, FirstName, LastName, Password, email) VALUES (?, ?, ?, ?, ?);";
        try {
            PreparedStatement update = con.prepareStatement(pstmt);

            for(int i = 1; i < (args.length + 1); ++i) {
                update.setString(i, args[i]);
            }

            int res = update.executeUpdate();

            if (res == 0) {
                System.out.println("FAIL");
                return false;
            }

            System.out.println("SUCCESS");
            return true;

        }catch(SQLException e) {
            e.printStackTrace();
        }


        return false;
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

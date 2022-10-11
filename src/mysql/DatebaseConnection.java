package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatebaseConnection {
    public static Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/MGN_schema",
                    "root", "leo66259148");

            if (connection == null) {
                System.out.println("資料庫連接失敗");
            }
            return connection;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}

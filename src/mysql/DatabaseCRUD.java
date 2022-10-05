package mysql;

import java.sql.*;
import java.util.List;

public class DatabaseCRUD {
    public static String getTargetMgni(String id) {
        Connection connection = DatebaseConnection.getConnection();
        String result = null;

        try {
            String getAllCashi = "SELECT * FROM MGN_schema.MGNI where MGNI_ID = '" + id + "'";
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(getAllCashi);
            while (resultSet.next()) {
                result = (resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getBigDecimal(4)+"\n");
                System.out.println(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getBigDecimal(4));
            }
        }
//        catch (ClassNotFoundException e) {
//            System.out.println("DriverClassNotFound :" + e.toString());
//        }//有可能會產生sqlexception
//        catch (SQLException x) {
//            System.out.println("Exception :" + x.toString());
//        }
        catch (Exception x) {
            System.out.println("Exception :" + x.toString());
        }
        return result;
    }
    public static String getTargetCashi(String id) {
        Connection connection = DatebaseConnection.getConnection();
        String result="";

        try {
            String getAllCashi = "SELECT * FROM MGN_schema.Cashi where CASHI_MGNI_ID = '" + id + "'";
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(getAllCashi);
            while (resultSet.next()) {
                result += ((resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getBigDecimal(4))+"\n");
                System.out.println(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getBigDecimal(4));
            }
        }
//        catch (ClassNotFoundException e) {
//            System.out.println("DriverClassNotFound :" + e.toString());
//        }//有可能會產生sqlexception
//        catch (SQLException x) {
//            System.out.println("Exception :" + x.toString());
//        }
        catch (Exception x) {
            System.out.println("Exception :" + x.toString());
        }
        return result;
    }

    public static String deleteMgni(String id) {
        Connection connection = DatebaseConnection.getConnection();
        try {

            //要執行的命令
            String createCashi = "insert into MGN_schema.CASHI values('MGI20221004100528100','1','TWD',1000)";
            //通過連接對象，將命令傳給資料庫
            Statement st = connection.createStatement();
            //執行
            st.execute(createCashi);
        } catch (Exception e) {
            System.out.println("Exception :" + e.toString());
        }
        return "刪除成功";
    }


//        Connection connection =DatebaseConnection.getConnection();
//        try {

//            要執行的命令
//            String createCashi ="insert into MGN_schema.CASHI values('MGI20221004100528100','1','TWD',1000)";
//            //通過連接對象，將命令傳給資料庫
//            Statement st=connection.createStatement();
//            //執行
//            st.execute(createCashi);
//            System.out.println("新增成功");

//            String getAllCashi = "SELECT * FROM MGN_schema.CASHI";
//            //通過連接對象，將命令傳給資料庫
//            Statement st = connection.createStatement();
//            //執行
//            ResultSet resultSet = st.executeQuery(getAllCashi);
//            while (resultSet.next()){
//                System.out.println(resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3)+" "+resultSet.getBigDecimal(4));
//            }
//
//
//        }
//        catch (ClassNotFoundException e) {
//            System.out.println("DriverClassNotFound :" + e.toString());
//        }//有可能會產生sqlexception
//        catch (SQLException x) {
//            System.out.println("Exception :" + x.toString());
//        }
//        catch (Exception x) {
//            System.out.println("Exception :" + x.toString());
//        }


}
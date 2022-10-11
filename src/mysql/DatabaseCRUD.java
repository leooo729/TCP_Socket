package mysql;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import tcpSocket.SocketServer;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseCRUD {
    static Connection connection = DatebaseConnection.getConnection();
    private static final Logger logger = Logger.getLogger(SocketServer.class);

    public static String createMgni(JSONObject request) {

        String id = "MGI" + DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now());

        try {
            Statement st = connection.createStatement();
            BigDecimal totalAmt = new BigDecimal(0);
//            String createCashi = "";

            for (Object accAmt : request.getJSONArray("accAmt")) {
                JSONObject cashi = new JSONObject(accAmt.toString());
                String createCashi = "insert into MGN_schema.CASHI values('" + id + "','" + cashi.getString("acc") + "','" + request.getString("ccy") + "','" + cashi.getBigDecimal("amt") + "')";
                totalAmt = totalAmt.add(cashi.getBigDecimal("amt"));
                st.execute(createCashi);
            }

            String createMgniSql = createMgniSql(id, request, totalAmt);
            st.execute(createMgniSql);

        } catch (SQLException e) {
            System.out.println("SQLException :" + e.toString());
            logger.error("SQLException happen");
            return "SQLException happen";
        }
        return "新增成功" + "\n" + getTargetMgni(id);
    }

    public static String getTargetMgni(String id) {
        String result=getTargetMgniSql(id);
        return result;
    }

    public static String getTargetCashi(String id) {
        String result = getCashiSql(id);
        return result;
    }

    public static String dynamicQueryMgni(JSONObject request) {
        String result;
        String cmNo = request.getString("cmNo");
        String ccy = request.getString("ccy");

        String baseSQL = "SELECT * FROM MGN_schema.MGNI where 1=1";
        StringBuilder builder = new StringBuilder();// 用於拼接SQL語句
        builder.append(baseSQL);

        if (!(cmNo).isEmpty()) {
            builder.append(" and MGNI_CM_NO='" + cmNo + "'");
        }
        if (!(ccy).isEmpty()) {
            builder.append(" and MGNI_CCY='" + ccy + "'");
        }
        try {
            Statement st = connection.createStatement();
            ResultSet mgni = st.executeQuery(builder.toString());

            result = mgniResult(mgni);

        } catch (SQLException e) {
            System.out.println("SQLException :" + e.toString());
            logger.error("SQLException happen");
            return "SQLException happen";
        }
        return result;
    }

    public static String updateMgni(JSONObject request) {

        try {
            String deleteCashi = "delete from MGN_schema.CASHI where CASHI_MGNI_ID='" + request.getString("id") + "'";
            Statement st = connection.createStatement();
            st.execute(deleteCashi);

            BigDecimal totalAmt = new BigDecimal(0);

            String createCashi;

            for (Object s : request.getJSONArray("accAmt")) {
                JSONObject jsonObject = new JSONObject(s.toString());
                createCashi = "insert into MGN_schema.CASHI values('" + request.getString("id") + "','" + jsonObject.getString("acc") + "','" + request.getString("ccy") + "','" + jsonObject.getBigDecimal("amt") + "')";
                totalAmt = totalAmt.add(jsonObject.getBigDecimal("amt"));
                st.execute(createCashi);
            }

            String updateMgni = "UPDATE MGN_schema.Mgni SET MGNI_U_TIME='" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())
                    + "',MGNI_CM_NO='" + request.getString("cmNo")
                    + "',MGNI_KAC_TYPE='" + request.getString("kacType")
                    + "',MGNI_BANK_NO='" + request.getString("bankNo")
                    + "',MGNI_CCY='" + request.getString("ccy")
                    + "',MGNI_PV_TYPE='" + request.getString("pvType")
                    + "',MGNI_BICACC_NO='" + request.getString("bicaccNo")
                    + "',MGNI_AMT=" + totalAmt
                    + ",MGNI_CT_NAME='" + request.getString("ctName")
                    + "',MGNI_CT_TEL='" + request.getString("ctTel")
                    + "' WHERE MGNI_ID ='" + request.getString("id") + "'";
            System.out.println(updateMgni);
            st.execute(updateMgni);
        } catch (SQLException e) {
            System.out.println("SQLException :" + e.toString());
            logger.error("SQLException happen");
            return "SQLException happen";
        }

        return "更新成功" + "\n" + getTargetMgni(request.getString("id"));
    }

    public static String deleteMgni(JSONObject request) {
        //要執行的命令
        String deleteCashi = "delete from MGN_schema.CASHI where CASHI_MGNI_ID='" + request.getString("id") + "'";
        String deleteMgni = "delete from MGN_schema.MGNI where MGNI_ID='" + request.getString("id") + "'";
        try {
            //通過連接對象，將命令傳給資料庫
            Statement st = connection.createStatement();
            //執行
            st.execute(deleteCashi);
            st.execute(deleteMgni);
        } catch (SQLException e) {
            System.out.println("SQLException :" + e.toString());
            logger.error("SQLException happen");
            return "SQLException happen";
        }
        return "刪除成功";
    }
//--------------------------------------------------------------------------------------------------------------------------------Method

    public static String mgniResult(ResultSet mgni) {
        String result = "";
        try {
            while (mgni.next()) {
                result += (" 申請ID:" + mgni.getString("MGNI_ID") + "\n"
                        + " 存入日期:" + mgni.getString("MGNI_TIME") + "\n"
                        + " 存入類型:" + mgni.getString("MGNI_TYPE") + "\n"
                        + " 結算會員代號:" + mgni.getBigDecimal("MGNI_CM_NO") + "\n"
                        + " 存入保管專戶別:" + mgni.getString("MGNI_KAC_TYPE") + "\n"
                        + " 存入結算銀行代碼:" + mgni.getString("MGNI_BANK_NO") + "\n"
                        + " 存入幣別:" + mgni.getString("MGNI_CCY") + "\n"
                        + " 存入方式:" + mgni.getString("MGNI_PV_TYPE") + "\n"
                        + " 實體帳號/虛擬帳號:" + mgni.getString("MGNI_BICACC_NO") + "\n"
                        + " 存入帳號明細:" + "\n" + getTargetCashi(mgni.getString("MGNI_ID"))
                        + " 存入類別:" + mgni.getString("MGNI_I_TYPE") + "\n"
                        + " 存入實體帳號原因:" + mgni.getString("MGNI_P_REASON") + "\n"
                        + " 總存入金額:" + mgni.getString("MGNI_AMT") + "\n"
                        + " 聯絡人姓名:" + mgni.getString("MGNI_CT_NAME") + "\n"
                        + " 聯絡人電話:" + mgni.getString("MGNI_CT_TEL") + "\n"
                        + " 申請狀態:" + mgni.getString("MGNI_STATUS") + "\n"
                        + " 更新時間:" + mgni.getString("MGNI_U_TIME") + "\n"
                        + "\n");
            }
        } catch (SQLException e) {
            System.out.println("SQLException :" + e.toString());
            logger.error("SQLException happen");
            return "SQLException happen";
        }
        return result;
    }

    private static String createMgniSql(String id, JSONObject request, BigDecimal totalAmt) {
        String createMgniSql = "insert into MGN_schema.MGNI values('" + id
                + "','" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())
                + "','1"
                + "','" + request.getString("cmNo")
                + "','" + request.getString("kacType")
                + "','" + request.getString("bankNo")
                + "','" + request.getString("ccy")
                + "','" + request.getString("pvType")
                + "','" + request.getString("bicaccNo")
                + "','" + request.getString("iType")
                + "','" + request.getString("pReason")
                + "'," + totalAmt
                + ",'" + request.getString("ctName")
                + "','" + request.getString("ctTel")
                + "','0"
                + "','" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())
                + "')";
        return createMgniSql;
    }

    private static String getTargetMgniSql(String id) {
        String result;
        try {
            String targetMgni = "SELECT * FROM MGN_schema.MGNI where MGNI_ID = '" + id + "'";
            Statement st = connection.createStatement();
            ResultSet mgni = st.executeQuery(targetMgni);
            result = mgniResult(mgni);
        } catch (SQLException e) {
            System.out.println("SQLException :" + e.toString());
            logger.error("SQLException happen");
            return "SQLException happen";
        }
        return result;
    }
    private static String getCashiSql(String id) {
        String result="";
        try {
            String getAllCashi = "SELECT * FROM MGN_schema.Cashi where CASHI_MGNI_ID = '" + id + "'";
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(getAllCashi);
            while (resultSet.next()) {
                result += ("    存入結算帳戶帳號:" + resultSet.getString("CASHI_ACC_NO") + "\n"
                        + "    幣別:" + resultSet.getString("CASHI_CCY") + "\n"
                        + "    金額:" + resultSet.getBigDecimal("CASHI_AMT") + "\n" + "\n");
            }
        } catch (SQLException e) {
            System.out.println("SQLException :" + e.toString());
            logger.error("SQLException happen");
            return "SQLException happen";
        }
        return result;
    }

}

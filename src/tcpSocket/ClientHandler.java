package tcpSocket;

import mysql.DatabaseCRUD;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread  {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    private static final Logger logger = Logger.getLogger(MultiThreadServer.class);

    @Override
    public void run() { //啟動Thread時會執行run
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String requestStr = br.readLine();

            JSONObject jsonObject = new JSONObject(requestStr);

            String requestType = jsonObject.getString("requestType");
            JSONObject request = jsonObject.getJSONObject("request");
            //-------------------------------------------------------
            switch (requestType) {
                case "0": {
                    bw.write(DatabaseCRUD.getTargetCashi(request.getString("id")));
                    logger.info("執行Cashi查詢");
                    break;
                }
                case "1": {
                    bw.write(DatabaseCRUD.getTargetMgni(request.getString("id")));
                    logger.info("執行Mgni查詢");
                    break;
                }
                case "2": {
                    bw.write(DatabaseCRUD.dynamicQueryMgni(request));
                    logger.info("Mgni動態查詢");
                    break;
                }
                case "3": {
                    bw.write(DatabaseCRUD.createMgni(request));
                    logger.info("Mgni新增");
                    break;
                }
                case "4": {
                    bw.write(DatabaseCRUD.updateMgni(request));
                    logger.info("Mgni更新");
                    break;
                }
                case "5": {
                    bw.write(DatabaseCRUD.deleteMgni(request));
                    logger.info("資料刪除");
                    break;
                }
                default: {
                    bw.write("請輸入有效查詢資料");
                    break;
                }
            }
            bw.newLine();
            bw.flush();
            //-------------------------------------------------------
            socket.close();

        } catch (IOException e) {
            System.out.println("Socket啟動有問題 !");
            System.out.println("IOException :" + e.toString());
        } catch (JSONException | NullPointerException e) {
            try {
                OutputStream outputStream = socket.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

                System.out.println(e);
                bw.write("輸入Json格式資料有誤"+e);
                bw.newLine();
                bw.flush();

                bw.close();
                outputStream.close();

                logger.error("輸入Json格式資料有誤"+e);

            } catch (IOException ex) {
                System.out.println("Socket啟動有問題 !");
                System.out.println("IOException :" + e.toString());
            }
        }
    }
}


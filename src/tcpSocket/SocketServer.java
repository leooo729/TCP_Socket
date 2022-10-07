package tcpSocket;

import mysql.DatabaseCRUD;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1010); //服務氣端點對象
        while (true) {
//            ServerSocket serverSocket = null;
            try {
//                serverSocket = new ServerSocket(1010); //服務氣端點對象
                Socket socket = serverSocket.accept(); //監聽客服端

                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

                String requestStr = br.readLine();
                if ("end".equals(requestStr)) {
                    break;
                }
                JSONObject jsonObject = new JSONObject(requestStr);
                String requestType = jsonObject.getString("requestType");
                JSONObject request = jsonObject.getJSONObject("request");
                //-------------------------------------------------------
                switch (requestType) {
                    case "0": {
                        bw.write(DatabaseCRUD.getTargetCashi(request));
                        break;
                    }
                    case "1": {
                        bw.write(DatabaseCRUD.getTargetMgni(request));
                        break;
                    }
                    case "2": {
                        bw.write(DatabaseCRUD.dynamicQueryMgni(request));
                        break;
                    }
                    case "3": {
                        bw.write(DatabaseCRUD.deleteMgni(request));
                        break;
                    }
                    case "4": {
                        bw.write(DatabaseCRUD.createMgni(request));
                        break;
                    }
                    default: {
bw.write("請輸入有效查詢資料");
                    }
                }
                bw.newLine();
                bw.flush();
                //-------------------------------------------------------
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
//            finally {
//                if (serverSocket != null) {
//                    serverSocket.close();
//                    System.out.println("接收結束");
//                }
//            }

        }
    }
}




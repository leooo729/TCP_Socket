package tcpSocket;

import mysql.DatabaseCRUD;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {

                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

                String requestStr = br.readLine();
//                if ("end".equals(requestStr)) {
//                    break;
//                }
                JSONObject jsonObject = new JSONObject(requestStr);
                String requestType = jsonObject.getString("requestType");
                JSONObject request = jsonObject.getJSONObject("request");
                //-------------------------------------------------------
                switch (requestType) {
                    case "0": {
                        bw.write(DatabaseCRUD.getTargetCashi(request.getString("id")));
                        break;
                    }
                    case "1": {
                        bw.write(DatabaseCRUD.getTargetMgni(request.getString("id")));
                        break;
                    }
                    case "2": {
                        bw.write(DatabaseCRUD.dynamicQueryMgni(request));
                        break;
                    }
                    case "3": {
                        bw.write(DatabaseCRUD.createMgni(request));
                        break;
                    }
                    case "4": {
                        bw.write(DatabaseCRUD.updateMgni(request));
                        break;
                    }
                    case "5": {
                        bw.write(DatabaseCRUD.deleteMgni(request));
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
//            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        super.run();
    }
}


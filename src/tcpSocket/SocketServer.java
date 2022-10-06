package tcpSocket;

import mysql.DatabaseCRUD;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class SocketServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(1010); //服務氣端點對象

            Socket socket = serverSocket.accept(); //監聽客服端
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream=socket.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));

            while (true) { // (line=br.readLine())!=null

                JSONObject jsonObject = new JSONObject(br.readLine());
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
//                    case "2": {
//                        bw.write(DatabaseCRUD.getTargetMgni(request));
//                        break;
//                    }
                    case "3": {
                        bw.write(DatabaseCRUD.deleteMgni(request));
                        break;
                    }
                    default: {

                    }
                }
                bw.newLine();
                bw.flush();
                //-------------------------------------------------------
                if ("bye".equals(requestType)) {
                    break;
                }
//                request.close();
            }

            br.close();
            inputStream.close();
            serverSocket.close();
            System.out.println("接收結束");



        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }

    }
    //-----------------------------------------------Method
//private boolean sendData(OutputStream outputStream,JSONObject request){
//    BufferedWriter request = new BufferedWriter(new OutputStreamWriter(outputStream);
//    request.write(DatabaseCRUD.getTargetCashi(request.getString("id")));
//    request.newLine();
//    request.flush();
//        return true;
//}
}


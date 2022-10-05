package tcpSocket;

import mysql.DatabaseCRUD;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(1010); //服務氣端點對象

            Socket socket = serverSocket.accept(); //監聽客服端
            InputStream inputStream = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            while (true) { // (line=br.readLine())!=null
                JSONObject jsonObject = new JSONObject(br.readLine());
                String requestType = jsonObject.getString("requestType");
                JSONObject requesttype=new JSONObject("request");
                System.out.println(line);

                //-------------------------------------------------------
switch (requestType){
    case "getTargetCashi":
    {

    }
    case "":
    {

    } default:{

    }

}

                //-------------------------------------------------------
                BufferedWriter request = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                request.write(DatabaseCRUD.getTargetCashi(line));
                request.newLine();
                request.flush();


                if ("bye".equals(line)) {
                    break;
                }
//                request.close();
            }


//            br.close();
//            inputStream.close();
//            serverSocket.close();
            System.out.println("接收結束");

            //server->client


        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
    {
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
    }
}


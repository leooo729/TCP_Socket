package tcpSocket;

import mysql.DatabaseCRUD;
import org.json.JSONObject;
import org.apache.log4j.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    private static final Logger logger = Logger.getLogger(SocketServer.class);

    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure("/Users/linyulin/Downloads/TCP_Socket/src/resources/log4j.properties");
        ServerSocket serverSocket = new ServerSocket(1010); //服務氣端點對象

        while (true) {
            try {
                logger.info("666");
                Socket socket = serverSocket.accept(); //監聽客服端
                //建立一thread
                ClientThread clientThread = new ClientThread(socket);
                clientThread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}





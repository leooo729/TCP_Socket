package tcpSocket;

import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {
//    private static final Logger logger = Logger.getLogger(SocketServer.class);

    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure("/Users/linyulin/Downloads/TCP_Socket/src/resources/log4j.properties"); //設定log4j
        ServerSocket serverSocket = new ServerSocket(1010); //服務器端點對象 建立好服務端socket

        while (true) {
            try {
                Socket socket = serverSocket.accept(); //監聽客服端 等待客戶端請求  //等服務端收到請求 創建一與之匹配Socket 兩Socket就可互相通信
                //建立一新Thread
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.start();
                //new Thread(clientHandler).start();

            } catch (IOException e) {
                System.out.println("Socket啟動有問題 !");
                System.out.println("IOException :" + e.toString());            }
        }
    }
}





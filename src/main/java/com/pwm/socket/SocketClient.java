package com.pwm.socket;

import io.netty.util.internal.StringUtil;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


/**
 * 网络编程（一对一聊天）
 * 客户端
 */

public class SocketClient {

    public static void main(String[] args) throws IOException {
        Socket socket=new Socket("localhost",7000);
        ClientHandler clientHandler=new ClientHandler(socket);
        clientHandler.start();
    }


}

class ClientHandler{
    Socket socket=null;
    boolean flag=true;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    Thread sendMessage=new Thread(new Runnable() {
        @SneakyThrows
        @Override
        public void run() {
            Scanner scanner=new Scanner(System.in);
            PrintWriter printWriter=new PrintWriter(socket.getOutputStream(),true);
            while (flag){
                String message=scanner.nextLine();
                if("".equals(message)) continue;//发送内容为空，则不发送
                printWriter.println(message);
                printWriter.flush();
                if ("再见".equals(message)) {
                    flag=false;//如果发送内容为end，则表示结束通信
                    break;
                }
            }
        }
    });
    Thread getMessage=new Thread(new Runnable() {
        @SneakyThrows
        @Override
        public void run() {
            BufferedReader socketReader=new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
            while (flag){
                String serverMsg=socketReader.readLine();
                if (StringUtil.isNullOrEmpty(serverMsg) || "再见".equals(serverMsg)){ //用户结束通话，线程执行完毕
                    System.out.println("聊天结束。");
                    socketReader.close();
                    flag=false;//如果发送内容为end，则表示结束通信
                    break;
                }else{
                    System.out.println("服务端："+serverMsg);
                }
            }
        }
    });
    public void start(){
        sendMessage.start();
        getMessage.start();
    }
}

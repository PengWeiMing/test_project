package com.pwm.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 网络编程（一对一聊天）
 * 服务端
 */
public class SocketServer {

    private static List<Handler> list=new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(7000);
        System.out.println("服务器已启动!");
        while (true){
            Socket socket=serverSocket.accept();
            System.out.println(String.format("客户端连接已经创建，客户端地址："+socket.getRemoteSocketAddress()));
            Handler talkThread=new Handler(socket);
            list.add(talkThread);
            talkThread.start();
        }
    }

    static class Handler extends Thread{
        Socket socket;
        public Handler(Socket socket){
            this.socket=socket;
        }

        public synchronized void removeSocket(List<Handler> list){   //移除socket连接
            synchronized (list){//
                list.remove(this);
            }
        }

        @Override
        public void run() {
            try {
                InputStream is=this.socket.getInputStream();
                OutputStream os=this.socket.getOutputStream();
                handle(is,os);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                removeSocket(list);//线程执行完毕，移除集合
            }
            System.out.println(socket.getRemoteSocketAddress()+"客户端连接已断开");
        }

        private void handle(InputStream inputStream,OutputStream outputStream) throws IOException {
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            BufferedReader br=new BufferedReader(new InputStreamReader(inputStream,StandardCharsets.UTF_8));

            PrintWriter printWriter=new PrintWriter(socket.getOutputStream(),true);

            bw.write("连接成功\n");
            bw.flush();
            while (true){
                String clientMsg=br.readLine();  //读取客户端发送过来的消息
                System.out.println("客户端："+clientMsg);

                if ("再见".equals(clientMsg)){ //用户结束通话，线程执行完毕
                    System.out.println("聊天结束。");
                    br.close();
                    bw.close();
                    break;
                }else{
                    String serverMsg = new Scanner(System.in).nextLine();
                    if("".equals(serverMsg)) continue;//发送内容为空，则不发送
                    printWriter.println(serverMsg);
                    printWriter.flush();
                    if("再见".equals(serverMsg)){//用户结束通话，线程执行完毕
                        System.out.println("聊天结束。");
                        br.close();
                        bw.close();
                        break;
                    }
                }
            }
        }
    }

}

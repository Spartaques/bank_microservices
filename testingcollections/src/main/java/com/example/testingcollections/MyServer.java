package com.example.testingcollections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import java.util.function.BinaryOperator;

public class MyServer {
    public static void main(String[] args) {
        // створюю сервер сокет
        // роблю цикл, у якому приймаю запити від клієнт сокета
        // обробляю цей запит синхронно через IO
        // запитую OUTPUT

        try {
            ServerSocket serverSocket = new ServerSocket(12345);

            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                String stringOptional = reader.readLine();
                System.out.println(stringOptional);
                printWriter.println("hello guys");
                socket.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

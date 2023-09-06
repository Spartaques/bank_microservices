package com.example.testingcollections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Optional;

public class MyClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        try {

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.println("Hello. How are you?");

            String response = reader.readLine();

            System.out.println(response);

            reader.close();

            socket.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

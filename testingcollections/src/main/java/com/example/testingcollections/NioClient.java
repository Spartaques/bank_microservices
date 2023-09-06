package com.example.testingcollections;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class NioClient {
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        try {
            SocketChannel clientChannel = SocketChannel.open();
            clientChannel.configureBlocking(false);
            clientChannel.connect(new InetSocketAddress("localhost", 8080));

            while (!clientChannel.finishConnect()) {
                // З'єднання встановлюється
            }

            System.out.println("З'єднання з сервером успішно встановлено.");

            String message = "Привіт, сервер!";
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
            clientChannel.write(buffer);

            ByteBuffer responseBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            clientChannel.read(responseBuffer);
            responseBuffer.flip();
            byte[] responseData = new byte[responseBuffer.remaining()];
            responseBuffer.get(responseData);
            String response = new String(responseData, StandardCharsets.UTF_8);
            System.out.println("Отримано відповідь від сервера: " + response);

            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

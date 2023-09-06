package com.example.testingcollections;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); // створюємо канал для сокету
            serverSocketChannel.bind(new InetSocketAddress(8080));
            serverSocketChannel.configureBlocking(false); // переводимо в неблокуючий режим

            Selector selector = Selector.open(); // відкриваємо селектора. Селектор використовується по причині неблокуючого каналу
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // реєструємо селектор для нашого каналу, який буде обробляти події реактивно.

            System.out.println("Сервер запущений. Очікування з'єднання...");

            while (true) {
                selector.select(); // очікуємо на події. Як тільки від epoll приходить подія, код продовжує виконання

                System.out.println("YES");

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isAcceptable()) {
                        handleAccept(key, selector); // обробляємо події
                    } else if (key.isReadable()) {
                        handleRead(key);// обробляємо події
                    }
                }
                keyIterator.remove();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleAccept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("З'єднання прийнято: " + clientChannel.getRemoteAddress());
    }

    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();

        if (!clientChannel.isOpen()) {
            System.out.println("Канал закритий.");
            return;
        }

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        int bytesRead;
        try {
            bytesRead = clientChannel.read(buffer);
        } catch (ClosedChannelException e) {
            System.out.println("Канал закритий.");
            clientChannel.close();
            return;
        } catch (SocketException e) {
            System.out.println("Помилка сокету");
            clientChannel.close();
            return;
        }

        if (bytesRead == -1) {
            System.out.println("З'єднання закрите: " + clientChannel.getRemoteAddress());
            clientChannel.close();
            return;
        }

        buffer.flip();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        String message = new String(data, StandardCharsets.UTF_8);
        System.out.println("Повідомлення від " + clientChannel.getRemoteAddress() + ": " + message);

        // Відправка відповіді клієнту
        String response = "Повідомлення успішно отримано";
        ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8));
        clientChannel.write(responseBuffer);
    }
}

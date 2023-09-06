package com.example.testingcollections;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 12345;
    private static final int MAX_THREADS = 10;
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");

                // Запускаємо обробку запиту в окремому потоці
                executorService.execute(new RequestHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    private static class RequestHandler implements Runnable {
        private final Socket socket;

        public RequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Отримання потоків для зчитування та запису даних
                InputStream inputStream = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                OutputStream outputStream = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(outputStream, true);

                // Отримання повідомлення від клієнта
                String message = reader.readLine();
                System.out.println("Client Message: " + message);

                // Відправка відповіді на клієнта
                writer.println("Hello, Client!");

                // Закриття з'єднання
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

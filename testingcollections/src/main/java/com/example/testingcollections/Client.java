package com.example.testingcollections;
// Клієнтська сторона
import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
        try {
            do {
                input = scanner.nextLine();

                // Встановлення з'єднання з сервером
                Socket socket = new Socket("localhost", 12345);

                // Отримання потоків для зчитування та запису даних
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(outputStream, true);

                InputStream inputStream = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                // Відправка повідомлення на сервер
                writer.println("Hello, Server!");

                // Отримання відповіді від сервера
                String response = reader.readLine();
                System.out.println("Server Response: " + response);

                // Закриття з'єднання
                socket.close();

            } while (!input.equalsIgnoreCase("exit"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
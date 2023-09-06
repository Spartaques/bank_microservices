package com.example.testingcollections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.random.RandomGenerator;

public class NioFile {
    public static void main(String[] args) throws IOException {
        Path filePath = Paths.get("./test.txt");

        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(filePath,
                StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

            String data = "Це дані, які будуть записані в файл.";
            ByteBuffer buffer = ByteBuffer.wrap(data.getBytes(StandardCharsets.UTF_8));

            Future<Integer> operation = channel.write(buffer, 0);
            while (!operation.isDone()) {
                System.out.println("hi men");
            }

            int bytesWritten = operation.get();
            System.out.println("Записано " + bytesWritten + " байт у файл.");
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

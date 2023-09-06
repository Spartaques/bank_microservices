package com.example.testingcollections;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class TestClass {
    public static void main(String[] args)  {
        ReadableByteChannel channel = Channels.newChannel(System.in);
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        System.out.println("Enter your input:");

        try {
            while (channel.read(buffer) != -1) {
                buffer.flip();
                String input = new String(buffer.array(), 0, buffer.limit());
                System.out.println("Input received: " + input);

                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

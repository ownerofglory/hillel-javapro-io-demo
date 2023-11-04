package ua.ithillel.io;

import ua.ithillel.io.adapter.infoservice.EuropeInfoService;
import ua.ithillel.io.adapter.infoservice.InfoService;
import ua.ithillel.io.adapter.infoservice.Information;
import ua.ithillel.io.adapter.infoservice.USAInfoServiceAdapter;
import ua.ithillel.io.adapter.othersystem.USAInfoSystem;
import ua.ithillel.io.decorator.notifier.DefaultNotifier;
import ua.ithillel.io.decorator.notifier.Notifier;
import ua.ithillel.io.decorator.ownimpl.SmsNotifier;
import ua.ithillel.io.logger.*;
import ua.ithillel.io.server.NonBlockingSimpleServer;
import ua.ithillel.io.server.SimpleServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class Application {
    private static Notifier notifier;

    private static InfoService infoService;

    public static void main(String[] args)  {
        try {
            final NonBlockingSimpleServer nonBlockingSimpleServer = new NonBlockingSimpleServer(8000);
            nonBlockingSimpleServer.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Path path = Paths.get("files/file.txt");  // nio alternative to File from io

        try (final FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE);) {

            final ByteBuffer readBuffer = ByteBuffer.allocate(10);

            int read;
            final StringBuffer buffer = new StringBuffer();
            while ((read = fileChannel.read(readBuffer)) != -1) {
                readBuffer.flip();

//                final CharBuffer charBuffer = readBuffer.asCharBuffer(); // UTF-16
                final CharBuffer decode = StandardCharsets.UTF_8.decode(readBuffer);

                byte b;
                while (readBuffer.hasRemaining()) {
                    b = readBuffer.get();
                    buffer.append((char) b);
                }
//                buffer.append(new String(readBuffer.array()));

                readBuffer.clear();
            }

            System.out.println(buffer);


            final ByteBuffer writeBuffer = ByteBuffer.allocate(30);
            writeBuffer.put("Hello".getBytes());
            writeBuffer.flip();
//            final ByteBuffer writeBuffer = ByteBuffer.wrap("Hello!".getBytes());

            fileChannel.write(writeBuffer);


            writeBuffer.put("Hello again".getBytes());
            writeBuffer.flip();

            fileChannel.write(writeBuffer);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            final SimpleServer simpleServer = new SimpleServer(8000);
            simpleServer.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        final LoggerConfig loadedConfig = FileLoggerConfigLoader.load("");
        Logger logger = new FileLogger(loadedConfig);

        // pasrse file
        // create map

        // shanpshot
        final FileLoggerConfig config = FileLoggerConfig.builder()
                .setFormat("[%s]")
                .setPath("log.txt")
                .build();

        final String format = config.getFormat();

        FileLoggerConfig.builder()
                .setFormat(config.getFormat())
                .setPath("new path");

        // d rwx rwx rwx
        // - rw- r-- r-- 644

        // - r-- r-- r-- 444
        // - rw- rw- rw- 666

        // represents file's metadata
        File file = new File("files/out.txt");
        if(file.exists()) {

            if (!file.canWrite()) {
                file.setWritable(true, true);
            }
        } else {
//            file.createNewFile();
        }

        try (
                // Byte input stream
                InputStream in = new BufferedInputStream(new FileInputStream("files/file.txt"));
                // Character reader
                Reader reader = new BufferedReader(new FileReader("files/file.txt"));

                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("files/out.txt", true));

                // Opens a server socket
                // binds the port 8000 to the current process
                ServerSocket serverSocket = new ServerSocket(8000);
        ) {


            // awaits a TCP connection on port 8080 and accepts it
            // creates a socket object for the connection
            final Socket connection = serverSocket.accept();
            // gets the socket's input stream
            final InputStream sin = connection.getInputStream();

            // reads from the socket
            String textFromSocket = readText(sin);
            System.out.println("Text from socket: " + textFromSocket);

//            final BufferedOutputStream sout = new BufferedOutputStream(connection.getOutputStream());
//            writeText(sout, "Hello from Java");

            // gets the socket's output stream
            OutputStream outputStream = connection.getOutputStream();
            // create an adapter OutputStream -> Writer
            Writer outputStreamWriter = new OutputStreamWriter(outputStream);
            // writes text into the sockets output stream
            writeText(new BufferedWriter(outputStreamWriter), "Hello with Writer");


            // reads text from the input stream (in this case a file)
            final String text = readText(in); // IOException !!!

            // writers text into in output stream (in this case a file)
            writeText(out, "Hello from java");

        } catch (FileNotFoundException e) {
            // handle the exception when no file was found
            // or user has insufficient access rights

            System.out.println("Unable to find the file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Unable to read/ write from/to file: " + e.getMessage());
        }


        // Adapter Demo
        // reads the value of environment variable 'REGION'
        String region = System.getenv("REGION");

        if (region.equals("Europe")) {
            infoService = new EuropeInfoService();
        } else if (region.equals("USA")) {
            // adapter
            infoService = new USAInfoServiceAdapter(new USAInfoSystem());
        }

        final Information ivanPetrenko = infoService.getInfo("Ivan Petrenko");



        // Decorator Demo
        notifier = new SmsNotifier(new DefaultNotifier());

        notifier.doNotify("Hello");

        System.out.println(Arrays.toString(args));

        final String variable1 = System.getenv("VARIABLE_1");
        System.out.printf("VARIABLE_1: %s%n", variable1);
    }

    private static void writeText(BufferedOutputStream outputStream, String text) throws IOException {
        final byte[] bytes = text.getBytes();

        outputStream.write(bytes);
        outputStream.flush();
    }

    private static void writeText(BufferedWriter writer, String text) throws IOException {
        writer.write(text);
        writer.flush();
    }

    private static String readText(InputStream inputStream) throws IOException {
        String text = "";

//        int read; // 0...255; -1
//        while ((read = inputStream.read()) != -1) {
//            text += (char) read;
//        }
////
        byte[] buffer = new byte[10];
        int count;// count; -1
        while ((count = inputStream.read(buffer)) != -1) {
            final String s = new String(Arrays.copyOf(buffer, count));
            text += s;
        }

        return text;
    }

    private static String readText(Reader reader) throws IOException {
        char[] buf = new char[10];
        int read;

        final StringBuffer buffer = new StringBuffer();
        while ((read = reader.read(buf)) != -1) {
            buffer.append(Arrays.copyOf(buf, read));
        }

        return buffer.toString();
    }
}

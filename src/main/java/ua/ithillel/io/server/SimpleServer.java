package ua.ithillel.io.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;

public class SimpleServer {
    private final ServerSocket serverSocket;

    public SimpleServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public void start() {

        while (true) {
            try (
                    final Socket connection = this.serverSocket.accept();
                    final InputStream inputStream = connection.getInputStream();
                    final OutputStream outputStream = connection.getOutputStream();

                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream));


                    final InputStream fileIn = getClass().getClassLoader().getResourceAsStream("index.html");
                    Reader fileReader = new InputStreamReader(fileIn);
            ) {

                final String siteText = readTextFile(fileReader);

                // success response
                final StringBuffer buffer = new StringBuffer();
                buffer.append("HTTP/1.1 200 OK\r\n");
                buffer.append("Content-Type: text/html\r\n");
                buffer.append("\r\n");
                buffer.append(siteText);

                writer.println(buffer.toString());
                writer.flush();


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String readTextFile(Reader fileReader) {
        final BufferedReader bufferedReader = new BufferedReader(fileReader);

        final Optional<String> reduced = bufferedReader.lines()
                .reduce((acc, cur) -> acc + cur + "\n");

        return reduced.orElse("");
    }
}

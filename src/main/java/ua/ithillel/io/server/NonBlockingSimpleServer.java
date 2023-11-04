package ua.ithillel.io.server;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class NonBlockingSimpleServer {
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;

    public NonBlockingSimpleServer(int port) throws IOException {
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.bind(new InetSocketAddress(port));
        this.serverSocketChannel.configureBlocking(false);

        this.selector = Selector.open();
        this.serverSocketChannel.register(selector, serverSocketChannel.validOps());

    }

    public void start() {
        while (true) {
            try {
//        final SocketChannel accept = serverSocketChannel.accept();
                final int select = selector.select();
                final Set<SelectionKey> selectionKeys = selector.selectedKeys();
                final Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {
                    // check the key
                    final SelectionKey key = iterator.next(); // signal

                    // ready to connect
                    if (key.isAcceptable()) {
                        // get connection
                        final SocketChannel connection = serverSocketChannel.accept();
                        connection.configureBlocking(false);
                        connection.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    }

                    // read to read
                    if (key.isReadable()) {
                        final SocketChannel channel = (SocketChannel) key.channel();
                        final ByteBuffer buffer = ByteBuffer.allocate(256);

                        channel.read(buffer);
                        buffer.flip();

                        final CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer);
                        System.out.println(charBuffer);

                    }

                    // ready to write
                    if (key.isWritable()) {
                        final SocketChannel channel = (SocketChannel) key.channel();

                        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream("index.html");
                        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        final String siteContent = readTextFile(inputStreamReader);
                        // success response
                        final StringBuffer buffer = new StringBuffer();
                        buffer.append("HTTP/1.1 200 OK\r\n");
                        buffer.append("Content-Type: text/html\r\n");
                        buffer.append("\r\n");
                        buffer.append(siteContent);

                        final ByteBuffer wrap = ByteBuffer.wrap(buffer.toString().getBytes());
                        channel.write(wrap);

                        channel.close();
                    }


                    iterator.remove();
                }

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

package ind.sq.study.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SocketTest {


    void init() throws IOException {
        var serverSocket = ServerSocketChannel.open();
        var address = new InetSocketAddress(9696);
        serverSocket.bind(address);

        var selector = Selector.open();
        new Dispatcher(selector).start();
        System.out.println("Dispatcher thread started");
        new Acceptor(serverSocket, selector).start();
        System.out.println("Acceptor thread started");

    }


    public static void main(String[] args) throws IOException {

        new SocketTest().init();

    }

    class Acceptor extends Thread {
        private ServerSocketChannel socket;
        private Selector selector;

        Acceptor(ServerSocketChannel socket, Selector selector) {
           this.socket = socket;
           this.selector = selector;
        }

        @Override
        public void run() {

            while (true) {
                try {
                    var channel = this.socket.accept();
                    channel.configureBlocking(false);
                    channel.register(this.selector, SelectionKey.OP_READ| SelectionKey.OP_WRITE );
                    System.out.println(channel + " connected ");
//                    selector.wakeup();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        }
    }

    class Dispatcher extends Thread {

        private Selector selector;

        Dispatcher(Selector selector) {
            this.selector = selector;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    var ops = selector.select(1000);
//                    System.out.println(ops + " ops selected");
//                    if (ops == 0) {
//                        System.out.println("Continue");
//                        continue;
//                    }
                    var keys = selector.selectedKeys();
                    var iter = keys.iterator();
                    var buffer = ByteBuffer.allocate(128);
                    while(iter.hasNext()) {
                        var key = iter.next();
                        iter.remove();
                        if (key.isReadable()) {
                            System.out.println(key.channel() + " is ready for read " + key);
                            buffer.clear();
                            var readBytes = ((SocketChannel)key.channel()).read(buffer);
                            System.out.println(readBytes + " bytes read from channel");

                            buffer.flip();
                            while(buffer.hasRemaining()) {
                                System.out.print( (char)buffer.get());
                            }
                            System.out.println();

                        } else if (key.isWritable()) {
//                            System.out.println(key.channel() + " is ready for write " + key);

                        } else {
                            System.out.println("Unknown key: " + key);
                        }
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

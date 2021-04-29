package ind.sq.study.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

/**
 * @author Sun Qin
 * @since 2021/3/15
 */
public class RandomAccessFileTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile(new File("/tmp/rftest"), "rw");

        MappedByteBuffer mappedByteBuffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 50L * 10);
        mappedByteBuffer.put(1, (byte)0xFF);


        var map = new HashMap<String, String> ();
        map.computeIfAbsent("a", v-> v + "b");
        System.out.println(map);
    }
}

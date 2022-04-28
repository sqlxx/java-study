package ind.sq.study.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class NameServiceTest {

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        for (int i = 0; i <= 100; i++) {
            var addresses = InetAddress.getAllByName("bi.maycur.com");
            Arrays.stream(addresses).forEach(address -> System.out.println(address.getHostAddress()));
            Thread.sleep(100);
        }
    }
}

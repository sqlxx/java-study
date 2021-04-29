package ind.sq.study.ice;

import com.maycur.common.ice.PropertyIceClient;
import com.maycur.ice.idgeneration.IdGenerationServicePrx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by sqlxx on 2019-07-17.
 * Copyright to Maycur Tech.
 */
public class IdGenerator {

    private static final Logger logger = LoggerFactory.getLogger(IdGenerator.class);

    enum test {
        E1, E2
    };
    public static void main(String[] args) throws InterruptedException {
//        var props = new String[]{"--Ice.Trace.Network=2", "--Ice.Trace.Locator=2", "--Ice.Trace.Protocol=0"};
//        var communicator = com.zeroc.Ice.Util.initialize(props);
//        var objectPrx = communicator.stringToProxy("IdGenerationService:default -h localhost -p 9100");
//        objectPrx = objectPrx.ice_connectionCached(false);
//        objectPrx = objectPrx.ice_endpointSelection(EndpointSelectionType.Random);
//
//        logger.info("{}", objectPrx.ice_getEndpoints());
//        var proxy = IdGenerationServicePrx.checkedCast(objectPrx);
//
//        for (int i = 0; i < 2; i ++) {
//            try {
//                var param = new MIceTestParam();
//                param.subsidiaryCode = "s_code";
//                param.departmentCode = "d_code";
//                param.setUserCode(null);
//                proxy.testIt(param);
//            } catch (Exception ex) {
//                logger.info("Ignored exception, {}", ex.getMessage(), ex);
//            }
//            Thread.sleep(1000);
//        }

        var iceClient = new PropertyIceClient();
        var env = new HashMap<String, String>();
        env.put("ice.service.endpoint.IdGenerationService", "default -h localhost -p 9100");
        iceClient.setEnvironment(env);
        iceClient.init();
        iceClient.addContextEnhancers(new Supplier<Map<String, String>>() {
            @Override
            public Map<String, String> get() {
                return SecurityContext.getContext();
            }
        });
        var prx = (IdGenerationServicePrx) iceClient.getServiceProxy(IdGenerationServicePrx.class);
        var prx2 = (IdGenerationServicePrx) iceClient.getServiceProxy(IdGenerationServicePrx.class, 100);
        var prx3 = (IdGenerationServicePrx) iceClient.getServiceProxy(IdGenerationServicePrx.class, 100);
        System.out.println(prx2 == prx3);
        System.out.println(prx.getUniqueId());
        System.out.println(prx2.getUniqueId());






    }
}

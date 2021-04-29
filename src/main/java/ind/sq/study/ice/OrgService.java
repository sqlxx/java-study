package ind.sq.study.ice;

import com.maycur.ice.organization.*;
import com.zeroc.Ice.EndpointSelectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;


/**
 * Created by sqlxx on 2019-09-16.
 * Copyright to Maycur Tech.
 */
public class OrgService {

    private static final Logger logger = LoggerFactory.getLogger(OrgService.class);

    public static void main(String[] args) throws InterruptedException, MIceDepartmentServiceException {
        var props = new String[]{"--Ice.Trace.Network=2", "--Ice.Trace.Locator=2", "--Ice.Trace.Protocol=0"};
        var communicator = com.zeroc.Ice.Util.initialize(props);
        var objectPrx = communicator.stringToProxy("DepartmentServant:default -h localhost -p 9023");
        objectPrx = objectPrx.ice_connectionCached(false);
        objectPrx = objectPrx.ice_endpointSelection(EndpointSelectionType.Random);

        logger.info("{}", objectPrx.ice_getEndpoints());
        var proxy = DepartmentServantPrx.checkedCast(objectPrx);

//        for (int i = 0; i < 200; i ++) {
//            try {
//                MIceEmployee employee = proxy.getByEmployeeId("EC1602151J4OHCAO", "1");
//                logger.info(employee.phoneNo);
//            } catch (Exception ex) {
//                logger.info("Ignored exception, {}", ex.getMessage());
//            }
//        }
        var es = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i ++){
            es.submit(() -> {
                for (int j = 0; j < 200; j++) {
                    MIceDepartment department = new MIceDepartment();
                    department.setBusinessCode("223");
                    department.setName("测试部2");
                    department.setLevel(0);
                    department.setPrinciple("1");
                    department.setParentCode("111");
                    department.setCostCenterCode("100001");
                    try {
                        proxy.saveDepartment("EC1602151J4OHCAO", "111", department, true);
                    } catch (Exception ex) {
                        logger.info( j + ":" + ex.getMessage());
                    }

                }

            });
        }


    }


}

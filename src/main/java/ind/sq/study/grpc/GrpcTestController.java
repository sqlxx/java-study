package ind.sq.study.grpc;

import com.google.protobuf.Message;
import io.grpc.CallOptions;
import io.grpc.ClientCall;
import io.grpc.ManagedChannelBuilder;
import io.grpc.MethodDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

@RestController
public class GrpcTestController {
    private static final Logger logger = LoggerFactory.getLogger(GrpcTestController.class);

    @GetMapping("/test/{service}/{method}")
    public Object testGrpc(String service, String method) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        logger.info("The service name is {}, the method is {}", service, method);
//        var clazz = Class.forName(service + "Stub");
//        var stub = clazz.getDeclaredConstructor().newInstance();
//        var serviceDesc = IdGenerationServiceGrpc.getServiceDescriptor();
//        var methods = serviceDesc.getMethods();

//        for (var met: methods) {
//            logger.info("full method name is {}", met.getFullMethodName());
//            var type = met.getType();
//            var builder = met.toBuilder();
//            var requestMarshaller = met.getRequestMarshaller();
//            var channel = ManagedChannelBuilder.forTarget("localhost:9900").build();
//            var call = channel.<Message, Message>newCall((MethodDescriptor<Message, Message>) met, CallOptions.DEFAULT);
//            call.start(new ClientCall.Listener<Message>() {
//                @Override
//                public void onMessage(Message message) {
//                    logger.info("!!!On Message {}", message);
//                }
//            }, null);
//            call.halfClose();
//            call.request(1);
//
//            logger.info("type name is {}", type.name());
//            var schemaDescriptor = met.getSchemaDescriptor();
//            logger.info("schema descriptor is {}", schemaDescriptor);
//        }
//        var messageTypes = IdGeneration.getDescriptor().getMessageTypes();
//        for (var msgType : messageTypes) {
//            System.out.println(msgType.getFullName());
//        }
//        JsonFormat.parser().merge(body, );


        return "success";
//        var methodToCall = clazz.getDeclaredMethod(method);
//        return methodToCall.invoke(stub);

    }
}

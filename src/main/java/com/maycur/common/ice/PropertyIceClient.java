package com.maycur.common.ice;

import com.zeroc.Ice.ObjectPrx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;

import java.util.Map;

/**
 * @author Sun Qin
 * @since 2021/1/4
 */
public class PropertyIceClient extends BaseIceClient {
    private static final Logger logger = LoggerFactory.getLogger(LocalIceClient.class);

    private static final String ENDPOINT_CONFIG_PREFIX = "ice.service.endpoint.";

    private Map<String, String> environment;

    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }

    @Override
    boolean endpointWillChange() {
        return false;
    }

    @Override
    protected String getEndPoint(Class<? extends ObjectPrx> clazz, String serviceName) {
        logger.warn("Reading service information from configuration file is only for local development usage.");
        String instanceName = IceUtil.getInstanceName(clazz);

        String endpoint = doGetEndPoint(instanceName);

        if (StringUtils.isBlank(endpoint)) {
            endpoint = doGetEndPoint(serviceName);
        }

        if (StringUtils.isBlank(endpoint)) {
            logger.error("Can't find endpoint configuration for {}", instanceName);
            throw new IllegalArgumentException("Can't find endpoint for " + instanceName);
        }

        return endpoint;
    }

    private String doGetEndPoint(String serviceName) {
        try {
            String endpoint = null;

            if (environment != null) {
                endpoint = environment.get(ENDPOINT_CONFIG_PREFIX + serviceName);
            }

            return endpoint;
        } catch (NoSuchMessageException ex) {
            // ignore
            return null;
        }
    }
}

package com.jd.journalq.broker.manage.exporter;

import com.google.common.collect.Maps;
import com.jd.journalq.broker.manage.config.BrokerManageConfig;
import com.jd.journalq.broker.manage.exporter.vertx.RoutingVerticle;
import com.jd.journalq.toolkit.service.Service;
import com.jd.laf.web.vertx.Environment;
import io.vertx.core.http.HttpServerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * BrokerManageExportServer
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2018/10/16
 */
public class BrokerManageExportServer extends Service {

    protected static final Logger logger = LoggerFactory.getLogger(BrokerManageExportServer.class);

    private BrokerManageConfig config;
    private RoutingVerticle routingVerticle;
    private Map<String, Object> serviceMap = Maps.newHashMap();

    public BrokerManageExportServer(BrokerManageConfig config) {
        this.config = config;
    }

    public void registerServices(Map<String, Object> serviceMap) {
        this.serviceMap.putAll(serviceMap);
    }

    public void registerService(String key, Object service) {
        serviceMap.put(key, service);
    }

    protected RoutingVerticle initRoutingVerticle(BrokerManageConfig config) {
        HttpServerOptions httpServerOptions = new HttpServerOptions();
        httpServerOptions.setHost(config.getExportHost());
        httpServerOptions.setPort(config.getExportPort());
        return new RoutingVerticle(new Environment.MapEnvironment(), httpServerOptions);
    }

    @Override
    protected void validate() throws Exception {
        this.routingVerticle = initRoutingVerticle(config);
    }

    @Override
    protected void doStart() throws Exception {
        try {
            routingVerticle.registerServices(serviceMap);
            routingVerticle.start();
            logger.info("broker manage server is started, host: {}, port: {}", config.getExportHost(), config.getExportPort());
        } catch (Exception e) {
            logger.error("broker manage server start exception", e);
            throw e;
        }
    }

    @Override
    protected void doStop() {
        try {
            routingVerticle.stop();
        } catch (Exception e) {
            logger.error("broker manage server stop exception", e);
        }
    }
}
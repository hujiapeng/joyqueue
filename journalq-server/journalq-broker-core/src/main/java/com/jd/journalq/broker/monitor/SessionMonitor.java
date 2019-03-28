package com.jd.journalq.broker.monitor;

import com.jd.journalq.monitor.Client;
import com.jd.journalq.network.session.Connection;
import com.jd.journalq.network.session.Consumer;
import com.jd.journalq.network.session.Producer;

import java.util.List;

public interface SessionMonitor {


    /**
     * 增加生产者
     * @param producer 生产者
     */
    void addProducer(Producer producer);

    /**
     * 增加Consumer
     * @param consumer 消费者对象
     */
    void addConsumer(Consumer consumer);

    /**
     * 移除生产者
     * @param producer 生产者
     */
    void removeProducer(Producer producer);

    /**
     * 移除消费者
     * @param consumer 消费者
     */
    void removeConsumer(Consumer consumer);

    /**
     * 获取当前生产者数量
     *
     * @param topic 主题
     * @param app   应用
     * @return 当前生产者数量
     */
    int getProducer(String topic, String app);

    /**
     * 获取当前消费者数量
     *
     * @param topic 主题
     * @param app   应用
     * @return 当前消费者数量
     */
    int getConsumer(String topic, String app);

    /**
     * 增加连接明细
     *
     * @param connection 连接内容
     */
    void addConnection(Connection connection);

    /**
     * 移除连接明细
     *
     * @param connection 连接内容
     */
    void removeConnection(Connection connection);

    /**
     * 获取连接明细
     *
     * @param topic 主题
     * @param app   应用
     */
    List<Client> getConnections(String topic, String app);
}
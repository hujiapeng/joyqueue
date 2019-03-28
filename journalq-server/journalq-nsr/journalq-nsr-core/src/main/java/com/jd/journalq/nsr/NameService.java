package com.jd.journalq.nsr;

import com.jd.journalq.domain.*;
import com.jd.journalq.event.NameServerEvent;
import com.jd.journalq.toolkit.concurrent.EventListener;
import com.jd.journalq.toolkit.lang.LifeCycle;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lixiaobin6
 */
public interface NameService extends LifeCycle {

    /**
     * 订阅
     */
    TopicConfig subscribe(Subscription subscription, ClientType clientType);

    /**
     * 订阅(mqtt)
     */
    List<TopicConfig> subscribe(List<Subscription> subscriptions, ClientType clientType);

    /**
     * 取消订阅
     */
    void unSubscribe(Subscription subscription);

    /**
     * 批量取消订阅
     */
    void unSubscribe(List<Subscription> subscriptions);

    /**
     * 是否有订阅
     *
     * @param app
     * @param subscribe
     * @return
     */
    boolean hasSubscribe(String app, Subscription.Type subscribe);

    /**
     * raft选举结果通知nameserver
     *
     * @param topic
     * @param partitionGroup partitionGroup
     * @param leaderBrokerId leaderBrokerId
     * @param isrId          isrId
     * @param termId         termId
     */
    void leaderReport(TopicName topic, int partitionGroup, int leaderBrokerId, Set<Integer> isrId, int termId);

    /**
     * broker启动获取该broker上所有的topic相关配置信息
     *
     * @param brokerId
     */
    Broker getBroker(int brokerId);


    /**
     * 获取所有Broker
     *
     * @return
     */
    List<Broker> getAllBrokers();

    /**
     * 添加主题
     */
    void addTopic(Topic topic, List<PartitionGroup> partitionGroups);

    /**
     * 获取topicConfig
     *
     * @param topic
     * @return
     */
    TopicConfig getTopicConfig(TopicName topic);

    /**
     * 获取所有主题
     *
     * @return
     */
    Set<String> getAllTopics();

    /**
     * 获取app订阅的topic
     *
     * @param app
     * @param subscription
     * @return
     */
    Set<String> getTopics(String app, Subscription.Type subscription);

    /**
     * 获取topicConfig
     *
     * @param brokerId
     * @return
     */
    Map<TopicName, TopicConfig> getTopicConfigByBroker(Integer brokerId);

    /**
     * 注册broker(用户启动时候)
     *
     * @param brokerId
     * @param brokerIp
     * @return
     */
    Broker register(Integer brokerId, String brokerIp, Integer port);

    /**
     * 获取ProducerConfig信息
     *
     * @param topic
     * @param app
     * @return
     */
    Producer getProducerByTopicAndApp(TopicName topic, String app);

    /**
     * 获取ProducerConfig信息
     *
     * @param topic
     * @param app
     * @return
     */
    Consumer getConsumerByTopicAndApp(TopicName topic, String app);

    /**
     * 根据app获取topic信息
     *
     * @param subscribeApp
     * @return
     */
    Map<TopicName, TopicConfig> getTopicConfigByApp(String subscribeApp, Subscription.Type subscribe);

    /**
     * 根据ip获取dataCenter
     *
     * @param ip
     * @return
     */
    DataCenter getDataCenter(String ip);

    /**
     * 获取k-v配置
     *
     * @param group
     * @param key
     * @return
     */
    String getConfig(String group, String key);

    /**
     * 获取k-v配置
     *
     * @return
     */
    List<Config> getAllConfigs();

    /**
     *
     * @param retryType
     * @return
     */
    List<Broker> getBrokerByRetryType(String retryType);

    /**
     * 根据topic该topic相关的获取消费配置
     *
     * @param topic
     * @return
     */
    List<Consumer> getConsumerByTopic(TopicName topic);

    List<Producer> getProducerByTopic(TopicName topic);

    List<Replica> getReplicaByBroker(Integer brokerId);

    AppToken getAppToken(String app, String token);

    void addListener(EventListener<NameServerEvent> listener);

    void removeListener(EventListener<NameServerEvent> listener);

    void addEvent(NameServerEvent event);

    enum Subscribe {
        Produce(1),
        Consumer(2);
        private int type;

        private Subscribe(int type) {
            this.type = type;
        }

        public static Subscribe valueOf(int type) {
            switch (type) {
                case 1:
                    return Produce;
                case 2:
                    return Consumer;
                default:
                    return null;
            }
        }

        public int type() {
            return type;
        }
    }
}
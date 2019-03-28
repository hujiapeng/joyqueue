package com.jd.journalq.client.internal.consumer.support;

import com.google.common.collect.Lists;
import com.jd.journalq.client.internal.consumer.BaseMessageListener;
import com.jd.journalq.client.internal.consumer.BatchMessageListener;
import com.jd.journalq.client.internal.consumer.MessageListener;

import java.util.List;

/**
 * MessageListenerManager
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2018/12/25
 */
public class MessageListenerManager {

    private List<MessageListener> listeners = Lists.newLinkedList();
    private List<BatchMessageListener> batchListeners = Lists.newLinkedList();

    public void addListener(BaseMessageListener messageListener) {
        if (messageListener instanceof MessageListener) {
            listeners.add((MessageListener) messageListener);
        } else if (messageListener instanceof BatchMessageListener) {
            batchListeners.add((BatchMessageListener) messageListener);
        }
    }

    public void removeListener(BaseMessageListener messageListener) {
        listeners.remove(messageListener);
        batchListeners.remove(messageListener);
    }

    public boolean isEmpty() {
        return listeners.isEmpty() && batchListeners.isEmpty();
    }

    public List<MessageListener> getListeners() {
        return listeners;
    }

    public List<BatchMessageListener> getBatchListeners() {
        return batchListeners;
    }

}
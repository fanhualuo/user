package com.hehe.common.event;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * 异步执行工具类
 *
 * @author xieqinghe .
 * @date 2017/11/15 下午1:39
 * @email qinghe101@qq.com
 */
@Slf4j
@Component
public class CoreEventDispatcher {
    protected final EventBus eventBus;

    @Autowired
    private ApplicationContext applicationContext;

    public CoreEventDispatcher() {
        this(Runtime.getRuntime().availableProcessors() + 1);
    }

    public CoreEventDispatcher(Integer threadCount) {
        eventBus = new AsyncEventBus(Executors.newFixedThreadPool(threadCount));
    }

    @PostConstruct
    public void registerListeners() {
        Map<String, EventListener> listeners = applicationContext.getBeansOfType(EventListener.class);
        for (EventListener eventListener : listeners.values()) {
            eventBus.register(eventListener);
        }
    }

    /**
     * 发布事件
     */
    public void publish(Object event) {
        eventBus.post(event);
    }

}

package com.next.interview.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: Administrator
 * @Date: 2020/5/23 22:03
 * @Desc: 分布式锁
 */
@Configuration
@Slf4j
public class DistributedLock {

    private CuratorFramework client = null;

    private static final String ZK_LOCK = "adu-zk-locks";

    private static final String DISTRIBUTED_LOCK = "adu-distributed-lock";

    private static CountDownLatch countDownLatch = new CountDownLatch(1); // 每次是往下减的

    public DistributedLock() {
        client = CuratorFrameworkFactory.builder()
                .connectString("node01:2181")
                .sessionTimeoutMs(10000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                .namespace("zk-namespace")
                .build();
        client.start();
    }

    @Bean
    public CuratorFramework getClient() {
        client = client.usingNamespace("zk-namespace");

        try {
            if (client.checkExists().forPath("/" + ZK_LOCK) == null) {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE);
            }
            addWatch("/" + ZK_LOCK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }

    private void addWatch(String path) throws Exception {
        PathChildrenCache cache = new PathChildrenCache(client, path, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    String path = event.getData().getPath();
                    if (path.contains(DISTRIBUTED_LOCK)) {
                        // 等待
                        countDownLatch.countDown();
                    }
                }
            }
        });
    }

    // 获得分布式锁
    public void getLock() {
        while (true) {
            try {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.EPHEMERAL) // 临时的
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/" + ZK_LOCK + "/" + DISTRIBUTED_LOCK);
                log.info("分布式锁获得成功......");
            } catch (Exception e) {
                e.printStackTrace();
                if (countDownLatch.getCount() <= 0) {
                    countDownLatch = new CountDownLatch(1); // 放1,还原回去
                }
                try {
                    countDownLatch.await();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            return;
        }
    }

    // 释放分布式锁： 订单创建成功 或者 异常的时候释放锁
    public boolean releaseLock() {
        try {
            if (client.checkExists().forPath("/" + ZK_LOCK + "/" + DISTRIBUTED_LOCK) != null) {
                client.delete().forPath("/" + ZK_LOCK + "/" + DISTRIBUTED_LOCK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        log.info("分布式锁释放成功......");
        return true;
    }
}

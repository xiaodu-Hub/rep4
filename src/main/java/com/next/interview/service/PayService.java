package com.next.interview.service;

import com.next.interview.utils.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Administrator
 * @Date: 2020/5/23 19:43
 * @Desc:
 */
@Service
@Slf4j
public class PayService {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private DistributedLock distributedLock;

    public boolean buy2(String itemId) {

        distributedLock.getLock(); // 拿到锁

        // 假设每次购买 9 个
        int buyCount = 9;

        // step1 拿到库存的数量
        int count = itemsService.getItemCounts(itemId);
        if (count < buyCount) {
            log.error("库存不足,下单失败, 购买数{}件,库存只有{}件", buyCount, count);
            distributedLock.releaseLock();
            return false;
        }

        // step2 创建订单
        boolean flag = ordersService.save(itemId);

        // TODO:  模拟高并发场景
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            distributedLock.releaseLock();
            e.printStackTrace();
        }


        //  step3 扣库存
        if (flag) {
            itemsService.reduceCount(itemId, buyCount);
            distributedLock.releaseLock();
        } else {
            log.error("订单创建失败...");
            distributedLock.releaseLock();
            return false;
        }
        return true;
    }


    public boolean buy(String itemId) {
        // 假设每次购买 9 个
        int buyCount = 9;

        // step1 拿到库存的数量
        int count = itemsService.getItemCounts(itemId);
        if (count < buyCount) {
            log.error("库存不足,下单失败, 购买数{}件,库存只有{}件", buyCount, count);
            return false;
        }

        // step2 创建订单
        boolean flag = ordersService.save(itemId);

        // TODO:  模拟高并发场景
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //  step3 扣库存
        if (flag) {
            itemsService.reduceCount(itemId, buyCount);
        } else {
            log.error("订单创建失败...");
            return false;
        }
        return true;
    }
}

package com.next.interview.service;

import com.next.interview.dao.OrdersDAO;
import com.next.interview.domain.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author: Administrator
 * @Date: 2020/5/23 18:44
 * @Desc:
 */
@Service
@Slf4j
public class OrdersService {

    @Autowired
    private OrdersDAO ordersDAO;

    public boolean save(String itemId){
        try {
            Orders o = new Orders();
            o.setId(UUID.randomUUID().toString());
            o.setItemId(itemId);

            ordersDAO.save(o);
            log.info("订单创建成功...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

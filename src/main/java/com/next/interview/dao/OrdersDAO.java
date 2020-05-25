package com.next.interview.dao;

import com.next.interview.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Administrator
 * @Date: 2020/5/23 18:09
 * @Desc:
 */
public interface OrdersDAO extends JpaRepository<Orders,String> {

}

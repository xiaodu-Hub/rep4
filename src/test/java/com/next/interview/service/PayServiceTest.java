package com.next.interview.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: Administrator
 * @Date: 2020/5/23 20:22
 * @Desc:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PayServiceTest {

    @Autowired
    private PayService payService;

    @Test
    public void testBuy() {
        payService.buy("1");
    }
}

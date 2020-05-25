package com.next.interview.controller;

import com.next.interview.service.PayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Administrator
 * @Date: 2020/5/23 20:45
 * @Desc:
 */
@RestController
public class BuyController {
    @Autowired
    private PayService payService;

    @GetMapping("/buy")
    @ResponseBody
    public String buy(String itemId) {
        if (StringUtils.isNotBlank(itemId)) {
            if (payService.buy2(itemId)) {
                return "订单创建成功了......";
            } else {
                return "订单创建失败了......";
            }
        } else {
            return "条目id不能为空";
        }
    }

    @GetMapping("/buy2")
    @ResponseBody
    public String buy2(String itemId) {
        if (StringUtils.isNotBlank(itemId)) {
            if (payService.buy2(itemId)) {
                return "订单创建成功了......";
            } else {
                return "订单创建失败了......";
            }
        } else {
            return "条目id不能为空";
        }
    }
}
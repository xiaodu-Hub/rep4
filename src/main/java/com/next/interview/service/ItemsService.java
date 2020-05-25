package com.next.interview.service;

import com.next.interview.dao.ItemsDAO;
import com.next.interview.domain.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author: Administrator
 * @Date: 2020/5/23 18:14
 * @Desc:
 */
@Service
public class ItemsService {

    @Autowired
    private ItemsDAO itemsDAO;

    public Items getItem(String itemId){
        return itemsDAO.findOne(itemId);
    }

    public void save(Items items){
        items.setId(UUID.randomUUID().toString());
        itemsDAO.save(items);
    }

    // 根据itemId获取库存量
    public int getItemCounts(String itemId){
        return itemsDAO.findOne(itemId).getCounts();
    }

    // 调整库存，必须减少
    public void reduceCount(String itemId, int count){
        Items items = getItem(itemId);
        items.setCounts(items.getCounts() - count);
        itemsDAO.save(items);
    }
}

package com.next.interview.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: Administrator
 * @Date: 2020/5/23 17:48
 * @Desc:
 */
@Entity
@Table
public class Orders {

    @Id
    private String id;

    private String itemId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}

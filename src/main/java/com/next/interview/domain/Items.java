package com.next.interview.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: Administrator
 * @Date: 2020/5/23 17:49
 * @Desc:
 */
@Entity
@Table // 既可以自动建表了
public class Items {

    @Id
    private String id;

    private String name;

    private Integer counts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }
}

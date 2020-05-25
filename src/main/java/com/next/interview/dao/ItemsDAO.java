package com.next.interview.dao;

import com.next.interview.domain.Items;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: Administrator
 * @Date: 2020/5/23 18:12
 * @Desc:
 */
public interface ItemsDAO extends JpaRepository<Items,String> {

}

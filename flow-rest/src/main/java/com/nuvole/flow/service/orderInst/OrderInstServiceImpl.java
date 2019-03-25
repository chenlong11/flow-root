package com.nuvole.flow.service.orderInst;

import com.nuvole.flow.domain.OrderInst;
import com.nuvole.flow.mapper.OrderInstMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderInstServiceImpl implements OrderInstService {

    @Autowired
    private OrderInstMapper orderInstMapper;

    @Override
    public boolean insert(OrderInst orderInst) {
        int i = orderInstMapper.insertSelective(orderInst);
        if (i < 0) {
            return true;
        } else {
            return false;
        }
    }

}

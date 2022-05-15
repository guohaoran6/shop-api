package io.recruitment.assessment.api.service;

import io.recruitment.assessment.api.dto.OrderItem;
import io.recruitment.assessment.api.entity.OrderItemEntity;
import io.recruitment.assessment.api.repository.OrderItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    OrderItemRepository orderItemRepository;

    private static ModelMapper modelMapper = new ModelMapper();


    /**
     * @Description Save Order items
     * @param orderItems
     */
    public void saveOrderItems(List<OrderItem> orderItems) {
        orderItems.forEach(orderItem -> orderItemRepository.save(modelMapper.map(orderItem, OrderItemEntity.class)));
    }


}

package io.recruitment.assessment.api.event;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface EventRepository {
    @Insert({"INSERT INTO event (product_id, event_message, updated_by)",
            "VALUES (#{eventEntity.productId}, #{eventEntity.eventMessage}, #{eventEntity.updatedBy})"})
    void save(@Param("eventEntity") EventEntity eventEntity);
}

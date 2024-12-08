package com.memariyan.components.mongo.entity;

import com.memariyan.components.common.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Setter
@Getter
public class MongoBaseEntity extends BaseEntity implements Serializable {

    @Id
    private String id;

}


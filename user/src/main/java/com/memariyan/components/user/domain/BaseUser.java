package com.memariyan.components.user.domain;

import com.memariyan.components.jpa.converter.AttributeEncryptionConverter;
import com.memariyan.components.jpa.entity.BaseJpaAuditableEntity;
import com.memariyan.components.user.domain.enums.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
@MappedSuperclass
@SQLRestriction("deleted <> 'true'")
public class BaseUser extends BaseJpaAuditableEntity {

    @Column(name = "user_id")
    private String userId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    @Convert(converter = AttributeEncryptionConverter.class)
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "deleted")
    private boolean deleted;

}

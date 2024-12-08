package com.memariyan.components.user.service.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserExistenceResult {

    private String userId;

    private boolean deleted;

}

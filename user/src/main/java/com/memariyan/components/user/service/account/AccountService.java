package com.memariyan.components.user.service.account;

import com.memariyan.components.user.domain.enums.UserStatus;
import com.memariyan.components.user.service.account.model.AccountModel;
import com.memariyan.components.user.service.account.model.AccountResult;

public interface AccountService {

    AccountResult createUser(AccountModel model);

    void changeStatus(String userId, UserStatus status);

    String getUsernameById(String userId);

}

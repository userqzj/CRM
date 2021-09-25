package com.jlju.crm.settings.service;

import com.jlju.crm.exception.LoginException;
import com.jlju.crm.settings.domain.User;

import java.util.List;

public interface UserService {

    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}

package com.dbm.blog.service;

import com.dbm.blog.po.User;

public interface UserService {
    User checkUser(String username, String password);
}

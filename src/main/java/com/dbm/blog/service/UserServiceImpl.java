package com.dbm.blog.service;

import com.dbm.blog.dao.UserRepository;
import com.dbm.blog.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 登录用户信息处理
 * */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;      // UserRepository注入

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username,
                DigestUtils.md5DigestAsHex(password.getBytes()));   // 密码使用MD5加密
        return user;
    }
}

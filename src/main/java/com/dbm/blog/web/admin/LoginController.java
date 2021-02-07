package com.dbm.blog.web.admin;

import com.dbm.blog.po.User;
import com.dbm.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * 登录界面控制
 * */
@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private UserService userService;      // UserService注入

    // 跳转至登录界面
    @GetMapping()
    public String loginPage() {
        return "admin/login";
    }

    // 登录：处理用户名和密码
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes) {
        User user = userService.checkUser(username, password);
        if (user != null) {
            user.setPassword(null);     // 不能把密码传到前面
            session.setAttribute("user", user);
            return "admin/admin_index";
        } else {
            attributes.addFlashAttribute("message", "用户名或密码错误");
            return "redirect:/admin";   // 重定向
        }
    }

    // 注销
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");   // 清空session
        return "redirect:/admin";   // 重定向
    }
}

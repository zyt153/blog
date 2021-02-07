package com.dbm.blog.web;


import com.dbm.blog.po.Comment;
import com.dbm.blog.po.User;
import com.dbm.blog.service.BlogService;
import com.dbm.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

//    @Value("${comment.avatar}")
//    private String avatar;  // 头像

    // 返回commentList片段
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    // 点击发布后提交信息
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));   // 根据博客id查询评论
        // 根据昵称获取头像
        String nickName = comment.getNickname();
        char last = nickName.charAt(nickName.length()-1);
        int avaterNum = (int) last % 10;
        String avatar = "/images/avatar" + avaterNum + ".jpg";

        // 判断是否为管理员
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
//            comment.setNickname(user.getNickname());
        } else {
            comment.setAvatar(avatar);
        }

        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }


}

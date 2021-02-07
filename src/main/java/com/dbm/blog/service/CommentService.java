package com.dbm.blog.service;

import com.dbm.blog.po.Comment;

import java.util.List;

public interface CommentService {
    // 根据博客id获取评论列表
    List<Comment> listCommentByBlogId(Long blogId);

    // 点击发布后进行保存提交
    Comment saveComment(Comment comment);
}

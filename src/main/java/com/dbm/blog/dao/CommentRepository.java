package com.dbm.blog.dao;


import com.dbm.blog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 根据博客id和父评论是否为空获取第一级评论列表
    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);
}

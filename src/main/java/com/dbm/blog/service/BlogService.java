package com.dbm.blog.service;

import com.dbm.blog.po.Blog;
import com.dbm.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    // 根据id获取博客
    Blog getBlog(Long id);

    // 获取博客并将md转换为html
    Blog getAndConvert(Long id);

    // 获取博客列表
    Page<Blog> listBlog(Pageable pageable);     // （1）仅根据分页获取博客列表
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);     // （2）根据分页及查询封装（vo.BlogQuery）获取博客列表
    List<Blog> listRecommendBlogTop(Integer size);              // （3）根据推荐获取大小为size的博客列表
    Page<Blog> listBlog(String query, Pageable pageable);       // （4）根据查询和分页获取博客
    Page<Blog> listBlog(Long tagId, Pageable pageable);         // （5）根据标签和分页获取博客

    // 按年份归档
    Map<String, List<Blog>> archiveBlog();

    // 统计博客数目
    Long countBlog();

    // 新增博客
    Blog saveBlog(Blog blog);

    // 修改博客
    Blog updateBlog(Long id, Blog blog);

    // 删除
    void deleteBlog(Long id);
}

package com.dbm.blog.dao;

import com.dbm.blog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>,
        JpaSpecificationExecutor<Blog> {        // JpaSpecificationExecutor用于动态组合查询

    // 根据分页对象和是否推荐查询
    @Query("select b from Blog b where b.recommend = true")
    List<Blog> findTop(Pageable pageable);

    // 根据query获取查询结果
    // select * from t_blog where title like "%query%"
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1 or b.description like ?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    // 更新浏览次数
    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    // 根据年份将博客分类
    @Query("select function('date_format',b.createTime,'%Y') as year from Blog b group by function('date_format',b.createTime,'%Y') order by function('date_format',b.createTime,'%Y') desc ")
    List<String> findGroupYear();

    // 获取某一年的博客列表
    @Query("select b from Blog b where function('date_format',b.createTime,'%Y') = ?1")
    List<Blog> findByYear(String year);
}

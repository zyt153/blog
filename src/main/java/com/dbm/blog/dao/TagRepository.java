package com.dbm.blog.dao;

import com.dbm.blog.po.Tag;
import com.dbm.blog.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {
    Tag findByName(String name);

    // 根据分页对象查询
    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
}

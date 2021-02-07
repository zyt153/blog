package com.dbm.blog.service;

import com.dbm.blog.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    Type saveType(Type type);   // 新增分类

    Type getType(Long id);      // 根据id获取分类

    Type getTypeByName(String name);            // 通过名称获取分类

    Page<Type> listType(Pageable pageable);     // 根据分页获取分类列表

    List<Type> listType();                      // 返回所有分类

    List<Type> listTypeTop(Integer size);       // 根据size获取分类列表

    Type updateType(Long id,Type type); // 修改分类

    void deleteType(Long id);           // 删除分类

}

package com.dbm.blog.service;


import com.dbm.blog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    Tag saveTag(Tag type);   // 新增标签

    Tag getTag(Long id);     // 根据id获取标签

    Tag getTagByName(String name);            // 通过名称获取标签

    Page<Tag> listTag(Pageable pageable);     // 根据分页获取标签列表

    List<Tag> listTag();                      // 获取所有标签

    List<Tag> listTagTop(Integer size);       // 根据size获取标签列表

    List<Tag> listTag(String ids);            // 通过id获取标签

    Tag updateTag(Long id, Tag type);         // 修改标签

    void deleteTag(Long id);                  // 删除标签
}

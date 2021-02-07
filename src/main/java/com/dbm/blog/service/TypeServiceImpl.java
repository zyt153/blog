package com.dbm.blog.service;

import com.dbm.blog.NotFoundException;
import com.dbm.blog.dao.TypeRepository;
import com.dbm.blog.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 分类管理
 * */
@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeRepository typeRepository;      // TypeRepository注入

    // 新增分类
    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    // 根据id获取分类
    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.findById(id).get();
    }

    // 通过名称获取分类
    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    // 根据分页获取分类列表
    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    // 返回所有分类
    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    // 根据size获取分类列表
    @Override
    public List<Type> listTypeTop(Integer size) {
        // 按分类下面的博客数目排序，取前size个
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        // Sort.order sort = new Sort.Order(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return typeRepository.findTop(pageable);
    }

    // 修改分类
    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.findById(id).get();
        if(t == null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type, t);
        return typeRepository.save(t);
    }

    // 删除分类
    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }
}

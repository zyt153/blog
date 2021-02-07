package com.dbm.blog.service;

import com.dbm.blog.NotFoundException;
import com.dbm.blog.dao.BlogRepository;
import com.dbm.blog.po.Blog;
import com.dbm.blog.po.Type;
import com.dbm.blog.util.MarkdownUtils;
import com.dbm.blog.util.MyBeanUtils;
import com.dbm.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogserviceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;

    // 根据id获取博客
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).get();
    }

    // 获取博客并将md转换为html
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findById(id).get();
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);       // 复制b防止下面操作修改数据库中内容
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        blogRepository.updateViews(id);         // 浏览次数更新
        return b;
    }

    // 获取博客列表
    // （1）仅根据分页获取
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    // （2）根据分页及查询封装（vo.BlogQuery）获取
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                // root：表的字段、属性值 criteriaQuery：查询条件容器 criteriaBuilder：设置具体条件表达式
                List<Predicate> predicates = new ArrayList<>();     // 放置组合条件
                // 根据博客标题查询（like方法）
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                // 根据Type id查询（equal方法）
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                // 根据是否推荐的布尔类型查询（equal方法）
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                // 转换为条件数组送到cq查询
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    // （3）根据推荐获取大小为size的博客列表
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        // 按博客的更新时间排序，取前size个推荐博客
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        // Sort.order sort = new Sort.Order(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    // （4）根据查询和分页获取博客
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    // （5）根据标签和分页获取博客
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    // 按年份归档
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    // 统计博客数目
    @Override
    public Long countBlog() {
        return blogRepository.count();
    }


    // 新增博客
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {  // 新增
            blog.setCreateTime(new Date());  // 发布时间
            blog.setUpdateTime(new Date());  // 更新时间
            blog.setViews(0);                // 初始浏览次数为0
        } else {  // 修改
            blog.setUpdateTime(new Date());  // 更新时间
        }

        return blogRepository.save(blog);
    }

    // 修改博客
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findById(id).get();
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));   // 仅复制非空属性，也可用mapper类
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    // 删除
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}

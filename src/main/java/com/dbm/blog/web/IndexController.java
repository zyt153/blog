package com.dbm.blog.web;

import com.dbm.blog.NotFoundException;
import com.dbm.blog.po.Blog;
import com.dbm.blog.service.BlogService;
import com.dbm.blog.service.TagService;
import com.dbm.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    // 首页：根据分页获取博客、分类、标签、推荐
    @GetMapping("/")
    public String index(@PageableDefault(size = 5, sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("types", typeService.listTypeTop(6));    // 获取前6个分类
        model.addAttribute("tags", tagService.listTagTop(10));      // 获取前10个标签
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));  // 获取前8个推荐博客


//        此部分用于error测试
//        int i = 9/0;
//        String blog = null;
//        if (blog == null){
//            throw new NotFoundException("博客不存在");
//        }
//        System.out.println("--------index---------");
        return "index";
    }

    // 搜索
    public String q;    // 用于在分页时传递query值
    @PostMapping("/search")
    public String search(@PageableDefault(size = 5, sort = {"updateTime"},
            direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model) {
        model.addAttribute("page", blogService.listBlog("%" + query + "%", pageable));
        model.addAttribute("query", query);
        q = query;
        return "search";
    }

    // 搜索页分页
    @GetMapping("/search")
    public String searchPage(@PageableDefault(size = 5 , sort = {"updateTime"} ,
            direction = Sort.Direction.DESC) Pageable pageable ,
                             Model model){
        model.addAttribute("page" , blogService.listBlog("%" + q + "%", pageable));
        model.addAttribute("query", q);
        return "search";
    }


    // 具体博客页面
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }

    // 底部最新博客
    @GetMapping("/footer/newblog")
    public String newblogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }
}

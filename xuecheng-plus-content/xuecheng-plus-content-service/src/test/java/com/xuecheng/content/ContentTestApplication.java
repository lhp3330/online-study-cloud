package com.xuecheng.content;

/*
   @Class:ContentTestApplication
   @Date:2024/1/7  16:58
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDTO;
import com.xuecheng.content.model.pojo.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class ContentTestApplication {

    @Resource
    CourseBaseMapper courseBaseMapper;

    @Resource
    CourseBaseInfoService courseBaseInfoService;


    @Test
    public void test1() {
        CourseBase courseBase = courseBaseMapper.selectById(18);
        System.out.println(courseBase);
    }

    @Test
    public void pageQueryTest() {
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(true, CourseBase::getName, "java");
        // 分页
        Page<CourseBase> page = new Page<>();
        page.setPages(1);
        page.setSize(10);
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, wrapper);
        List<CourseBase> records = pageResult.getRecords();
        records.forEach(System.out::println);
        long total = pageResult.getTotal();
        System.out.println(total);
    }

    @Test
    public void pageServiceTest() {
        PageParams params = new PageParams(1L, 10L);
        QueryCourseParamsDTO dto = new QueryCourseParamsDTO("测试", "202004", "203001");
        PageResult<CourseBase> result = courseBaseInfoService.QueryCourseBaseList(params, dto);
        System.out.println(result);
    }
}

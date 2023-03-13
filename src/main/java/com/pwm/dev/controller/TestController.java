package com.pwm.dev.controller;

import com.pwm.dev.service.TestService;
import com.pwm.dev.vo.QueryListInVo;
import com.pwm.dev.vo.QueryListOutVo;
import com.pwm.dev.vo.TestInVo;
import com.pwm.dev.vo.TestTableVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestService testService;

    @ResponseBody
    @RequestMapping(value = "/hello",method = RequestMethod.POST)
    public String test(@RequestBody TestInVo inVo) {
        return inVo.getName();
    }

    /**
     * 获取缓存（String）
     */
    @ResponseBody
    @RequestMapping(value = "/getRedisKey",method = RequestMethod.POST)
    public void getRedisKey(@RequestBody TestInVo inVo) {
        testService.getRedisKey(inVo);
    }


    /**
     * 设置缓存（String）
     */
    @ResponseBody
    @RequestMapping(value = "/setRedisKey",method = RequestMethod.POST)
    public void setRedisKey(@RequestBody TestInVo inVo) {
        testService.setRedisKey(inVo);
    }


    /**
     * 查询
     */
    @ResponseBody
    @RequestMapping(value = "/queryList",method = RequestMethod.POST)
    public QueryListOutVo queryList(@RequestBody QueryListInVo inVo) {
        return testService.queryList(inVo);
    }


    /**
     * 新增
     */
    @ResponseBody
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public void save(@RequestBody TestTableVo inVo) {
        testService.save(inVo);
    }

    /**
     * 发送邮件
     */
    @ResponseBody
    @RequestMapping(value = "/sendEmail",method = RequestMethod.POST)
    public void sendEmail() {
        testService.sendEmail();
    }
}

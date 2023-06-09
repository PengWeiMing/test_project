package com.pwm.dev.service;

import com.pwm.dev.vo.QueryListInVo;
import com.pwm.dev.vo.QueryListOutVo;
import com.pwm.dev.vo.TestInVo;
import com.pwm.dev.vo.TestTableVo;

public interface TestService {
    void getRedisKey(TestInVo inVo);

    void setRedisKey(TestInVo inVo);

    QueryListOutVo queryList(QueryListInVo inVo);

    void save(TestTableVo inVo);

    void sendEmail();
}

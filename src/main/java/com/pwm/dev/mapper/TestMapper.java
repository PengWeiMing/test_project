package com.pwm.dev.mapper;


import com.pwm.dev.po.TestTablePo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestMapper {

    List<TestTablePo> queryList(Map map);

    Page<TestTablePo> queryListByPage(int pageNumber, int pageSize);

}

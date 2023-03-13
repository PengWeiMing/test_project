package com.pwm.dev.repo;

import com.pwm.dev.po.TestTablePo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TestTableRepo extends CrudRepository<TestTablePo,String> {

    Optional<TestTablePo> queryByUserId(String userId);
}

package com.pwm.dev.repo;

import com.pwm.dev.po.TestTablePo;
import org.springframework.data.repository.CrudRepository;

public interface TestTableRepo extends CrudRepository<TestTablePo,String> {
}

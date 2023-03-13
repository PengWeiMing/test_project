package com.pwm.dev.vo;


import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


@Data
public class QueryByUserIdInVo {

    @NotBlank(message = "userId is null")
    private String userId;

}

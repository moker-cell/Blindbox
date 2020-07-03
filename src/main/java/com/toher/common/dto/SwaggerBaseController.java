package com.toher.common.dto;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @Author: lzh
 * @Date: 2020/2/14 16:38
 */
@ApiResponses({
        @ApiResponse(code = 500, message = "服务器内部错误", response = Result.class)
})
public class SwaggerBaseController {
}

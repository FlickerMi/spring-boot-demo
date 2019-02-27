package cn.notemi.controller;

import cn.notemi.annotations.ResponseResult;
import cn.notemi.config.SwaggerConfig;
import cn.notemi.model.request.AuthenticatedAccount;
import cn.notemi.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Title：AuthController
 *
 * @author Flicker
 * @create 2019-02-26 11:32:05
 **/
@ResponseResult
@RestController
@RequestMapping(APIController.AUTH_URL)
@Api(tags="授权相关API")
public class AuthController {

    @Autowired
    AccountService accountService;

    @PostMapping(value = "/local")
    @ApiOperation(value = "登录获取token")
    public AuthenticatedAccount loginLocal() {
        return accountService.loginLocal();
    }
}
package cn.notemi.controller;

import cn.notemi.annotations.ResponseResult;
import cn.notemi.model.request.AuthenticatedAccount;
import cn.notemi.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AuthController {

    @Autowired
    AccountService accountService;

    @PostMapping(value = "/local")
    public AuthenticatedAccount loginLocal() {
        return accountService.loginLocal();
    }

}
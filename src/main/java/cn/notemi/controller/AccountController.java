package cn.notemi.controller;

import cn.notemi.constant.AccountStatus;
import cn.notemi.model.entity.Account;
import cn.notemi.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Title：UserController
 *
 * @author Flicker
 * @create 2018/4/18 0018 11:40
 **/
@RestController
@RequestMapping(value = APIController.ACCOUNT_URL)
public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping
    public Account createAccount(@RequestBody Account account, HttpServletResponse response) {
        Account saveAccount = accountService.createAccount(account);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return saveAccount;
    }

    @DeleteMapping(value = {"/{id}"})
    public void deleteAccountById(@PathVariable(value = "id") Long id, HttpServletResponse response) {
        accountService.deleteByAccountId(id);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @PutMapping(value = {"/{id}"})
    public Account updateAccount(@PathVariable(value = "id") Long id, @RequestBody Account account) {
        account.setId(id);
        return accountService.updateAccount(account);
    }

    @GetMapping(value = {"/all"})
    public List<Account> findAccountAll(AccountStatus status) {
        return accountService.findAllAccount(status);
    }

    @GetMapping(value = {"/{id}"})
    public Account findAccountById(@PathVariable(value = "id") Long id) {
        return accountService.findByAccountId(id);
    }

    @GetMapping(value = {"/mine"})
    public Account findMyProfile() {
        return accountService.findMyLoginProfile();
    }
}

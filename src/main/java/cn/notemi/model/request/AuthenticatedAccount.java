package cn.notemi.model.request;

import cn.notemi.model.entity.Account;
import lombok.Data;

/**
 * Title：AuthenticatedAccount
 *
 * @author Flicker
 * @create 2019-02-25 23:29:37
 **/
@Data
public class AuthenticatedAccount {

    private Account account;
    private String authorization;
    private String wechatOpenId;

    public AuthenticatedAccount(Account account, String authorization) {
      this.account = account;
      this.authorization = authorization;
    }

    public AuthenticatedAccount(Account account, String authorization, String openId) {
        this.account = account;
        this.authorization = authorization;
        this.wechatOpenId = openId;
    }
    public AuthenticatedAccount(Account account) {
        this.account = account;
    }
}

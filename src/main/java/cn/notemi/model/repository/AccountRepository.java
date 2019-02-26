package cn.notemi.model.repository;

import cn.notemi.constant.AccountStatus;
import cn.notemi.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Title：AccountRepository
 *
 * @author Flicker
 * @create 2018/4/18 0018 11:01
 **/
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findOneByUsername(String username);

    Account findOneByWechatOpenId(String wechatOpenId);

    List<Account> findAllByStatus(AccountStatus status);
}

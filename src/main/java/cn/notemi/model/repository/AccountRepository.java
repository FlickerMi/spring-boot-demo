package cn.notemi.model.repository;

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
    List<Account> findAllByUsername(String username);
}

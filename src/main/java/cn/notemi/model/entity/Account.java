package cn.notemi.model.entity;

import cn.notemi.constant.AccountStatus;
import cn.notemi.constant.Gender;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Title：Account
 *
 * @author Flicker
 * @create 2018/4/18 0018 11:00
 **/
@Entity
@Data
@Table(name = "account")
public class Account extends BaseEntity {
    private String username;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private String password;
}

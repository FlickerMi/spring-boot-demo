package cn.notemi.model.entity;

import cn.notemi.utils.ObjectUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Title：BaseEntity
 *
 * @author Flicker
 * @create 2018/4/18 0018 11:03
 **/
@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @CreatedBy
    private String createdBy;
    @JsonIgnore
    @LastModifiedBy
    private String updatedBy;
    @JsonIgnore
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JsonIgnore
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public void merge(BaseEntity updated) {
        ObjectUtils.mergeChanges(updated, this);
    }

    public void merge(BaseEntity updated, String[] excludedProperties) {
        ObjectUtils.mergeChanges(updated, this, excludedProperties);
    }

}


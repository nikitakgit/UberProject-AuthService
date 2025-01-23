package org.example.uberprojectauthservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseModel {

    @Id // this makes id primary key of Review table
    @GeneratedValue(strategy = GenerationType.IDENTITY) //this make id autoinc and self generated
    protected Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP) //for automatically giving the time value
    @CreatedDate
    protected Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    protected Date updatedAt;
}

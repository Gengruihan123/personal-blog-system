package com.group3.blog.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="subscriptions")
public class Subscription {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,unique=true,length=120) private String email;
    @Column(nullable=false,length=20) private String status="ACTIVE";
    @Column(name="subscribed_at",nullable=false) private LocalDateTime subscribedAt;
    @Column(name="unsubscribe_token",nullable=false,unique=true,length=64) private String unsubscribeToken;
    @PrePersist void prePersist(){if(subscribedAt==null)subscribedAt=LocalDateTime.now();}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getEmail(){return email;} public void setEmail(String email){this.email=email;}
    public String getStatus(){return status;} public void setStatus(String status){this.status=status;}
    public LocalDateTime getSubscribedAt(){return subscribedAt;} public void setSubscribedAt(LocalDateTime subscribedAt){this.subscribedAt=subscribedAt;}
    public String getUnsubscribeToken(){return unsubscribeToken;} public void setUnsubscribeToken(String unsubscribeToken){this.unsubscribeToken=unsubscribeToken;}
}

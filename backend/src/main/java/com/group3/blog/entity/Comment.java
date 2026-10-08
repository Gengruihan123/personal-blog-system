package com.group3.blog.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="comments",indexes=@Index(name="idx_comment_article_status",columnList="article_id,status"))
public class Comment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="article_id") private Article article;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="user_id") private User user;
    @Column(nullable=false,length=500) private String content;
    @Column(nullable=false,length=20) private String status="PENDING";
    @Column(name="created_at",nullable=false) private LocalDateTime createdAt;
    @PrePersist void prePersist(){if(createdAt==null)createdAt=LocalDateTime.now();}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Article getArticle(){return article;} public void setArticle(Article article){this.article=article;}
    public User getUser(){return user;} public void setUser(User user){this.user=user;}
    public String getContent(){return content;} public void setContent(String content){this.content=content;}
    public String getStatus(){return status;} public void setStatus(String status){this.status=status;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
}

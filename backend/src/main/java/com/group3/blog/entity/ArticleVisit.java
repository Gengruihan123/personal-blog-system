package com.group3.blog.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="article_visits")
public class ArticleVisit {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="article_id") private Article article;
    @Column(name="visitor_key",length=100) private String visitorKey;
    @Column(name="visited_at",nullable=false) private LocalDateTime visitedAt;
    @PrePersist void prePersist(){if(visitedAt==null)visitedAt=LocalDateTime.now();}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Article getArticle(){return article;} public void setArticle(Article article){this.article=article;}
    public String getVisitorKey(){return visitorKey;} public void setVisitorKey(String visitorKey){this.visitorKey=visitorKey;}
    public LocalDateTime getVisitedAt(){return visitedAt;} public void setVisitedAt(LocalDateTime visitedAt){this.visitedAt=visitedAt;}
}

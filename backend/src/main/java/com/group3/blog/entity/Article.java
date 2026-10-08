package com.group3.blog.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity @Table(name="articles", indexes={@Index(name="idx_article_status_time",columnList="status,published_at"),@Index(name="idx_article_category",columnList="category_id")})
public class Article {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,length=150) private String title;
    @Column(nullable=false,length=500) private String summary;
    @Lob @Column(nullable=false,columnDefinition="LONGTEXT") private String content;
    @Column(length=500) private String cover;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="author_id") private User author;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="category_id") private Category category;
    @ManyToMany(fetch=FetchType.LAZY) @JoinTable(name="article_tags",joinColumns=@JoinColumn(name="article_id"),inverseJoinColumns=@JoinColumn(name="tag_id")) private Set<Tag> tags=new LinkedHashSet<>();
    @Column(nullable=false,length=20) private String status="DRAFT";
    @Column(name="published_at") private LocalDateTime publishedAt;
    @Column(name="created_at",nullable=false) private LocalDateTime createdAt;
    @Column(name="updated_at",nullable=false) private LocalDateTime updatedAt;
    @Column(name="view_count",nullable=false) private Long viewCount=0L;
    @Column(nullable=false) private boolean deleted=false;
    @Version private Long version;
    @PrePersist void prePersist(){LocalDateTime now=LocalDateTime.now();createdAt=createdAt==null?now:createdAt;updatedAt=now;}
    @PreUpdate void preUpdate(){updatedAt=LocalDateTime.now();}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getTitle(){return title;} public void setTitle(String title){this.title=title;}
    public String getSummary(){return summary;} public void setSummary(String summary){this.summary=summary;}
    public String getContent(){return content;} public void setContent(String content){this.content=content;}
    public String getCover(){return cover;} public void setCover(String cover){this.cover=cover;}
    public User getAuthor(){return author;} public void setAuthor(User author){this.author=author;}
    public Category getCategory(){return category;} public void setCategory(Category category){this.category=category;}
    public Set<Tag> getTags(){return tags;} public void setTags(Set<Tag> tags){this.tags=tags;}
    public String getStatus(){return status;} public void setStatus(String status){this.status=status;}
    public LocalDateTime getPublishedAt(){return publishedAt;} public void setPublishedAt(LocalDateTime publishedAt){this.publishedAt=publishedAt;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
    public LocalDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(LocalDateTime updatedAt){this.updatedAt=updatedAt;}
    public Long getViewCount(){return viewCount;} public void setViewCount(Long viewCount){this.viewCount=viewCount;}
    public boolean isDeleted(){return deleted;} public void setDeleted(boolean deleted){this.deleted=deleted;}
    public Long getVersion(){return version;} public void setVersion(Long version){this.version=version;}
}

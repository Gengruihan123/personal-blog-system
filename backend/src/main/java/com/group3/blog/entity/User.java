package com.group3.blog.entity;

import jakarta.persistence.*;

@Entity @Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true, length=50) private String username;
    @Column(name="password_hash", nullable=false) private String passwordHash;
    @Column(nullable=false, length=50) private String nickname;
    @Column(nullable=false, unique=true, length=120) private String email;
    @Column(length=500) private String avatar;
    @Column(nullable=false, length=20) private String role = "USER";
    @Column(nullable=false, length=20) private String status = "ACTIVE";
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getUsername(){return username;} public void setUsername(String username){this.username=username;}
    public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String passwordHash){this.passwordHash=passwordHash;}
    public String getNickname(){return nickname;} public void setNickname(String nickname){this.nickname=nickname;}
    public String getEmail(){return email;} public void setEmail(String email){this.email=email;}
    public String getAvatar(){return avatar;} public void setAvatar(String avatar){this.avatar=avatar;}
    public String getRole(){return role;} public void setRole(String role){this.role=role;}
    public String getStatus(){return status;} public void setStatus(String status){this.status=status;}
    public boolean isActive(){return "ACTIVE".equals(status);}
}

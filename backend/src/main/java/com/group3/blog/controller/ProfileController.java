package com.group3.blog.controller;

import com.group3.blog.dto.BlogDtos.*;
import com.group3.blog.entity.User;
import com.group3.blog.exception.BusinessException;
import com.group3.blog.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/profile")
public class ProfileController {
    private final UserRepository users;public ProfileController(UserRepository users){this.users=users;}
    @PutMapping @Transactional public UserDto update(@Valid @RequestBody ProfileRequest request,Authentication authentication){User user=users.findByUsername(authentication.getName()).orElseThrow();users.findByEmail(request.email().trim().toLowerCase()).filter(other->!other.getId().equals(user.getId())).ifPresent(other->{throw new BusinessException(HttpStatus.CONFLICT,"邮箱已被使用");});user.setNickname(request.nickname().trim());user.setEmail(request.email().trim().toLowerCase());user.setAvatar(request.avatar()==null?"":request.avatar().trim());users.save(user);return new UserDto(user.getId(),user.getUsername(),user.getNickname(),user.getEmail(),user.getAvatar(),user.getRole());}
}

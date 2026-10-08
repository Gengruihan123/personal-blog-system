package com.group3.blog.controller;

import com.group3.blog.dto.BlogDtos.*;
import com.group3.blog.entity.User;
import com.group3.blog.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;private final UserRepository users;
    public AuthController(AuthenticationManager authenticationManager,UserRepository users){this.authenticationManager=authenticationManager;this.users=users;}
    @PostMapping("/login") public UserDto login(@Valid @RequestBody LoginRequest request,HttpServletRequest servletRequest){Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(),request.password()));SecurityContext context=SecurityContextHolder.createEmptyContext();context.setAuthentication(authentication);SecurityContextHolder.setContext(context);servletRequest.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,context);return dto(users.findByUsername(authentication.getName()).orElseThrow());}
    @GetMapping("/me") public UserDto me(Authentication authentication){if(authentication==null||!authentication.isAuthenticated()||"anonymousUser".equals(authentication.getPrincipal()))return null;return dto(users.findByUsername(authentication.getName()).orElseThrow());}
    @PostMapping("/logout") public MessageResponse logout(HttpSession session){if(session!=null)session.invalidate();SecurityContextHolder.clearContext();return new MessageResponse("已退出登录");}
    private UserDto dto(User u){return new UserDto(u.getId(),u.getUsername(),u.getNickname(),u.getEmail(),u.getAvatar(),u.getRole());}
}

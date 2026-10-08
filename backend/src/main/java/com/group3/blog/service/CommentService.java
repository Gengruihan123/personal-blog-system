package com.group3.blog.service;

import com.group3.blog.dto.BlogDtos.*;
import com.group3.blog.entity.*;
import com.group3.blog.exception.BusinessException;
import com.group3.blog.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class CommentService {
    private final CommentRepository comments;private final ArticleRepository articles;private final UserRepository users;
    public CommentService(CommentRepository comments,ArticleRepository articles,UserRepository users){this.comments=comments;this.articles=articles;this.users=users;}
    @Transactional(readOnly=true) public List<CommentDto> publicList(Long articleId){return comments.findByArticleIdAndStatusOrderByCreatedAtDesc(articleId,"APPROVED").stream().map(this::toDto).toList();}
    @Transactional(readOnly=true) public List<CommentDto> all(){return comments.findAllByOrderByCreatedAtDesc().stream().map(this::toDto).toList();}
    @Transactional public CommentDto create(Long articleId,String username,String content){Article article=articles.findById(articleId).filter(a->!a.isDeleted()&&"PUBLISHED".equals(a.getStatus())).orElseThrow(()->new BusinessException(HttpStatus.NOT_FOUND,"文章不存在"));Comment comment=new Comment();comment.setArticle(article);comment.setUser(users.findByUsername(username).orElseThrow());comment.setContent(content.trim());return toDto(comments.save(comment));}
    @Transactional public CommentDto status(Long id,String value){String status=value.toUpperCase(Locale.ROOT);if(!Set.of("PENDING","APPROVED","REJECTED").contains(status))throw new BusinessException(HttpStatus.BAD_REQUEST,"评论状态无效");Comment comment=find(id);comment.setStatus(status);return toDto(comments.save(comment));}
    @Transactional public void delete(Long id){comments.delete(find(id));}
    private Comment find(Long id){return comments.findById(id).orElseThrow(()->new BusinessException(HttpStatus.NOT_FOUND,"评论不存在"));}
    private CommentDto toDto(Comment c){return new CommentDto(c.getId(),c.getArticle().getId(),c.getArticle().getTitle(),c.getUser().getId(),c.getUser().getNickname(),c.getContent(),c.getStatus(),c.getCreatedAt());}
}

package com.group3.blog.controller;

import com.group3.blog.dto.BlogDtos.*;
import com.group3.blog.exception.BusinessException;
import com.group3.blog.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api")
public class PublicController {
    private final ArticleService articles;private final CommentService comments;private final TaxonomyService taxonomy;private final SubscriptionService subscriptions;
    public PublicController(ArticleService articles,CommentService comments,TaxonomyService taxonomy,SubscriptionService subscriptions){this.articles=articles;this.comments=comments;this.taxonomy=taxonomy;this.subscriptions=subscriptions;}
    @GetMapping("/articles") public PageResponse<ArticleDto> articles(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="6") int size,@RequestParam(required=false) String keyword,@RequestParam(required=false) Long categoryId,@RequestParam(required=false) Long tagId){return articles.list(page,size,keyword,categoryId,tagId,false);}
    @GetMapping("/articles/{id}") public ArticleDto article(@PathVariable Long id,HttpServletRequest request){return articles.detail(id,request.getRemoteAddr(),false);}
    @GetMapping("/articles/{id}/comments") public List<CommentDto> comments(@PathVariable Long id){return comments.publicList(id);}
    @PostMapping("/articles/{id}/comments") @ResponseStatus(HttpStatus.CREATED) public CommentDto comment(@PathVariable Long id,@Valid @RequestBody CommentRequest request,Authentication auth){if(auth==null||!auth.isAuthenticated())throw new BusinessException(HttpStatus.UNAUTHORIZED,"请先登录");return comments.create(id,auth.getName(),request.content());}
    @GetMapping("/categories") public List<CategoryDto> categories(){return taxonomy.categories();}
    @GetMapping("/tags") public List<TagDto> tags(){return taxonomy.tags();}
    @PostMapping("/subscriptions") @ResponseStatus(HttpStatus.CREATED) public MessageResponse subscribe(@Valid @RequestBody SubscriptionRequest request){subscriptions.subscribe(request.email());return new MessageResponse("订阅成功");}
}

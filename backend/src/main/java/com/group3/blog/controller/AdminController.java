package com.group3.blog.controller;

import com.group3.blog.dto.BlogDtos.*;
import com.group3.blog.repository.*;
import com.group3.blog.service.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/admin")
public class AdminController {
    private final ArticleService articles;private final CommentService comments;private final TaxonomyService taxonomy;private final ArticleRepository articleRepo;private final CommentRepository commentRepo;private final SubscriptionRepository subscriptionRepo;
    public AdminController(ArticleService articles,CommentService comments,TaxonomyService taxonomy,ArticleRepository articleRepo,CommentRepository commentRepo,SubscriptionRepository subscriptionRepo){this.articles=articles;this.comments=comments;this.taxonomy=taxonomy;this.articleRepo=articleRepo;this.commentRepo=commentRepo;this.subscriptionRepo=subscriptionRepo;}
    @GetMapping("/articles") public PageResponse<ArticleDto> articles(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="50") int size,@RequestParam(required=false) String keyword){return articles.list(page,size,keyword,null,null,true);}
    @GetMapping("/articles/{id}") public ArticleDto article(@PathVariable Long id){return articles.detail(id,"admin",true);}
    @PostMapping("/articles") @ResponseStatus(HttpStatus.CREATED) public ArticleDto create(@Valid @RequestBody ArticleWriteRequest request,Authentication auth){return articles.create(request,auth.getName());}
    @PutMapping("/articles/{id}") public ArticleDto update(@PathVariable Long id,@Valid @RequestBody ArticleWriteRequest request){return articles.update(id,request);}
    @DeleteMapping("/articles/{id}") public MessageResponse delete(@PathVariable Long id){articles.delete(id);return new MessageResponse("文章已删除");}
    @GetMapping("/comments") public List<CommentDto> comments(){return comments.all();}
    @PatchMapping("/comments/{id}") public CommentDto status(@PathVariable Long id,@Valid @RequestBody StatusRequest request){return comments.status(id,request.status());}
    @DeleteMapping("/comments/{id}") public MessageResponse deleteComment(@PathVariable Long id){comments.delete(id);return new MessageResponse("评论已删除");}
    @PostMapping("/categories") @ResponseStatus(HttpStatus.CREATED) public CategoryDto createCategory(@Valid @RequestBody NameRequest r){return taxonomy.saveCategory(null,r);}
    @PutMapping("/categories/{id}") public CategoryDto updateCategory(@PathVariable Long id,@Valid @RequestBody NameRequest r){return taxonomy.saveCategory(id,r);}
    @DeleteMapping("/categories/{id}") public MessageResponse deleteCategory(@PathVariable Long id){taxonomy.deleteCategory(id);return new MessageResponse("分类已删除");}
    @PostMapping("/tags") @ResponseStatus(HttpStatus.CREATED) public TagDto createTag(@Valid @RequestBody NameRequest r){return taxonomy.saveTag(null,r);}
    @PutMapping("/tags/{id}") public TagDto updateTag(@PathVariable Long id,@Valid @RequestBody NameRequest r){return taxonomy.saveTag(id,r);}
    @DeleteMapping("/tags/{id}") public MessageResponse deleteTag(@PathVariable Long id){taxonomy.deleteTag(id);return new MessageResponse("标签已删除");}
    @GetMapping("/stats") public StatsDto stats(){return new StatsDto(articleRepo.countByDeletedFalse(),articleRepo.countByStatusAndDeletedFalse("PUBLISHED"),articleRepo.countByStatusAndDeletedFalse("DRAFT"),articleRepo.sumPublishedViews(),commentRepo.count(),commentRepo.countByStatus("PENDING"),subscriptionRepo.countByStatus("ACTIVE"));}
}

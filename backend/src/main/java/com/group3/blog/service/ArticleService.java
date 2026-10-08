package com.group3.blog.service;

import com.group3.blog.dto.BlogDtos.*;
import com.group3.blog.entity.*;
import com.group3.blog.exception.BusinessException;
import com.group3.blog.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ArticleService {
    private final ArticleRepository articles; private final CategoryRepository categories; private final TagRepository tags; private final UserRepository users; private final CommentRepository comments; private final ArticleVisitRepository visits;
    public ArticleService(ArticleRepository articles,CategoryRepository categories,TagRepository tags,UserRepository users,CommentRepository comments,ArticleVisitRepository visits){this.articles=articles;this.categories=categories;this.tags=tags;this.users=users;this.comments=comments;this.visits=visits;}

    @Transactional(readOnly=true)
    public PageResponse<ArticleDto> list(int page,int size,String keyword,Long categoryId,Long tagId,boolean admin){
        int safePage=Math.max(0,page),safeSize=Math.min(50,Math.max(1,size));
        String key=keyword==null?"":keyword.trim().toLowerCase(Locale.ROOT);
        List<Article> filtered=(admin?articles.findByDeletedFalseOrderByPublishedAtDesc():articles.findByStatusAndDeletedFalseOrderByPublishedAtDesc("PUBLISHED")).stream()
                .filter(a->categoryId==null||a.getCategory().getId().equals(categoryId))
                .filter(a->tagId==null||a.getTags().stream().anyMatch(t->t.getId().equals(tagId)))
                .filter(a->key.isBlank()||(a.getTitle()+" "+a.getSummary()+" "+a.getContent()+" "+a.getCategory().getName()+" "+a.getTags().stream().map(Tag::getName).reduce("",(x,y)->x+" "+y)).toLowerCase(Locale.ROOT).contains(key)).toList();
        int from=Math.min(filtered.size(),safePage*safeSize),to=Math.min(filtered.size(),from+safeSize);
        List<ArticleDto> content=filtered.subList(from,to).stream().map(this::toDto).toList();
        return new PageResponse<>(content,safePage,safeSize,Math.max(1,(int)Math.ceil(filtered.size()/(double)safeSize)),filtered.size());
    }

    @Transactional
    public ArticleDto detail(Long id,String visitorKey,boolean admin){
        Article article=find(id,admin);
        if(!admin){article.setViewCount(article.getViewCount()+1);ArticleVisit visit=new ArticleVisit();visit.setArticle(article);visit.setVisitorKey(visitorKey);visits.save(visit);}
        return toDto(article);
    }

    @Transactional
    public ArticleDto create(ArticleWriteRequest request,String username){
        Article article=new Article(); article.setAuthor(users.findByUsername(username).orElseThrow()); apply(article,request);
        return toDto(articles.save(article));
    }

    @Transactional
    public ArticleDto update(Long id,ArticleWriteRequest request){
        Article article=find(id,true);
        if(request.version()==null||!Objects.equals(request.version(),article.getVersion())) throw new BusinessException(HttpStatus.CONFLICT,"文章已被其他操作修改，请刷新后重试");
        apply(article,request); return toDto(articles.save(article));
    }

    @Transactional public void delete(Long id){Article article=find(id,true);article.setDeleted(true);articles.save(article);}

    private void apply(Article article,ArticleWriteRequest request){
        String status=request.status().toUpperCase(Locale.ROOT);
        if(!Set.of("DRAFT","PUBLISHED").contains(status)) throw new BusinessException(HttpStatus.BAD_REQUEST,"文章状态只能是 DRAFT 或 PUBLISHED");
        article.setTitle(request.title().trim()); article.setContent(request.content().trim());
        article.setSummary(request.summary()==null||request.summary().isBlank()?request.content().replaceAll("<[^>]+>","").substring(0,Math.min(120,request.content().replaceAll("<[^>]+>","").length())):request.summary().trim());
        article.setCover(request.cover()==null?"":request.cover().trim());
        article.setCategory(categories.findById(request.categoryId()).orElseThrow(()->new BusinessException(HttpStatus.BAD_REQUEST,"分类不存在")));
        article.setTags(new LinkedHashSet<>(tags.findAllById(request.tagIds()==null?List.of():request.tagIds())));
        if("PUBLISHED".equals(status)&&!"PUBLISHED".equals(article.getStatus())) article.setPublishedAt(LocalDateTime.now());
        article.setStatus(status);
    }

    private Article find(Long id,boolean admin){return articles.findById(id).filter(a->!a.isDeleted()&&(admin||"PUBLISHED".equals(a.getStatus()))).orElseThrow(()->new BusinessException(HttpStatus.NOT_FOUND,"文章不存在或已下架"));}
    public ArticleDto toDto(Article a){return new ArticleDto(a.getId(),a.getTitle(),a.getSummary(),a.getContent(),a.getCover(),a.getAuthor().getId(),a.getAuthor().getNickname(),a.getCategory().getId(),a.getCategory().getName(),a.getTags().stream().map(t->new TagDto(t.getId(),t.getName(),0)).toList(),a.getStatus(),a.getPublishedAt(),a.getCreatedAt(),a.getUpdatedAt(),a.getViewCount(),comments.countByArticleIdAndStatus(a.getId(),"APPROVED"),a.getVersion());}
}

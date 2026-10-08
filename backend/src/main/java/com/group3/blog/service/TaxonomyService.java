package com.group3.blog.service;

import com.group3.blog.dto.BlogDtos.*;
import com.group3.blog.entity.*;
import com.group3.blog.exception.BusinessException;
import com.group3.blog.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TaxonomyService {
    private final CategoryRepository categories;private final TagRepository tags;private final ArticleRepository articles;
    public TaxonomyService(CategoryRepository categories,TagRepository tags,ArticleRepository articles){this.categories=categories;this.tags=tags;this.articles=articles;}
    @Transactional(readOnly=true) public List<CategoryDto> categories(){List<Article> all=articles.findByStatusAndDeletedFalseOrderByPublishedAtDesc("PUBLISHED");return categories.findAllByOrderBySortOrderAsc().stream().map(c->new CategoryDto(c.getId(),c.getName(),c.getDescription(),c.getSortOrder(),all.stream().filter(a->a.getCategory().getId().equals(c.getId())).count())).toList();}
    @Transactional(readOnly=true) public List<TagDto> tags(){List<Article> all=articles.findByStatusAndDeletedFalseOrderByPublishedAtDesc("PUBLISHED");return tags.findAll().stream().map(t->new TagDto(t.getId(),t.getName(),all.stream().filter(a->a.getTags().stream().anyMatch(at->at.getId().equals(t.getId()))).count())).toList();}
    @Transactional public CategoryDto saveCategory(Long id,NameRequest r){Category c=id==null?new Category():categories.findById(id).orElseThrow(()->new BusinessException(HttpStatus.NOT_FOUND,"分类不存在"));c.setName(r.name().trim());c.setDescription(r.description()==null?"":r.description().trim());c.setSortOrder(r.sortOrder()==null?0:r.sortOrder());c=categories.save(c);return new CategoryDto(c.getId(),c.getName(),c.getDescription(),c.getSortOrder(),0);}
    @Transactional public void deleteCategory(Long id){if(articles.existsByCategoryIdAndDeletedFalse(id))throw new BusinessException(HttpStatus.CONFLICT,"该分类下仍有文章，不能删除");categories.deleteById(id);}
    @Transactional public TagDto saveTag(Long id,NameRequest r){Tag t=id==null?new Tag():tags.findById(id).orElseThrow(()->new BusinessException(HttpStatus.NOT_FOUND,"标签不存在"));t.setName(r.name().trim());t=tags.save(t);return new TagDto(t.getId(),t.getName(),0);}
    @Transactional public void deleteTag(Long id){Tag t=tags.findById(id).orElseThrow(()->new BusinessException(HttpStatus.NOT_FOUND,"标签不存在"));articles.findByDeletedFalseOrderByPublishedAtDesc().forEach(a->a.getTags().remove(t));tags.delete(t);}
}

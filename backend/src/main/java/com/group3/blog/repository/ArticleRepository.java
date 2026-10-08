package com.group3.blog.repository;
import com.group3.blog.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface ArticleRepository extends JpaRepository<Article,Long>{
    List<Article> findByDeletedFalseOrderByPublishedAtDesc();
    List<Article> findByStatusAndDeletedFalseOrderByPublishedAtDesc(String status);
    long countByDeletedFalse(); long countByStatusAndDeletedFalse(String status);
    boolean existsByCategoryIdAndDeletedFalse(Long categoryId);
    @Query("select coalesce(sum(a.viewCount),0) from Article a where a.deleted=false and a.status='PUBLISHED'") long sumPublishedViews();
}

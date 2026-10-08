package com.group3.blog.repository;
import com.group3.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CommentRepository extends JpaRepository<Comment,Long>{List<Comment> findByArticleIdAndStatusOrderByCreatedAtDesc(Long articleId,String status);List<Comment> findAllByOrderByCreatedAtDesc();long countByArticleIdAndStatus(Long articleId,String status);long countByStatus(String status);}

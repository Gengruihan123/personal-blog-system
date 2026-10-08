package com.group3.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public final class BlogDtos {
    private BlogDtos() {}
    public record LoginRequest(@NotBlank String username,@NotBlank String password){}
    public record UserDto(Long id,String username,String nickname,String email,String avatar,String role){}
    public record ProfileRequest(@NotBlank @Size(max=50) String nickname,@NotBlank @Email @Size(max=120) String email,@Size(max=500) String avatar){}
    public record TagDto(Long id,String name,long articleCount){}
    public record CategoryDto(Long id,String name,String description,Integer sortOrder,long articleCount){}
    public record ArticleDto(Long id,String title,String summary,String content,String cover,Long authorId,String authorNickname,Long categoryId,String categoryName,List<TagDto> tags,String status,LocalDateTime publishedAt,LocalDateTime createdAt,LocalDateTime updatedAt,Long viewCount,long commentCount,Long version){}
    public record ArticleWriteRequest(@NotBlank @Size(max=150) String title,@Size(max=500) String summary,@NotBlank String content,@Size(max=500) String cover,@NotNull Long categoryId,List<Long> tagIds,@NotBlank String status,Long version){}
    public record CommentRequest(@NotBlank @Size(min=2,max=500) String content){}
    public record CommentDto(Long id,Long articleId,String articleTitle,Long userId,String userNickname,String content,String status,LocalDateTime createdAt){}
    public record StatusRequest(@NotBlank String status){}
    public record NameRequest(@NotBlank @Size(max=50) String name,String description,Integer sortOrder){}
    public record SubscriptionRequest(@NotBlank @Email @Size(max=120) String email){}
    public record MessageResponse(String message){}
    public record PageResponse<T>(List<T> content,int page,int size,int totalPages,long totalElements){}
    public record StatsDto(long articles,long published,long drafts,long views,long comments,long pendingComments,long subscribers){}
}

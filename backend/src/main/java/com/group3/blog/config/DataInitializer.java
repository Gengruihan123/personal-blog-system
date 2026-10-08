package com.group3.blog.config;

import com.group3.blog.entity.*;
import com.group3.blog.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner initializeData(UserRepository users,CategoryRepository categories,TagRepository tags,ArticleRepository articles,CommentRepository comments,PasswordEncoder encoder){
        return args->{
            if(users.count()>0)return;
            User admin=user("admin",encoder.encode("admin123"),"张小明","admin@example.com","ADMIN");
            User normal=user("user",encoder.encode("user123"),"普通用户","user@example.com","USER");
            users.saveAll(List.of(admin,normal));
            Category tech=category("技术","编程、架构与工程实践",1),life=category("生活","工作方式与生活记录",2),book=category("读书","阅读札记与思考",3);
            categories.saveAll(List.of(tech,life,book));
            Tag java=tag("Java"),concurrency=tag("并发编程"),spring=tag("Spring"),mysql=tag("MySQL"),javascript=tag("JavaScript"),react=tag("React"),efficiency=tag("效率工具");
            tags.saveAll(List.of(java,concurrency,spring,mysql,javascript,react,efficiency));
            Article first=article(admin,tech,"Java并发编程：线程池的原理与最佳实践","本文深入探讨了Java线程池的工作原理，分析了核心参数的设置策略，并通过实例讲解最佳实践。",threadPoolContent(),List.of(java,concurrency),1200,LocalDateTime.now().minusDays(2));
            Article second=article(admin,life,"程序员的高效工作流：我的日常工具与习惯","分享日常使用的工具、编辑器配置以及提高生产力的习惯。","<p>高效并不是一直忙碌，而是把注意力放在最重要的事情上。</p><h2>一、每天只确定三个重点</h2><p>开始工作前选出三个最重要的任务，为深度工作预留不被打扰的时间。</p><h2>二、保持小步提交</h2><p>让每次变更都容易理解、测试和回退。</p>",List.of(efficiency),856,LocalDateTime.now().minusDays(7));
            Article third=article(admin,tech,"Spring Boot 3.0新特性全解析","Spring Boot 3.0带来了对 Jakarta EE 9、原生镜像和可观测性的改进。","<p>Spring Boot 3 基于 Spring Framework 6，最低要求 Java 17。</p><h2>Jakarta 命名空间</h2><p>项目需要将 javax 包迁移到 jakarta 包。</p><h2>原生镜像</h2><p>更完善的 AOT 支持使应用能够更快启动并减少内存占用。</p>",List.of(spring,java),1500,LocalDateTime.now().minusDays(12));
            articles.saveAll(List.of(first,second,third));
            Comment comment=new Comment();comment.setArticle(first);comment.setUser(normal);comment.setContent("线程池参数和执行流程讲得很清楚！");comment.setStatus("APPROVED");comments.save(comment);
        };
    }
    private User user(String username,String hash,String nickname,String email,String role){User u=new User();u.setUsername(username);u.setPasswordHash(hash);u.setNickname(nickname);u.setEmail(email);u.setRole(role);return u;}
    private Category category(String name,String description,int sort){Category c=new Category();c.setName(name);c.setDescription(description);c.setSortOrder(sort);return c;}
    private Tag tag(String name){Tag t=new Tag();t.setName(name);return t;}
    private Article article(User author,Category category,String title,String summary,String content,List<Tag> tags,long views,LocalDateTime published){Article a=new Article();a.setAuthor(author);a.setCategory(category);a.setTitle(title);a.setSummary(summary);a.setContent(content);a.setTags(new LinkedHashSet<>(tags));a.setStatus("PUBLISHED");a.setPublishedAt(published);a.setViewCount(views);return a;}
    private String threadPoolContent(){return """
        <p>在Java并发编程中，线程池是一种重要的技术，它能够有效地管理线程资源，提高系统性能。本文将深入探讨Java线程池的工作原理，分析核心参数的设置策略，并通过实例讲解线程池在实际项目中的最佳实践。</p>
        <h2>一、线程池的基本原理</h2><p>线程池预先维护一定数量的线程，当有任务需要执行时直接从池中获取线程。任务执行完毕后，线程返回池中等待下一个任务。</p>
        <ul><li>减少线程创建和销毁的开销</li><li>控制并发线程数量，避免资源耗尽</li><li>提高任务响应速度</li><li>便于统一管理和监控</li></ul>
        <h2>二、ThreadPoolExecutor的核心参数</h2><pre><code>public ThreadPoolExecutor(
    int corePoolSize,
    int maximumPoolSize,
    long keepAliveTime,
    TimeUnit unit,
    BlockingQueue&lt;Runnable&gt; workQueue,
    ThreadFactory threadFactory,
    RejectedExecutionHandler handler
)</code></pre>
        <h3>1. corePoolSize</h3><p>线程池维护的核心线程数量。</p><h3>2. maximumPoolSize</h3><p>线程池允许创建的最大线程数量。</p><h3>3. workQueue</h3><p>用于保存等待执行任务的阻塞队列，生产环境通常应选择有界队列。</p>
        <h2>三、线程池的工作流程</h2><ol><li>线程数小于核心线程数时创建新线程。</li><li>核心线程已满时把任务加入队列。</li><li>队列已满时继续创建非核心线程。</li><li>达到最大线程数后执行拒绝策略。</li></ol>
        <h2>四、最佳实践</h2><p>根据任务类型设置线程数，使用有界队列，明确拒绝策略，并在应用关闭时调用 shutdown 完成优雅停机。</p>
        <h2>五、总结</h2><p>合理使用线程池可以显著提高系统性能与资源利用率，也应通过监控持续观察活跃线程数、队列长度和任务完成情况。</p>
        """;}
}

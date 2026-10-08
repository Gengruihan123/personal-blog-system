package com.group3.blog.repository;
import com.group3.blog.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface SubscriptionRepository extends JpaRepository<Subscription,Long>{Optional<Subscription> findByEmail(String email);long countByStatus(String status);}

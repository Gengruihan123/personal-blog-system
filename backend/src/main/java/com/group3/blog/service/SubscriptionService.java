package com.group3.blog.service;
import com.group3.blog.entity.Subscription;
import com.group3.blog.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Locale;
import java.util.UUID;
@Service public class SubscriptionService{
    private final SubscriptionRepository subscriptions;public SubscriptionService(SubscriptionRepository subscriptions){this.subscriptions=subscriptions;}
    @Transactional public void subscribe(String email){String normalized=email.trim().toLowerCase(Locale.ROOT);Subscription s=subscriptions.findByEmail(normalized).orElseGet(Subscription::new);s.setEmail(normalized);s.setStatus("ACTIVE");if(s.getUnsubscribeToken()==null)s.setUnsubscribeToken(UUID.randomUUID().toString().replace("-",""));subscriptions.save(s);}
}

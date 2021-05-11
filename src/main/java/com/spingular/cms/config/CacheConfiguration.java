package com.spingular.cms.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.spingular.cms.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.spingular.cms.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.spingular.cms.domain.User.class.getName());
            createCache(cm, com.spingular.cms.domain.Authority.class.getName());
            createCache(cm, com.spingular.cms.domain.User.class.getName() + ".authorities");
            createCache(cm, com.spingular.cms.domain.PersistentToken.class.getName());
            createCache(cm, com.spingular.cms.domain.User.class.getName() + ".persistentTokens");
            createCache(cm, com.spingular.cms.domain.Appuser.class.getName());
            createCache(cm, com.spingular.cms.domain.Appuser.class.getName() + ".blogs");
            createCache(cm, com.spingular.cms.domain.Appuser.class.getName() + ".communities");
            createCache(cm, com.spingular.cms.domain.Appuser.class.getName() + ".notifications");
            createCache(cm, com.spingular.cms.domain.Appuser.class.getName() + ".comments");
            createCache(cm, com.spingular.cms.domain.Appuser.class.getName() + ".posts");
            createCache(cm, com.spingular.cms.domain.Appuser.class.getName() + ".followeds");
            createCache(cm, com.spingular.cms.domain.Appuser.class.getName() + ".followings");
            createCache(cm, com.spingular.cms.domain.Appuser.class.getName() + ".blockedusers");
            createCache(cm, com.spingular.cms.domain.Appuser.class.getName() + ".blockingusers");
            createCache(cm, com.spingular.cms.domain.Appuser.class.getName() + ".interests");
            createCache(cm, com.spingular.cms.domain.Appuser.class.getName() + ".activities");
            createCache(cm, com.spingular.cms.domain.Appuser.class.getName() + ".celebs");
            createCache(cm, com.spingular.cms.domain.Blog.class.getName());
            createCache(cm, com.spingular.cms.domain.Blog.class.getName() + ".posts");
            createCache(cm, com.spingular.cms.domain.Post.class.getName());
            createCache(cm, com.spingular.cms.domain.Post.class.getName() + ".comments");
            createCache(cm, com.spingular.cms.domain.Post.class.getName() + ".tags");
            createCache(cm, com.spingular.cms.domain.Post.class.getName() + ".topics");
            createCache(cm, com.spingular.cms.domain.Topic.class.getName());
            createCache(cm, com.spingular.cms.domain.Topic.class.getName() + ".posts");
            createCache(cm, com.spingular.cms.domain.Tag.class.getName());
            createCache(cm, com.spingular.cms.domain.Tag.class.getName() + ".posts");
            createCache(cm, com.spingular.cms.domain.Comment.class.getName());
            createCache(cm, com.spingular.cms.domain.Notification.class.getName());
            createCache(cm, com.spingular.cms.domain.Appphoto.class.getName());
            createCache(cm, com.spingular.cms.domain.Community.class.getName());
            createCache(cm, com.spingular.cms.domain.Community.class.getName() + ".blogs");
            createCache(cm, com.spingular.cms.domain.Community.class.getName() + ".cfolloweds");
            createCache(cm, com.spingular.cms.domain.Community.class.getName() + ".cfollowings");
            createCache(cm, com.spingular.cms.domain.Community.class.getName() + ".cblockedusers");
            createCache(cm, com.spingular.cms.domain.Community.class.getName() + ".cblockingusers");
            createCache(cm, com.spingular.cms.domain.Community.class.getName() + ".cinterests");
            createCache(cm, com.spingular.cms.domain.Community.class.getName() + ".cactivities");
            createCache(cm, com.spingular.cms.domain.Community.class.getName() + ".ccelebs");
            createCache(cm, com.spingular.cms.domain.Follow.class.getName());
            createCache(cm, com.spingular.cms.domain.Blockuser.class.getName());
            createCache(cm, com.spingular.cms.domain.Interest.class.getName());
            createCache(cm, com.spingular.cms.domain.Interest.class.getName() + ".appusers");
            createCache(cm, com.spingular.cms.domain.Activity.class.getName());
            createCache(cm, com.spingular.cms.domain.Activity.class.getName() + ".appusers");
            createCache(cm, com.spingular.cms.domain.Celeb.class.getName());
            createCache(cm, com.spingular.cms.domain.Celeb.class.getName() + ".appusers");
            createCache(cm, com.spingular.cms.domain.Cinterest.class.getName());
            createCache(cm, com.spingular.cms.domain.Cinterest.class.getName() + ".communities");
            createCache(cm, com.spingular.cms.domain.Cactivity.class.getName());
            createCache(cm, com.spingular.cms.domain.Cactivity.class.getName() + ".communities");
            createCache(cm, com.spingular.cms.domain.Cceleb.class.getName());
            createCache(cm, com.spingular.cms.domain.Cceleb.class.getName() + ".communities");
            createCache(cm, com.spingular.cms.domain.Urllink.class.getName());
            createCache(cm, com.spingular.cms.domain.Frontpageconfig.class.getName());
            createCache(cm, com.spingular.cms.domain.Feedback.class.getName());
            createCache(cm, com.spingular.cms.domain.ConfigVariables.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}

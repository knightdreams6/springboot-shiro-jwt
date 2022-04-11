package com.knight.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置
 *
 * @author knight
 * @date 2022/04/11
 */
@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, Object> objRedisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		// 替换默认序列化(默认采用的是JDK序列化)
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
				Object.class);
		// 使用StringRedisSerializer来序列化和反序列化redis的key/hashKey
		template.setKeySerializer(stringRedisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);
		// 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value/hashValue
		template.setValueSerializer(jackson2JsonRedisSerializer);
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}

	@Bean
	public RedisTemplate<String, Integer> intRedisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Integer> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		// 替换默认序列化(默认采用的是JDK序列化)
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		Jackson2JsonRedisSerializer<Integer> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
				Integer.class);
		// 使用StringRedisSerializer来序列化和反序列化redis的key/hashKey
		template.setKeySerializer(stringRedisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);
		// 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value/hashValue
		template.setValueSerializer(jackson2JsonRedisSerializer);
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}

}

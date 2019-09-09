package com.redis.distributed;
/**
 * 1.redis的测试类
 * 项目名称：redisDistributed 
 * 类名称：RedisTest
 * 开发者：Lenovo
 * 开发时间：2019年9月8日下午4:31:38
 */
public class RedisTest {

	public static void main(String[] args) {
		RedisService redisService = new RedisService();
		for (int i = 0; i < 50; i++) {
			redisService.seckill();
		}
	}
}

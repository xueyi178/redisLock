package com.redis.distributed;
/**
 * 1.使用线程来进行测试
 * 项目名称：redisDistributed 
 * 类名称：ThreadRedis
 * 开发者：Lenovo
 * 开发时间：2019年9月8日下午4:33:44
 */
public class ThreadRedis extends Thread{

	private RedisService redisService;

	public ThreadRedis(RedisService redisService) {
		this.redisService = redisService;
	}

	@Override
	public void run() {
		redisService.seckill();

	}

}

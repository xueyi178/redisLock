package com.redis.distributed;

import org.apache.commons.lang.StringUtils;

/**
 * 1. redis的测试类
 * 项目名称：redisDistributed 
 * 类名称：RedisTest
 * 开发者：Lenovo
 * 开发时间：2019年9月8日下午4:20:23
 */
public class RedisService{

	//创建LockRedis的对象
	private LockRedis lockRedis = new LockRedis();
	
	/**
	 * 1.测试分布式锁的方法
	 */
	public void seckill() {
		String identifier = lockRedis.getRedisLock("itmayiedu", 5000l, 5000l);
		if (StringUtils.isEmpty(identifier)) {
			// 获取锁失败
			System.out.println(Thread.currentThread().getName() + ",获取锁失败，原因时间超时!!!");
			return;
		}
		System.out.println(Thread.currentThread().getName() + "获取锁成功,锁id identifier:" + identifier + "，执行业务逻辑");
		try {
			Thread.sleep(30);
		} catch (Exception e) {

		}
		// 释放锁
		boolean releaseLock = lockRedis.unRedisLock("itmayiedu", identifier);
		if (releaseLock) {
			System.out.println(Thread.currentThread().getName() + "释放锁成功,锁id identifier:" + identifier);
		}
	}
}

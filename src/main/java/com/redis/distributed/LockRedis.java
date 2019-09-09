package com.redis.distributed;

import java.util.UUID;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
/**
 * 1.redis的分布式锁
 * 项目名称：redisDistributed 
 * 类名称：LockRedis
 * 开发者：Lenovo
 * 开发时间：2019年9月8日下午3:40:20
 */
public class LockRedis {

	//在redis中定义一个相通的key
	private String redisLockKey = "redis_lock";

	//redis实现分布式: 有两个超时时间问题
	/**
	 * 两个超时时间含义:
	 * 1. 获取锁之前的超时时间------在尝试获取锁的时候,如果在规定的时间内还没有获取锁,直接放弃<br>
	 * 2. 获取锁之后的超时时间------当获取锁成功之后,对应的key有对应的有效期
	 */

	/**
	 * 1.获取redis锁的方法
	 * @param lockKey
	 * 			锁的key
	 * @param acquireTimeout
	 * 			获取锁之前的超时时间
	 * @param timeOut
	 * 			获取锁之后的超时时间
	 * @return
	 */
	public String getRedisLock(String lockKey, Long acquireTimeout, Long timeOut) {
		JedisPool jedisPool = null;
		Jedis jedis = null;
		String identifierValue = null;
		try {
			//1.获取redis的连接
			jedisPool = JedisPoolUtil.getJedisPoolInstance();
			jedis = jedisPool.getResource();
			//2.随机生成一个value
			identifierValue  = UUID.randomUUID().toString();

			//3.定义锁的名称
			String lockName  = redisLockKey+lockKey;

			//4.定义上锁成功之后,锁的超时时间
			int expireLock = (int) (timeOut / 1000);

			//5.定义在没有获取锁之前,锁的超时时间
			Long endTime = System.currentTimeMillis() + acquireTimeout;

			//8.使用循环的方式获取锁
			while(System.currentTimeMillis() < endTime) {
				//9.使用setnx方法设置锁值
				if (jedis.setnx(lockName, identifierValue) == 1) {
					//10.判断返回结果如果为1,则可以成功获取锁,并且设置锁的超时时间
					//设置超时时间的原因: 是为了防止死锁的出现
					//zookeeper也需要防止死锁现象的出现,使用临时节点,设置session的有效期
					jedis.expire(lockName, expireLock);
					return identifierValue;
				}else {
					return null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//11.释放资源
			if(null != jedis) {
				JedisPoolUtil.release(jedisPool, jedis);
			}
		}
		return null;
	}

	/**
	 * 2.释放锁
	 * @return
	 */
	public boolean unRedisLock(String lockKey, String identifier) {
		JedisPool jedisPool = null;
		Jedis jedis = null;
		try {
			//1.获取redis的连接
			jedisPool = JedisPoolUtil.getJedisPoolInstance();
			jedis = jedisPool.getResource();
			// 2.定义锁的名称
			String lockName = redisLockKey + lockKey;
			// 3.如果value与redis中一致直接删除，否则等待超时
			if (identifier.equals(jedis.get(lockName))) {
				Long del = jedis.del(lockName);
				if(del == 1) {
					return true;
				}
				return false;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//11.释放资源
			if(null != jedis) {
				JedisPoolUtil.release(jedisPool, jedis);
			}
		}
		return false;
	}
}

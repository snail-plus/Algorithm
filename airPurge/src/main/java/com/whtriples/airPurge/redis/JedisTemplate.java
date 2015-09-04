package com.whtriples.airPurge.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whtriples.airPurge.util.ConfigUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * JedisTemplate 提供了一个template方法，负责对Jedis连接的获取与归还.
 * JedisAction<T> 和 JedisActionNoResult两种回调接口，适用于有无返回值两种情况.
 * 同时提供一些最常用函数的封装, 如get/set/zadd等.
 */
public class JedisTemplate {
    private static Logger logger = LoggerFactory.getLogger(JedisTemplate.class);
    private Pool<Jedis> jedisPool;

    public JedisTemplate(Pool<Jedis> jedisPool) {
    	logger.warn("初始化redis，使用数据库：db" +ConfigUtil.getConfig("redis.database") +"  redis服务器信息：" + jedisPool.getResource().info());
        this.jedisPool = jedisPool;
    }

    /**
     * 执行有返回结果的action.
     */
    public <T> T execute(JedisAction<T> jedisAction) throws JedisException {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = jedisPool.getResource();
            return jedisAction.action(jedis);
        } catch (JedisConnectionException e) {
            logger.error("Redis connection lost.", e);
            broken = true;
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * 执行无返回结果的action.
     */
    public void execute(JedisActionNoResult jedisAction) throws JedisException {
        Jedis jedis = null;
        boolean broken = false;
        try {
            jedis = jedisPool.getResource();
            jedisAction.action(jedis);
        } catch (JedisConnectionException e) {
            logger.error("Redis connection lost.", e);
            broken = true;
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
    }

    /**
     * 根据连接是否已中断的标志，分别调用returnBrokenResource或returnResource.
     */
    protected void closeResource(Jedis jedis, boolean connectionBroken) {
        if (jedis != null) {
            try {
                if (connectionBroken) {
                    jedisPool.returnBrokenResource(jedis);
                } else {
                    jedisPool.returnResource(jedis);
                }
            } catch (Exception e) {
                logger.error("Error happen when return jedis to pool, try to close it directly.", e);
                JedisUtils.closeJedis(jedis);
            }
        }
    }

    /**
     * 获取内部的pool做进一步的动作.
     */
    public Pool<Jedis> getJedisPool() {
        return jedisPool;
    }

    /**
     * 有返回结果的回调接口定义.
     */
    public interface JedisAction<T> {
        T action(Jedis jedis);
    }

    /**
     * 无返回结果的回调接口定义.
     */
    public interface JedisActionNoResult {
        void action(Jedis jedis);
    }

    // ////////////// 常用方法的封装 ///////////////////////// //

    // ////////////// 公共 ///////////////////////////

    /**
     * 删除key, 如果key存在返回true, 否则返回false.
     */
    public Boolean del(final String... keys) {
        return execute(new JedisAction<Boolean>() {

            @Override
            public Boolean action(Jedis jedis) {
                return jedis.del(keys) == 1;
            }
        });
    }

    public void flushDB() {
        execute(new JedisActionNoResult() {

            @Override
            public void action(Jedis jedis) {
                jedis.flushDB();
            }
        });
    }

    // ////////////// 关于String ///////////////////////////

    /**
     * 如果key不存在, 返回null.
     */
    public String get(final String key) {
        return execute(new JedisAction<String>() {

            @Override
            public String action(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    /**
     * 如果key不存在, 返回null.
     */
    public Long getAsLong(final String key) {
        String result = get(key);
        return result != null ? Long.valueOf(result) : null;
    }

    /**
     * 如果key不存在, 返回null.
     */
    public Integer getAsInt(final String key) {
        String result = get(key);
        return result != null ? Integer.valueOf(result) : null;
    }

    public void set(final String key, final String value) {
        execute(new JedisActionNoResult() {

            @Override
            public void action(Jedis jedis) {
                jedis.set(key, value);
            }
        });
    }

    public void setex(final String key, final String value, final int seconds) {
        execute(new JedisActionNoResult() {

            @Override
            public void action(Jedis jedis) {
                jedis.setex(key, seconds, value);
            }
        });
    }

    /**
     * 如果key还不存在则进行设置，返回true，否则返回false.
     */
    public Boolean setnx(final String key, final String value) {
        return execute(new JedisAction<Boolean>() {

            @Override
            public Boolean action(Jedis jedis) {
                return jedis.setnx(key, value) == 1;
            }
        });
    }

    /**
     * 综合setNX与setEx的效果.
     */
    public Boolean setnxex(final String key, final String value, final int seconds) {
        return execute(new JedisAction<Boolean>() {

            @Override
            public Boolean action(Jedis jedis) {
                String result = jedis.set(key, value, "NX", "EX", seconds);
                return JedisUtils.isStatusOk(result);
            }
        });
    }

    public Long incr(final String key) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.incr(key);
            }
        });
    }

    public Long decr(final String key) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.decr(key);
            }
        });
    }

    // ////////////// 关于List ///////////////////////////
    public void lpush(final String key, final String... values) {
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.lpush(key, values);
            }
        });
    }

    public List<String> lrange(final String key, final Long start, final Long end) {
        return execute(new JedisAction<List<String>>() {
            @Override
            public List<String> action(Jedis jedis) {
                return jedis.lrange(key, start, end);
            }
        });
    }

    public String rpop(final String key) {
        return execute(new JedisAction<String>() {

            @Override
            public String action(Jedis jedis) {
                return jedis.rpop(key);
            }
        });
    }

    /**
     * 返回List长度, key不存在时返回0，key类型不是list时抛出异常.
     */
    public Long llen(final String key) {
        return execute(new JedisAction<Long>() {

            @Override
            public Long action(Jedis jedis) {
                return jedis.llen(key);
            }
        });
    }

    /**
     * 删除List中的第一个等于value的元素，value不存在或key不存在时返回false.
     */
    public Boolean lremOne(final String key, final String value) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                Long count = jedis.lrem(key, 1, value);
                return (count == 1);
            }
        });
    }

    /**
     * 删除List中的所有等于value的元素，value不存在或key不存在时返回false.
     */
    public Boolean lremAll(final String key, final String value) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                Long count = jedis.lrem(key, 0, value);
                return (count > 0);
            }
        });
    }

    // ////////////// 关于Sorted Set ///////////////////////////

    /**
     * 加入Sorted set, 如果member在Set里已存在, 只更新score并返回false, 否则返回true.
     */
    public Boolean zadd(final String key, final String member, final double score) {
        return execute(new JedisAction<Boolean>() {

            @Override
            public Boolean action(Jedis jedis) {
                return jedis.zadd(key, score, member) == 1;
            }
        });
    }

    /**
     * 删除sorted set中的元素，成功删除返回true，key或member不存在返回false.
     */
    public Boolean zrem(final String key, final String member) {
        return execute(new JedisAction<Boolean>() {

            @Override
            public Boolean action(Jedis jedis) {
                return jedis.zrem(key, member) == 1 ? true : false;
            }
        });
    }

    /**
     * 当key不存在时返回null.
     */
    public Double zscore(final String key, final String member) {
        return execute(new JedisAction<Double>() {

            @Override
            public Double action(Jedis jedis) {
                return jedis.zscore(key, member);
            }
        });
    }

    /**
     * 返回sorted set长度, key不存在时返回0.
     */
    public Long zcard(final String key) {
        return execute(new JedisAction<Long>() {

            @Override
            public Long action(Jedis jedis) {
                return jedis.zcard(key);
            }
        });
    }

    // ////////////// 关于 Hash ///////////////////////////

    /**
     * 为指定的Key设定Field/Value对，如果Key不存在，该命令将创建新Key以参数中的Field/Value对，如果参数中的Field在该Key中已经存在，则用新值覆盖其原有值.
     */
    public Boolean hset(final String key, final String field, final String value) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.hset(key, field, value) == 1;
            }
        });
    }

    /**
     * 返回参数中Field的关联值，如果参数中的Key或Field不存，返回null.
     */
    public String hget(final String key, final String field) {
        return execute(new JedisAction<String>() {
            @Override
            public String action(Jedis jedis) {
                return jedis.hget(key, field);
            }
        });
    }

    /**
     * 只有当参数中的Key或Field不存在的情况下，为指定的Key设定Field/Value对，否则该命令不会进行任何操作.
     */
    public Boolean hsetnx(final String key, final String field, final String value) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.hsetnx(key, field, value) == 1;
            }
        });
    }

    /**
     * 逐对依次设置参数中给出的Field/Value对.如果其中某个Field已经存在，则用新值覆盖原有值.如果Key不存在，则创建新Key，同时设定参数中的Field/Value.
     */
    public void hmset(final String key, final Map<String, String> hash) {
        execute(new JedisActionNoResult() {
            @Override
            public void action(Jedis jedis) {
                jedis.hmset(key, hash);
            }
        });
    }

    /**
     * 获取和参数中指定Fields关联的一组Values.如果请求的Field不存在，其值返回null.如果Key不存在，该命令将其视为空Hash，因此返回一组null.
     */
    public List<String> hmget(final String key, final String... fields) {
        return execute(new JedisAction<List<String>>() {
            @Override
            public List<String> action(Jedis jedis) {
                return jedis.hmget(key, fields);
            }
        });
    }

    /**
     * 增加指定Key中指定Field关联的Value的值.如果Key或Field不存在，该命令将会创建一个新Key或新Field，并将其关联的Value初始化为0，之后再指定数字增加的操作.返回运算后的值
     */
    public long hincrBy(final String key, final String field, final long value) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.hincrBy(key, field, value);
            }
        });
    }

    /**
     * 判断指定Key中的指定Field是否存在.
     */
    public Boolean hexists(final String key, final String field) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.hexists(key, field);
            }
        });
    }

    /**
     * 从指定Key的Hashes Value中删除参数中指定的多个字段，如果不存在的字段将被忽略.如果Key不存在，则将其视为空Hashes，并返回0.返回实际删除的Field数量.
     */
    public Boolean hdel(final String key, final String... fields) {
        return execute(new JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return jedis.hdel(key, fields) == 1;
            }
        });
    }

    /**
     * 获取该Key所包含的Field的数量.
     */
    public Long hlen(final String key) {
        return execute(new JedisAction<Long>() {
            @Override
            public Long action(Jedis jedis) {
                return jedis.hlen(key);
            }
        });
    }

    /**
     * 返回指定Key的所有Fields名.
     */
    public Set<String> hkeys(final String key) {
        return execute(new JedisAction<Set<String>>() {
            @Override
            public Set<String> action(Jedis jedis) {
                return jedis.hkeys(key);
            }
        });
    }

    /**
     * 返回指定Key的所有Values名.
     */
    public List<String> hvals(final String key) {
        return execute(new JedisAction<List<String>>() {
            @Override
            public List<String> action(Jedis jedis) {
                return jedis.hvals(key);
            }
        });
    }

    /**
     * 获取该键包含的所有Field/Value.其返回格式为一个Field、一个Value，并以此类推.
     */
    public Map<String, String> hgetAll(final String key) {
        return execute(new JedisAction<Map<String, String>>() {
            @Override
            public Map<String, String> action(Jedis jedis) {
                return jedis.hgetAll(key);
            }
        });
    }

}
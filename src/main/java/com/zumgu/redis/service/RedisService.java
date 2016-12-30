package com.zumgu.redis.service;

import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 강홍구 on 2016-12-30.
 */
@Service
@Transactional
public class RedisService {
    @Resource(name="redisTemplate")
    private ValueOperations<String, String> valueOperations;

    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOperations;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    @Resource(name = "redisTemplate")
    private SetOperations<String, String> setOperations;

    @Resource(name="redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    public void setValue (String key, String value) {
        valueOperations.set(key, value);
    }

    public void pushList (String key, List<String> list) {
        for (String value : list) {
            listOperations.rightPush(key, value); // rpush test:task 자기소개 rpush [key] [value]
        }
    }

    public void putHash (String key, Map<String, String> map) {
        for (String mapKey : map.keySet()) {
            hashOperations.put(key, mapKey, map.get(mapKey));
        }
    }

    public void putSet (String key, Set<String> set) {
        for (String aSet : set) {
            setOperations.add(key, aSet);
        }
    }

    public void putZset (String key, Set<String> set) {
        int i = 1;
        for (String aSet : set) {
            zSetOperations.add(key, aSet, i++);
        }
    }

}

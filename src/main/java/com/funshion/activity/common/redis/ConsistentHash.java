package com.funshion.activity.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHash<S extends RedisTemplate> { // S类封装了机器节点的信息 ，如name、password、ip、port等

    private final static Logger logger = LoggerFactory.getLogger(ConsistentHash.class);

    private TreeMap<Long, S> nodes; // 虚拟节点

    private List<S> shards; // 真实机器节点

    private final int NODE_NUM = 1600; // 每个机器节点关联的虚拟节点个数

    public ConsistentHash(List<S> shards) {
        super();
        this.shards = shards;
        init();
    }

    private void init() { // 初始化一致性hash环   
        nodes = new TreeMap<Long, S>();
        for (int i = 0; i != shards.size(); ++i) { // 每个真实机器节点都需要关联虚拟节点   
            final S shardInfo = shards.get(i);

            for (int n = 0; n < NODE_NUM; n++) {
                // 一个真实机器节点关联NODE_NUM个虚拟节点
                LettuceConnectionFactory connectionFactory = ((LettuceConnectionFactory) shardInfo.getConnectionFactory());
                String addressPort = connectionFactory.getHostName() + "_" + connectionFactory.getPort();
                nodes.put(hash(addressPort + n), shardInfo);
            }
        }
    }


    public void add(S node) {
        for (int i = 0; i < NODE_NUM; i++) {
            /* 对于一个实际机器节点 node, 对应 NODE_NUM 个虚拟节点
             * 不同的虚拟节点(i不同)有不同的hash值,但都对应同一个实际机器node
             * 虚拟node一般是均衡分布在环上的,数据存储在顺时针方向的虚拟node上
             */
            LettuceConnectionFactory connectionFactory = ((LettuceConnectionFactory) node.getConnectionFactory());
            String addressPort = connectionFactory.getHostName() + "_" + connectionFactory.getPort();
            nodes.put(hash(addressPort + i), node);

        }


    }

    public void remove(S node) {
        for (int i = 0; i < NODE_NUM; i++) {
            LettuceConnectionFactory connectionFactory = ((LettuceConnectionFactory) node.getConnectionFactory());
            String addressPort = connectionFactory.getHostName() + "_" + connectionFactory.getPort();
            nodes.remove(hash(addressPort + i));
        }
    }


    public S getShardInfo(String key) {
        if (nodes.size() == 0) {
            logger.warn("ConsistentHash nodes is empty.");
            return null;
        }
        SortedMap<Long, S> tail = nodes.tailMap(hash(key)); // 沿环的顺时针找到一个虚拟节点
        if (tail.size() == 0) {
            return nodes.get(nodes.firstKey());
        }
        return tail.get(tail.firstKey()); // 返回该虚拟节点对应的真实机器节点的信息   
    }

    /**
     * MurMurHash算法，是非加密HASH算法，性能很高，
     * 比传统的CRC32,MD5，SHA-1（这两个算法都是加密HASH算法，复杂度本身就很高，带来的性能上的损害也不可避免）
     * 等HASH算法要快很多，而且据说这个算法的碰撞率很低.
     * http://murmurhash.googlepages.com/
     */
    private Long hash(String key) {

        ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
        int seed = 0x1234ABCD;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(
                    ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, do this first:   
            // finish.position(8-buf.remaining());   
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;
    }

    public List<S> getShards() {
        return shards;
    }
}  
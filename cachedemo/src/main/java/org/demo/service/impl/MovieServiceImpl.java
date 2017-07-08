package org.demo.service.impl;

import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.demo.dao.MovieDao;
import org.demo.entity.Movie;
import org.demo.service.MovieService;
import org.demo.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangl on 2017/5/4.
 */
@Service
@Transactional
public class MovieServiceImpl implements MovieService{

    @Autowired
    private MovieDao dao;

    /**
     * 语法格式:
     * 1. *秒 *分 *时 *日(几号) *月 *星期几 *年
     * 2. *秒 *分 *时 *日(几号) *月 *星期几
     *
     * "*"号：表示任意值
     * （例如：在分上是用*号，表示每分钟都会触发该事件）
     *
     * "/"号：表示起始时间开始触发，然后每隔固定时间触发一次
     * （例如：在秒上使用1/5，表示1秒后开始触发，每间隔5秒执行一次）
     *
     * "?"号只能出现在日或者星期几，因为日和星期几会相互制约和影响，
     * 因此两者其一使用？号表示忽略，不影响另一方
     * (例如：0 0 24 1 * ? 表示每月1号晚上的12点整开始触发，
     * 最后的？号表示不管1号是星期几都会触发)
     */
    @Override
    //@Scheduled(cron = "0 0 24 * * ?")
    @Scheduled(cron = "0/5 * * * * ?")
    public void cacheNewMovie() throws JSONException {
        System.out.println("执行定时任务...");
        //查询最新的热门电影
        List<Movie> list = dao.findMovies();
        //获取Jedis实例
        Jedis jedis = JedisUtil.getJedis();
        //方式一：先清空缓存
        jedis.del("list");
        //将查询的结果缓存到redis中
        for (Movie movie : list) {
            //将每一个Movie实例转换成json字符串
            String json = JSONUtil.serialize(movie, false);
            //放入redis的集合中
            jedis.lpush("list", json);
        }
        //方式二：裁剪缓存，只保留5条记录
        //jedis.ltrim("list",0, 4);

        jedis.close();
    }

    @Override
    public List<Map<String, String>> findCacheMovie() throws JSONException {
        Jedis jedis = JedisUtil.getJedis();
        List<String> jsonList = jedis.lrange("list",0, -1);
        List<Map<String,String>> mapList = new ArrayList<>();
        for (String json : jsonList) {
            //反序列化成map对象
            Map<String, String> map =(Map<String, String>)JSONUtil.deserialize(json);
            mapList.add(map);
        }
        jedis.close();
        return mapList;
    }
}

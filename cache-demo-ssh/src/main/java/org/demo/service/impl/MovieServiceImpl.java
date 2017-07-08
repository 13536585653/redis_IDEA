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
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Godql on 2017/5/4.
 */
@Service
@Transactional
public class MovieServiceImpl implements MovieService{

    @Autowired
    private MovieDao dao;

    @Override
    //@Scheduled(cron = "0 0 24 * * ?")  //每天晚上24点触发
    @Scheduled(cron = "0/5 * * * * ?") //间隔5秒中执行一次
    public void cacheNewMovie() throws JSONException {
        System.out.println("执行定时任务...");
        //查询最新热门电影
        List<Movie> list = dao.findMovies();
        //获取Jedis对象
        Jedis jedis = JedisUtil.getJedis();
        //方式一：先清空缓存
        //jedis.del("list");
        //将查询的结果缓存到redis中
        for (Movie movie : list){
            //将每一个Movie实例转换成json字符串
            String json = JSONUtil.serialize(movie, false);
            //放入redis集合中
            jedis.lpush("list", json);  //不定期清理 会堆积，最简单的办法可以裁剪
        }
        //方式二：裁剪缓存，只保留5条记录
        jedis.ltrim("list",0,4);
        jedis.close();
    }

    @Override
    public List<Map<String, String>> findCacheMovie() throws JSONException {
        Jedis jedis = JedisUtil.getJedis();
        List<String> jsonList = jedis.lrange("list", 0, -1);
        List<Map<String, String>> mapList = new ArrayList<>();
        for (String json : jsonList ) {
            //反序列化成map
            mapList.add((Map<String, String>)JSONUtil.deserialize(json)); //返回的是map对象 所以要强转
        }
        jedis.close();
        return mapList;
    }
}

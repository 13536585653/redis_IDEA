package org.demo.service;

import org.apache.struts2.json.JSONException;

import java.util.List;
import java.util.Map;

/**
 * Created by Godql on 2017/5/4.
 */
public interface MovieService {

    /**
     * 缓存最新的电影信息
     */
    public void cacheNewMovie() throws JSONException;

    /**
     * 查询缓存信息
     * @return
     */
    public List<Map<String, String>> findCacheMovie() throws JSONException;  //放的list返回map？
}

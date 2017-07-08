package org.demo.dao;

import org.apache.ibatis.annotations.Select;
import org.demo.entity.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Godql on 2017/5/4.
 */
@Repository
public interface MovieDao {

    /**
     * 经过统计和计算，得出最新热门的电影
     * 这里只是做了一个简单的查询，略表敬意
     * @return
     */
    @Select("SELECT M_ID AS mid, M_NAME AS movieName, RELEASE_DATE AS releaseDate, " +
            "M_DESCRIBE AS 'describe' FROM MOVIES " +
            "ORDER BY M_ID DESC LIMIT 0, 5 ")
    public List<Movie> findMovies();
}

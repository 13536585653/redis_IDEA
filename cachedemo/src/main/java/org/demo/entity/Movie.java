package org.demo.entity;

import org.apache.struts2.json.annotations.JSON;

import java.util.Date;

/**
 * Created by wangl on 2017/5/4.
 */
public class Movie {

    private int mid;
    private String movieName;
    private Date releaseDate;
    private String describe;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    //转换JSON的时候指定日期的格式
    @JSON(format = "yyyy-MM-dd hh:mm:ss")
    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}

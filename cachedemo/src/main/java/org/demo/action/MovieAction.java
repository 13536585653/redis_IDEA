package org.demo.action;

import org.apache.struts2.json.JSONException;
import org.demo.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * Created by wangl on 2017/5/4.
 */
@Controller
@Scope("prototype")
public class MovieAction {

    @Autowired
    private MovieService service;

    private List<Map<String, String>> list;

    public List<Map<String, String>> getList() {
        return list;
    }

    public void setList(List<Map<String, String>> list) {
        this.list = list;
    }

    public String findMovies(){
        try {
            list = service.findCacheMovie();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "success";
    }
}

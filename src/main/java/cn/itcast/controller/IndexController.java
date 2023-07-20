package cn.itcast.controller;

import cn.itcast.service.GetDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @Autowired
    private GetDataService getDataService;

    @RequestMapping("/index")
    public String showIndex(){
        return "index";
    }

    @RequestMapping(value = "/getData"
            ,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getData(){
        String data = getDataService.getData();
        return data;
    }
}

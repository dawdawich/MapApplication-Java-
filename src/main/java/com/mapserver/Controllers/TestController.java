package com.mapserver.Controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.PermitAll;

@Controller
public class TestController {

    @RequestMapping(path = "/test")
    @ResponseBody
    @PermitAll
    public String test()
    {
        return "success";
    }

}

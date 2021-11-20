package uofg.zehuilyu.queueapi.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;

@Controller
public class ServerListController {
    @RequestMapping("/user/chooseservice")
    public String ServiceList(
            Model model,
            HttpSession httpSession){
            return "redirect:/services.html";
    }
}

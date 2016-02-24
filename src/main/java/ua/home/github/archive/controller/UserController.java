package ua.home.github.archive.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String authorize(@RequestParam(value = "userId") String userId, HttpServletResponse response) {
        Cookie cookie = new Cookie("userId", userId);
        cookie.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(cookie);
        return "Done";
    }

}

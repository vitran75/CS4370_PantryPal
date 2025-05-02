package main.java.com.pantrypal.controllers;

import main.java.com.pantrypal.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * Handles user login at /login.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the login page.
     */
    @GetMapping
    public ModelAndView showLoginPage(@RequestParam(name = "error", required = false) String error) {
        userService.unAuthenticate(); // Log out any existing session

        ModelAndView mv = new ModelAndView("login_page");
        mv.addObject("errorMessage", error);
        return mv;
    }

    /**
     * Processes login form submission.
     */
    @PostMapping
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        try {
            boolean authenticated = userService.authenticate(username, password);
            if (authenticated) {
                return "redirect:/";
            } else {
                String message = URLEncoder.encode("Invalid username or password.", StandardCharsets.UTF_8);
                return "redirect:/login?error=" + message;
            }
        } catch (SQLException e) {
            String message = URLEncoder.encode("Login failed due to a server error. Please try again.", StandardCharsets.UTF_8);
            return "redirect:/login?error=" + message;
        }
    }
}

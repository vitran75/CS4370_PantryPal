package main.java.com.pantrypal.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

import main.java.com.pantrypal.models.FollowableUser;
import main.java.com.pantrypal.services.PeopleService;
import main.java.com.pantrypal.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles /chefs URL and follow/unfollow actions for PantryPal users.
 */
@Controller
@RequestMapping("/chefs")
public class PeopleController {

    private final UserService userService;
    private final PeopleService peopleService;

    @Autowired
    public PeopleController(UserService userService, PeopleService peopleService) {
        this.userService = userService;
        this.peopleService = peopleService;
    }

    /**
     * Displays all followable users (chefs), excluding the current user.
     */
    @GetMapping
    public ModelAndView showFollowableChefs(@RequestParam(name = "error", required = false) String error) throws SQLException {
        ModelAndView mv = new ModelAndView("chefs_page");

        try {
            String currentUserId = userService.getLoggedInUser().getUserId();
            List<FollowableUser> followableUsers = peopleService.getFollowableUsers(currentUserId);
            mv.addObject("users", followableUsers);

            if (followableUsers.isEmpty()) {
                mv.addObject("isNoContent", true);
            }
        } catch (Exception e) {
            mv.addObject("errorMessage", error != null ? error : "Unable to load users.");
        }

        return mv;
    }

    /**
     * Handles GET requests to follow or unfollow a chef.
     */
    @GetMapping("/{userId}/follow/{isFollow}")
    public String followOrUnfollowChef(@PathVariable("userId") String userId,
                                       @PathVariable("isFollow") boolean isFollow) throws SQLException {

        String currentUserId = userService.getLoggedInUser().getUserId();
        boolean success = isFollow
                ? peopleService.setFollow(currentUserId, userId)
                : peopleService.setUnfollow(currentUserId, userId);

        if (success) {
            return "redirect:/chefs";
        }

        String message = URLEncoder.encode("Failed to (un)follow the chef. Please try again.", StandardCharsets.UTF_8);
        return "redirect:/chefs?error=" + message;
    }
}

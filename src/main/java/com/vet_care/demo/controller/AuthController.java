package com.vet_care.demo.controller;

import com.vet_care.demo.model.PetUser;
import com.vet_care.demo.security.CustomUserDetails;
import com.vet_care.demo.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/vetcare")
    public String entryPoint() {
        return "redirect:/dashboard";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("owner") @Valid PetUser user,
                               BindingResult result, HttpServletRequest request) throws ServletException {

        PetUser savedUser = userService.registerAndLoginUser(user, result, request);
        if (result.hasErrors()) {
            return "register-form";
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        model.addAttribute("firstName", currentUser.getUser().getFirstName());
        return "dashboard";
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("loginError", "Invalid username or password");
        }
        return "login-form";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("owner", new PetUser());
        return "register-form";
    }
}

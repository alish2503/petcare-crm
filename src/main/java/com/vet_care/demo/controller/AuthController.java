package com.vet_care.demo.controller;

import com.vet_care.demo.model.PetOwner;
import com.vet_care.demo.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping("/vetcare")
    public String entryPoint(HttpSession session) {

        if (session.getAttribute("loggedUser") != null) {
            return "redirect:/dashboard";
        }

        return "redirect:/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("owner") @Valid PetOwner user,
                               BindingResult result,
                               Model model,
                               HttpSession session) {

        if (userRepository.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.owner", "Email already in use");
        }

        if (result.hasErrors()) {
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        prepareSessionAndModel(user, session, model);
        return "redirect:/dashboard";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String rawPassword,
                            Model model,
                            HttpSession session) {

        Optional<PetOwner> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            model.addAttribute("loginError", "Invalid email");
            return "login";
        }

        PetOwner user = optionalUser.get();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            model.addAttribute("loginError", "Invalid password");
            return "login";
        }

        prepareSessionAndModel(user, session, model);
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        PetOwner user = (PetOwner) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        return "dashboard";
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("owner", new PetOwner());
        return "register";
    }


    private void prepareSessionAndModel(PetOwner user, HttpSession session, Model model) {
        session.setAttribute("loggedUser", user);
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
    }
}

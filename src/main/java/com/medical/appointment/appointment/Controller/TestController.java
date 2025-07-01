package com.medical.appointment.appointment.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class TestController {
    @GetMapping("/home")
    public String home(Model model) {
        if(!getConnectedUsername().isEmpty()){
            model.addAttribute("email", getConnectedUsername());
        }
        return "assistance/home";
    }

    
    public String getConnectedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}

package com.example.bookingc51.controller;

import com.example.bookingc51.DTO.UserRegistrationDTO;
import com.example.bookingc51.entity.User;
import com.example.bookingc51.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping(path = "/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/authentication")
    public ModelAndView getAuthenticationView(ModelAndView modelAndView){
        log.info("Request authentication page");
        modelAndView.setViewName("authentication");
        return modelAndView;
    }

    @GetMapping("/registration")
    public ModelAndView getRegistrationView(ModelAndView modelAndView){
        log.info("Request registration page");
        modelAndView.addObject("userForm", new UserRegistrationDTO());
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping("/registration")
    public ModelAndView postRegistrationView(@Valid @ModelAttribute("userForm")UserRegistrationDTO userRegistrationDTO,
                                             BindingResult bindingResult, ModelAndView modelAndView,
                                             RedirectAttributes redirectAttributes){
        log.info("User '"+userRegistrationDTO.getUsername()+"' try to registration");
        if(!bindingResult.hasErrors()){
            log.info("No binding result errors");
            if(!userService.isUsernameExist(userRegistrationDTO.getUsername())){
                        log.info("Passwords match");
                        User user = userService.userRegistrationDTOToEntity(userRegistrationDTO);
                        userService.save(user);
                        log.info("User '"+user.getUsername()+"' has been registered");
                        redirectAttributes.addFlashAttribute("success", true);
                        modelAndView.setViewName("redirect:/user/authentication");
            }else{
                log.info("User with name '"+userRegistrationDTO.getUsername()+ "' already exists");
                modelAndView.addObject("usernameExistError", "Username '"+
                        userRegistrationDTO.getUsername()+"' is already exist!");
                modelAndView.setViewName("registration");
            }
        }else {
            modelAndView.setViewName("registration");
        }
        return modelAndView;
    }

    @GetMapping("/profile/{username}")
    public ModelAndView getProfileView(@PathVariable("username") String username, ModelAndView modelAndView,
                                       Principal principal){
        if(principal.getName().equals(username)){
            log.info(principal.getName());
            if(userService.isUsernameExist(username)) {
                User user = userService.getByUsername(username);
                modelAndView.addObject("user", user);
                modelAndView.addObject("userId", user.getId());
            }else {
                modelAndView.addObject("wrongUsername", "Input username is incorrect!");
            }
        }else {
            modelAndView.addObject("wrongUsername", "It's not your profile!");
        }
        modelAndView.setViewName("profile");
        return modelAndView;
    }
}

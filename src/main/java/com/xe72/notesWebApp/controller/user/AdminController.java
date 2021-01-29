package com.xe72.notesWebApp.controller.user;

import com.xe72.notesWebApp.dto.mapper.UserMapper;
import com.xe72.notesWebApp.dto.model.UserDto;
import com.xe72.notesWebApp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("")
    public String getAdminPage(Model model) {
        List<UserDto> users = userService.allUsers().stream().map(userMapper::toUserDto).collect(Collectors.toList());
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}

package com.xe72.notesWebApp.controller.user;

import com.xe72.notesWebApp.dto.mapper.UserMapper;
import com.xe72.notesWebApp.dto.model.UserDto;
import com.xe72.notesWebApp.entity.User;
import com.xe72.notesWebApp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new UserDto());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid UserDto userForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        // TODO: Убрать отсюда валидацию?
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
//            model.addAttribute("passwordError", "Пароли не совпадают");
            bindingResult.addError(new FieldError("userForm", "password", "Пароли не совпадают"));
            return "registration";
        }

        User user = userMapper.toUserEntity(userForm);
        if (!userService.saveUser(user)){
//            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            bindingResult.addError(new FieldError("userForm", "username", "Пользователь с таким именем уже существует"));
            return "registration";
        }

        return "redirect:/";
    }
}

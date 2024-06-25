package com.forum.accounting.service;

import com.forum.accounting.dto.RolesDto;
import com.forum.accounting.dto.UserDto;
import com.forum.accounting.dto.UserEditDto;
import com.forum.accounting.dto.UserRegisterDto;

public interface UserAccountService {
    UserDto register(UserRegisterDto userRegisterDto);
    //get + login
    UserDto getUser(String login);

    UserDto removeUser(String login);

    UserDto updateUser(String login, UserEditDto userEditDto);

    RolesDto changeRolesList(String login, String role, boolean isAddRole);

    void changePassword(String login, String newPassword);

}
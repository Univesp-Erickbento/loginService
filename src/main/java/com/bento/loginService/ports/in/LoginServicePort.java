package com.bento.loginService.ports.in;

import com.bento.loginService.domain.model.LoginEntity;

public interface LoginServicePort {
    LoginEntity login(String username, String password);

}

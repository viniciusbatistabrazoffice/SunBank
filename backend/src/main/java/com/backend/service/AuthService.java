package com.backend.service;

import com.backend.entity.Auth;

public interface AuthService {
    Auth signin(Auth auth);
    Auth signout(Auth auth);
    Auth forgot(Auth auth);
    Auth reset(Auth auth);
}

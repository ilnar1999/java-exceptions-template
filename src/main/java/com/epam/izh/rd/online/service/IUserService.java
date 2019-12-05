package com.epam.izh.rd.online.service;

import com.epam.izh.rd.online.exception.*;
import com.epam.izh.rd.online.entity.User;

public interface IUserService {

    User register(User user) throws UserAlreadyRegisteredException, SimplePasswordException;

    void delete(String login) throws NotAccessException;
}

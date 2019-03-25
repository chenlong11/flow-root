package com.nuvole.flow.controller.idm;

import com.nuvole.flow.domain.UserRepresentation;
import com.nuvole.flow.service.exception.NotFoundException;
import com.nuvole.flow.service.idm.RemoteIdmService;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/rest/flow")
public class UserResource {

    @Autowired
    protected RemoteIdmService remoteIdmService;

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET, produces = "application/json")
    public UserRepresentation getUser(@PathVariable String userId, HttpServletResponse response) {
        User user = remoteIdmService.getUser(userId);

        if (user == null) {
            throw new NotFoundException("User with id: " + userId + " does not exist or is inactive");
        }

        return new UserRepresentation(user);
    }

}

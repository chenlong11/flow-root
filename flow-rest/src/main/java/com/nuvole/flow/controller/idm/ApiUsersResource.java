package com.nuvole.flow.controller.idm;

import com.nuvole.flow.domain.GroupRepresentation;
import com.nuvole.flow.domain.UserInformation;
import com.nuvole.flow.domain.UserRepresentation;
import com.nuvole.flow.service.exception.NotFoundException;
import com.nuvole.flow.service.idm.UserService;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/flow")
public class ApiUsersResource {

    @Autowired
    protected UserService userService;

    @RequestMapping(value = "/idm/users/{userId}", method = RequestMethod.GET, produces = { "application/json" })
    public UserRepresentation getUserInformation(@PathVariable String userId) {
        UserInformation userInformation = userService.getUserInformation(userId);
        if (userInformation != null) {
            UserRepresentation userRepresentation = new UserRepresentation(userInformation.getUser());
            if (userInformation.getGroups() != null) {
                for (Group group : userInformation.getGroups()) {
                    userRepresentation.getGroups().add(new GroupRepresentation(group));
                }
            }
            if (userInformation.getPrivileges() != null) {
                for (String privilege : userInformation.getPrivileges()) {
                    userRepresentation.getPrivileges().add(privilege);
                }
            }
            return userRepresentation;
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping(value = "/idm/users", method = RequestMethod.GET, produces = { "application/json" })
    public List<UserRepresentation> findUsersByFilter(@RequestParam("filter") String filter) {
        List<User> users = userService.getUsers(filter, null, null);
        List<UserRepresentation> result = new ArrayList<UserRepresentation>();
        for (User user : users) {
            result.add(new UserRepresentation(user));
        }
        return result;
    }

}

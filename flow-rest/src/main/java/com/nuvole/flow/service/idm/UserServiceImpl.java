package com.nuvole.flow.service.idm;

import com.nuvole.flow.domain.UserInformation;
import com.nuvole.flow.service.exception.BadRequestException;
import com.nuvole.flow.service.exception.ConflictingRequestException;
import com.nuvole.flow.service.exception.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.Privilege;
import org.flowable.idm.api.User;
import org.flowable.idm.api.UserQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl extends AbstractIdmService implements UserService {

    private static final int MAX_USER_SIZE = 100;

    public List<User> getUsers(String filter, String sort, Integer start) {
        Integer startValue = start != null ? start.intValue() : 0;
        Integer size = MAX_USER_SIZE; // TODO: pass actual size
        return createUserQuery(filter, sort).listPage(startValue, (size != null && size > 0) ? size : MAX_USER_SIZE);
    }

    public long getUserCount(String filter, String sort, Integer start, String groupId) {
        return createUserQuery(filter, sort).count();
    }

    protected UserQuery createUserQuery(String filter, String sort) {
        UserQuery userQuery = identityService.createUserQuery();
        if (StringUtils.isNotEmpty(filter)) {
            userQuery.userFullNameLikeIgnoreCase("%" + filter + "%");
        }

        if (StringUtils.isNotEmpty(sort)) {
            if ("idDesc".equals(sort)) {
                userQuery.orderByUserId().desc();
            } else if ("idAsc".equals(sort)) {
                userQuery.orderByUserId().asc();
            } else if ("emailAsc".equals(sort)) {
                userQuery.orderByUserEmail().asc();
            } else if ("emailDesc".equals(sort)) {
                userQuery.orderByUserEmail().desc();
            }

        }
        return userQuery;
    }

    public void updateUserDetails(String userId, String firstName, String lastName, String email) {
        User user = identityService.createUserQuery().userId(userId).singleResult();
        if (user != null) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            identityService.saveUser(user);
        }
    }

    public void bulkUpdatePassword(List<String> userIds, String newPassword) {
        for (String userId : userIds) {
            User user = identityService.createUserQuery().userId(userId).singleResult();
            if (user != null) {
                user.setPassword(newPassword);
                identityService.updateUserPassword(user);
            }
        }
    }

    public void deleteUser(String userId) {
        List<Privilege> privileges = identityService.createPrivilegeQuery().userId(userId).list();
        for (Privilege privilege : privileges) {
            identityService.deleteUserPrivilegeMapping(privilege.getId(), userId);
        }

        List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
        if (groups != null && groups.size() > 0) {
            for (Group group : groups) {
                identityService.deleteMembership(userId, group.getId());
            }
        }
        identityService.deleteUser(userId);
    }

    public User createNewUser(String id, String firstName, String lastName, String email, String password) {
        if (StringUtils.isBlank(id) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(firstName)) {
            throw new BadRequestException("Id, password and first name are required");
        }

        if (email != null && identityService.createUserQuery().userEmail(email).count() > 0) {
            throw new ConflictingRequestException("User already registered", "ACCOUNT.SIGNUP.ERROR.ALREADY-REGISTERED");
        }

        User user = identityService.newUser(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        identityService.saveUser(user);

        User savedUser = identityService.createUserQuery().userEmail(email).singleResult();
        savedUser.setPassword(password);
        identityService.updateUserPassword(savedUser);

        return user;
    }

    @Override
    public UserInformation getUserInformation(String userId) {
        User user = identityService.createUserQuery().userId(userId).singleResult();
        if (user == null) {
            throw new NotFoundException();
        }

        List<Privilege> userPrivileges = identityService.createPrivilegeQuery().userId(userId).list();
        Set<String> privilegeNames = new HashSet<String>();
        for (Privilege userPrivilege : userPrivileges) {
            privilegeNames.add(userPrivilege.getName());
        }

        List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
        if (groups.size() > 0) {
            List<String> groupIds = new ArrayList<String>();
            for (Group group : groups) {
                groupIds.add(group.getId());
            }

            List<Privilege> groupPrivileges = identityService.createPrivilegeQuery().groupIds(groupIds).list();
            for (Privilege groupPrivilege : groupPrivileges) {
                privilegeNames.add(groupPrivilege.getName());
            }
        }

        return new UserInformation(user, groups, new ArrayList<>(privilegeNames));
    }

}

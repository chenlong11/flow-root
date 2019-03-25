package com.nuvole.flow.service.idm;

import com.nuvole.flow.domain.UserInformation;
import org.flowable.idm.api.User;

import java.util.List;

public interface UserService {

    List<User> getUsers(String filter, String sort, Integer start);

    long getUserCount(String filter, String sort, Integer start, String groupId);

    void updateUserDetails(String userId, String firstName, String lastName, String email);

    void bulkUpdatePassword(List<String> userIds, String newPassword);

    void deleteUser(String userId);

    User createNewUser(String id, String firstName, String lastName, String email, String password);

    UserInformation getUserInformation(String userId);

}

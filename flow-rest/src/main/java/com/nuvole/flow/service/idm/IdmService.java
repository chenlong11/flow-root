package com.nuvole.flow.service.idm;

import com.nuvole.flow.domain.UserRepresentation;

import java.util.List;

public interface IdmService {

    void delMembershipByGroupId(String groupId);

    List<UserRepresentation> getUsersByFilter(String filter);

}

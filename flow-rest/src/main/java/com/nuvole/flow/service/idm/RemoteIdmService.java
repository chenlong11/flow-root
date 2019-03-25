package com.nuvole.flow.service.idm;


import com.nuvole.flow.domain.RemoteGroup;
import com.nuvole.flow.domain.RemoteToken;
import com.nuvole.flow.domain.RemoteUser;

import java.util.List;


public interface RemoteIdmService {

    RemoteUser authenticateUser(String username, String password);

    RemoteToken getToken(String tokenValue);

    RemoteUser getUser(String userId);

    List<RemoteUser> findUsersByNameFilter(String filter);
    
    List<RemoteUser> findUsersByGroup(String groupId);
    
    RemoteGroup getGroup(String groupId);

    List<RemoteGroup> findGroupsByNameFilter(String filter);

}

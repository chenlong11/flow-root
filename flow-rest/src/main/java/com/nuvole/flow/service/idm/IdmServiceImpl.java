package com.nuvole.flow.service.idm;

import com.nuvole.flow.domain.UserRepresentation;
import com.nuvole.flow.mapper.IdmMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdmServiceImpl implements IdmService {

    @Autowired
    private IdmMapper idmMapper;

    @Override
    public void delMembershipByGroupId(String groupId) {
        idmMapper.delMembershipByGroupId(groupId);
    }

    @Override
    public List<UserRepresentation> getUsersByFilter(String filter) {
        return idmMapper.getUsersByFilter(filter);
    }

}

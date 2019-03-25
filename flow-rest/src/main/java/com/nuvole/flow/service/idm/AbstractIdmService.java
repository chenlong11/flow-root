
package com.nuvole.flow.service.idm;

import org.flowable.idm.api.IdmIdentityService;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractIdmService {

    @Autowired
    protected IdmIdentityService identityService;

}

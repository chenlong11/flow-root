/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nuvole.flow.service.idm;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.pagehelper.PageHelper;
import com.nuvole.flow.domain.RemoteGroup;
import com.nuvole.flow.domain.RemoteToken;
import com.nuvole.flow.domain.RemoteUser;
import com.nuvole.flow.domain.UserRepresentation;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class RemoteIdmServiceImpl implements RemoteIdmService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteIdmServiceImpl.class);

//    private static final String PROPERTY_URL = "idm.app.url";
//    private static final String PROPERTY_ADMIN_USER = "idm.admin.user";
//    private static final String PROPERTY_ADMIN_PASSWORD = "idm.admin.password";

    @Autowired
    protected IdmService idmservice;

    @Autowired
    protected Environment environment;

    @Autowired
    protected ObjectMapper objectMapper;

    @Value("${server.port}")
    protected String port;

    @Value("${server.servlet.context-path}")
    protected String contextPath;

    protected String url;
    protected String adminUser;
    protected String adminPassword;

    @PostConstruct
    protected void init() {
//        url = environment.getRequiredProperty(PROPERTY_URL);
//        adminUser = environment.getRequiredProperty(PROPERTY_ADMIN_USER);
//        adminPassword = environment.getRequiredProperty(PROPERTY_ADMIN_PASSWORD);
        url = "http://localhost:" + port + contextPath + "/rest/flow";
        adminUser = "";
        adminPassword = "";
    }

    @Override
    public RemoteUser authenticateUser(String username, String password) {
        JsonNode json = callRemoteIdmService(url + "/idm/users/" + encode(username), username, password);
        if (json != null) {
            return parseUserInfo(json);
        }
        return null;
    }

    @Override
    public RemoteToken getToken(String tokenValue) {
        JsonNode json = callRemoteIdmService(url + "/idm/tokens/" + encode(tokenValue), adminUser, adminPassword);
        if (json != null) {
            RemoteToken token = new RemoteToken();
            token.setId(json.get("id").asText());
            token.setValue(json.get("value").asText());
            token.setUserId(json.get("userId").asText());
            return token;
        }
        return null;
    }

    @Override
    public RemoteUser getUser(String userId) {
        JsonNode json = callRemoteIdmService(url + "/idm/users/" + encode(userId), adminUser, adminPassword);
        if (json != null) {
            return parseUserInfo(json);
        }
        return null;
    }

    @Override
    public List<RemoteUser> findUsersByNameFilter(String filter) {

        PageHelper.startPage(1, 100);
        List<UserRepresentation> result = new ArrayList<UserRepresentation>();
        if(StrUtil.isNotBlank(filter)){
            result = idmservice.getUsersByFilter(filter);
        }
        for (UserRepresentation user : result) {
            String lastName = user.getLastName() == null ? "" : user.getLastName();
            user.setFullName(user.getFirstName() + lastName);
        }

        //JsonNode json = callRemoteIdmService(url + "/idm/users?filter=" + encode(filter), adminUser, adminPassword);
        JsonNode json = null;
        try {
            json = objectMapper.readTree(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (json != null) {
            return parseUsersInfo(json);
        }
        return new ArrayList<>();
    }

    @Override
    public List<RemoteUser> findUsersByGroup(String groupId) {
        JsonNode json = callRemoteIdmService(url + "/idm/groups/" + encode(groupId) + "/users", adminUser, adminPassword);
        if (json != null) {
            return parseUsersInfo(json);
        }
        return new ArrayList<>();
    }

    @Override
    public RemoteGroup getGroup(String groupId) {
        JsonNode json = callRemoteIdmService(url + "/idm/groups/" + encode(groupId), adminUser, adminPassword);
        if (json != null) {
            return parseGroupInfo(json);
        }
        return null;
    }

    @Override
    public List<RemoteGroup> findGroupsByNameFilter(String filter) {
        JsonNode json = callRemoteIdmService(url + "/idm/groups?filter=" + encode(filter), adminUser, adminPassword);
        if (json != null) {
            return parseGroupsInfo(json);
        }
        return new ArrayList<>();
    }

    protected JsonNode callRemoteIdmService(String url, String username, String password) {

        HttpGet httpGet = new HttpGet(url);
//        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + new String(
//                Base64.encodeBase64((username + ":" + password).getBytes(Charset.forName("UTF-8")))));
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            sslsf = new SSLConnectionSocketFactory(builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            clientBuilder.setSSLSocketFactory(sslsf);
        } catch (Exception e) {
            LOGGER.warn("Could not configure SSL for http client", e);
        }

        CloseableHttpClient client = clientBuilder.build();

        try {
            HttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return objectMapper.readTree(response.getEntity().getContent());
            }
        } catch (Exception e) {
            LOGGER.warn("Exception while getting token", e);
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    LOGGER.warn("Exception while closing http client", e);
                }
            }
        }
        return null;
    }

    protected List<RemoteUser> parseUsersInfo(JsonNode json) {
        List<RemoteUser> result = new ArrayList<>();
        if (json != null && json.isArray()) {
            ArrayNode array = (ArrayNode) json;
            for (JsonNode userJson : array) {
                result.add(parseUserInfo(userJson));
            }
        }
        return result;
    }

    protected RemoteUser parseUserInfo(JsonNode json) {
        RemoteUser user = new RemoteUser();
        user.setId(json.get("id").asText());
        user.setFirstName(json.get("firstName").asText());
        user.setLastName(json.get("lastName").asText());
        user.setEmail(json.get("email").asText());
        user.setFullName(json.get("fullName").asText());

        if (json.has("groups")) {
            for (JsonNode groupNode : ((ArrayNode) json.get("groups"))) {
                user.getGroups().add(new RemoteGroup(groupNode.get("id").asText(), groupNode.get("name").asText()));
            }
        }

        if (json.has("privileges")) {
            for (JsonNode privilegeNode : ((ArrayNode) json.get("privileges"))) {
                user.getPrivileges().add(privilegeNode.asText());
            }
        }

        return user;
    }

    protected List<RemoteGroup> parseGroupsInfo(JsonNode json) {
        List<RemoteGroup> result = new ArrayList<>();
        if (json != null && json.isArray()) {
            ArrayNode array = (ArrayNode) json;
            for (JsonNode userJson : array) {
                result.add(parseGroupInfo(userJson));
            }
        }
        return result;
    }

    protected RemoteGroup parseGroupInfo(JsonNode json) {
        RemoteGroup group = new RemoteGroup();
        group.setId(json.get("id").asText());
        group.setName(json.get("name").asText());
        return group;
    }

    protected String encode(String s) {
        if (s == null) {
            return "";
        }

        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            LOGGER.warn("Could not encode url param", e);
            return null;
        }
    }

}

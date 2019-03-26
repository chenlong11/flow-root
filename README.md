##  集成说明
1. 加入依赖  
```  
compile 'org.flowable:flowable-spring-boot-starter:6.3.1'
compile 'org.flowable:flowable-json-converter:6.3.1'
compile 'cn.hutool:hutool-all:4.0.9'
```  

2. 在springboot 启动类中加入  
```  
@ComponentScan(basePackages = {"com.nuvole.flow","com.miracle"})
@MapperScan({"com.nuvole.flow.mapper","com.miracle.mapper"})
```  
3. yml配置文件加入
```  
flowable:
  check-process-definitions: false
  database-schema-update: false
  
mybatis:
  type-aliases-package: com.miracle.model, com.nuvole.flow.domain
```  

4. 数据库运行脚本

5. 项目中实现接口
```
AuthenticationService
```

6. maven pom文件配置
```  
<dependencies>
    <dependency>
        <groupId>com.nuvole</groupId>
        <artifactId>flow-rest</artifactId>
        <version>1.0.1</version>
        <scope>system</scope>
        <systemPath>${basedir}\src\libs\flow-rest-1.0.1-pg.jar</systemPath>
    </dependency>
    
    <dependency>
        <groupId>com.nuvole</groupId>
        <artifactId>flow-ui</artifactId>
        <version>1.0.1</version>
        <scope>system</scope>
        <systemPath>${basedir}\src\libs\flow-ui-1.0.1.jar</systemPath>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <includeSystemScope>true</includeSystemScope>
                <fork>true</fork>
            </configuration>
        </plugin>
    <plugins>
</build>		
```  
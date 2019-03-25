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
3. 数据库运行脚本

4. 项目中实现接口
```
AuthenticationService
```

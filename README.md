# 接口自动化测试

## 代码规范

1. 在命名的时候包名小写，其他的遵循驼峰法命名法

2. 每个测试类以`Test`结尾，每个测试方法以`test`开头，并在相应的测试的`XML`中对测试逻辑进行组装

3. 每个测试用例要进行代码描述，并在每个测试用例的`descrition`中增加如`-MAR12`用例标识

4. 在每个测试类上加入如下的类注释说明

   ```java
   /**
    * 企业轮播图配置<link>https://jira.souban.io/browse/MAR-17453</link>.
    *
    * @author Chengmingyuan
    * @since 1.0
    **/
   ```
   
5. 在 IDE 中安装 `Editorconfig` 和`SonarLint`插件，并尽量解决其提示的警告

6. [Google Java 编程代码规范](https://google.github.io/styleguide/javaguide.html)

7. [阿里巴巴代码规范](https://github.com/alibaba/p3c)

8. 本次测试是测试和开发同时进行，所以由产品准备测试用例，测试和开发相互review代码，使用 gitlab merge request 进行代码提交，由产品确定测试是否覆盖需要测试的功能。

9. 正常情况是：测试给出功能测试用例，开发，产品共同对测试用例进行审阅。通过后，测试人员开相关feature分支，并且在分支上进行测试编写，并提交 merge request 给开发人员review，最后由产品确认测试是否覆盖所需功能，上 rc 分支应当分支测试全部通过。 

10.新建case：需要先判断objectId是否在列表中返回，其次才是检查详情是否正确

11.删除case：删除后，判断objectId是否还在列表中返回

12.需要objectId的，请新建，不要直接获取列表数据,新建的数据，执行完case,请删除,避免遗留脏数据

13.常用api已抽出，在BaseStep内(楼宇、房源、合同、OA、租客)

14.testing-config.yml:切换环境、账号

15.测试用例写完记得在xml加一下，不然不会跑到的

16.如果接口401，大概率是token过期，清redis   redis-cli   FLUSHALL 
   
17.出现更新代码后编译报错，执行如下命令 mvn clean compile

# Api自动化测试框架使用

## 框架的组成

Java+fegin+testNg+swagger-codegen+assertJ+redis

## 需要知道的知识：

Java8 se，http,testNg测试框架的使用

## Start

1. 用idea打开项目，点击右边的maven，在lifecycle种点击compile，通过swagger-codegen来自动生成代码。最终生成的代码可以在target文件夹下来查看。

2. 创建你的测试类，命名规范请遵守驼峰命名，并且以test结尾
3. 声明你在后续测试中会用到的接口的变量，例如 `private BuildingApi buildingApi;`
4. 通过BeforeClass的注解，创建测试类的setUp method，在setUp中，你需要通过CreamsApiClient().buildClient(接口变量的名称.class)来实例化你的前面定义的接口变量
5. 通过Test注解，在测试类种编写你的case,记得加上description.description需要是（用例明-MAR-jiraId）
6. 如果在请求接口时需要requestBody，可以通过TestUtil.getJiraData方法来获取你的数据，该方法接受三个参数，Jira的id（String）,step id(int),requestBody对应的model名。最后该方法会帮你序列化数据为对应的model实例，作为返回值。
7. 如果你的requestBody还需要进行一些特殊处理，可以使用实例。set属性名的方式来设置对应的值。
8. 通过实例.对应的接口请求方法，传参来进行接口请求，具体传参列表可以查看Interface的定义
9. 使用与interface定义的对应的返回值类来接受返回（有一部分接口没有返回，可以不需要容器实例来接收）
10. 通过assertJ或者其他方式来进行数据断言（如果需要使用到AssertJ，请再maven的plugin中通过assertJ来生成断言工具类），如果需要使用到jira上的数据可以使用TestUtil.getJiraResult来获取。


## 用例的组织

用例一般编写好之后，我们需要通过xml的格式来将它们进行组织，在test文件夹下的resources的目录下，有suit-file的目录，可以根据项目或者业务来新建xml文件来进行用例的编排


* 参考例子：

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">

<suite name="workbench-suit-test">
    <listeners>
        <listener class-name="TestListener"/>
    </listeners>
    <test name="工单提醒">
        <classes>
            <class name="io.creams.testing.workbench.WorkOrderTest">
                <methods>
                    <include name="testCreateProject"/>
                    <include name="testCreateOrder"/>
                    <include name="testSearchTenantTicketList"/>
                    <include name="testSearchTicketWorkFlow"/>
                    <include name="testFinishTicket"/>
                    <include name="testSearchFinishTicketList"/>
                </methods>
            </class>
        </classes>
    </test>

    <test name="待办事项">
        <classes>
            <class name="TodoReminderTest">
                <methods>
                    <include name="testCreateDemand"/>
                    <include name="testCreateDemandNotice"/>
                    <include name="testSearchDemandNotificationList"/>
                    <include name="testSearchOaDemandList"/>
                    <include name="testSearchTodoNotification"/>
                    <include name="testCompleteNotification"/>
                    <include name="testDeleteNotification"/>
                </methods>
            </class>
        </classes>
    </test>

    <test name="账单提醒-30天内可收">
        <classes>
            <class name="BillNotificationTest">
                <methods>
                    <include name="testCreatePaymentDayAndYesterDayRentBill"/>
                    <include name="testCreatePaymentDayAndTodayRentBill"/>
                    <include name="testCreatePaymentTheDayAfterTenRentBill"/>
                    <include name="testCreatePaymentTheDayAfterTwentyNineDayRentBill"/>
                    <include name="testCreatePaymentTheDayAfterThirtyDayRentBill"/>
                    <include name="testCreatePaymentTheDayAfterThirtyOneDayRentBill"/>
                    <include name="testSearchTheRentBillInThirtyDay"/>
                    <include name="testSearchTheTypeRentBillInThirtyDay"/>
                </methods>
            </class>
        </classes>
    </test>

    <test name="账单提醒-逾期未结清客户明细">
        <classes>
            <class name="BillNotificationOutStandingTest">
                <methods>
                    <include name="testCreatePaymentDayAndYesterDayRentBill"/>
                    <include name="testCreatePaymentDayAndTodayRentBill"/>
                    <include name="testCreatePaymentTheDayAfterTenRentBill"/>
                    <include name="testCreatePaymentTheDayAfterTwentyNineDayRentBill"/>
                    <include name="testCreatePaymentTheDayAfterThirtyDayRentBill"/>
                    <include name="testCreatePaymentTheDayAfterThirtyOneDayRentBill"/>
                    <include name="testSearchOverdueClient"/>
                    <include name="testSearchOverDueClientTheSpecifiedTenant"/>
                </methods>
            </class>
        </classes>
    </test>

    <test name="账单提醒-逾期未结清">
        <classes>
            <class name="BillNotificationOverDueTest">
                <methods>
                    <include name="testCreatePaymentDayAndYesterDayRentBill"/>
                    <include name="testCreatePaymentDayAndTodayRentBill"/>
                    <include name="testCreatePaymentTheDayAfterTenRentBill"/>
                    <include name="testCreatePaymentTheDayAfterTwentyNineDayRentBill"/>
                    <include name="testCreatePaymentTheDayAfterThirtyDayRentBill"/>
                    <include name="testCreatePaymentTheDayAfterThirtyOneDayRentBill"/>
                    <include name="testSearchOverdueBillDetail"/>
                    <include name="testSearchOverDueClientTheSpecifiedType"/>
                </methods>
            </class>
        </classes>
    </test>

    <test name="账单提醒-账单跟进情况">
        <classes>
            <class name="BillNotificationFollowUp">
                <methods>
                    <include name="testCreatePaymentDayAndYesterDayRentBill"/>
                    <include name="testCreatePaymentDayAndTodayRentBill"/>
                    <include name="testCreatePaymentTheDayAfterTenRentBill"/>
                    <include name="testCreatePaymentTheDayAfterTwentyNineDayRentBill"/>
                    <include name="testCreatePaymentTheDayAfterThirtyDayRentBill"/>
                    <include name="testCreatePaymentTheDayAfterThirtyOneDayRentBill"/>
                    <include name="testSearchFollowUpInThirtyDayOverdueBill"/>
                    <include name="testSearchBillDetailOfTheFollowInThirtyDay"/>
                </methods>
            </class>
        </classes>
    </test>

    <test name="查看合同审核卡片、审核列表">
        <classes>
            <class name="ContractApprovalListTest">
                <methods>
                    <include name="setUp" description="初始化-MAR-18128"/>
                    <include name="testCreateContract" description="创建合同-MAR-17983"/>
                    <include name="testStartApproval" description="发起审批-MAR-17983"/>
                    <include name="testApprovals" description="审批列表-MAR-17983"/>
                    <include name="testStartByMe" description="我发起的审批列表-MAR-17983"/>
                    <include name="testTodo" description="待我审批列表-MAR-17983"/>
                    <include name="testInstancesContract" description="审批流程(审核合同)-MAR-17983"/>
                    <include name="testDone" description="我已审批列表-MAR-17983"/>
                    <include name="testCreateTermination" description="创建退租协议-MAR-17983"/>
                    <include name="testStartTermination" description="发起审批-MAR-17983"/>
                    <include name="testApprovals2" description="审批列表-MAR-17983"/>
                    <include name="testStartByMe2" description="我发起的审批列表-MAR-17983"/>
                    <include name="testTodo2" description="待我审批列表-MAR-17983"/>
                    <include name="testInstancesContract2" description="审批流程(退租协议)-MAR-17983"/>
                    <include name="testDone2" description="我已审批列表-MAR-17983"/>
                    <include name="testDeletedTenant" description="删除租客-MAR-17983"/>
                    <include name="testDeletedBuilding" description="删除楼宇-MAR-17983"/>
                </methods>
            </class>
        </classes>
    </test>

    <test name="作废合同审批列表">
        <classes>
            <class name="TerminationNullificationApprovalListTest">
                <methods>
                    <include name="setUp" description="初始化-MAR-18128"/>
                    <include name="testCreateContract" description="创建合同-MAR-18037"/>
                    <include name="testStartApproval" description="发起审批-MAR-18037"/>
                    <include name="testInstancesContract" description="审批流程(审核合同)-MAR-18037"/>
                    <include name="testCreateNullification" description="创建作废协议-MAR-18037"/>
                    <include name="testStartTermination" description="发起审批-MAR-18037"/>
                    <include name="testApprovals2" description="审核列表-MAR-18037"/>
                    <include name="testStartByMe" description="我发起的审核列表-MAR-18037"/>
                    <include name="testTodo" description="待我审核的列表-MAR-18037"/>
                    <include name="testInstancesContract2" description="审批流程(作废协议)-MAR-18037"/>
                    <include name="testDone" description="我已审批列表-MAR-18037"/>
                    <include name="testDeletedTenant" description="删除租客-MAR-18037"/>
                    <include name="testDeletedBuilding" description="删除楼宇-MAR-18037"/>
                </methods>
            </class>
        </classes>
    </test>

    <test name="发票作废审批卡片数据校对">
        <classes>
            <class name="InvoiceNullificationApprovalListTest">
                <methods>
                    <include name="setUp" description="初始化-MAR-18128"/>
                    <include name="testCreateInvoicesSales" description="创建发票销方单位-MAR-18051"/>
                    <include name="testUpdateBuilding" description="修改楼盘-MAR-18051"/>
                    <include name="testCreateBill" description="创建账单-MAR-18051"/>
                    <include name="testCreateCash" description="创建流水-MAR-18051"/>
                    <include name="testBatchCreateInvoices" description="批量创建发票-MAR-18051"/>
                    <include name="testBatchUpdateInvoices" description="编辑发票-MAR-18051"/>
                    <include name="testStart" description="发起审批(对发票发起作废)-MAR-18051"/>
                    <include name="testApprovals" description="审批列表-发票的审批列表-MAR-18051"/>
                    <include name="testStartedByMe" description="我发起的-MAR-18051"/>
                    <include name="testTodo" description="待我审批-MAR-18051"/>
                    <include name="testInstances" description="审批流程-MAR-18051"/>
                    <include name="testDone" description="我已审批-MAR-18051"/>
                    <include name="testDeletedInvoiceSales" description="删除发票销方单位-MAR-18051"/>
                    <include name="testDeletedTenant" description="删除租客-MAR-18051"/>
                    <include name="testDeletedBuilding" description="删除楼盘-MAR-18051"/>
                </methods>
            </class>
        </classes>
    </test>

    <test name="收据作废审批卡片数据校对">
        <classes>
            <class name="ReceiptNullifcationApprovalListTest">
                <methods>
                    <include name="setUp" description="初始化-MAR-18128"/>
                    <include name="testCreateBill" description="新建账单-MAR-18044"/>
                    <include name="testCreateCash" description="创建流水-MAR-18044"/>
                    <include name="testCreateReceiptTemplate" description="创建收据模板-MAR-18044"/>
                    <include name="testReceiptTemplate" description="收据模板列表-MAR-18044"/>
                    <include name="testCreateReceipt" description="创建收据-MAR-18044"/>
                    <include name="testStart" description="发起审批-MAR-18044"/>
                    <include name="testApprovals" description="审核列表-MAR-18044"/>
                    <include name="testStartByMe" description="我发起的-MAR-18044"/>
                    <include name="testTodo" description="待我审批-MAR-18044"/>
                    <include name="testInstances" description="审批流程-MAR-18044"/>
                    <include name="testDone" description="我已审批-MAR-18044"/>
                    <include name="testDeletedReceiptTemplate" description="删除收据模板-MAR-18044"/>
                    <include name="testDeletedTenant" description="删除租客-MAR-18044"/>
                    <include name="testDeletedBuilding" description="删除楼盘-MAR-18044"/>
                </methods>
            </class>
        </classes>
    </test>

    <test name="工作台-合同跟进分布">
        <classes>
            <class name="ContractFollowListTest">
                <methods>
                    <include name="setUp" description="初始化-MAR-18128"/>
                    <include name="testCirculationCreateContract" description="循环创建合同并执行检查操作-MAR-18128"/>
                    <include name="testContractsFollow" description="查询合同跟进分布统计表-MAR-18128"/>
                    <include name="testContractList" description="合同跟进分布的侧滑栏-MAR-18128"/>
                    <include name="testDeleteTenant" description="删除租客-MAR-18128"/>
                    <include name="testDeleteBuilding" description="删除楼盘-MAR-18128"/>
                </methods>
            </class>
        </classes>
    </test>

    <test name="工作台-租赁合同到期情况">
        <classes>
            <class name="ContractsExpiredTest">
                <methods>
                    <include name="setUp" description="初始化-MAR-18128"/>
                    <include name="testCirculationCreateContract" description="循环创建合同并执行检查操作-MAR-18131"/>
                    <include name="testContractsExpired" description="租赁合同到期情况-MAR-18131"/>
                    <include name="testContractsExpire" description="到期未处理合同列表-MAR-18131"/>
                    <include name="testContractsExpired30Days" description="30天内到期合同列表-MAR-18131"/>
                    <include name="testContractsExpired30DaysAnd90Days" description="30天-90天内到期合同列表-MAR-18131"/>
                    <include name="testDeleteTenant" description="删除租客-MAR-18131"/>
                    <include name="testDeleteBuilding" description="删除楼盘-MAR-18131"/>
                </methods>
            </class>
        </classes>
    </test>
</suite>

```


## 用例的运行

如果需要看下用例的运行情况，在idea中，可以右键xml文件，选择run，本地运行需要有redis服务，可以使用homebrew来进行安装。如果存在缓存数据影响到测试用例的运行，可以通过redis-cli来清除掉所有缓存。

如果最后本地运行无误，可以通过git将测试代码和xml文件进行push





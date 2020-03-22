package io.idwangmo.testing.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 这个类中写了一些基础的方法
 *
 * @author Silence
 * @Date 2018-12-18 17:04
 * @since 1.0
 */

public class BasicRequest {
    /**
     * 该方法根据objectType返回一个Integer类型的ObjectId
     *
     * @param objectType 对象类型
     * @return 返回一个ObjectId
     */
    public static Integer getObjectId(String objectType) {
        Map<String, Integer> map = new HashMap<>();

        //租赁合同
        map.put("CONTRACT", 74873);

        //房源需求(客户)
        map.put("DEMAND", 16965);

        //成本合同
        map.put("COST_CONTRACT", 67);

        //水电表
        map.put("WATER_ELECTRICITY_METER", 1319);

        //房源
        map.put("ROOM", 211946);

        //账单
        map.put("BILL", 687455);

        //现金流水
        map.put("CASH", 125405);

        //发票
        map.put("INVOICE", 380);

        //渠道联系人
        map.put("CHANNEL_CONTACT", 71748);

        //租客
        map.put("TENANT", 65658);

        //租客需求
        //map.put("TENANT_DEMAND", null);

        //收据
        map.put("RECEIPT", 3014);

        //租客端
        map.put("TENANT_TICKET", 4514);

        return map.get(objectType);
    }

}

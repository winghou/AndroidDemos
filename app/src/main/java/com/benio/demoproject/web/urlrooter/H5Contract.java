package com.benio.demoproject.web.urlrooter;

/**
 * h5到native的伪协议
 * Created by zhangzhibin on 2016/12/1.
 */
public interface H5Contract {
    String SCHEMA = "magapp";

    /**
     * 跳转组织销售目标
     * magapp://salestarget/viewOrgSalesTarget?orgId=&orgName=&orgType=&date=
     */
    interface SalesTarget extends H5Contract {
        String HOST = "salestarget";
        String PATH = "/viewOrgSalesTarget";
        String KEY_ORG_ID = "orgId";
        String KEY_ORG_NAME = "orgName";
        String KEY_ORG_TYPE = "orgType";
        String KEY_DATE = "date";
    }
}

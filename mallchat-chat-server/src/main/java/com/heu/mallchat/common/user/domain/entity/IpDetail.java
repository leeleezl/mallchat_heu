package com.heu.mallchat.common.user.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class IpDetail implements Serializable {
    private String ip;
    private String isp;
    private String isp_id;
    private String city;
    private String city_id;
    private String country;
    private String country_id;
    private String region;
    private String region_id;


    /**
     * "area": "",
     * 		"country": "中国",
     * 		"isp_id": "100026",
     * 		"queryIp": "112.96.166.230",
     * 		"city": "广州",
     * 		"ip": "112.96.166.230",
     * 		"isp": "联通",
     * 		"county": "",
     * 		"region_id": "440000",
     * 		"area_id": "",
     * 		"county_id": null,
     * 		"region": "广东",
     * 		"country_id": "CN",
     * 		"city_id": "440100"
     */

}

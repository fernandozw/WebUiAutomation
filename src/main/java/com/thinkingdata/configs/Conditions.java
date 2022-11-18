package com.thinkingdata.configs;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2022/10/14 18:16
 */

@Component
@ConfigurationProperties(prefix = "condition")
public class Conditions {
    private List<Map<String, String>> locationList;

    public List<Map<String, String>> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Map<String, String>> locationList) {
        this.locationList = locationList;
    }

    private List<Map<String, String>> actionList;

    public List<Map<String, String>> getActionList() {
        return actionList;
    }

    public void setActionList(List<Map<String, String>> actionList) {
        this.actionList = actionList;
    }

    private Map<String, String> statusMap;

    public Map<String, String> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(Map<String, String> statusMap) {
        this.statusMap = statusMap;
    }

    private List<Map<String, Object>> statusList;

    public List<Map<String, Object>> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Map<String, Object>> statusList) {
        this.statusList = statusList;
    }


    private Map<String, Object> actionParamMap;

    public Map<String, Object> getActionParamMap() {
        return actionParamMap;
    }

    public void setActionParamMap(Map<String, Object> actionParamMap) {
        this.actionParamMap = actionParamMap;
    }


    private Map<String, Object> verifyMap;

    public Map<String, Object> getVerifyMap() {
        return verifyMap;
    }

    public void setVerifyMap(Map<String, Object> verifyMap) {
        this.verifyMap = verifyMap;
    }

    private List<Map<String, Object>> verifyList;

    public List<Map<String, Object>> getVerifyList() {
        return verifyList;
    }

    public void setVerifyList(List<Map<String, Object>> verifyList) {
        this.verifyList = verifyList;
    }

    private List<Map<String, Object>> pageTypeList;

    public List<Map<String, Object>> getPageTypeList() {
        return pageTypeList;
    }

    public void setPageTypeList(List<Map<String, Object>> pageTypeList) {
        this.pageTypeList = pageTypeList;
    }


    private List<Map<String, Object>> phoneTypeList;

    public List<Map<String, Object>> getPhoneTypeList() {
        return phoneTypeList;
    }

    public void setPhoneTypeList(List<Map<String, Object>> phoneTypeList) {
        this.phoneTypeList = phoneTypeList;
    }


    private List<Map<String, Object>> browserTypeList;

    public List<Map<String, Object>> getBrowserTypeList() {
        return browserTypeList;
    }

    public void setBrowserTypeList(List<Map<String, Object>> browserTypeList) {
        this.browserTypeList = browserTypeList;
    }

    private List<Map<String, Object>> canUseList;

    public List<Map<String, Object>> getCanUseList() {
        return canUseList;
    }

    public void setCanUseList(List<Map<String, Object>> canUseList) {
        this.canUseList = canUseList;
    }

    private Map<String, Object> canUseMap;

    public Map<String, Object> getCanUseMap() {
        return canUseMap;
    }

    public void setCanUseMap(Map<String, Object> canUseMap) {
        this.canUseMap = canUseMap;
    }

    private List<Map<String, Object>> caseStatusList;

    public List<Map<String, Object>> getCaseStatusList() {
        return caseStatusList;
    }

    public void setCaseStatusList(List<Map<String, Object>> caseStatusList) {
        this.caseStatusList = caseStatusList;
    }

    private Map<String, Object> caseStatusMap;

    public Map<String, Object> getCaseStatusMap() {
        return caseStatusMap;
    }

    public void setCaseStatusMap(Map<String, Object> caseStatusMap) {
        this.caseStatusMap = caseStatusMap;
    }

    private List<Map<String, Object>> pageVerifyList;

    public List<Map<String, Object>> getPageVerifyList() {
        return pageVerifyList;
    }

    public void setPageVerifyList(List<Map<String, Object>> pageVerifyList) {
        this.pageVerifyList = pageVerifyList;
    }

    private List<String> needValueVerifyList;

    public List<String> getNeedValueVerifyList() {
        return needValueVerifyList;
    }

    public void setNeedValueVerifyList(List<String> needValueVerifyList) {
        this.needValueVerifyList = needValueVerifyList;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("conditions.yml"));
        configurer.setProperties(yaml.getObject());
        return configurer;
    }
}

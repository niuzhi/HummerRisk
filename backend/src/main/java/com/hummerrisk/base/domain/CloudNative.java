package com.hummerrisk.base.domain;

import java.io.Serializable;

public class CloudNative implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native.id
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native.name
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native.plugin_id
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    private String pluginId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native.plugin_name
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    private String pluginName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native.plugin_icon
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    private String pluginIcon;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native.status
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    private String status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native.create_time
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    private Long createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native.update_time
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    private Long updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native.creator
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    private String creator;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native.proxy_id
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    private Integer proxyId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native.operator_status
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    private String operatorStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native.credential
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    private String credential;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cloud_native
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native.id
     *
     * @return the value of cloud_native.id
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native.id
     *
     * @param id the value for cloud_native.id
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native.name
     *
     * @return the value of cloud_native.name
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native.name
     *
     * @param name the value for cloud_native.name
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native.plugin_id
     *
     * @return the value of cloud_native.plugin_id
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public String getPluginId() {
        return pluginId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native.plugin_id
     *
     * @param pluginId the value for cloud_native.plugin_id
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public void setPluginId(String pluginId) {
        this.pluginId = pluginId == null ? null : pluginId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native.plugin_name
     *
     * @return the value of cloud_native.plugin_name
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public String getPluginName() {
        return pluginName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native.plugin_name
     *
     * @param pluginName the value for cloud_native.plugin_name
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public void setPluginName(String pluginName) {
        this.pluginName = pluginName == null ? null : pluginName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native.plugin_icon
     *
     * @return the value of cloud_native.plugin_icon
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public String getPluginIcon() {
        return pluginIcon;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native.plugin_icon
     *
     * @param pluginIcon the value for cloud_native.plugin_icon
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public void setPluginIcon(String pluginIcon) {
        this.pluginIcon = pluginIcon == null ? null : pluginIcon.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native.status
     *
     * @return the value of cloud_native.status
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native.status
     *
     * @param status the value for cloud_native.status
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native.create_time
     *
     * @return the value of cloud_native.create_time
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native.create_time
     *
     * @param createTime the value for cloud_native.create_time
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native.update_time
     *
     * @return the value of cloud_native.update_time
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native.update_time
     *
     * @param updateTime the value for cloud_native.update_time
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native.creator
     *
     * @return the value of cloud_native.creator
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public String getCreator() {
        return creator;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native.creator
     *
     * @param creator the value for cloud_native.creator
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native.proxy_id
     *
     * @return the value of cloud_native.proxy_id
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public Integer getProxyId() {
        return proxyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native.proxy_id
     *
     * @param proxyId the value for cloud_native.proxy_id
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public void setProxyId(Integer proxyId) {
        this.proxyId = proxyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native.operator_status
     *
     * @return the value of cloud_native.operator_status
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public String getOperatorStatus() {
        return operatorStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native.operator_status
     *
     * @param operatorStatus the value for cloud_native.operator_status
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public void setOperatorStatus(String operatorStatus) {
        this.operatorStatus = operatorStatus == null ? null : operatorStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native.credential
     *
     * @return the value of cloud_native.credential
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public String getCredential() {
        return credential;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native.credential
     *
     * @param credential the value for cloud_native.credential
     *
     * @mbg.generated Sat Aug 27 19:56:06 CST 2022
     */
    public void setCredential(String credential) {
        this.credential = credential == null ? null : credential.trim();
    }
}
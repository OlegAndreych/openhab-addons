package org.openhab.binding.qingping.internal.client.http.dto.device.list.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceInfo {
    private final String mac;
    private final DeviceProductInfo product;
    private final String name;
    private final String version;
    private final long createdAt;
    private final int groupID;
    private final String groupName;
    private final DeviceStatus status;
    private final DeviceSetting setting;

    @JsonCreator
    public DeviceInfo(@JsonProperty("mac") String mac, @JsonProperty("product") DeviceProductInfo product,
            @JsonProperty("name") String name, @JsonProperty("version") String version,
            @JsonProperty("created_at") long createdAt, @JsonProperty("group_id") int groupID,
            @JsonProperty("group_name") String groupName, @JsonProperty("status") DeviceStatus status,
            @JsonProperty("setting") DeviceSetting setting) {
        this.mac = mac;
        this.product = product;
        this.name = name;
        this.version = version;
        this.createdAt = createdAt;
        this.groupID = groupID;
        this.groupName = groupName;
        this.status = status;
        this.setting = setting;
    }

    public String getMac() {
        return mac;
    }

    public DeviceProductInfo getProduct() {
        return product;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public int getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public DeviceSetting getSetting() {
        return setting;
    }
}

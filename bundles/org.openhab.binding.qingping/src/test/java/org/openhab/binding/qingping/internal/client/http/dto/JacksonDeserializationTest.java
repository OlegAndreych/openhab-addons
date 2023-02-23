package org.openhab.binding.qingping.internal.client.http.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.Device;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.DeviceListResponse;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.data.DeviceData;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.info.DeviceInfo;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.info.DeviceProductInfo;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.info.DeviceSetting;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.info.DeviceStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonDeserializationTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void shouldDeserializeDeviceListResponse() throws IOException {

        final DeviceListResponse deviceListResponse;
        try (InputStream responseStream = getClass().getClassLoader()
                .getResourceAsStream("device_list_response.json")) {
            deviceListResponse = OBJECT_MAPPER.readValue(responseStream, DeviceListResponse.class);
        }

        assertThat(deviceListResponse, notNullValue());
        assertThat(deviceListResponse.total(), equalTo(2));

        final List<Device> devices = deviceListResponse.devices();
        assertThat(devices, hasSize(2));

        assertDevice(devices.get(0), "582D3400C123", "Living-room Air Monitor", 1663522182L, 30, 60, 1671357161L,
                "24.799999237060547", "32.400001525878906", 363, 673, 7, 4);
        assertDevice(devices.get(1), "582D3400E321", "Bedroom Air Monitor", 1664822996L, 0, 0, 1671356700L,
                "24.899999618530273", "35.5", 385, 697, 0, 0);
    }

    private static void assertDevice(Device device, String mac, String deviceName, long createdAt, int collectInterval,
            int reportInterval, long timestamp, String temperature, String humidity, int tvoc, int co2, int pm25,
            int pm10) {

        final DeviceInfo info = device.getInfo();
        assertDeviceInfo(info, mac, deviceName, createdAt, collectInterval, reportInterval);
        final DeviceData data = device.getData();
        assertDeviceData(data, timestamp, temperature, humidity, tvoc, co2, pm25, pm10);
    }

    private static void assertDeviceData(DeviceData data, long timestamp, String temperature, String humidity, int tvoc,
            int co2, int pm25, int pm10) {

        assertThat(data, notNullValue());
        assertThat(data.getTimestamp().getValue(), equalTo(timestamp));
        assertThat(data.getBattery().getValue(), equalTo((byte) 100));
        assertThat(data.getTemperature().getValue(), equalTo(new BigDecimal(temperature)));
        assertThat(data.getHumidity().getValue(), equalTo(new BigDecimal(humidity)));
        assertThat(data.getTvoc().getValue(), equalTo(tvoc));
        assertThat(data.getCo2().getValue(), equalTo(co2));
        assertThat(data.getPm25().getValue(), equalTo(pm25));
        assertThat(data.getPm10().getValue(), equalTo(pm10));
    }

    private static void assertDeviceInfo(DeviceInfo info, String mac, String deviceName, long createdAt,
            int collectInterval, int reportInterval) {

        assertThat(info.getMac(), equalTo(mac));
        final DeviceProductInfo product = info.getProduct();
        assertProduct(product);
        assertThat(info.getName(), equalTo(deviceName));
        assertThat(info.getVersion(), equalTo("4.1.8_0256"));
        assertThat(info.getCreatedAt(), equalTo(createdAt));
        assertThat(info.getGroupID(), equalTo(0));
        assertThat(info.getGroupName(), equalTo(""));
        final DeviceStatus status = info.getStatus();
        assertDeviceStatus(status);
        final DeviceSetting setting = info.getSetting();
        assertDeviceSetting(setting, collectInterval, reportInterval);
    }

    private static void assertDeviceSetting(DeviceSetting setting, int collectInterval, int reportInterval) {
        assertThat(setting, notNullValue());
        assertThat(setting.getCollectInterval(), equalTo(collectInterval));
        assertThat(setting.getReportInterval(), equalTo(reportInterval));
    }

    private static void assertDeviceStatus(DeviceStatus status) {
        assertThat(status, notNullValue());
        assertThat(status.isOffline(), equalTo(false));
    }

    private static void assertProduct(DeviceProductInfo product) {
        assertThat(product, notNullValue());
        assertThat(product.getId(), equalTo("1201"));
        assertThat(product.getCode(), equalTo("CGS1"));
        assertThat(product.getName(), equalTo("青萍空气检测仪"));
        assertThat(product.getEnName(), equalTo("Qingping Air Monitor"));
        assertThat(product.isNoBleSetting(), equalTo(false));
    }
}

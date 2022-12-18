package org.openhab.binding.qingping.internal.client.http.dto.device.list.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceData {
    private final Timestamp timestamp;
    private final Battery battery;
    private final Temperature temperature;
    private final Humidity humidity;
    private final TVOC tvoc;
    private final CO2 co2;
    private final PM25 pm25;
    private final PM10 pm10;

    @JsonCreator
    public DeviceData(@JsonProperty("timestamp") Timestamp timestamp, @JsonProperty("battery") Battery battery,
            @JsonProperty("temperature") Temperature temperature, @JsonProperty("humidity") Humidity humidity,
            @JsonProperty("tvoc") TVOC tvoc, @JsonProperty("co2") CO2 co2, @JsonProperty("pm25") PM25 pm25,
            @JsonProperty("pm10") PM10 pm10) {
        this.timestamp = timestamp;
        this.battery = battery;
        this.temperature = temperature;
        this.humidity = humidity;
        this.tvoc = tvoc;
        this.co2 = co2;
        this.pm25 = pm25;
        this.pm10 = pm10;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Battery getBattery() {
        return battery;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public Humidity getHumidity() {
        return humidity;
    }

    public TVOC getTvoc() {
        return tvoc;
    }

    public CO2 getCo2() {
        return co2;
    }

    public PM25 getPm25() {
        return pm25;
    }

    public PM10 getPm10() {
        return pm10;
    }
}

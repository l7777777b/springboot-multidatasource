package com.laurensius.springbootmultids.web.rest;

import com.laurensius.springbootmultids.domain.MqttRawPackets;
import com.laurensius.springbootmultids.domain.MqttSensorAlerts;
import com.laurensius.springbootmultids.domain.MqttSensorMeasurements;
import com.laurensius.springbootmultids.repository.db1.MqttRawPacketsDb1Repo;
import com.laurensius.springbootmultids.repository.db1.MqttSensorAlertsDb1Repo;
import com.laurensius.springbootmultids.repository.db1.MqttSensorMeasurementsDb1Repo;
import com.laurensius.springbootmultids.repository.db2.MqttRawPacketsDb2Repo;
import com.laurensius.springbootmultids.repository.db2.MqttSensorAlertsDb2Repo;
import com.laurensius.springbootmultids.repository.db2.MqttSensorMeasurementsDb2Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private MqttRawPacketsDb1Repo mqttRawPacketsDb1Repo;

    @Autowired
    private MqttRawPacketsDb2Repo mqttRawPacketsDb2Repo;

    @Autowired
    private MqttSensorAlertsDb1Repo mqttSensorAlertsDb1Repo;

    @Autowired
    private MqttSensorAlertsDb2Repo mqttSensorAlertsDb2Repo;

    @Autowired
    private MqttSensorMeasurementsDb1Repo mqttSensorMeasurementsDb1Repo;

    @Autowired
    private MqttSensorMeasurementsDb2Repo mqttSensorMeasurementsDb2Repo;

    @GetMapping("/mqttrawpackets/db1/{id}")
    public ResponseEntity<MqttRawPackets> getMqttRawPacketsDb1(@PathVariable long id) {
        return ResponseEntity.ok().body(mqttRawPacketsDb1Repo.findOne(id));
    }
    @GetMapping("/mqttrawpackets/db2/{id}")
    public MqttRawPackets getMqttRawPacketsDb2(@PathVariable long id) {
        return mqttRawPacketsDb2Repo.findOne(id);
    }

    @GetMapping("/mqttsensoralerts/db1/{id}")
    public ResponseEntity<MqttSensorAlerts> getMqttSensorAlertsDb1(@PathVariable long id) {
        return ResponseEntity.ok().body(mqttSensorAlertsDb1Repo.findOne(id));
    }
    @GetMapping("/mqttsensoralerts/db2/{id}")
    public MqttSensorAlerts getMqttSensorAlertsDb2(@PathVariable long id) {
        return mqttSensorAlertsDb2Repo.findOne(id);
    }

    @GetMapping("/mqttsensormeasurements/db1/{id}")
    public ResponseEntity<MqttSensorMeasurements> getMqttSensorMeasurementsDb1(@PathVariable long id) {
        return ResponseEntity.ok().body(mqttSensorMeasurementsDb1Repo.findOne(id));
    }
    @GetMapping("/mqttsensormeasurements/db2/{id}")
    public MqttSensorMeasurements getMqttSensorMeasurementsDb2(@PathVariable long id) {
        return mqttSensorMeasurementsDb2Repo.findOne(id);
    }

}

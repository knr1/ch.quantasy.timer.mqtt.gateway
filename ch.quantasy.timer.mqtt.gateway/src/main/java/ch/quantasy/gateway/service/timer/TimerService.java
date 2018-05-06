/*
 *   "TimerMqWay"
 *
 *    TimerMqWay(tm): A gateway to provide a universal timer ability.
 *
 *    Copyright (c) 2016 Bern University of Applied Sciences (BFH),
 *    Research Institute for Security in the Information Society (RISIS), Wireless Communications & Secure Internet of Things (WiCom & SIoT),
 *    Quellgasse 21, CH-2501 Biel, Switzerland
 *
 *    Licensed under Dual License consisting of:
 *    1. GNU Affero General Public License (AGPL) v3
 *    and
 *    2. Commercial license
 *
 *
 *    1. This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *    2. Licensees holding valid commercial licenses for TiMqWay may use this file in
 *     accordance with the commercial license agreement provided with the
 *     Software or, alternatively, in accordance with the terms contained in
 *     a written agreement between you and Bern University of Applied Sciences (BFH),
 *     Research Institute for Security in the Information Society (RISIS), Wireless Communications & Secure Internet of Things (WiCom & SIoT),
 *     Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 *     For further information contact <e-mail: reto.koenig@bfh.ch>
 *
 *
 */
package ch.quantasy.gateway.service.timer;

import ch.quantasy.gateway.binding.EpochDeltaEvent;
import ch.quantasy.gateway.binding.TimerConfigurationStatus;
import ch.quantasy.gateway.binding.TimerIntent;
import ch.quantasy.gateway.binding.TimerServiceContract;
import ch.quantasy.gateway.binding.UnixEpochStatus;
import ch.quantasy.mqtt.gateway.client.GatewayClient;
import ch.quantasy.timer.DeviceTickerCancel;
import ch.quantasy.timer.DeviceTickerConfiguration;
import ch.quantasy.timer.TimerDevice;
import ch.quantasy.timer.TimerDeviceCallback;
import java.net.URI;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author reto
 */
public class TimerService extends GatewayClient<TimerServiceContract> implements TimerDeviceCallback {

    private final TimerDevice device;

    public TimerService(URI mqttURI, String instanceName) throws MqttException {
        super(mqttURI, "TimerService:" + instanceName, new TimerServiceContract(instanceName));

        configurations = new TreeSet<>();
        device = new TimerDevice(this);
        subscribe(getContract().INTENT + "/#", (topic, payload) -> {
            try {
                Set<TimerIntent> timerIntents = toMessageSet(payload, TimerIntent.class);
                for (TimerIntent timerIntent : timerIntents) {
                    if (!timerIntent.isValid()) {
                        continue;
                    }
                    if (timerIntent.cancel != null) {
                        device.cancel(new DeviceTickerCancel(timerIntent.id));
                    } else {
                        device.setTickerConfiguration(new DeviceTickerConfiguration(timerIntent.id, timerIntent.epoch, timerIntent.first, timerIntent.interval, timerIntent.last));
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(TimerService.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        super.connect();
        device.setTickerConfiguration(new DeviceTickerConfiguration(super.getIntent().clientID, null, null, 1000, null));
    }

    private SortedSet<DeviceTickerConfiguration> configurations;

    @Override
    public void tickerConfigurationUpdated(DeviceTickerConfiguration configuration) {
        if (configuration == null) {
            return;
        }
        if (configuration.getId().equals(super.getIntent().clientID)) {
            return;
        }
        configurations.add(configuration);
        super.getPublishingCollector().readyToPublish(getContract().STATUS_CONFIGURATION + "/" + configuration.getId(), new TimerConfigurationStatus(configuration));
    }

    @Override
    public void onTick(String id, Long epochDelta) {
        if (id.equals(super.getIntent().clientID)) {
            super.getPublishingCollector().readyToPublish(getContract().STATUS_UNIX_EPOCH, new UnixEpochStatus());
        } else {
            super.getPublishingCollector().readyToPublish(getContract().EVENT_TICK + "/" + id, new EpochDeltaEvent(epochDelta));
        }
    }

    @Override
    public void tickerConfigurationRemoved(DeviceTickerConfiguration configuration) {
        if (configuration == null) {
            return;
        }
        configurations.remove(configuration);
        super.getPublishingCollector().clearPublish(getContract().STATUS_CONFIGURATION + "/" + configuration.getId());
    }

}

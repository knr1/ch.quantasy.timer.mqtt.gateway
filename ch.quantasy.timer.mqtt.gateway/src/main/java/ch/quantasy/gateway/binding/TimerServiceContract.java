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
package ch.quantasy.gateway.binding;

import ch.quantasy.gateway.service.timer.*;
import ch.quantasy.gateway.binding.EpochDeltaEvent;
import ch.quantasy.gateway.binding.TimerConfigurationStatus;
import ch.quantasy.gateway.binding.TimerIntent;
import ch.quantasy.gateway.binding.UnixEpochStatus;
import ch.quantasy.mqtt.gateway.client.contract.AyamlServiceContract;
import ch.quantasy.mqtt.gateway.client.message.Message;
import java.util.Map;

/**
 *
 * @author reto
 */
public class TimerServiceContract extends AyamlServiceContract {

    private final String CONFIGURATION;
    public final String STATUS_CONFIGURATION;
    private final String TICK;
    public final String EVENT_TICK;
    private final String UNIX_EPOCH;
    public final String STATUS_UNIX_EPOCH;

    public TimerServiceContract(String instanceID) {
        super("Timer", "Tick", instanceID);
        CONFIGURATION = "configuration";
        STATUS_CONFIGURATION = STATUS + "/" + CONFIGURATION;
        TICK = "tick";
        EVENT_TICK = EVENT + "/" + TICK;
        UNIX_EPOCH = "unixEpoch";
        STATUS_UNIX_EPOCH = STATUS + "/" + UNIX_EPOCH;
    }

    @Override
    public void setMessageTopics(Map<String, Class<? extends Message>> messageTopicMap) {

        messageTopicMap.put(INTENT, TimerIntent.class);
        messageTopicMap.put(STATUS_CONFIGURATION + "/<id>", TimerConfigurationStatus.class);
        messageTopicMap.put(EVENT_TICK + "/<id>", EpochDeltaEvent.class);
        messageTopicMap.put(STATUS_UNIX_EPOCH, UnixEpochStatus.class);

    }
}

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

import ch.quantasy.mqtt.gateway.client.message.AnIntent;
import ch.quantasy.mqtt.gateway.client.message.annotations.NonNull;
import ch.quantasy.mqtt.gateway.client.message.annotations.Nullable;
import ch.quantasy.mqtt.gateway.client.message.annotations.Period;
import ch.quantasy.mqtt.gateway.client.message.annotations.StringForm;

/**
 *
 * @author reto
 */
public class TimerIntent extends AnIntent {

    @NonNull
    @StringForm
    public String id;
    @Period
    @Nullable
    public Long epoch;
    @Period(to = Integer.MAX_VALUE)
    @Nullable
    public Integer first;
    @Period(to = Integer.MAX_VALUE)
    @Nullable
    public Integer interval;
    @Period(to = Integer.MAX_VALUE)
    @Nullable
    public Integer last;
    @Nullable
    public Boolean cancel;

    public TimerIntent(String id) {
        this.id = id;
    }

    private TimerIntent() {
    }
    
    public TimerIntent(String id,Boolean cancel){
        this(id, null, null, null, null, cancel);
    }

    public TimerIntent(String id, Long epoch, Integer first, Integer interval, Integer last, Boolean cancel) {
        this.id = id;
        this.epoch = epoch;
        this.first = first;
        this.interval = interval;
        this.last = last;
        this.cancel = cancel;
    }
    
    

}

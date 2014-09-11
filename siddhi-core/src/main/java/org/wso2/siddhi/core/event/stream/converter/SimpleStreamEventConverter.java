/*
 * Copyright (c) 2005 - 2014, WSO2 Inc. (http://www.wso2.org)
 * All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.siddhi.core.event.stream.converter;

import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.stream.StreamEvent;
import org.wso2.siddhi.core.event.stream.StreamEventPool;

import java.util.List;

public class SimpleStreamEventConverter implements EventConverter {
    private StreamEventPool streamEventPool;
    private List<ConverterElement> converterElements;

    public SimpleStreamEventConverter(StreamEventPool streamEventPool, List<ConverterElement> converterElements) {
        this.streamEventPool = streamEventPool;
        this.converterElements = converterElements;
    }

    /**
     * Converts events to StreamEvent
     *
     * @param data
     * @param isExpected
     * @param timestamp
     * @return StreamEvent
     */
    private StreamEvent convertToInnerStreamEvent(Object[] data, boolean isExpected, long timestamp) {
        StreamEvent streamEvent = streamEventPool.borrowEvent();
        for (ConverterElement element : converterElements) {
            streamEvent.setOutputData(data[element.getFromPosition()], element.getToPosition()[1]);
        }
        streamEvent.setExpired(isExpected);
        streamEvent.setTimestamp(timestamp);

        return streamEvent;
    }


    public StreamEvent convertToStreamEvent(Event event) {
        return convertToInnerStreamEvent(event.getData(), event.isExpired(), event.getTimestamp());
    }

    public StreamEvent convertToStreamEvent(StreamEvent streamEvent) {
        return convertToInnerStreamEvent(streamEvent.getOutputData(), streamEvent.isExpired(), streamEvent.getTimestamp());
    }

    /**
     * Method to convert(change format) timeStamp and data into new StreamEvent
     *
     * @param timeStamp timeStamp of the event
     * @param data      output data of the event
     * @return converted StreamEvent
     */
    @Override
    public StreamEvent convertToStreamEvent(long timeStamp, Object[] data) {
        return convertToInnerStreamEvent(data, false, timeStamp);
    }

}

/*
 * Copyright 2020 Zetyun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zetyun.streamtau.streaming.transformer.node;

import com.zetyun.streamtau.runtime.context.RtEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

@RequiredArgsConstructor
public final class DataStreamNode extends StreamNode {
    @Getter
    private final DataStream<RtEvent> dataStream;

    @Override
    public DataStream<RtEvent> asDataStream() {
        return dataStream;
    }

    @Override
    public int getParallelism() {
        return dataStream.getParallelism();
    }

    @Override
    public StreamExecutionEnvironment getEnv() {
        return dataStream.getExecutionEnvironment();
    }
}

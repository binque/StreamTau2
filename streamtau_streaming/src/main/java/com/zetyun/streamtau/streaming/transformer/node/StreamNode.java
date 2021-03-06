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
import com.zetyun.streamtau.streaming.exception.InvalidUseOfStreamNode;
import lombok.Getter;
import lombok.Setter;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import javax.annotation.Nonnull;

public abstract class StreamNode {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String schemaId;

    @Nonnull
    public static DataStreamNode of(DataStream<RtEvent> dataStream) {
        return new DataStreamNode(dataStream);
    }

    @Nonnull
    public static DataStreamSinkNode of(DataStreamSink<RtEvent> dataStreamSink) {
        return new DataStreamSinkNode(dataStreamSink);
    }

    @Nonnull
    public static DataStreamSourceNode of(DataStreamSource<RtEvent> dataStreamSource) {
        return new DataStreamSourceNode(dataStreamSource);
    }

    public DataStream<RtEvent> asDataStream() {
        throw new InvalidUseOfStreamNode(this, "asDataStream");
    }

    public int getParallelism() {
        throw new InvalidUseOfStreamNode(this, "getParallelism");
    }

    public StreamExecutionEnvironment getEnv() {
        throw new InvalidUseOfStreamNode(this, "getEnv");
    }
}

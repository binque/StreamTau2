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

package com.zetyun.streamtau.streaming.runtime.filter;

import com.zetyun.streamtau.expr.runtime.RtExpr;
import com.zetyun.streamtau.runtime.context.RtEvent;
import lombok.RequiredArgsConstructor;
import org.apache.flink.api.common.functions.FilterFunction;

@RequiredArgsConstructor
public class ExprFilterFunction implements FilterFunction<RtEvent> {
    private static final long serialVersionUID = -6129554848710564475L;

    private final RtExpr expr;

    @Override
    public boolean filter(RtEvent event) throws Exception {
        return (boolean) expr.eval(event);
    }
}

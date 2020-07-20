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

package com.zetyun.streamtau.expr.op;

import com.zetyun.streamtau.expr.core.CompileContext;
import com.zetyun.streamtau.expr.runtime.RtConst;
import com.zetyun.streamtau.expr.runtime.RtExpr;
import com.zetyun.streamtau.expr.runtime.op.RtNotOp;
import com.zetyun.streamtau.expr.runtime.op.RtUnaryOp;

public class NotOp extends UnaryOp {
    public NotOp() {
        super(null);
    }

    @Override
    public RtExpr compileIn(CompileContext ctx) {
        RtExpr rtExpr = expr.compileIn(ctx);
        RtUnaryOp rt = new RtNotOp(rtExpr);
        if (rtExpr instanceof RtConst) {
            return new RtConst(rt.eval(null));
        }
        return rt;
    }

    @Override
    public Class<?> calcType(CompileContext ctx) {
        return Boolean.class;
    }
}
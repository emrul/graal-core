/*
 * Copyright (c) 2013, 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.graal.lir.aarch64;

import static com.oracle.graal.lir.LIRInstruction.OperandFlag.REG;
import static com.oracle.graal.lir.LIRInstruction.OperandFlag.STACK;
import jdk.vm.ci.meta.Value;

import com.oracle.graal.asm.aarch64.AArch64MacroAssembler;
import com.oracle.graal.asm.aarch64.AArch64MacroAssembler.AArch64ExceptionCode;
import com.oracle.graal.lir.LIRInstructionClass;
import com.oracle.graal.lir.Opcode;
import com.oracle.graal.lir.asm.CompilationResultBuilder;

@Opcode("BREAKPOINT")
public class AArch64BreakpointOp extends AArch64LIRInstruction {
    public static final LIRInstructionClass<AArch64BreakpointOp> TYPE = LIRInstructionClass.create(AArch64BreakpointOp.class);

    /**
     * A set of values loaded into the Java ABI parameter locations (for inspection by a debugger).
     */
    @Use({REG, STACK}) private Value[] parameters;

    public AArch64BreakpointOp(Value[] parameters) {
        super(TYPE);
        this.parameters = parameters;
    }

    @Override
    public void emitCode(CompilationResultBuilder crb, AArch64MacroAssembler masm) {
        masm.brk(AArch64ExceptionCode.BREAKPOINT);
    }
}

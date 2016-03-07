/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.graal.replacements.nodes;

import jdk.vm.ci.meta.ResolvedJavaMethod;

import com.oracle.graal.compiler.common.type.Stamp;
import com.oracle.graal.graph.NodeClass;
import com.oracle.graal.nodeinfo.NodeInfo;
import com.oracle.graal.nodes.CallTargetNode.InvokeKind;
import com.oracle.graal.nodes.ValueNode;
import com.oracle.graal.nodes.spi.Virtualizable;
import com.oracle.graal.nodes.spi.VirtualizerTool;
import com.oracle.graal.nodes.virtual.VirtualObjectNode;

/**
 * A helper class to allow elimination of byte code instrumentation that could interfere with escape
 * analysis.
 */
@NodeInfo
public class VirtualizableInvokeMacroNode extends MacroStateSplitNode implements Virtualizable {

    public static final NodeClass<VirtualizableInvokeMacroNode> TYPE = NodeClass.create(VirtualizableInvokeMacroNode.class);

    public VirtualizableInvokeMacroNode(InvokeKind invokeKind, ResolvedJavaMethod targetMethod, int bci, Stamp returnStamp, ValueNode... arguments) {
        super(TYPE, invokeKind, targetMethod, bci, returnStamp, arguments);
    }

    @Override
    public void virtualize(VirtualizerTool tool) {
        for (ValueNode arg : arguments) {
            ValueNode alias = tool.getAlias(arg);
            if (alias instanceof VirtualObjectNode) {
                tool.delete();
            }
        }
    }
}

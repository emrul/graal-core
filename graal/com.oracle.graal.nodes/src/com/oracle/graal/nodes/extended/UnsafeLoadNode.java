/*
 * Copyright (c) 2011, 2016, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.graal.nodes.extended;

import static com.oracle.graal.nodeinfo.InputType.Condition;
import static com.oracle.graal.nodeinfo.NodeCycles.CYCLES_2;
import static com.oracle.graal.nodeinfo.NodeSize.SIZE_1;

import com.oracle.graal.compiler.common.LocationIdentity;
import com.oracle.graal.compiler.common.type.StampFactory;
import com.oracle.graal.graph.NodeClass;
import com.oracle.graal.nodeinfo.NodeInfo;
import com.oracle.graal.nodes.LogicNode;
import com.oracle.graal.nodes.ValueNode;
import com.oracle.graal.nodes.java.LoadFieldNode;
import com.oracle.graal.nodes.spi.Lowerable;
import com.oracle.graal.nodes.spi.LoweringTool;
import com.oracle.graal.nodes.spi.Virtualizable;
import com.oracle.graal.nodes.spi.VirtualizerTool;
import com.oracle.graal.nodes.virtual.VirtualObjectNode;

import jdk.vm.ci.meta.Assumptions;
import jdk.vm.ci.meta.JavaKind;
import jdk.vm.ci.meta.ResolvedJavaField;

/**
 * Load of a value from a location specified as an offset relative to an object. No null check is
 * performed before the load.
 */
@NodeInfo(cycles = CYCLES_2, size = SIZE_1)
public final class UnsafeLoadNode extends UnsafeAccessNode implements Lowerable, Virtualizable {
    public static final NodeClass<UnsafeLoadNode> TYPE = NodeClass.create(UnsafeLoadNode.class);
    @OptionalInput(Condition) LogicNode guardingCondition;

    public UnsafeLoadNode(ValueNode object, ValueNode offset, JavaKind accessKind, LocationIdentity locationIdentity) {
        this(object, offset, accessKind, locationIdentity, null);
    }

    public UnsafeLoadNode(ValueNode object, ValueNode offset, JavaKind accessKind, LocationIdentity locationIdentity, LogicNode condition) {
        super(TYPE, StampFactory.forKind(accessKind.getStackKind()), object, offset, accessKind, locationIdentity);
        this.guardingCondition = condition;
    }

    public LogicNode getGuardingCondition() {
        return guardingCondition;
    }

    @Override
    public void lower(LoweringTool tool) {
        tool.getLowerer().lower(this, tool);
    }

    @Override
    public void virtualize(VirtualizerTool tool) {
        ValueNode alias = tool.getAlias(object());
        if (alias instanceof VirtualObjectNode) {
            VirtualObjectNode virtual = (VirtualObjectNode) alias;
            ValueNode offsetValue = tool.getAlias(offset());
            if (offsetValue.isConstant()) {
                long off = offsetValue.asJavaConstant().asLong();
                int entryIndex = virtual.entryIndexForOffset(off, accessKind());

                if (entryIndex != -1) {
                    ValueNode entry = tool.getEntry(virtual, entryIndex);
                    JavaKind entryKind = virtual.entryKind(entryIndex);
                    if (entry.getStackKind() == getStackKind() || entryKind == accessKind()) {
                        tool.replaceWith(entry);
                    }
                }
            }
        }
    }

    @Override
    protected ValueNode cloneAsFieldAccess(Assumptions assumptions, ResolvedJavaField field) {
        return LoadFieldNode.create(assumptions, object(), field);
    }

    @Override
    protected ValueNode cloneAsArrayAccess(ValueNode location, LocationIdentity identity) {
        return new UnsafeLoadNode(object(), location, accessKind(), identity, guardingCondition);
    }

    @NodeIntrinsic
    public static native Object load(Object object, long offset, @ConstantNodeParameter JavaKind kind, @ConstantNodeParameter LocationIdentity locationIdentity);
}

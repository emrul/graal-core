/*
 * Copyright (c) 2013, 2016, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.graal.phases.tiers;

import com.oracle.graal.compiler.common.spi.ConstantFieldProvider;
import com.oracle.graal.nodes.spi.LoweringProvider;
import com.oracle.graal.nodes.spi.NodeCostProvider;
import com.oracle.graal.nodes.spi.Replacements;
import com.oracle.graal.nodes.spi.StampProvider;
import com.oracle.graal.phases.util.Providers;

import jdk.vm.ci.meta.ConstantReflectionProvider;
import jdk.vm.ci.meta.MetaAccessProvider;

public class PhaseContext {

    private final MetaAccessProvider metaAccess;
    private final ConstantReflectionProvider constantReflection;
    private final ConstantFieldProvider constantFieldProvider;
    private final LoweringProvider lowerer;
    private final Replacements replacements;
    private final StampProvider stampProvider;
    private final NodeCostProvider nodeCostProvider;

    public PhaseContext(MetaAccessProvider metaAccess, ConstantReflectionProvider constantReflection, ConstantFieldProvider constantFieldProvider, LoweringProvider lowerer, Replacements replacements,
                    StampProvider stampProvider, NodeCostProvider nodeCostProvider) {
        this.metaAccess = metaAccess;
        this.constantReflection = constantReflection;
        this.constantFieldProvider = constantFieldProvider;
        this.lowerer = lowerer;
        this.replacements = replacements;
        this.stampProvider = stampProvider;
        this.nodeCostProvider = nodeCostProvider;
    }

    public PhaseContext(Providers providers) {
        this(providers.getMetaAccess(), providers.getConstantReflection(), providers.getConstantFieldProvider(), providers.getLowerer(), providers.getReplacements(), providers.getStampProvider(),
                        providers.getNodeCostProvider());
    }

    public MetaAccessProvider getMetaAccess() {
        return metaAccess;
    }

    public ConstantReflectionProvider getConstantReflection() {
        return constantReflection;
    }

    public ConstantFieldProvider getConstantFieldProvider() {
        return constantFieldProvider;
    }

    public LoweringProvider getLowerer() {
        return lowerer;
    }

    public Replacements getReplacements() {
        return replacements;
    }

    public StampProvider getStampProvider() {
        return stampProvider;
    }

    public NodeCostProvider getNodeCostProvider() {
        return nodeCostProvider;
    }
}

/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.faces.application.applicationimpl.events;

import javax.faces.event.SystemEvent;

import com.sun.faces.util.Cache;
import com.sun.faces.util.Cache.Factory;

/**
 * Utility class for dealing with {@link javax.faces.component.UIComponent} events.
 */
public class ComponentSystemEventHelper {

    private Cache<Class<?>, Cache<Class<? extends SystemEvent>, EventInfo>> sourceCache;

    // -------------------------------------------------------- Constructors

    public ComponentSystemEventHelper() {

        // Initialize the 'sources' cache for, ahem, readability...
        // ~generics++
        Factory<Class<?>, Cache<Class<? extends SystemEvent>, EventInfo>> eventCacheFactory = new Factory<Class<?>, Cache<Class<? extends SystemEvent>, EventInfo>>() {
            @Override
            public Cache<Class<? extends SystemEvent>, EventInfo> newInstance(final Class<?> sourceClass) throws InterruptedException {
                Factory<Class<? extends SystemEvent>, EventInfo> eventInfoFactory = new Factory<Class<? extends SystemEvent>, EventInfo>() {
                    @Override
                    public EventInfo newInstance(final Class<? extends SystemEvent> systemEventClass) throws InterruptedException {
                        return new EventInfo(systemEventClass, sourceClass);
                    }
                };
                return new Cache<>(eventInfoFactory);
            }
        };
        sourceCache = new Cache<>(eventCacheFactory);

    }

    // ------------------------------------------------------ Public Methods

    public EventInfo getEventInfo(Class<? extends SystemEvent> systemEvent, Class<?> sourceClass) {

        Cache<Class<? extends SystemEvent>, EventInfo> eventsCache = sourceCache.get(sourceClass);
        return eventsCache.get(systemEvent);

    }

}

/*
 * Copyright  1990-2009 Sun Microsystems, Inc. All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License version
 * 2 only, as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License version 2 for more details (a copy is
 * included at /legal/license.txt).
 * 
 * You should have received a copy of the GNU General Public License
 * version 2 along with this work; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 * 
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa
 * Clara, CA 95054 or visit www.sun.com if you need additional
 * information or have any questions.
 */

package com.sun.midp.lcdui;

import com.sun.orientation.OrientationHandler;
import com.sun.orientation.OrientationListener;
import com.sun.midp.orientation.OrientationFactory;
import com.sun.midp.security.*;
import com.sun.midp.events.EventQueue;
import com.sun.midp.events.EventTypes;
import com.sun.midp.events.NativeEvent;
import com.sun.midp.main.Configuration;
import javax.microedition.lcdui.Image;

/**
 * Initialize the LCDUI environment.
 */
public class LCDUIEnvironment implements OrientationListener {

    /** Stores array of active displays for a MIDlet suite isolate. */
    private DisplayContainer displayContainer;

    /**
     * Provides interface for display preemption, creation and other
     * functionality that can not be publicly added to a javax package.
     */
    private DisplayEventHandler displayEventHandler;

    /**
     * Saved instance of the event queue.
     */
    private EventQueue eventQueue;

    /**
     * Creates lcdui event producers/handlers/listeners.
     * 
     * @param internalSecurityToken
     * @param eventQueue
     * @param isolateId
     * @param foregroundController
     */
    public LCDUIEnvironment(SecurityToken internalSecurityToken,
                            EventQueue eventQueue, 
                            int isolateId,
                            ForegroundController foregroundController) {
        this(internalSecurityToken, eventQueue,
             new DefaultDisplayIdPolicy(isolateId), foregroundController);
    }

    /**
     * Creates lcdui event producers/handlers/listeners.
     * 
     * @param internalSecurityToken
     * @param eventQueue
     * @param idPolicy
     * @param foregroundController
     */
    public LCDUIEnvironment(SecurityToken internalSecurityToken,
                            EventQueue eventQueue, 
                            DisplayIdPolicy idPolicy,
                            ForegroundController foregroundController) {

        displayEventHandler =
            DisplayEventHandlerFactory.getDisplayEventHandler(
               internalSecurityToken);

        DisplayEventProducer displayEventProducer =
            new DisplayEventProducer(
                eventQueue);

        RepaintEventProducer repaintEventProducer =
            new RepaintEventProducer(
                eventQueue);

        displayContainer = new DisplayContainer(internalSecurityToken,
                                                idPolicy);

        DisplayDeviceContainer displayDeviceContainer =
            new DisplayDeviceContainer();

        /*
         * Because the display handler is implemented in a javax
         * package it cannot created outside of the package, so
         * we have to get it after the static initializer of display the class
         * has been run and then hook up its objects.
         */
        displayEventHandler.initDisplayEventHandler(
	    displayEventProducer,
            foregroundController,
            repaintEventProducer,
            displayContainer,
	    displayDeviceContainer);

        // Set a listener in the event queue for display events
        new DisplayEventListener(
            eventQueue,
            displayContainer,
            displayDeviceContainer);

        /*
         * Set a listener in the event queue for LCDUI events
         *
         * Bad style of type casting, but DisplayEventHandlerImpl
         * implements both DisplayEventHandler & ItemEventConsumer IFs 
         */
        new LCDUIEventListener(
            internalSecurityToken,
            eventQueue,
            (ItemEventConsumer)displayEventHandler);

        // Set a listener in the event queue for foreground events
        new ForegroundEventListener(eventQueue, displayContainer);

        // Initialize a handler to process rotation events
        String orientClassName = Configuration.getProperty("com.sun.midp.orientClassName");
		if (orientClassName != null && orientClassName.length() > 0) {
            OrientationHandler orientHandler = OrientationFactory.createOrientHandler(orientClassName);
		    if (orientHandler != null) {
		        this.eventQueue = eventQueue;
			    orientHandler.addListener(this);
			}
		}
    }
	
    /**
     * Calls when orientation is changed. 
     *
     * @param orientation the orientation state
     */
    public void orientationChanged(int orientation) {
        DisplayAccess da = displayContainer.findForegroundDisplay();
		if (da != null) {
		    NativeEvent rotEvent = new NativeEvent(EventTypes.ROTATION_EVENT);
			rotEvent.intParam4 = da.getDisplayId();
            eventQueue.post(rotEvent);
		}
	}

    /**
     * Gets DisplayContainer instance. 
     *
     * @return DisplayContainer
     */
    public DisplayContainer getDisplayContainer() {
	return displayContainer;
    }

    /** 
     * Called during system shutdown.  
     */
    public void shutDown() {

        // shutdown any preempting
	displayEventHandler.donePreempting(null);
    }

    /**
     * Sets the trusted state based on the passed in boolean.
     *
     * @param isTrusted if true state is set to trusted.
     */
    public void setTrustedState(boolean isTrusted) {
        displayEventHandler.setTrustedState(isTrusted);
    }
}

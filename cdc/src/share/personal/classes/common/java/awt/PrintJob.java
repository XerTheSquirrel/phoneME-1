/*
 * @(#)PrintJob.java	1.16 06/10/10
 *
 * Copyright  1990-2008 Sun Microsystems, Inc. All Rights Reserved.
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
 *
 */

package java.awt;

/** 
 * An abstract class which initiates and executes a print job.
 * It provides access to a print graphics object which renders
 * to an appropriate print device.
 *
 * @see Toolkit#getPrintJob
 *
 * @version 	1.12 08/19/02
 * @author 	Amy Fowler
 */
public abstract class PrintJob {
    /**
     * Gets a Graphics object that will draw to the next page.
     * The page is sent to the printer when the graphics 
     * object is disposed.  This graphics object will also implement
     * the PrintGraphics interface.
     * @see PrintGraphics
     */
    public abstract Graphics getGraphics();

    /**
     * Returns the dimensions of the page in pixels.
     * The resolution of the page is chosen so that it
     * is similar to the screen resolution.
     */
    public abstract Dimension getPageDimension();

    /**
     * Returns the resolution of the page in pixels per inch.
     * Note that this doesn't have to correspond to the physical
     * resolution of the printer.
     */
    public abstract int getPageResolution();

    /**
     * Returns true if the last page will be printed first.
     */
    public abstract boolean lastPageFirst();

    /**
     * Ends the print job and does any necessary cleanup.
     */
    public abstract void end();

    /**
     * Ends this print job once it is no longer referenced.
     * @see #end
     */
    public void finalize() {
        end();
    }
}

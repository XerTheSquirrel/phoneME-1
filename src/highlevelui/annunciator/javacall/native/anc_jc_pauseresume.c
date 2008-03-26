/*
 *
 *
 * Copyright  1990-2007 Sun Microsystems, Inc. All Rights Reserved.
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

#include <midpPauseResume.h>
#include <midp_logging.h>
#include <javacall_annunciator.h>
#include <javacall_lifecycle.h>

#if !ENABLE_MULTIPLE_ISOLATES
#include <javaTask.h>
#endif

/**
 * @file
 *
 * Platform dependent native code to handle VM pause and resume.
 */

/**
 * Platform handling code for VM pause notification call.
 */
void pdMidpNotifyPausedAll() {
    javacall_lifecycle_state_changed(JAVACALL_LIFECYCLE_MIDLET_PAUSED,
                                     JAVACALL_OK);
#if !ENABLE_MULTIPLE_ISOLATES
    stop_kill_timer();

    /* IMPL NOTE: don't suspend isolate if TCK is running */

    /* suspend the isolate */
    vmPaused = JAVACALL_TRUE;
#endif
}

/**
 * Platform handling code for VM resume notification call.
 */
void pdMidpNotifyResumedAll() {
    javacall_lifecycle_state_changed(JAVACALL_LIFECYCLE_MIDLET_RESUMED,
                                         JAVACALL_OK);
}

/**
 * Platform handling code for VM suspend notification call.
 */
void pdMidpNotifyInternalPausedAll() {
    javacall_lifecycle_state_changed(JAVACALL_LIFECYCLE_MIDLET_INTERNAL_PAUSED,
                                     JAVACALL_OK);
}

/**
 * Platform handling code for VM continue notification call.
 */
void pdMidpNotifyInternalResumedAll() {
    javacall_lifecycle_state_changed(JAVACALL_LIFECYCLE_MIDLET_INTERNAL_RESUMED,
                                     JAVACALL_OK);
}


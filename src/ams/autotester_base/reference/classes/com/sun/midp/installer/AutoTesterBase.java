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

package com.sun.midp.installer;

import java.io.IOException;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

import com.sun.midp.i18n.Resource;
import com.sun.midp.i18n.ResourceConstants;

import com.sun.midp.midlet.MIDletSuite;

import com.sun.midp.midletsuite.MIDletInfo;
import com.sun.midp.midletsuite.MIDletSuiteStorage;

import com.sun.midp.security.Permissions;

import com.sun.midp.log.Logging;
import com.sun.midp.log.LogChannels;

/**
 * Installs/Updates a test suite, runs the first MIDlet in the suite in a loop
 * until the new version of the suite is not found, then removes the suite.
 * <p>
 * The MIDlet uses these application properties as arguments: </p>
 * <ol>
 *   <li>arg-0: URL for the test suite
 *   <li>arg-1: Used to override the default domain used when installing
 *    an unsigned suite. The default is maximum to allow the runtime API tests
 *    be performed automatically without tester interaction. The domain name
 *    may be followed by a colon and a list of permissions that must be allowed
 *    even if they are not listed in the MIDlet-Permissions attribute in the
 *    application descriptor file. Instead of the list a keyword "all" can be
 *    specified indicating that all permissions must be allowed, for example:
 *    operator:all.
 * </ol>
 * <p>
 * If arg-0 is not given then a form will be used to query the tester for
 * the arguments.</p>
 */
abstract class AutoTesterBase extends MIDlet implements CommandListener,
    Runnable {

    /** Standard timeout for alerts. */
    static final int ALERT_TIMEOUT = 5000;
    /** Contains the default URL. */
    static final String defaultUrl = "http://";

    /** Display for this MIDlet. */
    Display display;
    /** Parameter form if there is not URL parameter given. */
    Form parameterForm;
    /** Contains the URL the user typed in. */
    TextField urlTextField;
    /** Contains the domain the user typed in. */
    TextField domainTextField;
    /** Command object for "Exit" command in the URL screen. */
    Command endCmd = new Command(Resource.getString
                                         (ResourceConstants.EXIT),
                                         Command.EXIT, 1);
    /** Command object for URL screen start testing. */
    Command testCmd =
        new Command(Resource.getString(ResourceConstants.GO),
                    Command.SCREEN, 1);
    /** URL of the test suite. */
    String url;
    /** Security domain to assign to unsigned suites. */
    String domain = Permissions.getUnsignedDomain();
    /** How many iterations to run the suite */
    int loopCount = -1;

    AutoTesterHelperBase helper;

    /**
     * Create and initialize a new auto tester MIDlet.
     */
    AutoTesterBase(AutoTesterHelperBase inp_helper) {
        helper = inp_helper;

        display = Display.getDisplay(this);

        // The arg-<n> properties are generic command arguments
        url = getAppProperty("arg-0");
        if (url != null) {
            // URL given as a argument, look for a domain arg and then start
            String arg1 = getAppProperty("arg-1");

            boolean hasLoopCount = false;
            if (arg1 != null) {
                // this can be domain or loop count
                try {
                    loopCount = Integer.parseInt(arg1);
                    hasLoopCount = true;
                } catch (NumberFormatException e) {
                    // then its domain
                    domain = arg1;
                }

                if (!hasLoopCount) {
                    String arg2 = getAppProperty("arg-2");
                    if (arg2 != null) {
                        try {
                            loopCount = Integer.parseInt(arg2);
                        } catch (NumberFormatException e) {
                            // just ignore
                        }
                    }
                }
            }
        }
    }

    /**
     * Start.
     */
    public void startApp() {
        // Avoid competing for foreground with Test MIDlet
        display.setCurrent(null);
        notifyPaused();
    }

    /**
     * Pause; there are no resources that need to be released.
     */
    public void pauseApp() {
    }

    /**
     * Destroy cleans up.
     *
     * @param unconditional is ignored; this object always
     * destroys itself when requested.
     */
    public void destroyApp(boolean unconditional) {
    }

    /**
     * Respond to a command issued on any Screen.
     *
     * @param c command activated by the user
     * @param s the Displayable the command was on.
     */
    public void commandAction(Command c, Displayable s) {
        if (c == testCmd) {
            getURLTextAndTest();
        } else if (c == endCmd || c == Alert.DISMISS_COMMAND) {
            // goto back to the manager midlet
            notifyDestroyed();
        }
    }

    abstract public void run();

    /**
     * Ask the user for the URL.
     */
    void getUrl() {
        try {
            parameterForm = new
                Form(Resource.getString
                     (ResourceConstants.AMS_AUTO_TESTER_TESTSUITE_PARAM));

            urlTextField = new TextField
                (Resource.getString(ResourceConstants.AMS_AUTO_TESTER_URL),
                              defaultUrl, 1024, TextField.ANY);
            urlTextField.setLayout(Item.LAYOUT_NEWLINE_AFTER | Item.LAYOUT_2);
            parameterForm.append(urlTextField);

            domainTextField =
                new TextField(Resource.getString(ResourceConstants.
                              AMS_AUTO_TESTER_UNSIGN_SECURITY_DOMAIN),
                              domain, 1024, TextField.ANY);
            domainTextField.setLayout(Item.LAYOUT_NEWLINE_AFTER |
                                      Item.LAYOUT_2);
            parameterForm.append(domainTextField);

            parameterForm.addCommand(endCmd);
            parameterForm.addCommand(testCmd);
            parameterForm.setCommandListener(this);

            display.setCurrent(parameterForm);
        } catch (Exception ex) {
            displayException(Resource.getString(ResourceConstants.EXCEPTION),
                             ex.toString());
        }
    }

    /**
     * Save the URL setting the user entered in to the urlTextBox.
     */
    void getURLTextAndTest() {
        url = urlTextField.getString();

        if (url == null || url.length() == 0) {
            Alert a = new Alert(Resource.getString(ResourceConstants.ERROR),
                                Resource.getString(ResourceConstants.
                                       AMS_AUTO_TESTER_ERROR_URL_MSG),
                                   null, AlertType.ERROR);
            a.setTimeout(ALERT_TIMEOUT);
            display.setCurrent(a, parameterForm);
            return;
        }

        domain = domainTextField.getString();

        if (domain == null || domain.length() == 0) {
            Alert a = new Alert(Resource.getString(ResourceConstants.ERROR),
                                Resource.getString(ResourceConstants.
                                    AMS_AUTO_TESTER_ERROR_SECURITY_DOMAIN_MSG),
                                null, AlertType.ERROR);
            a.setTimeout(ALERT_TIMEOUT);
            display.setCurrent(a, parameterForm);
            return;
        }

        startBackgroundTester(true);
    }

    /**
     * Start the background tester.
     */
    void startBackgroundTester(boolean setTestRunParams) {
        if (setTestRunParams) {
            helper.setTestRunParams(url, domain, loopCount);
        }
        new Thread(this).start();
    }

    /**
     * Handles an installer exceptions.
     *
     * @param message error message
     */
    void handleInstallerException(String message) {
        if (message != null) {
            displayException(Resource.getString(ResourceConstants.ERROR),
                             message);

            long start = System.currentTimeMillis();
            long time_left = ALERT_TIMEOUT;

            while (time_left > 0) {
                try {
                    Thread.sleep(time_left);
                    time_left = 0;
                } catch (InterruptedException ie) {
                    long tmp = System.currentTimeMillis();
                    time_left -= (tmp - start);
                    start = tmp;
                }
            }
        }
    }

    /**
     * Display an exception to the user, with a done command.
     *
     * @param title exception form's title
     * @param message exception message
     */
    void displayException(String title, String message) {
        Alert a = new Alert(title, message, null, AlertType.ERROR);

        // This application must log always.
        Logging.report(Logging.CRITICAL, LogChannels.LC_CORE, message);
        a.setTimeout(ALERT_TIMEOUT);
        a.setCommandListener(this);

        display.setCurrent(a);
    }

}

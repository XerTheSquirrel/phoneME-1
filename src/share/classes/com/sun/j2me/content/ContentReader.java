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

package com.sun.j2me.content;

import com.sun.j2me.io.Base64;

import javax.microedition.io.Connector;
import javax.microedition.io.Connection;
import javax.microedition.io.ContentConnection;
import javax.microedition.io.HttpConnection;

import java.io.IOException;

/**
 * The helper class extending GCF Connector functionality with demands
 * of the JSR 211 specification:
 * <ul>
 *  <li> the connetion may deliver the content from a cache.
 *  <li> the connection should use user credentials.
 * </ul>
 */
class ContentReader {

    private String url;
    private String username;
    private char[] password;

    ContentReader(String url, String username, char[] password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Creates and opens a Connection to the content addressed by
     * the <code>url</code>. This method is
     * similar to <code>Connector.open(url, READ, timeouts)</code>
     * but may deliver the content from a cache.
     * Regardless of whether or not the content is cached, the
     * application must have permission to access
     * the content via the <code>url</code>.
     *
     * @param timeouts         a flag to indicate that the caller
     *                         wants timeout exceptions
     * @return                 a Connection object
     *
     * @exception ConnectionNotFoundException is thrown if:
     *   <ul>
     *      <li>the target URL can not be found, or</li>
     *      <li>the requested protocol type is not supported</li>
     *   </ul>
     * @exception NullPointerException if the URL is null
     * @exception IllegalArgumentException if an <code>url</code> parameter is invalid.
     * @exception java.io.IOException  if some other kind of I/O error occurs
     * @exception SecurityException is thrown if access to the
     *   protocol handler is prohibited
     */
    Connection open(boolean timeouts) throws IOException, SecurityException {
        return openPrim(timeouts, false);
    }

    /**
     * Finds the type of the content in this Invocation.
     * <p>
     * The calling thread blocks while the type is being determined.
     * If a network access is needed there may be an associated delay.
     *
     * @return the content type.
     *          May be <code>null</code> if the type can not be determined.
     *
     * @exception IOException if access to the content fails
     * @exception IllegalArgumentException if the content is accessed via
     *  the URL and the URL is invalid
     * @exception SecurityException is thrown if access to the content
     *  is required and is not permitted
     */
    String findType() throws IOException, SecurityException {
        String type = null;
        ContentConnection conn = (ContentConnection)openPrim(true, true);

        if (conn != null) {
            type = (conn).getType();
            conn.close();

            if (type != null) {
                // Check for and remove any parameters (rfc2616)
                int ndx = type.indexOf(';');
                if (ndx >= 0) {
                    type = type.substring(0, ndx);
                }
                type = type.trim();
                if (type.length() == 0) {
                    type = null;
                }
            }
        }

        return type;
    }

    /**
     * The method currently supports only HTTP protocol and basic authentication.
     *
     * @param timeouts a flag to indicate that the caller
     *                         wants timeout exceptions.
     * @param headsOnly open connection for content type discover.
     *
     * @return a Connection object
     *
     * @exception IOException if access to the content fails
     * @exception IllegalArgumentException if the content is accessed via
     *  the URL and the URL is invalid
     * @exception SecurityException is thrown if access to the content
     *  is required and is not permitted
     */
    private Connection openPrim(boolean timeouts, boolean headsOnly)
            throws IOException, SecurityException {
        Connection conn;

        conn = Connector.open(url, Connector.READ, timeouts);
        if (conn instanceof HttpConnection) {
            HttpConnection httpc = (HttpConnection)conn;
            boolean authorized = false;
            while (true) {
                if (headsOnly) {
                    httpc.setRequestMethod(httpc.HEAD);
                }

                // actual connection performed, some delay...
                int rc = httpc.getResponseCode();

                if (rc == HttpConnection.HTTP_OK) {
                    break;
                }

                // try to set authorization
                if (rc == HttpConnection.HTTP_UNAUTHORIZED ||
                        rc == HttpConnection.HTTP_PROXY_AUTH) {
                    if (username == null && password == null) {
                        // TODO: show form requested user input.
                        throw new IOException("Connection needs authorization");
                    }
                    String authType = httpc.getHeaderField("WWW-Authenticate");
                    if (authType == null || !authType.trim().equalsIgnoreCase("basic")) {
                        throw new IOException("not supported authorization");
                    }
                    if (authorized) {
                        // there always was one attempt to authorize.
                        throw new IOException("authorization fails");
                    }

                    // reopen connection with authorization property set
                    conn = Connector.open(url, Connector.READ, timeouts);
                    httpc = (HttpConnection)conn;
                    httpc.setRequestProperty(
                            rc == HttpConnection.HTTP_UNAUTHORIZED?
                            "Authorization": "Proxy-Authorization",
                            formatAuthCredentials(username, password));
                    authorized = true;
                    continue;
                }

                throw new IOException("Connection failed: "+httpc.getResponseMessage());
            }
        } else if (headsOnly) {
            // inly HTTP protocol is supported for type discovering.
            conn.close();
            conn = null;
        }

        return conn;
    }


    /**
     * Formats the username and password for HTTP basic authentication
     * according RFC 2617.
     *
     * @param username for HTTP authentication
     * @param password for HTTP authentication
     *
     * @return properly formated basic authentication credential
     */
    private static String formatAuthCredentials(String username,
                                                char[] password) {
        byte[] data = new byte[username.length() + password.length + 1];
        int j = 0;

        for (int i = 0; i < username.length(); i++, j++) {
            data[j] = (byte)username.charAt(i);
        }

        data[j] = (byte)':';
        j++;

        for (int i = 0; i < password.length; i++, j++) {
            data[j] = (byte)password[i];
        }

        return "Basic " + Base64.encode(data, 0, data.length);
    }

}

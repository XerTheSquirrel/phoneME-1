/*
 * Copyright  1990-2006 Sun Microsystems, Inc. All Rights Reserved.
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

package com.sun.jump.common;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.net.URL;
import com.sun.jump.common.JUMPContent;
import java.util.Iterator;

/**
 * A representation of executable application content in the jump environment.
 * Because we can have different app types (e.g., XLET v. MIDLET),
 * use this class to hold relevant information needed to start
 * an application properly.
 */
public class JUMPApplication
        implements java.io.Serializable, JUMPContent {
    
    protected HashMap props = null; // additional properties
    
    public static final String ICONPATH_KEY = "JUMPApplication_iconPath";
    public static final String TITLE_KEY = "JUMPApplication_title";
    public static final String APPMODEL_KEY = "JUMPApplication_appModel";
    
    /**
     * Create an instance of an application.
     * @param title The application's title, can be null
     * @param iconPath The location of the application's icon in, can be null
     * @param type The application's type
     */
    public JUMPApplication(String title, URL iconPath, JUMPAppModel type) {
        addProperty(TITLE_KEY, title);
        addProperty(ICONPATH_KEY, iconPath.getFile());
        addProperty(APPMODEL_KEY, type.getName());
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[app type=\""+getAppType()+"\",");
        sb.append(" iconPath=\""+getIconPath()+"\",");
        sb.append(" title=\""+getTitle()+"\",");
        sb.append(" props=["+getPropsAsString()+"] ]");
        return sb.toString();
    }
    
    private String getPropsAsString() {
        if (props == null) {
            return "null";
        }
        
        StringBuffer sb = new StringBuffer();
        for (Iterator i = getPropertyNames(); i.hasNext(); ) {
            String name = (String)i.next();
            String value = getProperty(name);
            sb.append(name+"="+value+", ");
        }
        return sb.toString();
    }

    /**
     * Determine the type of this application.
     *
     * @return One of JUMPApplication's defined application types,
     *         as defined in JUMPAppModel.
     */
    public JUMPAppModel getAppType() {
        String type = getProperty(APPMODEL_KEY);
        if (type.equals(JUMPAppModel.XLET.getName())) {
            return JUMPAppModel.XLET;
        } else if (type.equals(JUMPAppModel.MAIN.getName())) {
            return JUMPAppModel.MAIN;
        } else if (type.equals(JUMPAppModel.MIDLET.getName())) {
            return JUMPAppModel.MIDLET;
        }
        return null;
    }
    
    /**
     * Get the application's title.
     * @return The application's title.
     */
    public String getTitle() {
        return getProperty(TITLE_KEY);
    }
    
    /**
     * Set the application's title.
     *
     */
    public void setTitle( String title ) {
        addProperty(TITLE_KEY, title);
        return;
    }
    
    /**
     * Get the path to the application's icon.
     * @return A URL defining the path to the icon in
     *         the downloaded content.
     */
    public URL getIconPath() {
        String file = getProperty(ICONPATH_KEY);
        URL url = null;
        try {
            url = new URL("file", null, file);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        return url;
    }
    
    /**
     * Set the path to the application's icon.
     */
    public void setIconPath( URL path ) {
        addProperty(ICONPATH_KEY, path.getFile());
        return;
    }
    
    /**
     * Add a key/value pair to the application's list
     * of properties.
     * @param key - A key value.
     * @param value - An object be associated with the key.
     * @throws NullPointerException - If either key or value is
     *                           <code>null</code>.
     */
    public void addProperty( String key, String value ) //throws SyntaxException
    {
        if ( key == null || value == null ) {
            throw new NullPointerException("null key or value");
        }
        if ( props == null ) {
            props = new HashMap();
        }
        props.put( key, value );
        
        return;
    }
    
    /**
     * Get a key/value pair to the application's list
     * of properties.
     * @param key - A key to search the properties for.
     * @throws NullPointerException - If key is
     *                           <code>null</code>.
     */
    public String getProperty( String key ) //throws SyntaxException
    {
        if ( props != null ) {
            return (String) props.get(key);
        }
        
        return null;
    }
    
    /**
     * Returns the names of this JUMPApplication's property entries as an
     * Iterator of String objects, or an empty Iterator if
     * the JUMPApplication have no properties associated.
     */
    public Iterator getPropertyNames() {
        if ( props != null ) {
            return props.keySet().iterator();
        }
        
        return null;
    }
    
    public String getContentType() {
        return "Application";
    }
    
}


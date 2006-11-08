/*
 * Copyright 1990-2006 Sun Microsystems, Inc. All Rights Reserved. 
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER 
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 only,
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 2 for more details (a copy is included at /legal/license.txt).
 * 
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 or visit www.sun.com if you need additional information or have
 * any questions.
 */

package java.security;

import java.io.*;

/**
 * This class is used to represent an Identity that can also digitally
 * sign data.
 *
 * <p>The management of a signer's private keys is an important and
 * sensitive issue that should be handled by subclasses as appropriate
 * to their intended use.
 *
 * @see Identity
 *
 * @version 1.37 00/02/02
 * @author Benjamin Renaud
 *
 * @deprecated This class is no longer used. Its functionality has been
 * replaced by <code>java.security.KeyStore</code>, the
 * <code>java.security.cert</code> package, and
 * <code>java.security.Principal</code>.
 */
public abstract class Signer extends Identity {

    /**
     * The signer's private key.
     *
     * @serial
     */
    private PrivateKey privateKey;

    /**
     * Creates a signer. This constructor should only be used for
     * serialization.
     */
    protected Signer() {
	super();
    }


    /**
     * Creates a signer with the specified identity name.
     *
     * @param name the identity name.
     */
    public Signer(String name) {
	super(name);
    }

    /**
     * Creates a signer with the specified identity name and scope.
     *
     * @param name the identity name.
     *
     * @param scope the scope of the identity.
     *
     * @exception KeyManagementException if there is already an identity
     * with the same name in the scope.
     */
    public Signer(String name, IdentityScope scope)
    throws KeyManagementException {
	super(name, scope);
    }

    /**
     * Returns this signer's private key.
     *
     * <p>First, if there is a security manager, its <code>checkSecurityAccess</code> 
     * method is called with <code>"getSignerPrivateKey"</code> 
     * as its argument to see if it's ok to return the private key. 
     * 
     * @return this signer's private key, or null if the private key has
     * not yet been set.
     * 
     * @exception  SecurityException  if a security manager exists and its  
     * <code>checkSecurityAccess</code> method doesn't allow 
     * returning the private key.
     * 
     * @see SecurityManager#checkSecurityAccess
     */
    public PrivateKey getPrivateKey() {
	check("getSignerPrivateKey");
	return privateKey;
    }

   /**
     * Sets the key pair (public key and private key) for this signer.
     *
     * <p>First, if there is a security manager, its <code>checkSecurityAccess</code> 
     * method is called with <code>"setSignerKeyPair"</code> 
     * as its argument to see if it's ok to set the key pair. 
     * 
     * @param pair an initialized key pair.
     *
     * @exception InvalidParameterException if the key pair is not
     * properly initialized.
     * @exception KeyException if the key pair cannot be set for any
     * other reason.
     * @exception  SecurityException  if a security manager exists and its  
     * <code>checkSecurityAccess</code> method doesn't allow 
     * setting the key pair.
     * 
     * @see SecurityManager#checkSecurityAccess
     */
    public final void setKeyPair(KeyPair pair)
    throws InvalidParameterException, KeyException {
	check("setSignerKeyPair");
	final PublicKey pub = pair.getPublic();
	PrivateKey priv = pair.getPrivate();

	if (pub == null || priv == null) {
	    throw new InvalidParameterException();
	}
	try {
	    AccessController.doPrivileged(new PrivilegedExceptionAction() {
		public Object run() throws KeyManagementException {
		    setPublicKey(pub);
		    return null;
		}
	    });
	} catch (PrivilegedActionException pae) {
	    throw (KeyManagementException) pae.getException();
	}
	privateKey = priv;
    }

    String printKeys() {
	String keys = "";
	PublicKey publicKey = getPublicKey();
	if (publicKey != null && privateKey != null) {
	    keys = "\tpublic and private keys initialized";

	} else {
	    keys = "\tno keys";
	}
	return keys;
    }

    /**
     * Returns a string of information about the signer.
     *
     * @return a string of information about the signer.
     */
    public String toString() {
	return "[Signer]" + super.toString();
    }

    private static void check(String directive) {
	SecurityManager security = System.getSecurityManager();
	if (security != null) {
	    security.checkSecurityAccess(directive);
	}
    }

}


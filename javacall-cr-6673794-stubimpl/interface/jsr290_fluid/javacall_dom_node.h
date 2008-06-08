/*
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
*/

#include <javacall_dom.h>

/**
 * Returns the name of this node, depending on its type; see 
 * <code>javacall_dom_node_types</code>. 
 * 
 * Note: If retValueLen is less then length of the returned string this function 
 *       has to return with JAVACALL_OUT_OF_MEMORY code and fill retValueLen 
 *       with actual length of the returned string.
 *
 * @param handle Pointer to the object representing this node.
 * @param retValue the name of this node
 * @param retValueLen Length of the returned string
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_OUT_OF_MEMORY if length of the returend string is more then 
 *                                specified in retValueLen,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_node_name(javacall_handle handle,
                                /* OUT */ javacall_utf16_string retValue,
                                /* INOUT */ javacall_uint32* retValueLen);

/**
 * Returns the value of this node, depending on its type; see 
 * <code>javacall_dom_node_types</code>. 
 * When it is defined to be <code>NULL</code>, setting it has no effect.
 * 
 * Note: If retValueLen is less then length of the returned string this function 
 *       has to return with JAVACALL_OUT_OF_MEMORY code and fill retValueLen 
 *       with actual length of the returned string.
 *
 * @param handle Pointer to the object representing this node.
 * @param retValue a String containing the value of this node
 * @param retValueLen Length of the returned string
 * @param exceptionCode Code of the error if function fails; 
 *                      see javacall_dom_exceptions 
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_OUT_OF_MEMORY if length of the returend string is more then 
 *                                specified in retValueLen,
 *         JAVACALL_FAIL if error occured; in this case exceptionCode has to be 
 *                                filled,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_node_value(javacall_handle handle,
                                 /* OUT */ javacall_utf16_string retValue,
                                 /* INOUT */ javacall_uint32* retValueLen,
                                 /* OUT */ javacall_dom_exceptions* exceptionCode);

/**
 * Sets the value of this node, depending on its type; see 
 * <code>javacall_dom_node_types</code>. 
 * When it is defined to be <code>NULL</code>, setting it has no effect,
 * including if the node is read-only.
 * 
 * @param handle Pointer to the object representing this node.
 * @param nodeValue the value of the node
 * @param exceptionCode Code of the error if function fails; 
 *                      see javacall_dom_exceptions 
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_FAIL if error occured; in this case exceptionCode has to be 
                                  filled,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_set_node_value(javacall_handle handle,
                                 javacall_const_utf16_string nodeValue,
                                 /* OUT */ javacall_dom_exceptions* exceptionCode);

/**
 * Returns a code representing the type of the underlying object, as defined in 
 * <code>javacall_dom_node_types</code>.
 * 
 * @param handle Pointer to the object representing this node.
 * @param retValue A code representing the type of the underlying object
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_node_type(javacall_handle handle,
                                /* OUT */ javacall_dom_node_types* retValue);

/**
 * Returns the parent of this node. All nodes, except <code>Attr</code>, 
 * <code>Document</code>, <code>DocumentFragment</code>, 
 * <code>Entity</code>, and <code>Notation</code> may have a parent. 
 * However, if a node has just been created and not yet added to the 
 * tree, or if it has been removed from the tree, this is 
 * <code>NULL</code>.
 * 
 * @param handle Pointer to the object representing this node.
 * @param retValue Pointer to the object representing 
 *   the parent of this node, or <code>NULL</code>
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_parent_node(javacall_handle handle,
                                  /* OUT */ javacall_handle* retValue);

/**
 * Returns a <code>NodeList</code> that contains all children of this node. If 
 * there are no children, this is a <code>NodeList</code> containing no 
 * nodes.
 * 
 * @param handle Pointer to the object representing this node.
 * @param retValue Pointer to the object representing 
 *   a <code>NodeList</code> that contains all children of this node.
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_child_nodes(javacall_handle handle,
                                  /* OUT */ javacall_handle* retValue);

/**
 * Returns the first child of this node. If there is no such node, this returns 
 * <code>NULL</code>.
 * 
 * @param handle Pointer to the object representing this node.
 * @param retValue Pointer to the object representing 
 *   the first child of this node or <code>NULL</code>
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_first_child(javacall_handle handle,
                                  /* OUT */ javacall_handle* retValue);

/**
 * Returns the last child of this node. If there is no such node, this returns 
 * <code>NULL</code>.
 * 
 * @param handle Pointer to the object representing this node.
 * @param retValue Pointer to the object representing 
 *   the last child of this node or <code>NULL</code>.
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_last_child(javacall_handle handle,
                                 /* OUT */ javacall_handle* retValue);

/**
 * Returns the node immediately preceding this node. If there is no such node, 
 * this returns <code>NULL</code>.
 * 
 * @param handle Pointer to the object representing this node.
 * @param retValue Pointer to the object representing 
 *   the node immediately preceding this node or <code>NULL</code>
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_previous_sibling(javacall_handle handle,
                                       /* OUT */ javacall_handle* retValue);

/**
 * Returns the node immediately following this node. If there is no such node, 
 * this returns <code>NULL</code>.
 * 
 * @param handle Pointer to the object representing this node.
 * @param retValue Pointer to the object representing 
 *   the node immediately following this node or <code>NULL</code>
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_next_sibling(javacall_handle handle,
                                   /* OUT */ javacall_handle* retValue);

/**
 * Returns a <code>NamedNodeMap</code> containing the attributes of this node (if 
 * it is an <code>Element</code>) or <code>NULL</code> otherwise. 
 * 
 * @param handle Pointer to the object representing this node.
 * @param retValue Pointer to the object representing 
 *   a <code>NamedNodeMap</code> containing the attributes of this node, or  <code>NULL</code>
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_attributes(javacall_handle handle,
                                 /* OUT */ javacall_handle* retValue);

/**
 * Returns the <code>Document</code> object associated with this node. This is 
 * also the <code>Document</code> object used to create new nodes. When 
 * this node is a <code>Document</code> or a <code>DocumentType</code> 
 * which is not used with any <code>Document</code> yet, this is 
 * <code>NULL</code>.
 * 
 * @param handle Pointer to the object representing this node.
 * @param retValue Pointer to the object representing 
 *   the <code>Document</code> object associated with this node, or <code>NULL</code>
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_owner_document(javacall_handle handle,
                                     /* OUT */ javacall_handle* retValue);

/**
 * Inserts the node <code>newChild</code> before the existing child node 
 * <code>refChild</code>. If <code>refChild</code> is <code>NULL</code>, 
 * insert <code>newChild</code> at the end of the list of children.
 * <br>If <code>newChild</code> is a <code>DocumentFragment</code> object, 
 * all of its children are inserted, in the same order, before 
 * <code>refChild</code>. If the <code>newChild</code> is already in the 
 * tree, it is first removed.
 * <p ><b>Note:</b>  Inserting a node before itself is implementation 
 * dependent. 
 * 
 * @param handle Pointer to the object representing this node.
 * @param newChild Pointer to the object of
 *   the node to insert.
 * @param refChild Pointer to the object of
 *   the reference node, i.e., the node before which the new 
 *   node must be inserted.
 * @param retValue Pointer to the object representing 
 *   the node being inserted.
 * @param exceptionCode Code of the error if function fails; 
 *                      see javacall_dom_exceptions 
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_FAIL if error occured; in this case exceptionCode has to be 
                                  filled,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_insert_before(javacall_handle handle,
                                javacall_handle newChild,
                                javacall_handle refChild,
                                /* OUT */ javacall_handle* retValue,
                                /* OUT */ javacall_dom_exceptions* exceptionCode);

/**
 * Replaces the child node <code>oldChild</code> with <code>newChild</code>
 *  in the list of children, and returns the <code>oldChild</code> node.
 * <br>If <code>newChild</code> is a <code>DocumentFragment</code> object, 
 * <code>oldChild</code> is replaced by all of the 
 * <code>DocumentFragment</code> children, which are inserted in the 
 * same order. If the <code>newChild</code> is already in the tree, it 
 * is first removed.
 * 
 * @param handle Pointer to the object representing this node.
 * @param newChild Pointer to the object of
 *   the new node to put in the child list.
 * @param oldChild Pointer to the object of
 *   the node being replaced in the list.
 * @param retValue Pointer to the object representing 
 *   the node replaced.
 * @param exceptionCode Code of the error if function fails; 
 *                      see javacall_dom_exceptions 
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_FAIL if error occured; in this case exceptionCode has to be 
                                  filled,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_replace_child(javacall_handle handle,
                                javacall_handle newChild,
                                javacall_handle oldChild,
                                /* OUT */ javacall_handle* retValue,
                                /* OUT */ javacall_dom_exceptions* exceptionCode);

/**
 * Removes the child node indicated by <code>oldChild</code> from the list 
 * of children, and returns it.
 * 
 * @param handle Pointer to the object representing this node.
 * @param oldChild Pointer to the object of
 *   the node being removed.
 * @param retValue Pointer to the object representing 
 *   the node removed.
 * @param exceptionCode Code of the error if function fails; 
 *                      see javacall_dom_exceptions 
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_FAIL if error occured; in this case exceptionCode has to be 
                                  filled,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_remove_child(javacall_handle handle,
                               javacall_handle oldChild,
                               /* OUT */ javacall_handle* retValue,
                               /* OUT */ javacall_dom_exceptions* exceptionCode);

/**
 * Adds the node <code>newChild</code> to the end of the list of children 
 * of this node. If the <code>newChild</code> is already in the tree, it 
 * is first removed.
 * 
 * @param handle Pointer to the object representing this node.
 * @param newChild Pointer to the object of
 *   the node to add.If it is a <code>DocumentFragment</code>
 *    object, the entire contents of the document fragment are moved 
 *   into the child list of this node
 * @param retValue Pointer to the object representing 
 *   the node added.
 * @param exceptionCode Code of the error if function fails; 
 *                      see javacall_dom_exceptions 
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_FAIL if error occured; in this case exceptionCode has to be 
                                  filled,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_append_child(javacall_handle handle,
                               javacall_handle newChild,
                               /* OUT */ javacall_handle* retValue,
                               /* OUT */ javacall_dom_exceptions* exceptionCode);

/**
 * Returns whether this node has any children.
 * 
 * @param handle Pointer to the object representing this node.
 * @param retValue  <code>true</code> if this node has any children, 
 *   <code>false</code> otherwise.
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_has_child_nodes(javacall_handle handle,
                                  /* OUT */ javacall_bool* retValue);

/**
 * Returns a duplicate of this node, i.e., serves as a generic copy 
 * constructor for nodes. The duplicate node has no parent (
 * <code>parentNode</code> is <code>NULL</code>) and no user data. User 
 * data associated to the imported node is not carried over. However, if 
 * any <code>UserDataHandlers</code> has been specified along with the 
 * associated data these handlers will be called with the appropriate 
 * parameters before this method returns.
 * <br>Cloning an <code>Element</code> copies all attributes and their 
 * values, including those generated by the XML processor to represent 
 * defaulted attributes, but this method does not copy any children it 
 * contains unless it is a deep clone. This includes text contained in 
 * an the <code>Element</code> since the text is contained in a child 
 * <code>Text</code> node. Cloning an <code>Attr</code> directly, as 
 * opposed to be cloned as part of an <code>Element</code> cloning 
 * operation, returns a specified attribute (<code>specified</code> is 
 * <code>true</code>). Cloning an <code>Attr</code> always clones its 
 * children, since they represent its value, no matter whether this is a 
 * deep clone or not. Cloning an <code>EntityReference</code> 
 * automatically constructs its subtree if a corresponding 
 * <code>Entity</code> is available, no matter whether this is a deep 
 * clone or not. Cloning any other type of node simply returns a copy of 
 * this node.
 * <br>Note that cloning an immutable subtree results in a mutable copy, 
 * but the children of an <code>EntityReference</code> clone are readonly
 * . In addition, clones of unspecified <code>Attr</code> nodes are 
 * specified. And, cloning <code>Document</code>, 
 * <code>DocumentType</code>, <code>Entity</code>, and 
 * <code>Notation</code> nodes is implementation dependent.
 * 
 * @param handle Pointer to the object representing this node.
 * @param deep If <code>true</code>, recursively clone the subtree under 
 *   the specified node; if <code>false</code>, clone only the node 
 *   itself (and its attributes, if it is an <code>Element</code>).
 * @param retValue Pointer to the object representing 
 *   the duplicate node.
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_clone_node(javacall_handle handle,
                             javacall_bool deep,
                             /* OUT */ javacall_handle* retValue);

/**
 * Puts all <code>Text</code> nodes in the full depth of the sub-tree 
 * underneath this <code>Node</code>, including attribute nodes, into a 
 * "normal" form where only structure (e.g., elements, comments, 
 * processing instructions, CDATA sections, and entity references) 
 * separates <code>Text</code> nodes, i.e., there are neither adjacent 
 * <code>Text</code> nodes nor empty <code>Text</code> nodes. This can 
 * be used to ensure that the DOM view of a document is the same as if 
 * it were saved and re-loaded, and is useful when operations (such as 
 * XPointer [<a href='http://www.w3.org/TR/2003/REC-xptr-framework-20030325/'>XPointer</a>]
 * lookups) that depend on a particular document tree 
 * structure are to be used. 
 * <p><b>Note:</b> In cases where the document contains 
 * <code>CDATASections</code>, the normalize operation alone may not be 
 * sufficient, since XPointers do not differentiate between 
 * <code>Text</code> nodes and <code>CDATASection</code> nodes.
 * 
 * @param handle Pointer to the object representing this node.
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_normalize(javacall_handle handle);

/**
 * Tests whether the DOM implementation implements a specific feature and 
 * that feature is supported by this node.
 * 
 * @param handle Pointer to the object representing this node.
 * @param feature The name of the feature to test. This is the same name 
 *   which can be passed to the method <code>hasFeature</code> on 
 *   <code>DOMImplementation</code>.
 * @param version This is the version number of the feature to test. In 
 *   Level 2, version 1, this is the string "2.0". If the version is not 
 *   specified, supporting any version of the feature will cause the 
 *   method to return <code>true</code>.
 * @param retValue Returns <code>true</code> if the specified feature is 
 *   supported on this node, <code>false</code> otherwise.
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_is_supported(javacall_handle handle,
                               javacall_const_utf16_string feature,
                               javacall_const_utf16_string version,
                               /* OUT */ javacall_bool* retValue);

/**
 * Returns the namespace URI of this node, or <code>NULL</code> if it is 
 * unspecified.
 * <br>This is not a computed value that is the result of a namespace 
 * lookup based on an examination of the namespace declarations in 
 * scope. It is merely the namespace URI given at creation time.
 * <br>For nodes of any type other than <code>ELEMENT_NODE</code> and 
 * <code>ATTRIBUTE_NODE</code> and nodes created with a DOM Level 1 
 * method, such as <code>Document.createElement</code>, this is always
 * <code>NULL</code>.
 * <p><b>Note:</b> Per the <em>Namespaces in XML</em> Specification [<a href='http://www.w3.org/TR/1999/REC-xml-names-19990114/'>XML Namespaces</a>]
 *  an attribute does not inherit 
 * its namespace from the element it is attached to. If an attribute is 
 * not explicitly given a namespace, it simply has no namespace.
 * 
 * Note: If retValueLen is less then length of the returned string this function 
 *       has to return with JAVACALL_OUT_OF_MEMORY code and fill retValueLen 
 *       with actual length of the returned string.
 *
 * @param handle Pointer to the object representing this node.
 * @param retValue  The namespace URI of this node, or <code>NULL</code>
 * @param retValueLen Length of the returned string
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_OUT_OF_MEMORY if length of the returend string is more then 
 *                                specified in retValueLen,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_namespace_uri(javacall_handle handle,
                                    /* OUT */ javacall_utf16_string retValue,
                                    /* INOUT */ javacall_uint32* retValueLen);

/**
 * Returns the namespace prefix of this node, or <code>NULL</code> if it is 
 * unspecified. When it is defined to be <code>NULL</code>, setting it 
 * has no effect, including if the node is read-only.
 * <br>Note that setting this attribute, when permitted, changes the 
 * <code>nodeName</code> attribute, which holds the qualified name, as 
 * well as the <code>tagName</code> and <code>name</code> attributes of 
 * the <code>Element</code> and <code>Attr</code> interfaces, when 
 * applicable.
 * <br>Setting the prefix to <code>NULL</code> makes it unspecified, 
 * setting it to an empty string is implementation dependent.
 * <br>Note also that changing the prefix of an attribute that is known to 
 * have a default value, does not make a new attribute with the default 
 * value and the original prefix appear, since the 
 * <code>namespaceURI</code> and <code>localName</code> do not change.
 * <br>For nodes of any type other than <code>ELEMENT_NODE</code> and 
 * <code>ATTRIBUTE_NODE</code> and nodes created with a DOM Level 1 
 * method, such as <code>createElement</code> from the 
 * <code>Document</code> interface, this is always <code>NULL</code>.
 * 
 * Note: If retValueLen is less then length of the returned string this function 
 *       has to return with JAVACALL_OUT_OF_MEMORY code and fill retValueLen 
 *       with actual length of the returned string.
 *
 * @param handle Pointer to the object representing this node.
 * @param retValue The namespace prefix of this node, or <code>NULL</code>
 * @param retValueLen Length of the returned string
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_OUT_OF_MEMORY if length of the returend string is more then 
 *                                specified in retValueLen,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_prefix(javacall_handle handle,
                             /* OUT */ javacall_utf16_string retValue,
                             /* INOUT */ javacall_uint32* retValueLen);

/**
 * Sets the namespace prefix of this node, or <code>NULL</code> if it is 
 * unspecified. When it is defined to be <code>NULL</code>, setting it 
 * has no effect, including if the node is read-only. 
 * <i>Note: This means that the value of the prefix is not changed
 * and no exception is thrown in the case where the node is read-only.</i>
 * <br>Note that setting this attribute, when permitted, changes the 
 * <code>nodeName</code> attribute, which holds the qualified name, as 
 * well as the <code>tagName</code> and <code>name</code> attributes of 
 * the <code>Element</code> and <code>Attr</code> interfaces, when 
 * applicable.
 * <br>Setting the prefix to <code>NULL</code> makes it unspecified, 
 * setting it to an empty string is implementation dependent.
 * <br>Note also that changing the prefix of an attribute that is known to 
 * have a default value, does not make a new attribute with the default 
 * value and the original prefix appear, since the 
 * <code>namespaceURI</code> and <code>localName</code> do not change.
 * <i>Note: The prefix will thereby not be changed.</i>
 * <br>For nodes of any type other than <code>ELEMENT_NODE</code> and 
 * <code>ATTRIBUTE_NODE</code> and nodes created with a DOM Level 1 
 * method, such as <code>createElement</code> from the 
 * <code>Document</code> interface, this is always <code>NULL</code>.
 * <i>Note: This also means that the implementation will not throw any
 * exception.</i>
 * 
 * 
 * @param handle Pointer to the object representing this node.
 * @param prefix This node namespace prefix.
 * @param exceptionCode Code of the error if function fails; 
 *                      see javacall_dom_exceptions 
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_FAIL if error occured; in this case exceptionCode has to be 
                                  filled,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_set_prefix(javacall_handle handle,
                             javacall_const_utf16_string prefix,
                             /* OUT */ javacall_dom_exceptions* exceptionCode);

/**
 * Returns returns the local part of the qualified name of this node.
 * <br>For nodes of any type other than <code>ELEMENT_NODE</code> and 
 * <code>ATTRIBUTE_NODE</code> and nodes created with a DOM Level 1 
 * method, such as <code>Document.createElement</code>, this is always
 * <code>NULL</code>.
 * 
 * Note: If retValueLen is less then length of the returned string this function 
 *       has to return with JAVACALL_OUT_OF_MEMORY code and fill retValueLen 
 *       with actual length of the returned string.
 *
 * @param handle Pointer to the object representing this node.
 * @param retValue the local part of the qualified name of this node
 * @param retValueLen Length of the returned string
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_OUT_OF_MEMORY if length of the returend string is more then 
 *                                specified in retValueLen,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_local_name(javacall_handle handle,
                                 /* OUT */ javacall_utf16_string retValue,
                                 /* INOUT */ javacall_uint32* retValueLen);

/**
 * Returns whether this node (if it is an element) has any attributes.
 * 
 * @param handle Pointer to the object representing this node.
 * @param retValue <code>true</code> if this node has any attributes, 
 *   <code>false</code> otherwise.
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_has_attributes(javacall_handle handle,
                                 /* OUT */ javacall_bool* retValue);

/**
 * Returns this attribute returns the text content of this node and its 
 * descendants. When it is defined to be <code>NULL</code>, setting it 
 * has no effect. On setting, any possible children this node may have 
 * are removed and, if it the new string is not empty or 
 * <code>NULL</code>, replaced by a single <code>Text</code> node 
 * containing the string this attribute is set to. 
 * <br> On getting, no serialization is performed, the returned string 
 * does not contain any markup. No whitespace normalization is performed 
 * and the returned string does not contain the white spaces in element 
 * content (see the attribute 
 * <code>Text.isElementContentWhitespace</code>). Similarly, on setting, 
 * no parsing is performed either, the input string is taken as pure 
 * textual content. 
 * <br>The string returned is made of the text content of this node 
 * depending on its type, as defined below: 
 * <table border='1' cellpadding='3'>
 * <tr>
 * <th>Node type</th>
 * <th>Content</th>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>
 * ELEMENT_NODE, ATTRIBUTE_NODE, ENTITY_NODE, ENTITY_REFERENCE_NODE, 
 * DOCUMENT_FRAGMENT_NODE</td>
 * <td valign='top' rowspan='1' colspan='1'>concatenation of the <code>textContent</code> 
 * attribute value of every child node, excluding COMMENT_NODE and 
 * PROCESSING_INSTRUCTION_NODE nodes. This is the empty string if the 
 * node has no children.</td>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>TEXT_NODE, CDATA_SECTION_NODE, COMMENT_NODE, 
 * PROCESSING_INSTRUCTION_NODE</td>
 * <td valign='top' rowspan='1' colspan='1'><code>nodeValue</code></td>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>DOCUMENT_NODE, 
 * DOCUMENT_TYPE_NODE, NOTATION_NODE</td>
 * <td valign='top' rowspan='1' colspan='1'><em>NULL</em></td>
 * </tr>
 * </table>
 * 
 * Note: If retValueLen is less then length of the returned string this function 
 *       has to return with JAVACALL_OUT_OF_MEMORY code and fill retValueLen 
 *       with actual length of the returned string.
 *
 * @param handle Pointer to the object representing this node.
 * @param retValue a String containing the text content of this node and its descendants
 * @param retValueLen Length of the returned string
 * @param exceptionCode Code of the error if function fails; 
 *                      see javacall_dom_exceptions 
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_OUT_OF_MEMORY if length of the returend string is more then 
 *                                specified in retValueLen,
 *         JAVACALL_FAIL if error occured; in this case exceptionCode has to be 
 *                                filled,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_text_content(javacall_handle handle,
                                   /* OUT */ javacall_utf16_string retValue,
                                   /* INOUT */ javacall_uint32* retValueLen,
                                   /* OUT */ javacall_dom_exceptions* exceptionCode);

/**
 * Sets this attribute returns the text content of this node and its 
 * descendants. When it is defined to be <code>NULL</code>, setting it 
 * has no effect. On setting, any possible children this node may have 
 * are removed and, if it the new string is not empty or 
 * <code>NULL</code>, replaced by a single <code>Text</code> node 
 * containing the string this attribute is set to. 
 * <br> On getting, no serialization is performed, the returned string 
 * does not contain any markup. No whitespace normalization is performed 
 * and the returned string does not contain the white spaces in element 
 * content (see the attribute 
 * <code>Text.isElementContentWhitespace</code>). Similarly, on setting, 
 * no parsing is performed either, the input string is taken as pure 
 * textual content. 
 * <br>The string returned is made of the text content of this node 
 * depending on its type, as defined below: 
 * <table border='1' cellpadding='3'>
 * <tr>
 * <th>Node type</th>
 * <th>Content</th>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>
 * ELEMENT_NODE, ATTRIBUTE_NODE, ENTITY_NODE, ENTITY_REFERENCE_NODE, 
 * DOCUMENT_FRAGMENT_NODE</td>
 * <td valign='top' rowspan='1' colspan='1'>concatenation of the <code>textContent</code> 
 * attribute value of every child node, excluding COMMENT_NODE and 
 * PROCESSING_INSTRUCTION_NODE nodes. This is the empty string if the 
 * node has no children.</td>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>TEXT_NODE, CDATA_SECTION_NODE, COMMENT_NODE, 
 * PROCESSING_INSTRUCTION_NODE</td>
 * <td valign='top' rowspan='1' colspan='1'><code>nodeValue</code></td>
 * </tr>
 * <tr>
 * <td valign='top' rowspan='1' colspan='1'>DOCUMENT_NODE, 
 * DOCUMENT_TYPE_NODE, NOTATION_NODE</td>
 * <td valign='top' rowspan='1' colspan='1'><em>NULL</em></td>
 * </tr>
 * </table>
 * 
 * @param handle Pointer to the object representing this node.
 * @param textContent a String containing the new text content for this node
 * @param exceptionCode Code of the error if function fails; 
 *                      see javacall_dom_exceptions 
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_FAIL if error occured; in this case exceptionCode has to be 
                                  filled,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_set_text_content(javacall_handle handle,
                                   javacall_const_utf16_string textContent,
                                   /* OUT */ javacall_dom_exceptions* exceptionCode);

/**
 * Returns  This method returns a specialized object which implements the 
 * specialized APIs of the specified feature and version, as specified 
 * in <a href="http://www.w3.org/TR/DOM-Level-3-Core/core.html#DOMFeatures"> 
 * DOM Features</a>. The specialized object may also be obtained by using 
 * binding-specific casting methods but is not necessarily expected to, 
 * as discussed in <a href="http://www.w3.org/TR/DOM-Level-3-Core/core.html#Embedded-DOM">
 * Mixed DOM Implementations</a>. This method also allows the implementation 
 * to provide specialized objects which do not support the <code>Node</code>
 * interface. 
 * <p><b>Note:</b> when using the methods that take a feature and a 
 * version as parameters, applications can use NULL or empty string 
 * for the version parameter if they don't wish to specify a particular
 * version for the specified feature.
 * 
 * @param handle Pointer to the object representing this node.
 * @param feature  The name of the feature requested. Note that any plus 
 *   sign "+" prepended to the name of the feature will be ignored since 
 *   it is not significant in the context of this method. 
 * @param version  This is the version number of the feature to test. 
 * @param retValue Pointer to the object representing 
 *    Returns an object which implements the specialized APIs of 
 *   the specified feature and version, if any, or <code>NULL</code> if 
 *   there is no object which implements interfaces associated with that 
 *   feature. If the <code>DOMObject</code> returned by this method 
 *   implements the <code>Node</code> interface, it must delegate to the 
 *   primary core <code>Node</code> and not return results inconsistent 
 *   with the primary core <code>Node</code> such as attributes, 
 *   childNodes, etc. 
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_get_feature(javacall_handle handle,
                              javacall_const_utf16_string feature,
                              javacall_const_utf16_string version,
                              /* OUT */ javacall_handle* retValue);

/** 
 * Deletes object representing this node
 * 
 * @param handle Pointer to the object representing this node.
 * 
 * @return JAVACALL_OK if all done successfuly,
 *         JAVACALL_NOT_IMPLEMENTED when the stub was called
 */
javacall_result
javacall_dom_node_finalize(javacall_handle handle);


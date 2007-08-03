/*
 *
 *
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

/*
 * SUBSYSTEM: WMA (Wireless Messaging API)
 * FILE:      cbsProtocol.c
 * OVERVIEW:  This file handles WMA functions such as:
 *            - Opening a CBS connection.
 *            - Closing a CBS connection.
 *            - Receiving a CBS message.
 */

//#include <sys/types.h>
#include <string.h>
#include <errno.h>
#include <kni.h>

#include <sni.h>
#include <app_package.h>

#ifdef ENABLE_MIDP
#include <midpServices.h>
#include <commonKNIMacros.h>
#include <ROMStructs.h>
#include <midpError.h>
#include <midp_properties_port.h>
#include <midp_logging.h>
#include <midpResourceLimit.h>
#endif

#ifdef ENABLE_CDC
#ifdef ENABLE_PCSL
#include <pcsl_memory.h>
#else
  #define pcsl_mem_malloc malloc
  #define pcsl_mem_free free
#endif
#endif

#ifdef ENABLE_CDC
#define JSR120_KNI_LAYER
#ifdef JSR_120_ENABLE_JUMPDRIVER
#include <jsr120_jumpdriver.h>
#include <JUMPEvents.h>
#endif
#endif

#ifdef ENABLE_MIDP
  #include "midpError.h"
#else
  static const char* const midpOutOfMemoryError = "java/lang/OutOfMemoryError";
  static const char* const midpIOException = "java/io/IOException";
  static const char* const midpRuntimeException = "java/lang/RuntimeException";
  static const char* const midpIllegalArgumentException = "java/lang/IllegalArgumentException";
#endif
	

/* CBS protocol, native layer APIs and message pool APIs */
#include <jsr120_cbs_protocol.h>
#include <jsr120_cbs_pool.h>
#include <jsr120_cbs_listeners.h>

/*
 * Protocol methods
 */
#ifndef ENABLE_CDC
static void wma_setBlockedCBSHandle(int msgID, int eventType);
#endif

unsigned char cbsBuffer[sizeof(CbsMessage)];

/** Close flag. */
static int isClosed = 0;


/**
 * Open a CBS connection.
 *
 * @param msgID The message ID to be matched against incoming messages.
 * Can be <code>null</code> for unblock sending and receiving messages.
 * @param msid The MIDlet suite ID string.
 *
 * @return A handle to the open CBS connection.
 */
KNIEXPORT KNI_RETURNTYPE_INT
KNIDECL(com_sun_midp_io_j2me_cbs_Protocol_open0) {

    /* The ID to be matched against incoming messages. */
    int msgID;

    /* The midlet suite name for this connection. */
    AppIdType msid = UNUSED_APP_ID;

    /* The handle associated with this CBS connection. */
    jint handle = 0;

    /* The status from registering a listener with the message ID. */
    WMA_STATUS status = WMA_ERR;

    /* Set closed flag to false */
    isClosed = 0;

    /* Pick up the CBS message ID. */
    msgID = KNI_GetParameterAsInt(1);

    /* When msgID is 0 then return else continue */
    if (msgID) {
        /* Pick up the Midlet Suite ID */
        msid = KNI_GetParameterAsInt(2);

        do {
            /* Register the message ID with the message pool. */
            if (jsr120_cbs_is_midlet_msgID_registered((jchar)msgID) == WMA_ERR) {

                /* Fetch a unique handle that identifies this CBS communication session. */
#ifndef ENABLE_CDC
                handle = (int)(pcsl_mem_malloc(1));
#else
#ifdef JSR_120_ENABLE_JUMPDRIVER
                handle = (int)jumpEventCreate();
#endif
#endif
                if (handle == 0) {
                    KNI_ThrowNew(midpOutOfMemoryError,
                        "Unable to start CBS.");
                    break;
                }

                status = jsr120_cbs_register_midlet_msgID((jchar)msgID, msid,
                                                          handle);
                if (status == WMA_ERR) {
                    KNI_ThrowNew(midpIOException, "Port already in use.");
                    break;
                }

            } else {
                KNI_ThrowNew(midpIOException, "Port already in use.");
                break;
            }
        } while (0);
    }

    KNI_ReturnInt(handle);
}

/**
 * Close a CBS connection.
 *
 * @param msgID The message ID associated with this connection.
 * @param handle The handle of the open CBS message connection.
 * @param deRegister Deregistration msgID when parameter is 1.
 *
 * @return <code>0</code> if successful, <code>-1</code> failed.
 */
KNIEXPORT KNI_RETURNTYPE_INT
KNIDECL(com_sun_midp_io_j2me_cbs_Protocol_close0) {
    int status = 0;
    jint msgID = 0;
    jint handle = 0;

    /** Deregistration flag. */
    int deRegister;

    /* Set closed flag to true */
    isClosed = 1;

    msgID = KNI_GetParameterAsInt(1);
    handle = KNI_GetParameterAsInt(2);
    deRegister = KNI_GetParameterAsInt(3);

    if (msgID > 0 && handle != 0) {

#ifndef ENABLE_CDC
        /* Unblock any blocked threads. */
        jsr120_cbs_unblock_thread((int)handle, WMA_CBS_READ_SIGNAL);
#else
#ifdef JSR_120_ENABLE_JUMPDRIVER
        jumpEventHappens((JUMPEvent)handle);
#endif
#endif

        if (deRegister) {
            /* Unregister the CBS port from the CBS pool. */
            jsr120_cbs_unregister_midlet_msgID((jchar)msgID);

            /* Release the handle associated with this connection. */
#ifndef ENABLE_CDC
            pcsl_mem_free((void *)handle);
#else
#ifdef JSR_120_ENABLE_JUMPDRIVER
            jumpEventDestroy((JUMPEvent)handle);
#endif
#endif
        }

    }

    KNI_ReturnInt(status);
}

/**
 * Receive a CBS message.
 *
 * @param msgID The message ID to be matched against incoming messages.
 * @param handle The handle to the open CBS message connection.
 * @param messageObject The Java message object to be populated.
 *
 * @return The length of the incoming message, or <code>-1</code>, if a message
 *         could not be received.
 */
KNIEXPORT KNI_RETURNTYPE_INT
KNIDECL(com_sun_midp_io_j2me_cbs_Protocol_receive0) {
#ifndef ENABLE_CDC
    MidpReentryData* info = (MidpReentryData*)SNI_GetReentryData(NULL);
#endif

    /* The CBS message identifier */
    jint msgID;

    /* The handle associated with this CBS connection. */
    jint handle = 0;

    /* The message object from the pool. */
    CbsMessage* pCbsData = NULL;

    /* Assume a message could not be received. */
    int messageLength = -1;

    /* The midlet suite name for this connection. */
    AppIdType msid = UNUSED_APP_ID;

    if (!isClosed) { /* No close in progress */
        KNI_StartHandles(3);

        /* This is the handle to the serialized CBSPacket class fields. */
        KNI_DeclareHandle(messageClazz);

        /* These are handles to Java fields that will be populated. */
        KNI_DeclareHandle(messageObject);
        KNI_DeclareHandle(byteArray);

        msgID = KNI_GetParameterAsInt(1);
        msid = KNI_GetParameterAsInt(2);

        handle = KNI_GetParameterAsInt(3);
        KNI_GetParameterAsObject(4, messageObject);

        do {
            /* If this is the first time, peek into the pool for a message. */
#ifndef ENABLE_CDC
            if (info == NULL) {
#endif
                pCbsData = jsr120_cbs_pool_peek_next_msg((jchar)msgID);

                /*
                 * If there is no message, register the connection, if it hasn't
                 * already been registered. Then block and wait for a message to
                 * appear in the pool.
                 */
                if (pCbsData == NULL) {

#ifndef ENABLE_CDC
                    /* Wait for a message to arrive in the pool. */
                    wma_setBlockedCBSHandle(handle, WMA_CBS_READ_SIGNAL);
#else
#ifdef JSR_120_ENABLE_JUMPDRIVER
        CVMD_gcSafeExec(_ee, {
                    if (jumpEventWait((JUMPEvent)handle) == 0) {
                        pCbsData = jsr120_cbs_pool_peek_next_msg((jchar)msgID);
                    }
        }); 
#endif
#endif
                }
#ifndef ENABLE_CDC
            } else {

                /* Re-entry */
                pCbsData = jsr120_cbs_pool_peek_next_msg((jchar)msgID);
            }
#endif
            /*
             * If a message is waiting, go through the steps of fetching the message
             * and processing its contents.
             */
            if (pCbsData != NULL) {

                /* Fetch message data, while clearing pool entry to make room. */
                if ((pCbsData = jsr120_cbs_pool_retrieve_next_msg(msgID)) != NULL) {

                    /*
                     * A message has been retrieved successfully. Notify
                     * the platform.
                     */
                    jsr120_notify_incoming_cbs(pCbsData->encodingType,
                                               pCbsData->msgID,
                                               pCbsData->msgBuffer,
                                               pCbsData->msgLen);

                    /*
                     * At this point, the message entry from the pool has been removed,
                     * while the message data have been preserved here, for further
                     * processing.
                     *
                     * The object is converted to class form in order to gain access to
                     * the fields within the class.
                     */
                    KNI_GetObjectClass(messageObject, messageClazz);
                    if(KNI_IsNullHandle(messageClazz)) {
                        KNI_ThrowNew(midpOutOfMemoryError, NULL);
                        break;
                    } else {

                        jfieldID encodingType_field_id;
                        jfieldID msgID_field_id;
                        jfieldID message_field_id;

                        /* Create the Java field references. */
                        encodingType_field_id =
                            KNI_GetFieldID(messageClazz, "encodingType","I");
                        msgID_field_id =
                            KNI_GetFieldID(messageClazz, "msgID","I");
                        message_field_id =
                            KNI_GetFieldID(messageClazz, "message","[B");

                        /* Make sure all fields exist before populating them. */
                        if ((encodingType_field_id == 0) ||
                            (msgID_field_id == 0) ||
                            (message_field_id == 0)) {

                            /* REPORT_ERROR(LC_WMA, "ERROR can't get class field ID");*/
                            KNI_ThrowNew(midpRuntimeException, NULL);
                            break;

                        } else {

                            /*
                             * Populate the Java fields.
                             */
                            KNI_SetIntField(messageObject, encodingType_field_id,
                                pCbsData->encodingType);
                            KNI_SetIntField(messageObject, msgID_field_id,
                                pCbsData->msgID);

                            messageLength = pCbsData->msgLen;
                            /* If there's message data, make a copy of it. */
                            if (pCbsData->msgLen > 0) {
                                int i;

                                /* Make a copy of all message bytes. */
                                SNI_NewArray(SNI_BYTE_ARRAY, messageLength, byteArray);
                                if (KNI_IsNullHandle(byteArray)) {
                                    KNI_ThrowNew(midpOutOfMemoryError, NULL);
                                    break;
                                } else {
                                    for (i = 0; i < messageLength; i++) {
                                       KNI_SetByteArrayElement(byteArray, i,
                                           pCbsData->msgBuffer[i]);
                                    }
                                    KNI_SetObjectField(messageObject, message_field_id,
                                        byteArray);
                                }
                            }
                        }

                    }  /* if message data could be retrieved and processed. */

                }  /* if peeking indicated that a message is in the pool. */

            }  /* if message data exist in the pool. */
        } while (0);

        /* Delete the message data. */
        jsr120_cbs_delete_msg(pCbsData);

        KNI_EndHandles();
    }

    KNI_ReturnInt(messageLength);
}


/**
 * Wait for a message to become available.
 *
 * @param msgID The message ID to be matched against incoming CBS messages.
 * @param handle The handle to the open CBS message connection.
 *
 * @return The length of the incoming CBS message, or <code>-1</code> if no
 *     message is available.
 */
KNIEXPORT KNI_RETURNTYPE_INT
KNIDECL(com_sun_midp_io_j2me_cbs_Protocol_waitUntilMessageAvailable0) {
#ifndef ENABLE_CDC
    MidpReentryData *info = (MidpReentryData*)SNI_GetReentryData(NULL);
#endif

    int msgID;
    int handle;
    int messageLength = -1;

    /* Pointer to CBS message data. */
    CbsMessage* pCbsData = NULL;

    if (!isClosed) { /* No close in progress */
        msgID = KNI_GetParameterAsInt(1);
        handle = KNI_GetParameterAsInt(2);

        if (msgID == 0) {
            /*
             * A receive thread wouldn't have been started with a msgID
             * of 0. But a connection can be closed and msgID set to 0,
             * just before this method is called.
             * The receiverThread uses the IllegalArgumentException to
             * check for this situation and gracefully terminates.
             */
            KNI_ThrowNew(midpIllegalArgumentException, "No message ID available.");
        } else {

            /* See if there is a new message in the pool. */
            pCbsData = jsr120_cbs_pool_peek_next_msg1(msgID, 1);
            if (pCbsData != NULL) {
                 messageLength = pCbsData->msgLen;
            } else {

#ifndef ENABLE_CDC
                if (!info) {
                     /* Block and wait for a message. */
                     wma_setBlockedCBSHandle(handle, WMA_CBS_READ_SIGNAL);
#else
#ifdef JSR_120_ENABLE_JUMPDRIVER
        CVMD_gcSafeExec(_ee, {
                    if (jumpEventWait((JUMPEvent)handle) != 0) {
                        messageLength = -1;
                    } else {
                        pCbsData = jsr120_cbs_pool_peek_next_msg1(msgID, 1);
                        if (pCbsData != NULL) {
                            messageLength = pCbsData->msgLen;
                        }
                    }
        }); 
#endif
#endif

#ifndef ENABLE_CDC
                } else {
                     /* May have been awakened due to interrupt. */
                     messageLength = -1;
                }
#endif
            }
        }
    }
    KNI_ReturnInt(messageLength);
}

#ifndef ENABLE_CDC
/**
 * Marks an open connection as being blocked on a CBS operation. The
 * blocked status of the connection is stored in the current Java
 * thread.
 *
 * @param msgID The ID to be matched in addition to the event type.
 * @param eventType The type of event to trigger an unblock.
 */
static void
wma_setBlockedCBSHandle(int msgID, int eventType) {
    /* Pick up the event data record. */
    MidpReentryData* eventInfo = (MidpReentryData*)SNI_GetReentryData(NULL);
    if (eventInfo == NULL) {
        eventInfo = (MidpReentryData*)SNI_AllocateReentryData(
                                          sizeof(MidpReentryData));
    }

    /* Populate the event information fields with the latest event data. */
    eventInfo->waitingFor = eventType;
    eventInfo->descriptor = msgID;
    eventInfo->status = 0;

    /* No data available. Try again, later. */
    SNI_BlockThread();
}
#endif

/**
 * Native finalization of the Java object
 *
 * CLASS:    com.sun.midp.io.j2me.cbs.Protocol
 * TYPE:     virtual native function
 * INTERFACE (operand stack manipulation):
 */
KNIEXPORT KNI_RETURNTYPE_VOID
KNIDECL(com_sun_midp_io_j2me_cbs_Protocol_finalize) {

    KNI_StartHandles(1);
    KNI_DeclareHandle(instance);

    KNI_GetThisPointer(instance);

//    REPORT_ERROR(LC_WMA, "Stubbed out: Java_com_sun_midp_io_j2me_cbs_Protocol_finalize");

    KNI_EndHandles();

    KNI_ReturnVoid();
}


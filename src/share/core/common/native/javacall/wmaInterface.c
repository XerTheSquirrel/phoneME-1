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

/**
 * @file
 *
 * JSR120 interface methods.
 */

#include <jsr120_sms_protocol.h>
#include <jsr120_cbs_protocol.h>
#include <jsr120_sms_listeners.h>
#if (ENABLE_JSR_205)
#include <jsr205_mms_listeners.h>
#include <jsr205_mms_protocol.h>
#endif

#include "javacall_sms.h"

#include <wmaInterface.h>
#ifdef ENABLE_MIDP
  #include <push_server_resource_mgmt.h>
  #include <midpServices.h>
#endif
#include <stdio.h>
#include <string.h>

#ifdef ENABLE_CDC
  #include "jsr120_signals.h"
#endif

/**
 * Defined in wmaUDPEmulator.c
 * No need in current implementation.
 */
WMA_STATUS init_jsr120() {
#ifdef ENABLE_CDC
    jsr120_init_signal();
#endif
    javacall_wma_init();
    return WMA_NET_SUCCESS;
}

void javanotify_incoming_sms(
        javacall_sms_encoding   msgType,
        char*                   sourceAddress,
        unsigned char*          msgBuffer,
        int                     msgBufferLen,
        unsigned short          sourcePortNum,
        unsigned short          destPortNum,
        javacall_int64          timeStamp) {

    SmsMessage* sms = jsr120_sms_new_msg(
        msgType, sourceAddress, sourcePortNum, destPortNum, timeStamp, msgBufferLen, msgBuffer);
    jsr120_sms_pool_add_msg(sms);
}

/**
 * Defined in wmaUDPEmulator.c
 * No need in current implementation.
 */
void finalize_jsr120() {
#ifdef ENABLE_CDC
    jsr120_finalize_signal();
#endif
    javacall_wma_close();
}

#ifdef ENABLE_MIDP
// see below
static int checkfilter(char *filter, char *ip);
#else
int check_push_filter(int port, char* senderPhone);
#endif

/**
 * Defined in wmaSocket.c
 * Called from midp loop on WMA signal.
 */
jboolean jsr120_check_signal(midpSignalType signalType, int fd) {
    char *filter = NULL;

    switch (signalType) {
    case WMA_SMS_READ_SIGNAL:
    {
        SmsMessage* sms = (SmsMessage*)fd;
#ifdef ENABLE_MIDP
        filter = pushgetfilter("sms://:", sms->destPortNum);
        if (filter == NULL || checkfilter(filter, sms->msgAddr)) {
            pushsetcachedflag("sms://:", sms->destPortNum);
            jsr120_sms_pool_add_msg(sms);
        }
#else
        if (check_push_filter(sms->destPortNum, sms->msgAddr)) {
            jsr120_sms_pool_add_msg(sms);
        }
#endif
        return KNI_TRUE;
    }
    case WMA_SMS_WRITE_SIGNAL:
        jsr120_sms_message_sent_notifier();
        return KNI_TRUE;
    case WMA_CBS_READ_SIGNAL:
    {
        CbsMessage* cbs = (CbsMessage*)fd;
        jsr120_cbs_pool_add_msg(cbs);
        return KNI_TRUE;
    }
#if (ENABLE_JSR_205)
    case WMA_MMS_READ_SIGNAL:
    {
        MmsMessage* mms = (MmsMessage*)fd;
#ifdef ENABLE_MIDP
        filter = pushgetfiltermms("mms://:", mms->appID);
        if (filter == NULL || checkfilter(filter, mms->replyToAppID)) {
            pushsetcachedflagmms("mms://:", mms->appID);
            if (jsr205_fetch_mms() == WMA_OK) {
                jsr205_mms_pool_add_msg(mms);
            }
        }
#else
	jsr205_mms_pool_add_msg(mms);
#endif
        return KNI_TRUE;
    }
    case WMA_MMS_WRITE_SIGNAL:
        jsr205_mms_message_sent_notifier();
    	return KNI_TRUE;
#endif
    }

    return KNI_FALSE;
}

/**
 * check the SMS header against the push filter.
 * @param filter The filter string to be used
 * @param cmsidn The caller's MSIDN number to be tested by the filter
 * @return <code>1</code> if the comparison is successful; <code>0</code>,
 *     otherwise.
 */
static int checkfilter(char *filter, char *cmsidn) {
    char *p1 = NULL;
    char *p2 = NULL;

#ifdef ENABLE_MIDP
#if REPORT_LEVEL <= LOG_INFORMATION
    if (filter != NULL && cmsidn != NULL) {
        reportToLog(LOG_INFORMATION, LC_PROTOCOL,
                    "in checkfilter[%s , %s]",
                    filter, cmsidn);
    }
#endif
#endif
    if ((cmsidn == NULL) || (filter == NULL)) return 0;

    /* Filter is exactly "*", then all MSIDN numbers are allowed. */
    if (strcmp(filter, "*") == 0) return 1;

    /*
     * Otherwise walk through the filter string looking for character
     * matches and wildcard matches.
     * The filter pointer is incremented in the main loop and the
     * MSIDN pointer is incremented as characters and wildcards
     * are matched. Checking continues until there are no more filter or
     * MSIDN characters available.
     */
    for (p1=filter, p2=cmsidn; *p1 && *p2; p1++) {
        /*
         * For an asterisk, consume all the characters up to
         * a matching next character.
         */
        if (*p1 == '*') {
            /* Initialize the next two filter characters. */
            char f1 = *(p1+1);
            char f2 = '\0';
            if (f1 != '\0') {
                f2 = *(p1+2);
            }

            /* Skip multiple wild cards. */
            if (f1 == '*') {
                continue;
            }

            /*
             * Consume all the characters up to a match of the next
             * character from the filter string. Stop consuming
             * characters, if the address is fully consumed.
             */
            while (*p2) {
                /*
                 * When the next character matches, check the second character
                 * from the filter string. If it does not match, continue
                 * consuming characters from the address string.
                 */
                if(*p2 == f1 || f1 == '?') {
                    if (*(p2+1) == f2 || f2 == '?' || f2 == '*') {
                        /* Always consume an address character. */
                        p2++;
                        if (f2 != '?' || *(p2+1) == '.' || *(p2+1) == '\0') {
                            /* Also, consume a filter character. */
                            p1++;
                        }
                        break;
                    }
                }
                p2++;
            }
        } else if (*p1 == '?') {
            p2 ++;
        } else if (*p1 != *p2) {
            /* If characters do not match, filter failed. */
            return 0;
        } else {
            p2 ++;
 	}
    }

    if (!(*p1)  && !(*p2) ) {
        /* 
         * All available filter and MSIDN characters were checked.
         */
        return 1;
    } else {
        /*
         * Mismatch in length of filter and MSIDN string
         */
        return 0;
    }
}

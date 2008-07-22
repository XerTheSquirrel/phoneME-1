/*
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

#include "KNICommon.h"
#include "SNI.h"

#include "jsrop_exceptions.h"

/*  private native int nGetWidth ( int handle ) ; */
KNIEXPORT KNI_RETURNTYPE_INT
KNIDECL(com_sun_mmedia_DirectPlayer_nGetWidth) {

    jint handle = KNI_GetParameterAsInt(1);
    jint returnValue = 0;
    long width;
    long height;
    KNIPlayerInfo* pKniInfo = (KNIPlayerInfo*)handle;

    if (pKniInfo && pKniInfo->pNativeHandle &&
        JAVACALL_OK == javacall_media_get_video_size(pKniInfo->pNativeHandle, &width, &height)) {
        returnValue = width;
    }

    MMP_DEBUG_STR2("[kni_video] -nGetWidth %d ret %d\n", width, returnValue);

    KNI_ReturnInt(returnValue);
}

/*  private native int nGetHeight ( int handle ) ; */
KNIEXPORT KNI_RETURNTYPE_INT
KNIDECL(com_sun_mmedia_DirectPlayer_nGetHeight) {

    jint handle = KNI_GetParameterAsInt(1);
    jint returnValue = 0;
    long width;
    long height;
    KNIPlayerInfo* pKniInfo = (KNIPlayerInfo*)handle;

    if (pKniInfo && pKniInfo->pNativeHandle &&
        JAVACALL_OK == javacall_media_get_video_size(pKniInfo->pNativeHandle, &width, &height)) {
        returnValue = height;
    }

    MMP_DEBUG_STR2("[kni_video] -nGetHeight %d ret %d\n", height, returnValue);

    KNI_ReturnInt(returnValue);
}

/*  private native int nSetLocation ( int handle , int x , int y , int w , int h ) ; */
KNIEXPORT KNI_RETURNTYPE_BOOLEAN
KNIDECL(com_sun_mmedia_DirectPlayer_nSetLocation) {

    jint handle = KNI_GetParameterAsInt(1);
    jint x = KNI_GetParameterAsInt(2);
    jint y = KNI_GetParameterAsInt(3);
    jint w = KNI_GetParameterAsInt(4);
    jint h = KNI_GetParameterAsInt(5);
    KNIPlayerInfo* pKniInfo = (KNIPlayerInfo*)handle;

    jboolean returnValue = KNI_FALSE;

    MMP_DEBUG_STR4("[kni_video] +nSetLocation %d %d %d %d\n", x, y, w, h);

    if (pKniInfo && pKniInfo->pNativeHandle &&
        JAVACALL_OK == javacall_media_set_video_location(pKniInfo->pNativeHandle, x, y, w, h)) {
        returnValue = KNI_TRUE;
    }

    MMP_DEBUG_STR1("[kni_video] -nSetLocation ret %d\n", returnValue);

    KNI_ReturnBoolean(returnValue);
}

/*  private native boolean nSetVisible ( int handle, boolean visible ) ; */
KNIEXPORT KNI_RETURNTYPE_BOOLEAN
KNIDECL(com_sun_mmedia_DirectPlayer_nSetVisible) {

    jint handle = KNI_GetParameterAsInt(1);
    jboolean visible = KNI_GetParameterAsBoolean(2);
    KNIPlayerInfo* pKniInfo = (KNIPlayerInfo*)handle;

    jboolean returnValue = KNI_FALSE;

    MMP_DEBUG_STR1("[kni_video] +nSetVisible %d\n", visible);

    if (pKniInfo && pKniInfo->pNativeHandle &&
        JAVACALL_OK == javacall_media_set_video_visible(pKniInfo->pNativeHandle, 
                           (KNI_TRUE == visible ? JAVACALL_TRUE : JAVACALL_FALSE))) 
    {
        returnValue = KNI_TRUE;
    }

    MMP_DEBUG_STR1("[kni_video] -nSetVisible ret %d\n", returnValue);

    KNI_ReturnBoolean(returnValue);
}

/*  private native int nSetAlpha (boolean on, int color) ; */
KNIEXPORT KNI_RETURNTYPE_INT
KNIDECL(com_sun_mmedia_DirectPlayer_nSetAlpha) {

    jint handle = KNI_GetParameterAsInt(1);
    jboolean isOn = KNI_GetParameterAsBoolean(2);
    jint color = KNI_GetParameterAsInt(3);
    javacall_result ret = JAVACALL_FAIL;
    KNIPlayerInfo* pKniInfo = (KNIPlayerInfo*)handle;

    MMP_DEBUG_STR2("[kni_video] +nSetAlpha on=%d alpha=%d\n", isOn, color);

    if (pKniInfo && pKniInfo->pNativeHandle ) {
        ret = javacall_media_set_video_color_key(pKniInfo->pNativeHandle, 
					KNI_TRUE == isOn ? JAVACALL_TRUE : JAVACALL_FALSE, 
                                         (javacall_pixel)color);
    }

    MMP_DEBUG_STR1("[kni_video] -nSetAlpha ret %d\n", ret);

    KNI_ReturnInt(JAVACALL_SUCCEEDED(ret) ? 1 : 0);  
}

KNIEXPORT KNI_RETURNTYPE_BOOLEAN 
KNIDECL(com_sun_mmedia_DirectPlayer_nSetFullScreenMode) {
    KNI_ReturnBoolean( KNI_FALSE );
}


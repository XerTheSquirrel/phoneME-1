/*
 * Copyright  1990-2009 Sun Microsystems, Inc. All Rights Reserved.
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

#include "sublimeLinuxWin32Bridge.h"

static int process_id = 0;

void set_current_process_id(int pid) {
    process_id = pid;
}


#ifdef WIN32 
#include <jni.h>

int get_current_process_id() {
    return process_id != 0 ? process_id : GetCurrentProcessId();
}

void WaitForMutex(MUTEX_HANDLE m) {
    WaitForSingleObject(m, INFINITE); 
}

void WaitForEvent(EVENT_HANDLE e) { 
    WaitForSingleObject(e, INFINITE); 
}

/* empty implementation: this method is used only by the linux version */ 
JNIEXPORT void JNICALL Java_com_sun_kvem_Sublime_setTempFilesDirectory(JNIEnv * env, jclass clz, jstring jstr) {}

void yield() {
  Sleep(0); 
}

#else /* !WIN32 */ 

#include <unistd.h> 
#include <sys/types.h>
#include <sys/stat.h>
#include <limits.h>
#include <fcntl.h>
#include <stdio.h>
#include <errno.h>
#include "com_sun_kvem_Sublime.h"
#include <sched.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <semaphore.h>
#include <assert.h>

struct GenericMutexStruct;

typedef void (*GenericMutexOp)(struct GenericMutexStruct *);

typedef struct GenericMutexStruct {
    GenericMutexOp lock;
    GenericMutexOp unlock;
    GenericMutexOp destroy;
} GenericMutex;

typedef struct LocalMutexStruct {
    GenericMutex super;

    pthread_mutex_t mutex;
} LocalMutex;

typedef struct GlobalMutexStruct {
    GenericMutex super;

    char *semaphoreName;
    sem_t *semaphore;
} GlobalMutex;

struct GenericEventStruct;

typedef void (*GenericEventOp)(struct GenericEventStruct *);

typedef struct GenericEventStruct {
    GenericEventOp signal;
    GenericEventOp wait;
    GenericEventOp destroy;
} GenericEvent;

typedef struct LocalEventStruct {
    GenericEvent super;

    int signalled;

    pthread_mutex_t mutex;
    pthread_cond_t cond;
} LocalEvent;

typedef struct GlobalEventStruct {
    GenericEvent super;

    char *semaphoreName;
    sem_t *semaphore;
} GlobalEvent;

/* Directory for temporary files used by Events/SharedBuffers */
static char * tempFilesLocation; 

static char *constructSemaphoreName(const char *name);

static GenericMutex *localMutex_create();

static void localMutex_lock(LocalMutex *localMutex);
static void localMutex_unlock(LocalMutex *localMutex);
static void localMutex_destroy(LocalMutex *localMutex);

static GenericMutex *globalMutex_create(const char *name);

static void globalMutex_lock(GlobalMutex *globalMutex);
static void globalMutex_unlock(GlobalMutex *globalMutex);
static void globalMutex_destroy(GlobalMutex *globalMutex);

static GenericEvent *localEvent_create();

static void localEvent_signal(LocalEvent *localEvent);
static void localEvent_wait(LocalEvent *localEvent);
static void localEvent_destroy(LocalEvent *localEvent);

static GenericEvent *globalEvent_create(const char *name);

static void globalEvent_signal(GlobalEvent *globalEvent);
static void globalEvent_wait(GlobalEvent *globalEvent);
static void globalEvent_destroy(GlobalEvent *globalEvent);

int get_current_process_id() {
    return process_id != 0 ? process_id : getpid();
}


/* After the directory for temporary files was created on the Java part, the path 
 *  should be updated with this function 
*/ 
JNIEXPORT void JNICALL Java_com_sun_kvem_Sublime_setTempFilesDirectory(JNIEnv * env, jclass clz, jstring jstr) {
    jsize length = (*env)->GetStringUTFLength(env, jstr);
    tempFilesLocation = (char*) malloc(length + 1);
    (*env)->GetStringUTFRegion(env, jstr, 0, length, tempFilesLocation); 
    tempFilesLocation[length] = '\0';
}

/* if the calling process for this function is the Java process - 
 * the return value is the "tempFilesLocation" var. if it is 
 * a process generated by the Java process - the return value 
 * is an environment var. 
 */ 
char* getTempDirLocation(void) {
    char* res = NULL; 
    jstring jstr; 
    if ((res = (char *)getenv("LIME_TMP_DIR")) != NULL) {
        return res; 
    }
    return tempFilesLocation; 
}

void itoa(int num, char *buf, int radix) {
    int tmp, index, length;
    char c; 
    index = 0 ; 
    while(num != 0) { 
        tmp = num % radix; 
        num = num / radix; 
        buf[index++] = tmp + 48 ;
    }
    buf[index--] = 0; 
    length = index; 
    for (;index > (length - index); index--) { 
        c = buf[index]; 
        buf[index] = buf[length - index];
        buf[length - index] = c; 
    }
}

MUTEX_HANDLE LimeCreateMutex(void *sa, int initialState, const char *name) {
    assert(sa == NULL);
    assert(!initialState);
    
    return (name != NULL) ? globalMutex_create(name) : localMutex_create(); 
}

void WaitForMutex(MUTEX_HANDLE m) {
    GenericMutex *genericMutex = (GenericMutex *) m;
    if (genericMutex != NULL) {
        genericMutex->lock(genericMutex);
    }
}

void LimeReleaseMutex(MUTEX_HANDLE m) {
    GenericMutex *genericMutex = (GenericMutex *) m;
    if (genericMutex != NULL) {
        genericMutex->unlock(genericMutex);
    }
}

void LimeDestroyMutex(MUTEX_HANDLE m) {
    GenericMutex *genericMutex = (GenericMutex *) m;
    if (genericMutex != NULL) {
        genericMutex->destroy(genericMutex);
    }
}

EVENT_HANDLE LimeCreateEvent(void *sa, int manualReset, int initialState, 
                             const char *name) {
    assert(sa == NULL);
    assert(!manualReset);
    assert(!initialState);

    return (name != NULL) ? globalEvent_create(name) : localEvent_create();
}

void LimeSetEvent(EVENT_HANDLE e) {
    GenericEvent *genericEvent = (GenericEvent *) e;
    if (genericEvent != NULL) {
        genericEvent->signal(genericEvent);
    }
}

void WaitForEvent(EVENT_HANDLE e) {
    GenericEvent *genericEvent = (GenericEvent *) e;
    if (genericEvent != NULL) {
        genericEvent->wait(genericEvent);
    }
}

void LimeDestroyEvent(EVENT_HANDLE e) {
    GenericEvent *genericEvent = (GenericEvent *) e;
    if (genericEvent != NULL) {
        genericEvent->destroy(genericEvent);
    }
}

static GenericMutex *localMutex_create() {
    LocalMutex *localMutex = (LocalMutex *) malloc(sizeof(LocalMutex));
    if (localMutex == NULL) {
        return NULL;
    }

    if (pthread_mutex_init(&localMutex->mutex, NULL) != 0) {
        free(localMutex);
        return NULL;
    }
 
    localMutex->super.lock = 
            (GenericMutexOp) localMutex_lock;
    localMutex->super.unlock = 
            (GenericMutexOp) localMutex_unlock;
    localMutex->super.destroy = 
            (GenericMutexOp) localMutex_destroy;

    return (GenericMutex *) localMutex;
}

static void localMutex_lock(LocalMutex *localMutex) {
    pthread_mutex_lock(&localMutex->mutex);
}

static void localMutex_unlock(LocalMutex *localMutex) {
    pthread_mutex_unlock(&localMutex->mutex);
}

static void localMutex_destroy(LocalMutex *localMutex) {
    pthread_mutex_destroy(&localMutex->mutex);
    free(localMutex);
}

static GenericMutex *globalMutex_create(const char *name) {
    char *semaphoreName;
    sem_t *semaphore;
    size_t nameLength;

    GlobalMutex *globalMutex = (GlobalMutex *) malloc(sizeof(GlobalMutex));
    if (globalMutex == NULL) {
        return NULL;
    }

    semaphoreName = constructSemaphoreName(name); 
    if (semaphoreName == NULL) {
        free(globalMutex);
        return NULL;
    }
    
    semaphore = sem_open(semaphoreName, O_CREAT, 0644, 1);
    if (semaphore == SEM_FAILED) {
        free(semaphoreName);
        free(globalMutex);
        return NULL;
    }

    globalMutex->semaphoreName = semaphoreName;
    globalMutex->semaphore = semaphore;
 
    globalMutex->super.lock = 
            (GenericMutexOp) globalMutex_lock;
    globalMutex->super.unlock = 
            (GenericMutexOp) globalMutex_unlock;
    globalMutex->super.destroy = 
            (GenericMutexOp) globalMutex_destroy;

    return (GenericMutex *) globalMutex;
}

static void globalMutex_lock(GlobalMutex *globalMutex) {
    sem_wait(globalMutex->semaphore);
}

static void globalMutex_unlock(GlobalMutex *globalMutex) {
    /* block the semaphore from counting above 1 */
    sem_trywait(globalMutex->semaphore);
    sem_post(globalMutex->semaphore);
}

static void globalMutex_destroy(GlobalMutex *globalMutex) {
    sem_close(globalMutex->semaphore);
    sem_unlink(globalMutex->semaphoreName);

    free(globalMutex->semaphoreName);
    free(globalMutex);
}

static GenericEvent *localEvent_create() {
    LocalEvent *localEvent = (LocalEvent *) malloc(sizeof(LocalEvent));
    if (localEvent == NULL) {
        return NULL;
    }

    if (pthread_mutex_init(&localEvent->mutex, NULL) != 0) {
        free(localEvent);
        return NULL;
    }

    if (pthread_cond_init(&localEvent->cond, NULL) != 0) {
        pthread_mutex_destroy(&localEvent->mutex);
        free(localEvent);
        return NULL;
    }

    localEvent->signalled = 0;
 
    localEvent->super.signal = 
            (GenericEventOp) localEvent_signal;
    localEvent->super.wait = 
            (GenericEventOp) localEvent_wait;
    localEvent->super.destroy = 
            (GenericEventOp) localEvent_destroy;

    return (GenericEvent *) localEvent;
}

static void localEvent_signal(LocalEvent *localEvent) {
    pthread_mutex_lock(&localEvent->mutex);
    localEvent->signalled = 1;
    pthread_cond_broadcast(&localEvent->cond);
    pthread_mutex_unlock(&localEvent->mutex);
}

static void localEvent_wait(LocalEvent *localEvent) {
    pthread_mutex_lock(&localEvent->mutex);
    while (!localEvent->signalled) {
        pthread_cond_wait(&localEvent->cond, &localEvent->mutex);
    }
    localEvent->signalled = 0;
    pthread_mutex_unlock(&localEvent->mutex);
}

static void localEvent_destroy(LocalEvent *localEvent) {
    pthread_mutex_destroy(&localEvent->mutex);
    pthread_cond_destroy(&localEvent->cond);
    free(localEvent);
}

static GenericEvent *globalEvent_create(const char *name) {
    char *semaphoreName;
    sem_t *semaphore;
    size_t nameLength;

    GlobalEvent *globalEvent = (GlobalEvent *) malloc(sizeof(GlobalEvent));
    if (globalEvent == NULL) {
        return NULL;
    }

    semaphoreName = constructSemaphoreName(name); 
    if (semaphoreName == NULL) {
        free(globalEvent);
        return NULL;
    }
    
    semaphore = sem_open(semaphoreName, O_CREAT, 0644, 0);
    if (semaphore == SEM_FAILED) {
        free(semaphoreName);
        free(globalEvent);
        return NULL;
    }

    globalEvent->semaphoreName = semaphoreName;
    globalEvent->semaphore = semaphore;
 
    globalEvent->super.signal = 
            (GenericEventOp) globalEvent_signal;
    globalEvent->super.wait = 
            (GenericEventOp) globalEvent_wait;
    globalEvent->super.destroy = 
            (GenericEventOp) globalEvent_destroy;

    return (GenericEvent *) globalEvent;
}

static void globalEvent_signal(GlobalEvent *globalEvent) {
    /* block the semaphore from counting above 1 */
    sem_trywait(globalEvent->semaphore);
    sem_post(globalEvent->semaphore);
}

static void globalEvent_wait(GlobalEvent *globalEvent) {
    sem_wait(globalEvent->semaphore);
}

static void globalEvent_destroy(GlobalEvent *globalEvent) {
    sem_close(globalEvent->semaphore);
    sem_unlink(globalEvent->semaphoreName);

    free(globalEvent->semaphoreName);
    free(globalEvent);
}

static char *constructSemaphoreName(const char *name) {
    char *semaphoreName;
    sem_t semaphore;
    size_t nameLength;

    nameLength = strlen(name);
    semaphoreName = (char *) malloc((nameLength + 2) * sizeof(char));
    if (semaphoreName == NULL) {
        return NULL;
    }

    /* prefix the name with '/' to get a global identifier */
    semaphoreName[0] = '/';
    strcpy(semaphoreName + 1, name);
    
    return semaphoreName;
}

int GetLastError() {
    return errno; 
}

void yield() {
  sched_yield(); 
}

#endif
  

#
# Copyright  1990-2006 Sun Microsystems, Inc. All Rights Reserved.  
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER  
#   
# This program is free software; you can redistribute it and/or  
# modify it under the terms of the GNU General Public License version  
# 2 only, as published by the Free Software Foundation.   
#   
# This program is distributed in the hope that it will be useful, but  
# WITHOUT ANY WARRANTY; without even the implied warranty of  
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU  
# General Public License version 2 for more details (a copy is  
# included at /legal/license.txt).   
#   
# You should have received a copy of the GNU General Public License  
# version 2 along with this work; if not, write to the Free Software  
# Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  
# 02110-1301 USA   
#   
# Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa  
# Clara, CA 95054 or visit www.sun.com if you need additional  
# information or have any questions. 
#
# @(#)defs.mk	1.11 06/10/10

#
# defs for darwin-powerpc target
#

# Needed for JVMTI suport. Seems to be automatically included on
# other platforms.
LINK_ARCH_LIBS += -liconv

CVM_TARGETOBJS_SPEED += \
	invokeNative_powerpc_darwin.o  \

CVM_TARGETOBJS_OTHER += \
	atomic_powerpc.o \
	memory_asm_cpu.o \

CVM_SRCDIRS   += \
	$(CVM_TOP)/src/$(TARGET_OS)-$(TARGET_CPU_FAMILY)/javavm/runtime \

CVM_INCLUDES  += \
	-I$(CVM_TOP)/src/$(TARGET_OS)-$(TARGET_CPU_FAMILY)

#
# JIT related settings
#

ifeq ($(CVM_JIT), true)

CVM_TARGETOBJS_SPACE += \
	jit_arch.o        \
        jitemitter_arch.o \

CVM_TARGETOBJS_OTHER += \
	flushcache_cpu.o

CVM_SRCDIRS   += \
	$(CVM_TOP)/src/$(TARGET_OS)-$(TARGET_CPU_FAMILY)/javavm/runtime/jit

endif

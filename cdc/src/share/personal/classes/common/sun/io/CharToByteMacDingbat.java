/*
 * @(#)CharToByteMacDingbat.java	1.15 06/10/10
 *
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
 *
 */

package sun.io;

/**
 * Tables and data to convert Unicode to MacDingbat
 *
 * @author  ConverterGenerator tool
 * @version >= JDK1.1.6
 */

public class CharToByteMacDingbat extends CharToByteSingleByte {
    public String getCharacterEncoding() {
        return "MacDingbat";
    }

    public CharToByteMacDingbat() {
        super.mask1 = 0xFF00;
        super.mask2 = 0x00FF;
        super.shift = 8;
        super.index1 = index1;
        super.index2 = index2;
    }
    private final static String index2 =

        "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007" + 
        "\b\t\n\u000B\f\r\u000E\u000F" + 
        "\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017" + 
        "\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F" + 
        "\u0020\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u007F" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u00D5\u0000\u00D6\u00D7\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u00AC\u00AD" + 
        "\u00AE\u00AF\u00B0\u00B1\u00B2\u00B3\u00B4\u00B5" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u006E\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0073\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0074\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0075\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u006C" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0077" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0048\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0025\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u002A\u0000" + 
        "\u0000\u002B\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u00AB\u0000\u0000\u00A8\u0000" + 
        "\u00AA\u00A9\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0021\"\u0023\u0024\u0000" + 
        "\u0026\'\u0028\u0029\u0000\u0000\u002C\u002D" + 
        "\u002E\u002F\u0030\u0031\u0032\u0033\u0034\u0035" + 
        "\u0036\u0037\u0038\u0039\u003A\u003B\u003C\u003D" + 
        "\u003E\u003F\u0040\u0041\u0042\u0043\u0044\u0045" + 
        "\u0046\u0047\u0000\u0049\u004A\u004B\u004C\u004D" + 
        "\u004E\u004F\u0050\u0051\u0052\u0053\u0054\u0055" + 
        "\u0056\u0057\u0058\u0059\u005A\u005B\\\u005D" + 
        "\u005E\u005F\u0060\u0061\u0062\u0063\u0064\u0065" + 
        "\u0066\u0067\u0068\u0069\u006A\u006B\u0000\u006D" + 
        "\u0000\u006F\u0070\u0071\u0072\u0000\u0000\u0000" + 
        "\u0076\u0000\u0078\u0079\u007A\u007B\u007C\u007D" + 
        "\u007E\u0000\u0000\u00A1\u00A2\u00A3\u00A4\u00A5" + 
        "\u00A6\u00A7\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u00B6\u00B7\u00B8\u00B9\u00BA\u00BB\u00BC\u00BD" + 
        "\u00BE\u00BF\u00C0\u00C1\u00C2\u00C3\u00C4\u00C5" + 
        "\u00C6\u00C7\u00C8\u00C9\u00CA\u00CB\u00CC\u00CD" + 
        "\u00CE\u00CF\u00D0\u00D1\u00D2\u00D3\u00D4\u0000" + 
        "\u0000\u0000\u00D8\u00D9\u00DA\u00DB\u00DC\u00DD" + 
        "\u00DE\u00DF\u00E0\u00E1\u00E2\u00E3\u00E4\u00E5" + 
        "\u00E6\u00E7\u00E8\u00E9\u00EA\u00EB\u00EC\u00ED" + 
        "\u00EE\u00EF\u0000\u00F1\u00F2\u00F3\u00F4\u00F5" + 
        "\u00F6\u00F7\u00F8\u00F9\u00FA\u00FB\u00FC\u00FD" + 
        "\u00FE\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000"; 
    private final static short index1[] = {
            0, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 
            128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 
            128, 238, 128, 128, 398, 504, 755, 1010, 128, 128, 128, 128, 128, 128, 128, 128, 
            128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 
            128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 
            128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 
            128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 
            128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 
            128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 
            128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 
            128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 
            128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 
            128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 
            128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 
            128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 
            128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 128, 
        };
}

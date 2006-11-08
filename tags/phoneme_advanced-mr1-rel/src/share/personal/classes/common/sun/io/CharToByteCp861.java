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

package sun.io;

/**
 * Tables and data to convert Unicode to Cp861
 *
 * @author  ConverterGenerator tool
 * @version >= JDK1.1.6
 */

public class CharToByteCp861 extends CharToByteSingleByte {
    public String getCharacterEncoding() {
        return "Cp861";
    }

    public CharToByteCp861() {
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
        "\u0020\u0021\"\u0023\u0024\u0025\u0026\'" + 
        "\u0028\u0029\u002A\u002B\u002C\u002D\u002E\u002F" + 
        "\u0030\u0031\u0032\u0033\u0034\u0035\u0036\u0037" + 
        "\u0038\u0039\u003A\u003B\u003C\u003D\u003E\u003F" + 
        "\u0040\u0041\u0042\u0043\u0044\u0045\u0046\u0047" + 
        "\u0048\u0049\u004A\u004B\u004C\u004D\u004E\u004F" + 
        "\u0050\u0051\u0052\u0053\u0054\u0055\u0056\u0057" + 
        "\u0058\u0059\u005A\u005B\\\u005D\u005E\u005F" + 
        "\u0060\u0061\u0062\u0063\u0064\u0065\u0066\u0067" + 
        "\u0068\u0069\u006A\u006B\u006C\u006D\u006E\u006F" + 
        "\u0070\u0071\u0072\u0073\u0074\u0075\u0076\u0077" + 
        "\u0078\u0079\u007A\u007B\u007C\u007D\u007E\u007F" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u00FF\u00AD\u0000\u009C\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u00AE\u00AA\u0000\u0000\u0000" + 
        "\u00F8\u00F1\u00FD\u0000\u0000\u00E6\u0000\u00FA" + 
        "\u0000\u0000\u0000\u00AF\u00AC\u00AB\u0000\u00A8" + 
        "\u0000\u00A4\u0000\u0000\u008E\u008F\u0092\u0080" + 
        "\u0000\u0090\u0000\u0000\u0000\u00A5\u0000\u0000" + 
        "\u008B\u0000\u0000\u00A6\u0000\u0000\u0099\u0000" + 
        "\u009D\u0000\u00A7\u0000\u009A\u0097\u008D\u00E1" + 
        "\u0085\u00A0\u0083\u0000\u0084\u0086\u0091\u0087" + 
        "\u008A\u0082\u0088\u0089\u0000\u00A1\u0000\u0000" + 
        "\u008C\u0000\u0000\u00A2\u0093\u0000\u0094\u00F6" + 
        "\u009B\u0000\u00A3\u0096\u0081\u0098\u0095\u0000" + 
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
        "\u0000\u009F\u0000\u0000\u0000\u0000\u0000\u0000" + 
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
        "\u0000\u0000\u00E2\u0000\u0000\u0000\u0000\u00E9" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u00E4\u0000\u0000\u00E8\u0000\u0000" + 
        "\u00EA\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u00E0\u0000\u0000\u00EB\u00EE\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u00E3" + 
        "\u0000\u0000\u00E5\u00E7\u0000\u00ED\u0000\u0000" + 
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
        "\u0000\u0000\u0000\u0000\u0000\u00FC\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u009E\u0000\u0000" + 
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
        "\u0000\u0000\u0000\u0000\u0000\u0000\u00F9\u00FB" + 
        "\u0000\u0000\u0000\u00EC\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u00EF\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u00F7\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u00F0\u0000" + 
        "\u0000\u00F3\u00F2\u0000\u0000\u0000\u0000\u0000" + 
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
        "\u0000\u0000\u0000\u0000\u0000\u00A9\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u00F4\u00F5\u0000" + 
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
        "\u0000\u0000\u0000\u0000\u0000\u00C4\u0000\u00B3" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u00DA\u0000\u0000\u0000\u00BF\u0000\u0000" + 
        "\u0000\u00C0\u0000\u0000\u0000\u00D9\u0000\u0000" + 
        "\u0000\u00C3\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u00B4\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u00C2\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u00C1\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u00C5\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u00CD\u00BA\u00D5" + 
        "\u00D6\u00C9\u00B8\u00B7\u00BB\u00D4\u00D3\u00C8" + 
        "\u00BE\u00BD\u00BC\u00C6\u00C7\u00CC\u00B5\u00B6" + 
        "\u00B9\u00D1\u00D2\u00CB\u00CF\u00D0\u00CA\u00D8" + 
        "\u00D7\u00CE\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u00DF\u0000\u0000" + 
        "\u0000\u00DC\u0000\u0000\u0000\u00DB\u0000\u0000" + 
        "\u0000\u00DD\u0000\u0000\u0000\u00DE\u00B0\u00B1" + 
        "\u00B2\u0000\u0000\u0000\u0000\u0000\u0000\u0000" + 
        "\u0000\u0000\u0000\u0000\u0000\u00FE\u0000\u0000" + 
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
        "\u0000\u0000\u0000\u0000\u0000"; 
    private final static short index1[] = {
            0, 255, 402, 511, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
            402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
            710, 402, 941, 1181, 402, 1437, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
            402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
            402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
            402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
            402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
            402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
            402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
            402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
            402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
            402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
            402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
            402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
            402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
            402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 402, 
        };
}

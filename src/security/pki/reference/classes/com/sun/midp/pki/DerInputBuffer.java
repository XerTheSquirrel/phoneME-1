/*
 *
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

package com.sun.midp.pki;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Calendar;

/**
 * DER input buffer ... this is the main abstraction in the DER library
 * which actively works with the "untyped byte stream" abstraction.  It
 * does so with impunity, since it's not intended to be exposed to
 * anyone who could violate the "typed value stream" DER model and hence
 * corrupt the input stream of DER values.
 *
 * @author David Brownell
 */
class DerInputBuffer extends ByteArrayInputStream {

    DerInputBuffer(byte[] buf) { super(buf); }

    DerInputBuffer(byte[] buf, int offset, int len) {
        super(buf, offset, len);
    }

    DerInputBuffer dup() {
        return new DerInputBuffer(toByteArray());    
    }

    byte[] toByteArray() {
        int     len = available();
        if (len <= 0)
            return null;
        byte[]  retval = new byte[len];

        System.arraycopy(buf, pos, retval, 0, len);
        return retval;
    }

    int peek() throws IOException {
        if (pos >= count)
            throw new IOException("out of data");
        else
            return buf[pos];
    }

    /**
     * Compares this DerInputBuffer for equality with the specified
     * object.
     */
    public boolean equals(Object other) {
        if (other instanceof DerInputBuffer)
            return equals((DerInputBuffer)other);
        else
            return false;
    }

    boolean equals(DerInputBuffer other) {
        if (this == other)
            return true;

        int max = this.available();
        if (other.available() != max)
            return false;
        for (int i = 0; i < max; i++) {
            if (this.buf[this.pos + i] != other.buf[other.pos + i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a hashcode for this DerInputBuffer.
     *
     * @return a hashcode for this DerInputBuffer.
     */
    public int hashCode() {
        int retval = 0;

        int len = available();
        int p = pos;

        for (int i = 0; i < len; i++)
            retval += buf[p + i] * i;
        return retval;
    }

    void truncate(int len) throws IOException {
        if (len > available())
            throw new IOException("insufficient data");
        count = pos + len;
    }

    /**
     * Returns the integer which takes up the specified number
     * of bytes in this buffer.
     * @throws IOException if the result is not within the valid
     * range for integer, i.e. between Integer.MIN_VALUE and
     * Integer.MAX_VALUE.
     * @param len the number of bytes to use.
     * @return the integer.
     */
    public int getInteger(int len) throws IOException {

        int ret = 0;
        for (int i = 0; i < len; i++) {
            ret = (ret << 8) + buf[pos + i];
        }

        if (ret < Integer.MIN_VALUE) {
            throw new IOException("Integer below minimum valid value");
        }
        if (ret > Integer.MAX_VALUE) {
            throw new IOException("Integer exceeds maximum valid value");
        }
        return ret;
    }

    /**
     * Returns the bit string which takes up the specified
     * number of bytes in this buffer.
     */
    public byte[] getBitString(int len) throws IOException {
        if (len > available())
            throw new IOException("short read of bit string");

        if (len == 0) {
            throw new IOException("Invalid encoding: zero length bit string");
        }

        int numOfPadBits = buf[pos];
        if ((numOfPadBits < 0) || (numOfPadBits > 7)) {
            throw new IOException("Invalid number of padding bits");
        }
        // minus the first byte which indicates the number of padding bits
        byte[] retval = new byte[len - 1];
        System.arraycopy(buf, pos + 1, retval, 0, len - 1);
        if (numOfPadBits != 0) {
            // get rid of the padding bits
            retval[len - 2] &= (0xff << numOfPadBits);
        }
        skip(len);
        return retval;
    }

    /**
     * Returns the bit string which takes up the rest of this buffer.
     */
    byte[] getBitString() throws IOException {
        return getBitString(available());
    }


    /**
     * Returns the UTC Time value that takes up the specified number
     * of bytes in this buffer.
     * @param len the number of bytes to use
     */
    public Date getUTCTime(int len) throws IOException {
        if (len > available())
            throw new IOException("short read of DER UTC Time");

        if (len < 11 || len > 17)
            throw new IOException("DER UTC Time length error");

        return getTime(len, false);
    }

    /**
     * Returns the Generalized Time value that takes up the specified
     * number of bytes in this buffer.
     * @param len the number of bytes to use
     */
    public Date getGeneralizedTime(int len) throws IOException {
        if (len > available())
            throw new IOException("short read of DER Generalized Time");

        if (len < 13 || len > 23)
            throw new IOException("DER Generalized Time length error");

        return getTime(len, true);

    }

    /**
     * Private helper routine to extract time from the der value.
     * @param len the number of bytes to use
     * @param generalized true if Generalized Time is to be read, false
     * if UTC Time is to be read.
     */
    private Date getTime(int len, boolean generalized) throws IOException {

        /*
         * UTC time encoded as ASCII chars:
         *       YYMMDDhhmmZ
         *       YYMMDDhhmmssZ
         *       YYMMDDhhmm+hhmm
         *       YYMMDDhhmm-hhmm
         *       YYMMDDhhmmss+hhmm
         *       YYMMDDhhmmss-hhmm
         * UTC Time is broken in storing only two digits of year.
         * If YY < 50, we assume 20YY;
         * if YY >= 50, we assume 19YY, as per RFC 3280.
         *
         * Generalized time has a four-digit year and allows any
         * precision specified in ISO 8601. However, for our purposes,
         * we will only allow the same format as UTC time, except that
         * fractional seconds (millisecond precision) are supported.
         */

        int year, month, day, hour, minute, second, millis;
        String type = null;

        if (generalized) {
            type = "Generalized";
            year = 1000 * Character.digit((char)buf[pos++], 10);
            year += 100 * Character.digit((char)buf[pos++], 10);
            year += 10 * Character.digit((char)buf[pos++], 10);
            year += Character.digit((char)buf[pos++], 10);
            len -= 2; // For the two extra YY
        } else {
            type = "UTC";
            year = 10 * Character.digit((char)buf[pos++], 10);
            year += Character.digit((char)buf[pos++], 10);

            if (year < 50)              // origin 2000
                year += 2000;
            else
                year += 1900;   // origin 1900
        }

        month = 10 * Character.digit((char)buf[pos++], 10);
        month += Character.digit((char)buf[pos++], 10);

        day = 10 * Character.digit((char)buf[pos++], 10);
        day += Character.digit((char)buf[pos++], 10);

        hour = 10 * Character.digit((char)buf[pos++], 10);
        hour += Character.digit((char)buf[pos++], 10);

        minute = 10 * Character.digit((char)buf[pos++], 10);
        minute += Character.digit((char)buf[pos++], 10);

        len -= 10; // YYMMDDhhmm

        /*
         * We allow for non-encoded seconds, even though the
         * IETF-PKIX specification says that the seconds should
         * always be encoded even if it is zero.
         */

        millis = 0;
        if (len > 2 && len < 12) {
            second = 10 * Character.digit((char)buf[pos++], 10);
            second += Character.digit((char)buf[pos++], 10);
            len -= 2;
            // handle fractional seconds (if present)
            if (buf[pos] == '.' || buf[pos] == ',') {
                len --;
                pos++;
                // handle upto milisecond precision only
                int precision = 0;
                int peek = pos;
                while (buf[peek] != 'Z' &&
                       buf[peek] != '+' &&
                       buf[peek] != '-') {
                    peek++;
                    precision++;
                }
                switch (precision) {
                case 3:
                    millis += 100 * Character.digit((char)buf[pos++], 10);
                    millis += 10 * Character.digit((char)buf[pos++], 10);
                    millis += Character.digit((char)buf[pos++], 10);
                    break;
                case 2:
                    millis += 100 * Character.digit((char)buf[pos++], 10);
                    millis += 10 * Character.digit((char)buf[pos++], 10);
                    break;
                case 1:
                    millis += 100 * Character.digit((char)buf[pos++], 10);
                    break;
                default:
                        throw new IOException("Parse " + type +
                            " time, unsupported precision for seconds value");
                }
                len -= precision;
            }
        } else
            second = 0;

        if (month == 0 || day == 0
            || month > 12 || day > 31
            || hour >= 24 || minute >= 60 || second >= 60)
            throw new IOException("Parse " + type + " time, invalid format");

        /*
         * Generalized time can theoretically allow any precision,
         * but we're not supporting that.
         */
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, millis);
        long time = c.getTime().getTime();

        /*
         * Finally, "Z" or "+hhmm" or "-hhmm" ... offsets change hhmm
         */
        if (! (len == 1 || len == 5))
            throw new IOException("Parse " + type + " time, invalid offset");

        int hr, min;

        switch (buf[pos++]) {
        case '+':
            hr = 10 * Character.digit((char)buf[pos++], 10);
            hr += Character.digit((char)buf[pos++], 10);
            min = 10 * Character.digit((char)buf[pos++], 10);
            min += Character.digit((char)buf[pos++], 10);

            if (hr >= 24 || min >= 60)
                throw new IOException("Parse " + type + " time, +hhmm");

            time -= ((hr * 60) + min) * 60 * 1000;
            break;

        case '-':
            hr = 10 * Character.digit((char)buf[pos++], 10);
            hr += Character.digit((char)buf[pos++], 10);
            min = 10 * Character.digit((char)buf[pos++], 10);
            min += Character.digit((char)buf[pos++], 10);

            if (hr >= 24 || min >= 60)
                throw new IOException("Parse " + type + " time, -hhmm");

            time += ((hr * 60) + min) * 60 * 1000;
            break;

        case 'Z':
            break;

        default:
            throw new IOException("Parse " + type + " time, garbage offset");
        }
        return new Date(time);
    }
}

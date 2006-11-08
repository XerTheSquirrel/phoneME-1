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

package java.lang;

/** The CharacterData class encapsulates the large tables found in
    Java.lang.Character. */

class CharacterData {

    /* The character properties are currently encoded into 32 bits in the following manner:
        1 bit   mirrored property
        4 bits  directionality property
        9 bits  signed offset used for converting case
        1 bit   if 1, adding the signed offset converts the character to lowercase
        1 bit   if 1, subtracting the signed offset converts the character to uppercase
        1 bit   if 1, this character has a titlecase equivalent (possibly itself)
        3 bits  0  may not be part of an identifier
                1  ignorable control; may continue a Unicode identifier or Java identifier
                2  may continue a Java identifier but not a Unicode identifier (unused)
                3  may continue a Unicode identifier or Java identifier
                4  is a Java whitespace character
                5  may start or continue a Java identifier;
                   may continue but not start a Unicode identifier (underscores)
                6  may start or continue a Java identifier but not a Unicode identifier ($)
                7  may start or continue a Unicode identifier or Java identifier
                Thus:
                   5, 6, 7 may start a Java identifier
                   1, 2, 3, 5, 6, 7 may continue a Java identifier
                   7 may start a Unicode identifier
                   1, 3, 5, 7 may continue a Unicode identifier
                   1 is ignorable within an identifier
                   4 is Java whitespace
        2 bits  0  this character has no numeric property
                1  adding the digit offset to the character code and then
                   masking with 0x1F will produce the desired numeric value
                2  this character has a "strange" numeric value
                3  a Java supradecimal digit: adding the digit offset to the
                   character code, then masking with 0x1F, then adding 10
                   will produce the desired numeric value
        5 bits  digit offset
        5 bits  character type

        The encoding of character properties is subject to change at any time.
     */

    static int getProperties(char ch) {
        return A[Y[(X[ch>>5]<<4)|((ch>>1)&0xF)]|(ch&0x1)];
    }

    static int getType(char ch) {
        return (getProperties(ch) & 0x1F);
    }

    static boolean isLowerCase(char ch) {
        return getType(ch) == Character.LOWERCASE_LETTER;
    }

    static boolean isUpperCase(char ch) {
        return getType(ch) == Character.UPPERCASE_LETTER;
    }

    static boolean isTitleCase(char ch) {
        return getType(ch) == Character.TITLECASE_LETTER;
    }

    static boolean isDigit(char ch) {
        return getType(ch) == Character.DECIMAL_DIGIT_NUMBER;
    }

    static boolean isDefined(char ch) {
        return getType(ch) != Character.UNASSIGNED;
    }

    static boolean isLetter(char ch) {
        return (((((1 << Character.UPPERCASE_LETTER) |
            (1 << Character.LOWERCASE_LETTER) |
            (1 << Character.TITLECASE_LETTER) |
            (1 << Character.MODIFIER_LETTER) |
            (1 << Character.OTHER_LETTER)) >> getType(ch)) & 1) != 0);
    }

    static boolean isLetterOrDigit(char ch) {
        return (((((1 << Character.UPPERCASE_LETTER) |
            (1 << Character.LOWERCASE_LETTER) |
            (1 << Character.TITLECASE_LETTER) |
            (1 << Character.MODIFIER_LETTER) |
            (1 << Character.OTHER_LETTER) |
            (1 << Character.DECIMAL_DIGIT_NUMBER)) >> getType(ch)) & 1) != 0);
    }

    static boolean isSpaceChar(char ch) {
        return (((((1 << Character.SPACE_SEPARATOR) |
                   (1 << Character.LINE_SEPARATOR) |
                   (1 << Character.PARAGRAPH_SEPARATOR))
                >> getType(ch)) & 1) != 0);
    }


    static boolean isJavaIdentifierStart(char ch) {
        return (getProperties(ch) & 0x00007000) >= 0x00005000;
    }

    static boolean isJavaIdentifierPart(char ch) {
        return (getProperties(ch) & 0x00003000) != 0;
    }

    static boolean isUnicodeIdentifierStart(char ch) {
        return (getProperties(ch) & 0x00007000) == 0x00007000;
    }

    static boolean isUnicodeIdentifierPart(char ch) {
        return (getProperties(ch)& 0x00001000) != 0;
    }

    static boolean isIdentifierIgnorable(char ch) {
        return (getProperties(ch) & 0x00007000) == 0x00001000;
    }

    static char toLowerCase(char ch) {
        char mapChar = ch;
        int val = getProperties(ch);

        if ((val & 0x00020000) != 0) {
          if ((val & 0x07FC0000) == 0x07FC0000) {
            switch(ch) {
              // map the offset overflow chars
            case '\u2126' : mapChar = '\u03C9'; break;
            case '\u212A' : mapChar = '\u006B'; break;
            case '\u212B' : mapChar = '\u00E5'; break;
              // map the titlecase chars with both a 1:M uppercase map
              // and a lowercase map
            case '\u1F88' : mapChar = '\u1F80'; break;
            case '\u1F89' : mapChar = '\u1F81'; break;
            case '\u1F8A' : mapChar = '\u1F82'; break;
            case '\u1F8B' : mapChar = '\u1F83'; break;
            case '\u1F8C' : mapChar = '\u1F84'; break;
            case '\u1F8D' : mapChar = '\u1F85'; break;
            case '\u1F8E' : mapChar = '\u1F86'; break;
            case '\u1F8F' : mapChar = '\u1F87'; break;
            case '\u1F98' : mapChar = '\u1F90'; break;
            case '\u1F99' : mapChar = '\u1F91'; break;
            case '\u1F9A' : mapChar = '\u1F92'; break;
            case '\u1F9B' : mapChar = '\u1F93'; break;
            case '\u1F9C' : mapChar = '\u1F94'; break;
            case '\u1F9D' : mapChar = '\u1F95'; break;
            case '\u1F9E' : mapChar = '\u1F96'; break;
            case '\u1F9F' : mapChar = '\u1F97'; break;
            case '\u1FA8' : mapChar = '\u1FA0'; break;
            case '\u1FA9' : mapChar = '\u1FA1'; break;
            case '\u1FAA' : mapChar = '\u1FA2'; break;
            case '\u1FAB' : mapChar = '\u1FA3'; break;
            case '\u1FAC' : mapChar = '\u1FA4'; break;
            case '\u1FAD' : mapChar = '\u1FA5'; break;
            case '\u1FAE' : mapChar = '\u1FA6'; break;
            case '\u1FAF' : mapChar = '\u1FA7'; break;
            case '\u1FBC' : mapChar = '\u1FB3'; break;
            case '\u1FCC' : mapChar = '\u1FC3'; break;
            case '\u1FFC' : mapChar = '\u1FF3'; break;
              // default mapChar is already set, so no
              // need to redo it here.
              // default       : mapChar = ch;
            }
          }
          else {
            int offset = val << 5 >> (5+18);
            mapChar = (char)(ch + offset);
          }
        }
        return mapChar;
    }

    static char toUpperCase(char ch) {
        char mapChar = ch;
        int val = getProperties(ch);

        if ((val & 0x00010000) != 0) {
          if ((val & 0x07FC0000) == 0x07FC0000) {
            switch(ch) {
              // map chars with overflow offsets
            case '\u00B5' : mapChar = '\u039C'; break;
            case '\u017F' : mapChar = '\u0053'; break;
            case '\u1FBE' : mapChar = '\u0399'; break;
              // map char that have both a 1:1 and 1:M map
            case '\u1F80' : mapChar = '\u1F88'; break;
            case '\u1F81' : mapChar = '\u1F89'; break;
            case '\u1F82' : mapChar = '\u1F8A'; break;
            case '\u1F83' : mapChar = '\u1F8B'; break;
            case '\u1F84' : mapChar = '\u1F8C'; break;
            case '\u1F85' : mapChar = '\u1F8D'; break;
            case '\u1F86' : mapChar = '\u1F8E'; break;
            case '\u1F87' : mapChar = '\u1F8F'; break;
            case '\u1F90' : mapChar = '\u1F98'; break;
            case '\u1F91' : mapChar = '\u1F99'; break;
            case '\u1F92' : mapChar = '\u1F9A'; break;
            case '\u1F93' : mapChar = '\u1F9B'; break;
            case '\u1F94' : mapChar = '\u1F9C'; break;
            case '\u1F95' : mapChar = '\u1F9D'; break;
            case '\u1F96' : mapChar = '\u1F9E'; break;
            case '\u1F97' : mapChar = '\u1F9F'; break;
            case '\u1FA0' : mapChar = '\u1FA8'; break;
            case '\u1FA1' : mapChar = '\u1FA9'; break;
            case '\u1FA2' : mapChar = '\u1FAA'; break;
            case '\u1FA3' : mapChar = '\u1FAB'; break;
            case '\u1FA4' : mapChar = '\u1FAC'; break;
            case '\u1FA5' : mapChar = '\u1FAD'; break;
            case '\u1FA6' : mapChar = '\u1FAE'; break;
            case '\u1FA7' : mapChar = '\u1FAF'; break;
            case '\u1FB3' : mapChar = '\u1FBC'; break;
            case '\u1FC3' : mapChar = '\u1FCC'; break;
            case '\u1FF3' : mapChar = '\u1FFC'; break;
              // ch must have a 1:M case mapping, but we
              // can't handle it here. Return ch.
              // since mapChar is already set, no need
              // to redo it here.
              //default       : mapChar = ch;
            }
          }
          else {
            int offset = val  << 5 >> (5+18);
            mapChar =  (char)(ch - offset);
          }
        }
        return mapChar;
    }

    static char toTitleCase(char ch) {
        char mapChar = ch;
        int val = getProperties(ch);

        if ((val & 0x00008000) != 0) {
            // There is a titlecase equivalent.  Perform further checks:
            if ((val & 0x00010000) == 0) {
                // The character does not have an uppercase equivalent, so it must
                // already be uppercase; so add 1 to get the titlecase form.
                mapChar = (char)(ch + 1);
            }
            else if ((val & 0x00020000) == 0) {
                // The character does not have a lowercase equivalent, so it must
                // already be lowercase; so subtract 1 to get the titlecase form.
                mapChar = (char)(ch - 1);
            }
            // else {
            // The character has both an uppercase equivalent and a lowercase
            // equivalent, so it must itself be a titlecase form; return it.
            // return ch;
            //}
        }
        else if ((val & 0x00010000) != 0) {
            // This character has no titlecase equivalent but it does have an
            // uppercase equivalent, so use that (subtract the signed case offset).
            mapChar = toUpperCase(ch);
        }
        return mapChar;
    }

    static int digit(char ch, int radix) {
        int value = -1;
        if (radix >= Character.MIN_RADIX && radix <= Character.MAX_RADIX) {
            int val = getProperties(ch);
            int kind = val & 0x1F;
            if (kind == Character.DECIMAL_DIGIT_NUMBER) {
                value = ch + ((val & 0x3E0) >> 5) & 0x1F;
            }
            else if ((val & 0xC00) == 0x00000C00) {
                // Java supradecimal digit
                value = (ch + ((val & 0x3E0) >> 5) & 0x1F) + 10;
            }
        }
        return (value < radix) ? value : -1;
    }

    static int getNumericValue(char ch) {
        int val = getProperties(ch);
        int retval = -1;

        switch (val & 0xC00) {
        default: // cannot occur
        case (0x00000000):         // not numeric
            retval = -1;
            break;
        case (0x00000400):              // simple numeric
            retval = ch + ((val & 0x3E0) >> 5) & 0x1F;
            break;
        case (0x00000800)      :       // "strange" numeric
            switch (ch) {
                case '\u0BF1': retval = 100; break;         // TAMIL NUMBER ONE HUNDRED
                case '\u0BF2': retval = 1000; break;        // TAMIL NUMBER ONE THOUSAND
                case '\u1375': retval = 40; break;          // ETHIOPIC NUMBER FORTY
                case '\u1376': retval = 50; break;          // ETHIOPIC NUMBER FIFTY
                case '\u1377': retval = 60; break;          // ETHIOPIC NUMBER SIXTY
                case '\u1378': retval = 70; break;          // ETHIOPIC NUMBER SEVENTY
                case '\u1379': retval = 80; break;          // ETHIOPIC NUMBER EIGHTY
                case '\u137A': retval = 90; break;          // ETHIOPIC NUMBER NINETY
                case '\u137B': retval = 100; break;         // ETHIOPIC NUMBER HUNDRED
                case '\u137C': retval = 10000; break;       // ETHIOPIC NUMBER TEN THOUSAND
                case '\u215F': retval = 1; break;           // FRACTION NUMERATOR ONE
                case '\u216C': retval = 50; break;          // ROMAN NUMERAL FIFTY
                case '\u216D': retval = 100; break;         // ROMAN NUMERAL ONE HUNDRED
                case '\u216E': retval = 500; break;         // ROMAN NUMERAL FIVE HUNDRED
                case '\u216F': retval = 1000; break;        // ROMAN NUMERAL ONE THOUSAND
                case '\u217C': retval = 50; break;          // SMALL ROMAN NUMERAL FIFTY
                case '\u217D': retval = 100; break;         // SMALL ROMAN NUMERAL ONE HUNDRED
                case '\u217E': retval = 500; break;         // SMALL ROMAN NUMERAL FIVE HUNDRED
                case '\u217F': retval = 1000; break;        // SMALL ROMAN NUMERAL ONE THOUSAND
                case '\u2180': retval = 1000; break;        // ROMAN NUMERAL ONE THOUSAND C D
                case '\u2181': retval = 5000; break;        // ROMAN NUMERAL FIVE THOUSAND
                case '\u2182': retval = 10000; break;       // ROMAN NUMERAL TEN THOUSAND
                default:       retval = -2; break;
            }
            break;
        case (0x00000C00):           // Java supradecimal
            retval = (ch + ((val & 0x3E0) >> 5) & 0x1F) + 10;
            break;
        }
        return retval;
    }

    static boolean isWhitespace(char ch) {
        return (getProperties(ch) & 0x00007000) == 0x00004000;
    }

    static byte getDirectionality(char ch) {
        int val = getProperties(ch);
        byte directionality = (byte)((val & 0x78000000) >> 27);
        if (directionality == 0xF ) {
            switch(ch) {
                case '\u202A' :
                    // This is the only char with LRE
                    directionality = Character.DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING;
                    break;
                case '\u202B' :
                    // This is the only char with RLE
                    directionality = Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING;
                    break;
                case '\u202C' :
                    // This is the only char with PDF
                    directionality = Character.DIRECTIONALITY_POP_DIRECTIONAL_FORMAT;
                    break;
                case '\u202D' :
                    // This is the only char with LRO
                    directionality = Character.DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE;
                    break;
                case '\u202E' :
                    // This is the only char with RLO
                    directionality = Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE;
                    break;
                default :
                    directionality = Character.DIRECTIONALITY_UNDEFINED;
                    break;
            }
        }
        return directionality;
    }

    static boolean isMirrored(char ch) {
        return (getProperties(ch) & 0x80000000) != 0;
    }

    static char toUpperCaseEx(char ch) {
        char mapChar = ch;
        int val = getProperties(ch);

        if ((val & 0x00010000) != 0) {
            if ((val & 0x07FC0000) != 0x07FC0000) {
                int offset = val  << 5 >> (5+18);
                mapChar =  (char)(ch - offset);
            }
            else {
                switch(ch) {
                    // map overflow characters
                    case '\u00B5' : mapChar = '\u039C'; break;
                    case '\u017F' : mapChar = '\u0053'; break;
                    case '\u1FBE' : mapChar = '\u0399'; break;
                    default       : mapChar = Character.CHAR_ERROR; break;
                }
            }
        }
        return mapChar;
    }
    // The following tables and code generated using:
  // java GenerateCharacter -template src/share/tools/GenerateCharacter/CharacterData.java.template -spec src/share/tools/GenerateCharacter/UnicodeData.txt -specialcasing src/share/tools/GenerateCharacter/SpecialCasing.txt -o /tmp/java/lang/CharacterData.java -string -usecharforbyte 11 4 1
      static final char[][][] charMap;
// The X table has 2048 entries for a total of 4096 bytes.

  static final char X[] = new char[2048];
/* = (
**    "\000\001\002\003\004\005\006\007\010\011\012\013\014\015\016\017\010\020\021"+
**    "\022\023\024\025\026\027\027\030\031\032\033\034\035\036\037\040\010\041\010"+
**    "\042\043\044\045\046\047\050\051\052\053\054\055\056\057\060\060\061\062\063"+
**    "\064\065\044\060\066\044\044\044\044\044\044\044\044\044\044\067\070\071\072"+
**    "\073\074\075\076\077\100\101\102\103\104\105\106\073\107\110\111\112\113\114"+
**    "\115\116\117\120\121\122\123\124\121\122\125\126\121\127\130\131\132\133\134"+
**    "\135\044\136\137\140\044\141\142\143\144\145\146\147\044\150\151\152\044\044"+
**    "\153\154\155\150\150\156\150\150\157\150\160\161\150\162\150\163\164\165\166"+
**    "\164\150\167\170\044\150\150\171\133\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\172\173\150\150\174\044\044\044\044\150\175"+
**    "\176\177\200\150\201\202\150\203\044\044\044\044\044\044\044\044\044\044\044"+
**    "\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044"+
**    "\044\044\044\044\044\044\044\044\044\044\044\044\010\010\010\010\204\010\010"+
**    "\205\206\207\210\211\212\213\214\215\216\217\220\221\222\223\224\225\226\227"+
**    "\230\231\232\233\234\235\236\237\240\241\242\243\244\245\246\247\250\251\252"+
**    "\044\044\044\253\254\255\256\257\260\261\262\253\253\253\253\263\264\265\266"+
**    "\267\253\253\270\044\044\044\044\271\272\273\274\275\276\044\044\253\253\253"+
**    "\253\253\253\253\253\044\044\044\044\044\044\044\044\044\044\044\044\044\044"+
**    "\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044"+
**    "\044\044\044\044\044\044\044\044\044\044\044\277\253\253\235\253\253\253\253"+
**    "\253\253\263\300\301\302\133\150\303\133\150\304\305\306\150\150\307\202\044"+
**    "\044\310\311\312\313\314\315\316\317\250\250\250\320\250\250\321\317\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\322\044\044\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\323\044\044\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\324\325\326\044\044\044\044\044\044\044\044\044\044\044"+
**    "\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044"+
**    "\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044\044"+
**    "\044\044\044\044\044\044\044\044\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150"+
**    "\150\150\150\150\150\150\150\150\150\150\150\150\150\150\150\327\044\044\330"+
**    "\330\330\330\330\330\330\330\330\330\330\330\330\330\330\330\330\330\330\330"+
**    "\330\330\330\330\330\330\330\330\330\330\330\330\330\330\330\330\330\330\330"+
**    "\330\330\330\330\330\330\330\330\330\330\330\330\330\330\330\330\330\330\330"+
**    "\330\330\330\330\330\330\331\331\331\331\331\331\331\331\331\331\331\331\331"+
**    "\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331"+
**    "\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331"+
**    "\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331"+
**    "\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331"+
**    "\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331"+
**    "\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331"+
**    "\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331"+
**    "\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331"+
**    "\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331"+
**    "\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\331\150\150\150"+
**    "\150\150\150\150\150\150\332\044\044\044\044\044\044\333\334\335\060\060\336"+
**    "\337\060\060\060\060\060\060\060\060\060\060\340\341\060\342\060\343\344\044"+
**    "\345\346\347\060\060\060\350\351\352\353\354\355\356\357\360").toCharArray();
*/

  // The Y table has 3856 entries for a total of 7712 bytes.

  static final char Y[] = new char[3856];
/* = (
**    "\000\000\000\000\002\004\006\000\000\000\000\000\000\000\010\004\012\014\016"+
**    "\020\022\024\026\030\032\032\032\032\032\034\036\040\042\044\044\044\044\044"+
**    "\044\044\044\044\044\044\044\046\050\052\054\056\056\056\056\056\056\056\056"+
**    "\056\056\056\056\060\062\064\000\000\066\000\000\000\000\000\000\000\000\000"+
**    "\000\000\000\000\070\072\072\074\076\100\102\104\106\110\112\114\116\120\122"+
**    "\124\126\126\126\126\126\126\126\126\126\126\126\130\126\126\126\132\134\134"+
**    "\134\134\134\134\134\134\134\134\134\136\134\134\134\140\142\142\142\142\142"+
**    "\142\142\142\142\142\142\142\142\142\142\142\142\142\142\142\142\142\142\142"+
**    "\144\142\142\142\146\150\150\150\150\150\150\150\152\142\142\142\142\142\142"+
**    "\142\142\142\142\142\142\142\142\142\142\142\142\142\142\142\142\142\154\150"+
**    "\150\152\156\142\142\160\162\164\166\170\172\162\174\176\142\200\202\204\142"+
**    "\142\142\206\210\200\142\206\212\214\150\216\142\220\142\222\224\224\226\230"+
**    "\232\226\234\150\150\150\150\150\150\150\236\142\142\142\142\142\142\142\142"+
**    "\142\240\232\142\242\142\142\142\142\244\142\142\142\142\142\142\142\142\142"+
**    "\244\244\244\244\244\244\244\244\244\244\244\244\244\244\200\246\250\252\254"+
**    "\256\200\200\260\262\200\200\264\200\200\266\200\270\272\200\200\200\200\200"+
**    "\274\276\200\200\274\300\200\200\200\302\200\200\200\200\200\200\200\200\200"+
**    "\200\200\200\200\244\304\304\304\304\306\310\304\304\304\312\312\312\312\312"+
**    "\312\312\304\312\312\312\312\312\312\312\304\304\306\312\312\312\312\314\244"+
**    "\244\244\244\244\244\244\244\316\316\316\316\316\316\316\316\316\316\316\316"+
**    "\316\316\316\316\316\316\320\316\316\316\316\322\244\244\244\244\244\244\244"+
**    "\244\316\322\244\244\244\244\244\244\244\244\312\244\244\314\244\324\244\244"+
**    "\312\326\330\332\334\336\340\126\126\126\126\126\126\126\126\342\126\126\126"+
**    "\126\344\346\350\134\134\134\134\134\134\134\134\352\134\134\134\134\354\356"+
**    "\360\362\364\366\244\142\142\142\142\142\142\142\142\142\142\142\370\372\244"+
**    "\244\244\244\244\244\374\374\374\374\374\374\374\374\126\126\126\126\126\126"+
**    "\126\126\126\126\126\126\126\126\126\126\134\134\134\134\134\134\134\134\134"+
**    "\134\134\134\134\134\134\134\376\376\376\376\376\376\376\376\142\u0100\316"+
**    "\322\u0102\244\142\142\142\142\142\142\142\142\142\142\u0104\150\u0106\u0108"+
**    "\u0106\u0108\u0106\244\142\142\142\142\142\142\142\142\142\142\142\142\142"+
**    "\142\142\142\142\142\142\244\142\244\244\244\244\244\244\244\244\244\244\244"+
**    "\244\244\244\244\244\244\244\244\244\244\244\244\244\244\244\244\u010A\u010C"+
**    "\u010C\u010C\u010C\u010C\u010C\u010C\u010C\u010C\u010C\u010C\u010C\u010C\u010C"+
**    "\u010C\u010C\u010C\u010C\u010E\u0110\u0112\u0112\u0112\u0114\u0116\u0116\u0116"+
**    "\u0116\u0116\u0116\u0116\u0116\u0116\u0116\u0116\u0116\u0116\u0116\u0116\u0116"+
**    "\u0116\u0116\u0118\u011A\u011C\244\244\u011E\316\316\316\316\316\316\316\316"+
**    "\u011E\316\316\316\316\316\316\316\316\316\316\316\u011E\316\u0120\u0120\u0122"+
**    "\322\244\244\244\244\244\u0124\u0124\u0124\u0124\u0124\u0124\u0124\u0124\u0124"+
**    "\u0124\u0124\u0124\u0124\u0126\244\244\u0124\u0128\u012A\244\244\244\244\244"+
**    "\244\244\244\244\244\244\u012C\244\244\244\244\244\244\u012E\244\u012E\u0130"+
**    "\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0134"+
**    "\244\244\u0136\u0132\u0132\u0132\u0132\u0138\316\316\316\316\316\244\244\244"+
**    "\244\244\u013A\u013A\u013A\u013A\u013A\u013C\u013E\244\u0140\u0132\u0132\u0132"+
**    "\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132"+
**    "\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132"+
**    "\u0132\u0132\u0132\u0132\u0142\316\316\316\u0144\u0146\316\316\u0148\u014A"+
**    "\u014C\316\316\244\032\032\032\032\032\u0132\u014E\u0150\u0152\u0152\u0152"+
**    "\u0152\u0152\u0152\u0152\u0154\u0138\u0132\u0132\u0132\u0132\u0132\u0132\u0132"+
**    "\u0132\u0132\u0132\u0132\u0132\u0132\u0134\244\316\316\316\316\316\316\316"+
**    "\316\316\316\316\316\316\322\244\244\244\244\244\244\244\244\244\244\u0132"+
**    "\u0132\u0132\316\316\316\316\316\322\244\244\244\244\244\244\244\u011E\u0156"+
**    "\u0158\224\224\224\224\224\224\224\224\224\224\224\224\224\224\224\224\224"+
**    "\224\224\224\224\224\224\224\224\224\244\u015A\u015C\u015E\316\316\316\u0156"+
**    "\u015C\u015E\244\u0160\316\322\244\224\224\224\224\224\316\u0112\u0162\u0162"+
**    "\u0162\u0162\u0162\u0164\244\244\244\244\244\244\244\u011E\u015C\u0158\224"+
**    "\224\224\u0166\u0158\u0166\u0158\224\224\224\224\224\224\224\224\224\224\u0166"+
**    "\224\224\224\u0166\u0166\244\224\224\244\322\u015C\u015E\316\322\u0168\u016A"+
**    "\u0168\u015E\244\244\244\244\u0168\244\244\224\u0158\224\316\244\u0162\u0162"+
**    "\u0162\u0162\u0162\224\072\u016C\u016C\u016E\u0170\244\244\244\322\u0158\224"+
**    "\224\u0166\244\u0158\u0166\u0158\224\224\224\224\224\224\224\224\224\224\u0166"+
**    "\224\224\224\u0166\224\u0158\u0166\224\244\322\u015C\u015E\322\244\u011E\322"+
**    "\u011E\316\244\244\244\244\244\u0158\224\u0166\u0166\244\244\244\u0162\u0162"+
**    "\u0162\u0162\u0162\316\224\u0166\244\244\244\244\244\u011E\u0156\u0158\224"+
**    "\224\224\u0158\u0158\224\u0158\224\224\224\224\224\224\224\224\224\224\u0166"+
**    "\224\224\224\u0166\224\u0158\224\224\244\u015A\u015C\u015E\316\316\u011E\u0156"+
**    "\u0168\u015E\244\u0166\244\244\244\244\244\244\244\u0166\244\244\u0162\u0162"+
**    "\u0162\u0162\u0162\244\244\244\244\244\244\244\244\224\224\224\224\u0166\224"+
**    "\224\224\u0166\224\244\224\224\244\u015A\u015E\u015E\316\244\u0168\u016A\u0168"+
**    "\u015E\244\244\244\244\u0156\244\244\224\u0158\224\244\244\u0162\u0162\u0162"+
**    "\u0162\u0162\u0170\244\244\244\244\244\244\244\244\u0156\u0158\224\224\u0166"+
**    "\244\224\u0166\224\224\244\u0158\u0166\u0166\224\244\u0158\u0166\244\224\u0166"+
**    "\244\224\224\224\224\u0158\224\244\244\u015C\u0156\u016A\244\u015C\u016A\u015C"+
**    "\u015E\244\244\244\244\u0168\244\244\244\244\244\244\244\u0172\u0162\u0162"+
**    "\u0162\u0162\u0174\u0176\244\244\244\244\244\244\u0168\u015C\u0158\224\224"+
**    "\224\u0166\224\u0166\224\224\224\224\224\224\224\224\224\224\224\u0166\224"+
**    "\224\224\224\224\u0158\224\224\244\244\316\u0156\u015C\u016A\316\322\316\316"+
**    "\244\244\244\u011E\322\244\244\244\244\224\244\244\u0162\u0162\u0162\u0162"+
**    "\u0162\244\244\244\244\244\244\244\244\244\u015C\u0158\224\224\224\u0166\224"+
**    "\u0166\224\224\224\224\224\224\224\224\224\224\224\u0166\224\224\224\224\224"+
**    "\u0158\224\224\244\244\u015E\u015C\u015C\u016A\u0156\u016A\u015C\316\244\244"+
**    "\244\u0168\u016A\244\244\244\u0166\224\224\224\224\u0166\224\224\224\224\224"+
**    "\224\224\224\244\244\u015C\u015E\316\244\u015C\u016A\u015C\u015E\244\244\244"+
**    "\244\u0168\244\244\244\244\244\u015C\u0158\224\224\224\224\224\224\224\224"+
**    "\u0166\244\224\224\224\224\224\224\224\224\224\224\224\224\u0158\224\224\224"+
**    "\224\u0158\244\224\224\224\u0166\244\322\244\u0168\u015C\316\322\322\u015C"+
**    "\u015C\u015C\u015C\244\244\244\244\244\244\244\244\244\u015C\u0164\244\244"+
**    "\244\244\244\u0158\224\224\224\224\224\224\224\224\224\224\224\224\224\224"+
**    "\224\224\224\224\224\224\224\224\224\u0160\224\316\316\316\322\244\u0178\224"+
**    "\224\224\u017A\316\316\316\u017C\u017E\u017E\u017E\u017E\u017E\u0112\244\244"+
**    "\u0158\u0166\u0166\u0158\u0166\u0166\u0158\244\244\244\224\224\u0158\224\224"+
**    "\224\u0158\224\u0158\u0158\244\224\u0158\224\u0160\224\316\316\316\u011E\u015A"+
**    "\244\224\224\u0166\314\316\316\316\244\u017E\u017E\u017E\u017E\u017E\244\224"+
**    "\244\u0180\u0182\u0112\u0112\u0112\u0112\u0112\u0112\u0112\u0184\u0182\u0182"+
**    "\316\u0182\u0182\u0182\u0186\u0186\u0186\u0186\u0186\u0188\u0188\u0188\u0188"+
**    "\u0188\u0100\u0100\u0100\u018A\u018A\u015C\224\224\224\224\u0158\224\224\224"+
**    "\224\224\224\224\224\224\224\224\224\224\224\224\224\u0166\244\244\u011E\316"+
**    "\316\316\316\316\316\u0156\316\316\u017C\316\224\224\244\244\316\316\316\316"+
**    "\u011E\316\316\316\316\316\316\316\316\316\316\316\316\316\316\316\316\316"+
**    "\322\u0182\u0182\u0182\u0182\u018C\u0182\u0182\u0170\u018E\244\244\244\244"+
**    "\244\244\244\244\224\224\224\224\224\224\224\224\224\224\224\224\224\224\224"+
**    "\224\224\u0158\224\224\u0158\u0166\u015E\316\u0156\322\244\316\u015E\244\244"+
**    "\244\u0186\u0186\u0186\u0186\u0186\u0112\u0112\u0112\224\224\224\u015C\316"+
**    "\244\244\244\362\362\362\362\362\362\362\362\362\362\362\362\362\362\362\362"+
**    "\362\362\362\244\244\244\244\244\224\224\224\224\224\224\224\224\224\224\224"+
**    "\224\224\224\224\224\224\224\224\u0166\244\u011A\244\244\224\224\224\224\224"+
**    "\224\224\224\224\224\224\224\224\244\244\u0158\224\u0166\244\244\224\224\224"+
**    "\224\224\224\224\224\224\224\224\224\224\224\224\224\224\224\224\224\224\224"+
**    "\224\224\224\244\244\244\224\224\224\u0166\224\224\224\224\224\224\224\224"+
**    "\224\224\224\224\224\224\224\u0166\u0166\224\224\244\224\224\224\u0166\u0166"+
**    "\224\224\244\224\224\224\u0166\u0166\224\224\244\224\224\224\224\224\224\224"+
**    "\224\224\224\224\224\224\224\224\u0166\u0166\224\224\244\224\224\224\u0166"+
**    "\u0166\224\224\244\224\224\224\u0166\224\224\224\u0166\224\224\224\224\224"+
**    "\224\224\224\224\224\224\u0166\224\224\224\224\224\224\224\224\224\224\224"+
**    "\u0166\224\224\224\224\224\224\224\224\224\u0166\244\244\u011A\u0112\u0112"+
**    "\u0112\u0190\u0192\u0192\u0192\u0192\u0194\u0196\u0198\u0198\u0198\u0176\244"+
**    "\224\224\224\224\224\224\224\224\224\224\u0166\244\244\244\244\244\224\224"+
**    "\224\224\224\224\u019A\u019C\224\224\224\u0166\244\244\244\244\u019E\224\224"+
**    "\224\224\224\224\224\224\224\224\224\224\u01A0\u01A2\244\224\224\224\224\224"+
**    "\u019A\u0112\u01A4\u01A6\244\244\244\244\244\244\244\224\224\224\224\224\224"+
**    "\224\224\224\224\u015C\u015E\316\316\316\u015C\u015C\u015C\u015C\u0156\u015E"+
**    "\316\316\316\316\316\u0112\u0112\u0112\u01A8\u0164\244\u0186\u0186\u0186\u0186"+
**    "\u0186\244\244\244\244\244\244\244\244\244\244\244\020\020\020\u01AA\020\u01AC"+
**    "\u01AE\u01B0\u017E\u017E\u017E\u017E\u017E\244\244\244\224\u01B2\224\224\224"+
**    "\224\224\224\224\224\224\224\224\224\224\224\224\224\224\224\224\224\224\224"+
**    "\224\224\224\224\244\244\244\244\224\224\224\224\u0160\244\244\244\244\244"+
**    "\244\244\244\244\244\244\142\142\142\142\142\142\142\142\142\142\142\u01B4"+
**    "\u01B4\u01B6\244\244\142\142\142\142\142\142\142\142\142\142\142\142\142\244"+
**    "\244\244\u01B8\u01B8\u01B8\u01B8\u01BA\u01BA\u01BA\u01BA\u01B8\u01B8\u01B8"+
**    "\244\u01BA\u01BA\u01BA\244\u01B8\u01B8\u01B8\u01B8\u01BA\u01BA\u01BA\u01BA"+
**    "\u01B8\u01B8\u01B8\u01B8\u01BA\u01BA\u01BA\u01BA\u01B8\u01B8\u01B8\244\u01BA"+
**    "\u01BA\u01BA\244\u01BC\u01BC\u01BC\u01BC\u01BE\u01BE\u01BE\u01BE\u01B8\u01B8"+
**    "\u01B8\u01B8\u01BA\u01BA\u01BA\u01BA\u01C0\u01C2\u01C2\u01C4\u01C6\u01C8\u01CA"+
**    "\244\u01B4\u01B4\u01B4\u01B4\u01CC\u01CC\u01CC\u01CC\u01B4\u01B4\u01B4\u01B4"+
**    "\u01CC\u01CC\u01CC\u01CC\u01B4\u01B4\u01B4\u01B4\u01CC\u01CC\u01CC\u01CC\u01B8"+
**    "\u01B4\u01CE\u01B4\u01BA\u01D0\u01D2\u01D4\312\u01B4\u01CE\u01B4\u01D6\u01D6"+
**    "\u01D2\312\u01B8\u01B4\244\u01B4\u01BA\u01D8\u01DA\312\u01B8\u01B4\u01DC\u01B4"+
**    "\u01BA\u01DE\u01E0\312\244\u01B4\u01CE\u01B4\u01E2\u01E4\u01D2\u01E6\u01E8"+
**    "\u01E8\u01E8\u01EA\u01E8\u01EC\u01AE\u01EE\u01F0\u01F0\u01F0\020\u01F2\u01F4"+
**    "\u01F2\u01F4\020\020\020\020\u01F6\u01F8\u01F8\u01FA\u01FC\u01FC\u01FE\020"+
**    "\u0200\u0202\020\u0204\u0206\020\u0208\u020A\020\020\020\244\244\244\244\244"+
**    "\244\244\244\244\244\244\244\244\244\u01AE\u01AE\u01AE\u020C\244\110\110\110"+
**    "\u020E\u0208\u0210\u0212\u0212\u0212\u0212\u0212\u020E\u0208\u020A\244\244"+
**    "\244\244\244\244\244\244\072\072\072\072\072\072\072\072\244\244\244\244\244"+
**    "\244\244\244\244\244\244\244\244\244\244\244\316\316\316\316\316\316\u0144"+
**    "\u0102\u0146\u0102\244\244\244\244\244\244\244\244\244\244\244\244\244\244"+
**    "\074\u0214\074\u0216\074\u0218\362\200\362\u021A\u0216\074\u0216\362\362\074"+
**    "\074\074\u0214\u021C\u0214\u021E\362\u0220\362\u0216\220\224\u0222\u0224\244"+
**    "\244\244\244\244\244\244\244\244\244\244\u0226\122\122\122\122\122\122\u0228"+
**    "\u0228\u0228\u0228\u0228\u0228\u022A\u022A\u022C\u022C\u022C\u022C\u022C\u022C"+
**    "\u022E\u022E\u0230\u0232\244\244\244\244\244\244\u0234\u0234\u0236\074\074"+
**    "\u0234\074\074\u0236\u0238\074\u0236\074\074\074\u0236\074\074\074\074\074"+
**    "\074\074\074\074\074\074\074\074\074\074\u0234\074\u0236\u0236\074\074\074"+
**    "\074\074\074\074\074\074\074\074\074\074\074\074\244\244\244\244\244\244\u023A"+
**    "\u023C\036\u0234\u023C\u023C\u023C\u0234\u023A\u020E\u023A\036\u0234\u023C"+
**    "\u023C\u023A\u023C\036\036\036\u0234\u023A\u023C\u023C\u023C\u023C\u0234\u0234"+
**    "\u023A\u023A\u023C\u023C\u023C\u023C\u023C\u023C\u023C\u023C\036\u0234\u0234"+
**    "\u023C\u023C\u0234\u0234\u0234\u0234\u023A\036\036\u023C\u023C\u023C\u023C"+
**    "\u0234\u023C\u023C\u023C\u023C\u023C\u023C\u023C\u023C\u023C\u023C\u023C\u023C"+
**    "\u023C\u023C\u023C\036\u023A\u023C\036\u0234\u0234\036\u0234\u0234\u0234\u0234"+
**    "\u023C\u0234\u023C\u023C\u023C\u023C\u023C\u023C\u023C\u023C\u023C\036\u0234"+
**    "\u0234\u023C\u0234\u0234\u0234\u0234\u023A\u023C\u023C\u0234\u023C\u0234\u0234"+
**    "\u023C\u023C\u023C\u023C\u023C\u023C\u023C\u023C\u023C\u023C\u023C\u023C\u0234"+
**    "\u023C\244\244\244\244\244\244\244\074\074\074\074\u023C\u023C\074\074\074"+
**    "\074\074\074\074\074\074\074\u023C\074\074\074\u023E\u0240\074\074\074\074"+
**    "\074\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182"+
**    "\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182"+
**    "\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0242\u0244\074\074"+
**    "\074\074\074\074\074\074\074\074\074\u0246\074\074\u0224\244\244\074\074\074"+
**    "\074\074\074\074\074\074\074\074\074\074\074\074\074\074\074\074\u0224\244"+
**    "\244\244\244\244\244\244\244\244\244\244\244\074\074\074\074\074\u0224\244"+
**    "\244\244\244\244\244\244\244\244\244\u0248\u0248\u0248\u0248\u0248\u0248\u0248"+
**    "\u0248\u0248\u0248\u024A\u024A\u024A\u024A\u024A\u024A\u024A\u024A\u024A\u024A"+
**    "\u024C\u024C\u024C\u024C\u024C\u024C\u024C\u024C\u024C\u024C\u0182\u0182\u0182"+
**    "\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u024E\u024E\u024E"+
**    "\u024E\u024E\u024E\u024E\u024E\u024E\u024E\u024E\u024E\u024E\u0250\u0250\u0250"+
**    "\u0250\u0250\u0250\u0250\u0250\u0250\u0250\u0250\u0250\u0250\u0252\244\244"+
**    "\244\244\244\244\244\244\244\244\074\074\074\074\074\074\074\074\074\074\074"+
**    "\244\244\244\244\244\074\074\074\074\074\074\074\074\074\074\074\u0238\074"+
**    "\074\074\074\u0238\074\074\074\074\074\074\074\074\074\074\074\074\074\074"+
**    "\074\074\074\074\074\074\074\074\074\074\074\074\074\244\244\244\244\074\074"+
**    "\074\074\074\074\074\074\074\074\244\244\u0244\074\074\074\074\074\074\074"+
**    "\074\074\074\u0238\074\244\244\244\244\244\244\244\u0244\074\u0224\074\074"+
**    "\244\074\074\074\074\074\074\074\074\074\074\074\074\074\074\u0244\074\074"+
**    "\074\074\074\074\074\074\074\074\074\074\074\074\074\074\074\u0244\u0244\074"+
**    "\u0224\244\u0224\074\074\074\u0224\u0244\074\074\074\244\244\244\244\244\244"+
**    "\244\u0254\u0254\u0254\u0254\u0254\u0256\u0256\u0256\u0256\u0256\u0258\u0258"+
**    "\u0258\u0258\u0258\u0224\244\074\074\074\074\074\074\074\074\074\074\074\074"+
**    "\u0244\074\074\074\074\074\074\u0224\074\074\074\074\074\074\074\074\074\074"+
**    "\074\074\074\u0244\074\074\244\244\244\244\244\244\244\244\074\074\074\074"+
**    "\074\074\244\244\012\020\u025A\u025C\022\022\022\022\022\074\022\022\022\022"+
**    "\u025E\u0260\u0262\u0264\u0264\u0264\u0264\316\316\316\u0266\304\304\074\u0268"+
**    "\u026A\244\074\224\224\224\224\224\224\224\224\224\224\u0166\244\u011E\u026C"+
**    "\310\314\224\224\224\224\224\224\224\224\224\224\224\224\224\u026E\304\314"+
**    "\244\244\u0158\224\224\224\224\224\224\224\224\224\224\224\224\224\224\224"+
**    "\224\224\224\224\u0166\244\u0158\224\224\224\224\224\224\224\224\224\224\224"+
**    "\224\224\224\u0166\u0182\u0188\u0188\u0182\u0182\u0182\u0182\u0182\u0182\u0182"+
**    "\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0170"+
**    "\244\u0188\u0188\u0188\u0188\u0188\u0182\u0182\u0182\u0182\u0182\u0182\u0182"+
**    "\u0182\u0182\u0182\u0182\u0182\u0182\244\244\244\244\244\244\244\244\244\244"+
**    "\244\244\244\244\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182"+
**    "\u0182\u0182\u0182\u0182\244\u018E\u0270\u0270\u0270\u0270\u0270\u0182\u0182"+
**    "\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182"+
**    "\u0182\u0182\u0182\u0182\u0170\244\244\244\244\244\244\244\u0182\u0182\u0182"+
**    "\u0182\u0182\u0182\244\244\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182"+
**    "\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182"+
**    "\u0182\u0182\u0170\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182"+
**    "\u0182\u0170\244\u018E\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182"+
**    "\u0182\u0182\u0182\u0182\u0182\u0182\u0182\u0182\244\224\224\224\224\224\224"+
**    "\224\224\224\224\224\244\244\244\244\244\224\224\224\244\244\244\244\244\244"+
**    "\244\244\244\244\244\244\244\224\224\224\224\224\224\u0166\244\074\074\074"+
**    "\074\074\074\074\074\074\244\074\074\074\074\074\074\074\074\u0244\074\074"+
**    "\074\074\074\u0224\074\u0224\u0224\244\244\244\244\244\244\244\244\244\244"+
**    "\244\244\224\224\244\244\244\244\244\244\244\244\244\244\244\244\244\244\u0272"+
**    "\u0272\u0272\u0272\u0272\u0272\u0272\u0272\u0272\u0272\u0272\u0272\u0272\u0272"+
**    "\u0272\u0272\u0274\u0274\u0274\u0274\u0274\u0274\u0274\u0274\u0274\u0274\u0274"+
**    "\u0274\u0274\u0274\u0274\u0274\224\224\224\224\224\224\224\244\244\244\244"+
**    "\244\244\244\244\244\u01B4\u01B4\u01B4\u01CE\244\244\244\244\244\u0276\u01B4"+
**    "\u01B4\244\244\u0278\u027A\u0124\u0124\u0124\u0124\u027C\u0124\u0124\u0124"+
**    "\u0124\u0124\u0124\u0126\u0124\u0124\u0126\u0126\u0124\u0278\u0126\u0124\u0124"+
**    "\u0124\u0124\u0124\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132"+
**    "\u0132\u0132\u0132\u0132\u0132\u0132\u0132\244\244\244\244\244\244\244\244"+
**    "\244\244\244\244\244\244\244\244\u0130\u0132\u0132\u0132\u0132\u0132\u0132"+
**    "\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132"+
**    "\u0132\u0132\u018A\244\244\244\244\244\244\244\244\u0132\u0132\u0132\u0132"+
**    "\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\244"+
**    "\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\244\244"+
**    "\244\244\244\244\244\244\244\244\244\244\244\244\244\244\244\244\244\244\u0132"+
**    "\u0132\u0132\u0132\u0132\u0132\244\244\316\316\244\244\244\244\244\244\u027E"+
**    "\u0280\u0282\u0284\u0284\u0284\u0284\u0284\u0284\u0284\u01A2\244\u0286\020"+
**    "\u0204\u0288\034\u012C\u028A\020\u025E\u0284\u0284\u028C\020\u028E\u0234\u0290"+
**    "\u0292\u01FE\244\244\u0132\u0134\u0134\u0132\u0132\u0132\u0132\u0132\u0132"+
**    "\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132\u0132"+
**    "\u0134\u0154\u0286\014\016\020\u018A\024\026\030\032\032\032\032\032\034\u0234"+
**    "\u0294\042\044\044\044\044\044\044\044\044\044\044\044\044\u0296\u0298\052"+
**    "\054\056\056\056\056\056\056\056\056\056\056\056\056\u029A\u029C\u0290\u0286"+
**    "\u018A\u0204\224\224\224\224\224\u029E\224\224\224\224\224\224\224\224\224"+
**    "\224\224\224\224\224\224\224\224\224\224\224\224\224\304\224\224\224\224\224"+
**    "\224\224\224\224\224\224\224\224\224\224\u0166\244\224\224\224\244\224\224"+
**    "\224\244\224\224\224\244\224\u0166\244\072\u02A0\u02A2\u02A4\u0238\u0234\u0236"+
**    "\u0224\244\244\244\244\u0154\u01AE\074\244").toCharArray();
**/

  // The A table has 678 entries for a total of 2712 bytes.

  static final int A[] = new int[678];
/**  static final String A_DATA =
**    "\u4800\u100F\u4800\u100F\u4800\u100F\u5800\u400F\u5000\u400F\u5800\u400F\u6000"+
**    "\u400F\u5000\u400F\u5000\u400F\u5000\u400F\u6000\u400C\u6800\030\u6800\030"+
**    "\u2800\030\u2800\u601A\u2800\030\u6800\030\u6800\030\uE800\025\uE800\026\u6800"+
**    "\030\u2800\031\u3800\030\u2800\024\u3800\030\u2000\030\u1800\u3609\u1800\u3609"+
**    "\u3800\030\u6800\030\uE800\031\u6800\031\uE800\031\u6800\030\u6800\030\202"+
**    "\u7FE1\202\u7FE1\202\u7FE1\202\u7FE1\uE800\025\u6800\030\uE800\026\u6800\033"+
**    "\u6800\u5017\u6800\033\201\u7FE2\201\u7FE2\201\u7FE2\201\u7FE2\uE800\025\u6800"+
**    "\031\uE800\026\u6800\031\u4800\u100F\u4800\u100F\u5000\u100F\u3800\014\u6800"+
**    "\030\u2800\u601A\u2800\u601A\u6800\034\u6800\034\u6800\033\u6800\034\000\u7002"+
**    "\uE800\035\u6800\031\u6800\024\u6800\034\u6800\033\u2800\034\u2800\031\u1800"+
**    "\u060B\u1800\u060B\u6800\033\u07FD\u7002\u6800\034\u6800\030\u6800\033\u1800"+
**    "\u050B\000\u7002\uE800\036\u6800\u080B\u6800\u080B\u6800\u080B\u6800\030\202"+
**    "\u7001\202\u7001\202\u7001\u6800\031\202\u7001\u07FD\u7002\201\u7002\201\u7002"+
**    "\201\u7002\u6800\031\201\u7002\u061D\u7002\006\u7001\005\u7002\u04E6\u7001"+
**    "\u03A1\u7002\000\u7002\006\u7001\005\u7002\006\u7001\005\u7002\u07FD\u7002"+
**    "\u061E\u7001\006\u7001\000\u7002\u034A\u7001\u033A\u7001\006\u7001\005\u7002"+
**    "\u0336\u7001\u0336\u7001\006\u7001\005\u7002\000\u7002\u013E\u7001\u032A\u7001"+
**    "\u032E\u7001\006\u7001\u033E\u7001\u067D\u7002\u034E\u7001\u0346\u7001\000"+
**    "\u7002\000\u7002\u034E\u7001\u0356\u7001\000\u7002\u035A\u7001\u036A\u7001"+
**    "\006\u7001\005\u7002\u036A\u7001\005\u7002\u0366\u7001\u0366\u7001\006\u7001"+
**    "\005\u7002\u036E\u7001\000\u7002\000\u7005\000\u7002\u0721\u7002\000\u7005"+
**    "\000\u7005\012\uF001\007\uF003\011\uF002\012\uF001\007\uF003\011\uF002\011"+
**    "\uF002\006\u7001\005\u7002\u013D\u7002\u07FD\u7002\012\uF001\u067E\u7001\u0722"+
**    "\u7001\u7800\000\u7800\000\000\u7002\u0349\u7002\u0339\u7002\000\u7002\u0335"+
**    "\u7002\u0335\u7002\000\u7002\u0329\u7002\000\u7002\u032D\u7002\u0335\u7002"+
**    "\000\u7002\000\u7002\u033D\u7002\u0345\u7002\u034D\u7002\000\u7002\u034D\u7002"+
**    "\u0355\u7002\000\u7002\000\u7002\u0359\u7002\u0369\u7002\000\u7002\000\u7002"+
**    "\u0369\u7002\u0365\u7002\u0365\u7002\u036D\u7002\000\u7002\000\u7004\000\u7004"+
**    "\000\u7004\u6800\033\u6800\033\000\u7004\u6800\033\u6800\033\000\u7004\u7800"+
**    "\000\u4000\u3006\u4000\u3006\u4000\u3006\u46B1\u3006\u4000\u3006\u7800\000"+
**    "\u6800\030\u7800\000\232\u7001\u6800\030\226\u7001\226\u7001\226\u7001\u7800"+
**    "\000\u0102\u7001\u7800\000\376\u7001\376\u7001\u07FD\u7002\202\u7001\u7800"+
**    "\000\202\u7001\231\u7002\225\u7002\225\u7002\225\u7002\u07FD\u7002\201\u7002"+
**    "\175\u7002\201\u7002\u0101\u7002\375\u7002\375\u7002\u7800\000\371\u7002\345"+
**    "\u7002\000\u7001\000\u7001\000\u7001\275\u7002\331\u7002\000\u7002\u0159\u7002"+
**    "\u0141\u7002\u013D\u7002\000\u7002\u0142\u7001\u0142\u7001\u0141\u7002\u0141"+
**    "\u7002\000\034\u4000\u3006\u4000\007\u4000\007\000\u7001\006\u7001\005\u7002"+
**    "\u7800\000\u7800\000\006\u7001\u7800\000\302\u7001\302\u7001\302\u7001\302"+
**    "\u7001\u7800\000\u7800\000\000\u7004\000\030\000\030\u7800\000\301\u7002\301"+
**    "\u7002\301\u7002\301\u7002\u07FD\u7002\u7800\000\000\030\u6800\024\u7800\000"+
**    "\u7800\000\u4000\u3006\u0800\030\u4000\u3006\u4000\u3006\u0800\030\u0800\u7005"+
**    "\u0800\u7005\u0800\u7005\u7800\000\u0800\u7005\u0800\030\u0800\030\u7800\000"+
**    "\u3800\030\u7800\000\u7800\000\u1000\030\u7800\000\u1000\u7005\u1000\u7005"+
**    "\u1000\u7005\u1000\u7005\u7800\000\u1000\u7004\u1000\u7005\u1000\u7005\u4000"+
**    "\u3006\u3000\u3409\u3000\u3409\u2800\030\u3000\030\u3000\030\u1000\030\u4000"+
**    "\u3006\u1000\u7005\u1000\030\u1000\u7005\u4000\u3006\u4000\007\u4000\007\u4000"+
**    "\u3006\u4000\u3006\u1000\u7004\u1000\u7004\u4000\u3006\u4000\u3006\u6800\034"+
**    "\u1000\u7005\u1000\034\u1000\034\u7800\000\u1000\030\u1000\030\u7800\000\u4800"+
**    "\u1010\u4000\u3006\000\u3008\u7800\000\000\u7005\u4000\u3006\000\u7005\000"+
**    "\u3008\000\u3008\000\u3008\u4000\u3006\000\u7005\u4000\u3006\000\u3749\000"+
**    "\u3749\000\030\u7800\000\000\u7005\u7800\000\u7800\000\000\u3008\000\u3008"+
**    "\u7800\000\000\u05AB\000\u05AB\000\013\000\u06EB\000\034\u7800\000\u7800\000"+
**    "\000\u3749\000\u074B\000\u080B\000\u080B\u7800\000\u7800\000\u2800\u601A\000"+
**    "\u7004\u4000\u3006\u4000\u3006\000\030\000\u3609\000\u3609\000\u7005\000\034"+
**    "\000\034\000\034\000\030\000\034\000\u3409\000\u3409\000\013\000\013\u6800"+
**    "\025\u6800\026\u4000\u3006\000\034\u7800\000\000\034\000\030\000\u3709\000"+
**    "\u3709\000\u3709\000\u070B\000\u042B\000\u054B\000\u080B\000\u080B\000\u080B"+
**    "\000\u7005\000\030\000\030\000\u7005\u6000\u400C\000\u7005\000\u7005\u6800"+
**    "\025\u6800\026\u7800\000\000\u046B\000\u046B\000\u046B\u7800\000\000\030\u2800"+
**    "\u601A\u6800\024\u6800\030\u6800\030\u4800\u1010\u4800\u1010\u4800\u1010\u4800"+
**    "\u1010\u7800\000\000\u7005\000\u7004\u07FD\u7002\u07FD\u7002\u07FD\u7002\355"+
**    "\u7002\u07E1\u7002\u07E1\u7002\u07E2\u7001\u07E2\u7001\u07FD\u7002\u07E1\u7002"+
**    "\u7800\000\u07E2\u7001\u06D9\u7002\u06D9\u7002\u06A9\u7002\u06A9\u7002\u0671"+
**    "\u7002\u0671\u7002\u0601\u7002\u0601\u7002\u0641\u7002\u0641\u7002\u0609\u7002"+
**    "\u0609\u7002\u07FF\uF003\u07FF\uF003\u07FD\u7002\u7800\000\u06DA\u7001\u06DA"+
**    "\u7001\u07FF\uF003\u6800\033\u07FD\u7002\u6800\033\u06AA\u7001\u06AA\u7001"+
**    "\u0672\u7001\u0672\u7001\u7800\000\u6800\033\u07FD\u7002\u07E5\u7002\u0642"+
**    "\u7001\u0642\u7001\u07E6\u7001\u6800\033\u0602\u7001\u0602\u7001\u060A\u7001"+
**    "\u060A\u7001\u6800\033\u7800\000\u6000\u400C\u6000\u400C\u6000\u400C\u6000"+
**    "\014\u6000\u400C\u4800\u400C\000\u1010\u0800\u1010\u6800\024\u6800\024\u6800"+
**    "\035\u6800\036\u6800\025\u6800\035\u6000\u400D\u5000\u400E\u7800\u1010\u7800"+
**    "\u1010\u7800\u1010\u6000\014\u2800\030\u2800\030\u2800\030\u6800\030\u6800"+
**    "\030\uE800\035\uE800\036\u6800\030\u6800\030\u6800\u5017\u6800\u5017\u6800"+
**    "\030\u6800\031\uE800\025\uE800\026\u7800\000\u1800\u060B\u7800\000\u2800\031"+
**    "\u2800\031\uE800\026\000\u7002\u1800\u040B\u1800\u040B\000\u7001\u6800\034"+
**    "\u6800\034\000\u7001\000\u7002\000\u7001\000\u7001\000\u7002\u07FE\u7001\u6800"+
**    "\034\u07FE\u7001\u07FE\u7001\u2800\034\000\u7002\000\u7005\000\u7002\u6800"+
**    "\034\u7800\000\u7800\000\u6800\u080B\102\u742A\102\u742A\102\u780A\102\u780A"+
**    "\101\u762A\101\u762A\101\u780A\101\u780A\000\u780A\000\u780A\000\u780A\000"+
**    "\u700A\u6800\031\u6800\031\u6800\031\u6800\034\u6800\034\u6800\031\u6800\031"+
**    "\uE800\031\uE800\031\uE800\031\u6800\034\uE800\025\uE800\026\u6800\034\000"+
**    "\034\u6800\034\u7800\000\u6800\034\u6800\034\000\034\u1800\u042B\u1800\u042B"+
**    "\u1800\u05AB\u1800\u05AB\u1800\u072B\u1800\u072B\152\034\152\034\151\034\151"+
**    "\034\u1800\u06CB\u7800\000\u6800\u056B\u6800\u056B\u6800\u042B\u6800\u042B"+
**    "\u6800\u06EB\u6800\u06EB\u6800\034\000\u7004\000\u7005\000\u772A\u6800\024"+
**    "\u6800\025\u6800\026\u6800\026\u6800\034\000\u740A\000\u740A\000\u740A\u6800"+
**    "\024\000\u7004\000\u764A\000\u776A\000\u748A\u7800\000\u4000\u3006\u6800\033"+
**    "\000\u7005\u6800\u5017\000\u042B\000\u042B\000\023\000\023\000\022\000\022"+
**    "\u7800\000\u07FD\u7002\u7800\000\u0800\u7005\u4000\u3006\u0800\u7005\u0800"+
**    "\u7005\u2800\031\u6800\030\u6800\024\u6800\024\u6800\u5017\u6800\u5017\u6800"+
**    "\025\u6800\026\u6800\025\u7800\000\u6800\030\u6800\u5017\u6800\u5017\u6800"+
**    "\030\u3800\030\u6800\026\u2800\030\u2800\031\u2800\024\u6800\031\u7800\000"+
**    "\u6800\030\u2800\u601A\u6800\031\u6800\030\202\u7FE1\u6800\025\u6800\030\u6800"+
**    "\026\201\u7FE2\u6800\025\u6800\031\u6800\026\000\u7004\000\u7005\u6800\031"+
**    "\u6800\033\u6800\034\u2800\u601A\u2800\u601A\u7800\000";
**/

  // In all, the character property tables require 14520 bytes.

    static {
            charMap = new char[102][2][3];
/*
**            charMap = new char[][][] {
**        { {'\u00DF'}, {'\u0053', '\u0053', } },
**        { {'\u0149'}, {'\u02BC', '\u004E', } },
**        { {'\u01F0'}, {'\u004A', '\u030C', } },
**        { {'\u0390'}, {'\u0399', '\u0308', '\u0301', } },
**        { {'\u03B0'}, {'\u03A5', '\u0308', '\u0301', } },
**        { {'\u0587'}, {'\u0535', '\u0552', } },
**        { {'\u1E96'}, {'\u0048', '\u0331', } },
**        { {'\u1E97'}, {'\u0054', '\u0308', } },
**        { {'\u1E98'}, {'\u0057', '\u030A', } },
**        { {'\u1E99'}, {'\u0059', '\u030A', } },
**        { {'\u1E9A'}, {'\u0041', '\u02BE', } },
**        { {'\u1F50'}, {'\u03A5', '\u0313', } },
**        { {'\u1F52'}, {'\u03A5', '\u0313', '\u0300', } },
**        { {'\u1F54'}, {'\u03A5', '\u0313', '\u0301', } },
**        { {'\u1F56'}, {'\u03A5', '\u0313', '\u0342', } },
**        { {'\u1F80'}, {'\u1F08', '\u0399', } },
**        { {'\u1F81'}, {'\u1F09', '\u0399', } },
**        { {'\u1F82'}, {'\u1F0A', '\u0399', } },
**        { {'\u1F83'}, {'\u1F0B', '\u0399', } },
**        { {'\u1F84'}, {'\u1F0C', '\u0399', } },
**        { {'\u1F85'}, {'\u1F0D', '\u0399', } },
**        { {'\u1F86'}, {'\u1F0E', '\u0399', } },
**        { {'\u1F87'}, {'\u1F0F', '\u0399', } },
**        { {'\u1F88'}, {'\u1F08', '\u0399', } },
**        { {'\u1F89'}, {'\u1F09', '\u0399', } },
**        { {'\u1F8A'}, {'\u1F0A', '\u0399', } },
**        { {'\u1F8B'}, {'\u1F0B', '\u0399', } },
**        { {'\u1F8C'}, {'\u1F0C', '\u0399', } },
**        { {'\u1F8D'}, {'\u1F0D', '\u0399', } },
**        { {'\u1F8E'}, {'\u1F0E', '\u0399', } },
**        { {'\u1F8F'}, {'\u1F0F', '\u0399', } },
**        { {'\u1F90'}, {'\u1F28', '\u0399', } },
**        { {'\u1F91'}, {'\u1F29', '\u0399', } },
**        { {'\u1F92'}, {'\u1F2A', '\u0399', } },
**        { {'\u1F93'}, {'\u1F2B', '\u0399', } },
**        { {'\u1F94'}, {'\u1F2C', '\u0399', } },
**        { {'\u1F95'}, {'\u1F2D', '\u0399', } },
**        { {'\u1F96'}, {'\u1F2E', '\u0399', } },
**        { {'\u1F97'}, {'\u1F2F', '\u0399', } },
**        { {'\u1F98'}, {'\u1F28', '\u0399', } },
**        { {'\u1F99'}, {'\u1F29', '\u0399', } },
**        { {'\u1F9A'}, {'\u1F2A', '\u0399', } },
**        { {'\u1F9B'}, {'\u1F2B', '\u0399', } },
**        { {'\u1F9C'}, {'\u1F2C', '\u0399', } },
**        { {'\u1F9D'}, {'\u1F2D', '\u0399', } },
**        { {'\u1F9E'}, {'\u1F2E', '\u0399', } },
**        { {'\u1F9F'}, {'\u1F2F', '\u0399', } },
**        { {'\u1FA0'}, {'\u1F68', '\u0399', } },
**        { {'\u1FA1'}, {'\u1F69', '\u0399', } },
**        { {'\u1FA2'}, {'\u1F6A', '\u0399', } },
**        { {'\u1FA3'}, {'\u1F6B', '\u0399', } },
**        { {'\u1FA4'}, {'\u1F6C', '\u0399', } },
**        { {'\u1FA5'}, {'\u1F6D', '\u0399', } },
**        { {'\u1FA6'}, {'\u1F6E', '\u0399', } },
**        { {'\u1FA7'}, {'\u1F6F', '\u0399', } },
**        { {'\u1FA8'}, {'\u1F68', '\u0399', } },
**        { {'\u1FA9'}, {'\u1F69', '\u0399', } },
**        { {'\u1FAA'}, {'\u1F6A', '\u0399', } },
**        { {'\u1FAB'}, {'\u1F6B', '\u0399', } },
**        { {'\u1FAC'}, {'\u1F6C', '\u0399', } },
**        { {'\u1FAD'}, {'\u1F6D', '\u0399', } },
**        { {'\u1FAE'}, {'\u1F6E', '\u0399', } },
**        { {'\u1FAF'}, {'\u1F6F', '\u0399', } },
**        { {'\u1FB2'}, {'\u1FBA', '\u0399', } },
**        { {'\u1FB3'}, {'\u0391', '\u0399', } },
**        { {'\u1FB4'}, {'\u0386', '\u0399', } },
**        { {'\u1FB6'}, {'\u0391', '\u0342', } },
**        { {'\u1FB7'}, {'\u0391', '\u0342', '\u0399', } },
**        { {'\u1FBC'}, {'\u0391', '\u0399', } },
**        { {'\u1FC2'}, {'\u1FCA', '\u0399', } },
**        { {'\u1FC3'}, {'\u0397', '\u0399', } },
**        { {'\u1FC4'}, {'\u0389', '\u0399', } },
**        { {'\u1FC6'}, {'\u0397', '\u0342', } },
**        { {'\u1FC7'}, {'\u0397', '\u0342', '\u0399', } },
**        { {'\u1FCC'}, {'\u0397', '\u0399', } },
**        { {'\u1FD2'}, {'\u0399', '\u0308', '\u0300', } },
**        { {'\u1FD3'}, {'\u0399', '\u0308', '\u0301', } },
**        { {'\u1FD6'}, {'\u0399', '\u0342', } },
**        { {'\u1FD7'}, {'\u0399', '\u0308', '\u0342', } },
**        { {'\u1FE2'}, {'\u03A5', '\u0308', '\u0300', } },
**        { {'\u1FE3'}, {'\u03A5', '\u0308', '\u0301', } },
**        { {'\u1FE4'}, {'\u03A1', '\u0313', } },
**        { {'\u1FE6'}, {'\u03A5', '\u0342', } },
**        { {'\u1FE7'}, {'\u03A5', '\u0308', '\u0342', } },
**        { {'\u1FF2'}, {'\u1FFA', '\u0399', } },
**        { {'\u1FF3'}, {'\u03A9', '\u0399', } },
**        { {'\u1FF4'}, {'\u038F', '\u0399', } },
**        { {'\u1FF6'}, {'\u03A9', '\u0342', } },
**        { {'\u1FF7'}, {'\u03A9', '\u0342', '\u0399', } },
**        { {'\u1FFC'}, {'\u03A9', '\u0399', } },
**        { {'\uFB00'}, {'\u0046', '\u0046', } },
**        { {'\uFB01'}, {'\u0046', '\u0049', } },
**        { {'\uFB02'}, {'\u0046', '\u004C', } },
**        { {'\uFB03'}, {'\u0046', '\u0046', '\u0049', } },
**        { {'\uFB04'}, {'\u0046', '\u0046', '\u004C', } },
**        { {'\uFB05'}, {'\u0053', '\u0054', } },
**        { {'\uFB06'}, {'\u0053', '\u0054', } },
**        { {'\uFB13'}, {'\u0544', '\u0546', } },
**        { {'\uFB14'}, {'\u0544', '\u0535', } },
**        { {'\uFB15'}, {'\u0544', '\u053B', } },
**        { {'\uFB16'}, {'\u054E', '\u0546', } },
**        { {'\uFB17'}, {'\u0544', '\u053D', } },
**    };
**        { // THIS CODE WAS AUTOMATICALLY CREATED BY GenerateCharacter:
**            char[] data = A_DATA.toCharArray();
**            //assert (data.length == (678 * 2));
**            int i = 0, j = 0;
**            while (i < (678 * 2)) {
**                int entry = data[i++] << 16;
**                A[j++] = entry | data[i++];
**            }
**        }
**/

        setArrays();
    }        

    public static native void setArrays();
}

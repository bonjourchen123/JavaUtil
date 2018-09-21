package com.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 
 * @author Bonjour
 *
 */
public class Base64Util {
	
	static byte[] code;
    static int[]  decode;

    static {
        code = new byte[64];
        decode = new int[128];
        for (int i=0;i<26;i++) code[i]      = BaseUtil.getByte('A' + i);
        for (int i=0;i<26;i++) code[26 + i] = BaseUtil.getByte('a' + i);
        for (int i=0;i<10;i++) code[52 + i] = BaseUtil.getByte('0' + i);
        code[62] = (byte)'+';
        code[63] = (byte)'/';
        for (int i=0;i<128;i++) decode[i] = -1;
        for (int i=0;i<64;i++)  decode[ (int)code[i] ] = i;
    }
    
    public static String encode(String sIn) throws IOException {
           return new String(encode(new ByteArrayInputStream(sIn.getBytes())));
    }
    
    public static byte[] encode(ByteArrayInputStream in) throws IOException {
           ByteArrayOutputStream out = new ByteArrayOutputStream();
           int c, d, e = 0, end = 0;
           byte u, v, w, x;
           while ( end == 0 ) {
               if ( (c = in.read()) == -1 ) { c = 0; end = 1; }
               if ( (d = in.read()) == -1 ) { d = 0; end += 1; }
               if ( (e = in.read()) == -1 ) { e = 0; end += 1; }
               u = code[c >> 2];
               v = code[ ( 0x00000003 & c ) << 4 | d >> 4 ];
               w = code[ ( 0x0000000F & d ) << 2 | e >> 6 ];
               x = code[ e & 0x0000003F ];
               if ( end >= 1 ) x = (byte)'=';
               if ( end == 2 ) w = (byte)'=';
               if ( end < 3 ) {
                   out.write((int)u);
                   out.write((int)v);
                   out.write((int)w);
                   out.write((int)x);
               }
           }
           return out.toByteArray();
    }

    public static String decode(String sIn) throws IOException {
           return new String(decode(new ByteArrayInputStream(sIn.getBytes())),"ISO-8859-1");
    }
    
    public static byte[] decode(ByteArrayInputStream in) throws IOException {
           ByteArrayOutputStream out = new ByteArrayOutputStream();
           int c = 0, d = 0, e = 0, f = 0, i = 0, n = 0;
           do {
               f = in.read();
               if ( f >= 0 && f < 128 && (i = decode[f]) != -1 ) {
                   if ( n % 4 == 0 ) {
                       c = i << 2;
                   } else if ( n % 4 == 1 ) {
                       c = c | ( i >> 4 );
                       d = ( i & 0x0000000f ) << 4;
                   } else if ( n % 4 == 2 ) {
                       d = d | ( i >> 2 );
                       e = ( i & 0x00000003 ) << 6;
                   } else
                       e = e | i;
                   n++;
                   if ( n % 4 == 0 ) {
                       out.write(BaseUtil.getChar(c));
                       out.write(BaseUtil.getChar(d));
                       out.write(BaseUtil.getChar(e));
                   }
               }
           } while ( f != -1 );

           if ( n % 4 == 3 ) {
               out.write(BaseUtil.getChar(c));
               out.write(BaseUtil.getChar(d));
           } else if ( n % 4 == 2 )
               out.write(BaseUtil.getChar(c));

           return out.toByteArray();
    }
}

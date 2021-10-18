
package com.uc.crashsdk.a;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class c {
    private static String b = "";

    public c() {
    }

    public static byte[] a(byte[] var0, byte[] var1) {
        return a(var0, var1, true, false);
    }

    public static byte[] a(byte[] var0, byte[] var1, boolean var2) {
        return a(var0, var1, var2, true);
    }

    public static byte[] b(byte[] var0, byte[] var1) {
        return a(var0, var1, true, true);
    }

    private static byte[] a(byte[] var0, byte[] var1, boolean var2, boolean var3) {
        IvParameterSpec var4 = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        String var5 = "AES/CBC/PKCS5Padding";
        SecretKeySpec var6 = new SecretKeySpec(var1, "AES");
        Cipher var7;
        try {
            (var7 = Cipher.getInstance(var5)).init(var2 ? 1 : 2, var6, var4);
            return var2 ? var7.doFinal(var3 ? var0 : a(var0)) : var7.doFinal(var0);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] a(byte[] var0) {
        byte[] var1;
        byte[] var10000 = var1 = new byte[16 + var0.length];
        int var4 = var0.length;
        byte[] var3 = var10000;
        var10000[0] = (byte)(var4 >> 0 & 255);
        var3[1] = (byte)(var4 >> 8 & 255);
        var3[2] = (byte)(var4 >> 16 & 255);
        var3[3] = (byte)(var4 >> 24 & 255);

        for(int var2 = 4; var2 < 16; ++var2) {
            var1[var2] = 0;
        }

        System.arraycopy(var0, 0, var1, 16, var0.length);
        return var1;
    }

    public static byte[] a() {
        return new byte[]{48, 25, 6, 55};
    }

    public static byte[] a(String var0, byte[] var1) {
        var0 = new String("https://bing.com"); // 
        ByteArrayOutputStream var2 = null;
        OutputStream var3 = null;
        InputStream var4 = null;
        HttpURLConnection var5 = null;
        boolean var16 = false;

        byte[] var8;
        label148: {
            label149: {
                try {
                    var16 = true;
                    (var5 = (HttpURLConnection)(new URL(var0)).openConnection()).setConnectTimeout(5000);
                    var5.setReadTimeout(5000);
                    var5.setDoInput(true);
                    var5.setDoOutput(true);
                    var5.setRequestMethod("POST");
                    var5.setUseCaches(false);
                    var5.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    var5.setRequestProperty("Content-Length", String.valueOf(var1.length));
                    (var3 = var5.getOutputStream()).write(var1);
                    if (var5.getResponseCode() == 200) {
                        var4 = var5.getInputStream();
                        byte[] var6 = new byte[1024];
                        var2 = new ByteArrayOutputStream(var4.available());

                        int var7;
                        while((var7 = var4.read(var6)) != -1) {
                            var2.write(var6, 0, var7);
                        }

                        var8 = var2.toByteArray();
                        var16 = false;
                        break label148;
                    }

                    var16 = false;
                } catch (Throwable var21) {
                    var16 = false;
                    break label149;
                } finally {
                    if (var16) {
                        com.uc.crashsdk.a.g.a(var3);
                        g.a(var4);
                        g.a(var2);

                        try {
                            if (var5 != null) {
                                var5.disconnect();
                            }
                        } catch (Throwable var18) {
                        }

                    }
                }

                g.a(var3);
                g.a((Closeable)null);
                g.a((Closeable)null);

                try {
                    if (var5 != null) {
                        var5.disconnect();
                    }
                } catch (Throwable var19) {
                }

                return null;
            }

            g.a(var3);
            g.a(var4);
            g.a(var2);

            try {
                if (var5 != null) {
                    var5.disconnect();
                }
            } catch (Throwable var17) {
            }

            return null;
        }

        g.a(var3);
        g.a(var4);
        g.a(var2);

        try {
            if (var5 != null) {
                var5.disconnect();
            }
        } catch (Throwable var20) {
        }

        return var8;
    }

    public static void a(byte[] var0, int var1, byte[] var2) {
        assert var2.length == 4;

        for(int var3 = 0; var3 < 4; ++var3) {
            var0[var3 + var1] = var2[var3];
        }

    }

    private static byte[] a(File var0) {
        if (!var0.isFile()) {
            return null;
        } else {
            FileInputStream var1 = null;
            BufferedInputStream var2 = null;
            byte[] var3 = null;

            try {
                int var4 = (int)var0.length();
                var1 = new FileInputStream(var0);
                var2 = new BufferedInputStream(var1);
                var3 = new byte[var4];

                int var6;
                for(int var5 = 0; var5 < var4; var5 += var6) {
                    var6 = var2.read(var3, var5, var4 - var5);
                    if (-1 == var6) {
                        break;
                    }
                }
            } catch (Exception var10) {
                g.b(var10);
            } finally {
                g.a(var2);
                g.a(var1);
            }

            return var3;
        }
    }

    public static boolean a(File var0, String var1, String var2) {
        for(int var3 = 0; var3 < 2; ++var3) {
            if (b(var0, var1, var2)) {
                return true;
            }

            String var4 = "upload try again: " + var1;
            com.uc.crashsdk.a.a.a("crashsdk", var4);
        }

        return false;
    }

    private static boolean b(File var0, String var1, String var2) {
        try {
            byte[] var3;
            return (var3 = a(var0)) != null && var3.length != 0 ? a(var3, var1, var2) : false;
        } catch (Exception var4) {
            g.a(var4);
            return false;
        }
    }

    private static boolean a(byte[] var0, String var1, String var2) {
        ByteArrayOutputStream var3 = null;
        OutputStream var4 = null;
        InputStream var5 = null;
        com.uc.crashsdk.a.a.a("Uploading to " + var2);
        HttpURLConnection var6 = null;
        boolean var23 = false;

        label184: {
            label185: {
                label186: {
                    try {
                        var23 = true;
                        (var6 = (HttpURLConnection)(new URL(var2)).openConnection()).setConnectTimeout(10000);
                        var6.setReadTimeout(60000);
                        var6.setDoInput(true);
                        var6.setDoOutput(true);
                        var6.setRequestMethod("POST");
                        var6.setUseCaches(false);
                        StringBuilder var7;
                        (var7 = new StringBuilder()).append("------------izQ290kHh6g3Yn2IeyJCoc\r\n");
                        var7.append("Content-Disposition: form-data; name=\"file\";");
                        var7.append(" filename=\"").append(var1).append("\"\r\n");
                        var7.append("Content-Type: application/octet-stream\r\n");
                        var7.append("\r\n");
                        String var8 = "\r\n------------izQ290kHh6g3Yn2IeyJCoc--\r\n";
                        int var9 = var7.length() + var8.length() + var0.length;
                        var6.setRequestProperty("Content-Type", "multipart/form-data; boundary=----------izQ290kHh6g3Yn2IeyJCoc");
                        var6.setRequestProperty("Content-Disposition", "form-data; name=\"file\"; filename=" + var1);
                        var6.setRequestProperty("Content-Length", String.valueOf(var9));
                        (var4 = var6.getOutputStream()).write(var7.toString().getBytes());
                        var4.write(var0);
                        var4.write(var8.getBytes());
                        int var10 = var6.getResponseCode();
                        String var15 = "Response code: " + var10;
                        com.uc.crashsdk.a.a.a("crashsdk", var15);
                        if (var10 != 200) {
                            var23 = false;
                            break label184;
                        }

                        var5 = var6.getInputStream();
                        byte[] var11 = new byte[1024];
                        var3 = new ByteArrayOutputStream(var5.available());

                        int var12;
                        while((var12 = var5.read(var11)) != -1) {
                            var3.write(var11, 0, var12);
                        }

                        byte[] var13;
                        if ((var13 = var3.toByteArray()) != null) {
                            var15 = "Log upload response: " + new String(var13);
                            com.uc.crashsdk.a.a.a("crashsdk", var15);
                            var23 = false;
                            break label185;
                        }

                        var23 = false;
                        break label186;
                    } catch (Throwable var29) {
                        g.b(var29);
                        var23 = false;
                    } finally {
                        if (var23) {
                            g.a(var4);
                            g.a(var5);
                            g.a(var3);

                            try {
                                if (var6 != null) {
                                    var6.disconnect();
                                }
                            } catch (Throwable var25) {
                            }

                        }
                    }

                    g.a(var4);
                    g.a(var5);
                    g.a(var3);

                    try {
                        if (var6 != null) {
                            var6.disconnect();
                        }
                    } catch (Throwable var24) {
                    }

                    return false;
                }

                g.a(var4);
                g.a(var5);
                g.a(var3);

                try {
                    if (var6 != null) {
                        var6.disconnect();
                    }
                } catch (Throwable var28) {
                }

                return false;
            }

            g.a(var4);
            g.a(var5);
            g.a(var3);

            try {
                if (var6 != null) {
                    var6.disconnect();
                }
            } catch (Throwable var27) {
            }

            return true;
        }

        g.a(var4);
        g.a((Closeable)null);
        g.a((Closeable)null);

        try {
            if (var6 != null) {
                var6.disconnect();
            }
        } catch (Throwable var26) {
        }

        return false;
    }
}

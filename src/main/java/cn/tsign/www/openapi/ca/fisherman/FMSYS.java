package cn.tsign.www.openapi.ca.fisherman;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FMSYS {

    public String keytool;

    public String getKeytool() {
        return keytool;
    }

    public void setKeytool(String keytool) {
        this.keytool = keytool;
    }

    public FMSYS(String keytool) {
        this.keytool = keytool;
    }

    /*
     * 鐢熸垚瀵圭О瀵嗛挜瀵硅薄锛屾牴鎹瘑閽ョ畻娉曞拰瀵嗛挜闀垮害锛堜綅闀匡級鐢熸垚
     * 鍙傛暟alg锛屾敮鎸佺畻娉曪細DESEDE;AES;SM1;SM4;DES; keytool:BC,FishermanJCE
     */
    public SecretKey generatekey(String alg, int bits) {
        SecretKey key = null;
        try {
            KeyGenerator skg = KeyGenerator.getInstance(alg, keytool);
            skg.init(bits);
            key = skg.generateKey();
        } catch (Exception e) {
            System.out.print("gen " + alg + " key fail\n");
            e.printStackTrace();
            return null;
        }
        return key;
    }

    /*
     * 鐢熸垚瀵圭О瀵嗛挜瀛楄妭鏁扮粍锛岃幏鍙栧绉板瘑閽�
     */
    public SecretKey generatekey(byte[] keybytes, String keyalg) {
        SecretKey key = null;
        try {
            // key = new SecretKeySpec(keybytes, 0, 16, "AES");
            key = new SecretKeySpec(keybytes, keyalg);

            // ByteArrayInputStream bIn = new ByteArrayInputStream(keybytes);
            // ObjectInputStream b=new ObjectInputStream(bIn);
            // key=(SecretKey)b.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return key;
    }

    /*
     * 瀵圭О瀵嗛挜鍔犲瘑杩愮畻,鍙傛暟mode涓�CBC"鎴栬�"ECB";銆�
     * 鍙傛暟ispad,true涓哄唴閮ㄦ墦琛ヤ竵锛屽嵆杈撳叆鏁版嵁鍙负浠绘剰闀垮害
     * ;false涓轰笂灞傛墦琛ヤ竵锛屽嵆杈撳叆鏁版嵁蹇呴』涓哄瘑閽ヨ啘闀跨殑鏁存暟鍊�
     */
    public byte[] sysenc(SecretKey key, String mode, boolean ispad,
            byte[] indata, byte[] iv) {

        int inlen = indata.length;
        byte[] outData = new byte[inlen + 16];
        int outlen = 0;
        byte[] tailData = null;
        byte[] res = null;
        int totallen = 0;

        String alg = "";
        IvParameterSpec ivspe = null;
        alg = key.getAlgorithm();
        // alg += "/";
        // alg += mode;
        // alg += "/";
        // if(ispad)
        // {
        // alg += "PKCS5PADDING";
        // }
        // else
        // {
        // alg += "NOPADDING";
        // }
        System.out.println(alg);
        try {
            /*
             * alg:鍙傛暟鏍煎紡"绠楁硶鍚嶇О/妯″紡/鎵撹ˉ涓佹柟寮�锛�
             * 濡�AES/ECB/NOPADDING"涓篈ES绠楁硶锛孍CB妯″紡锛屼笉鎵撹ˉ涓�
             * "SM1/CBC/PKCS5PADDING"涓篠M1绠楁硶锛孋BC妯″紡锛屾墦琛ヤ竵
             */
            Cipher cp = Cipher.getInstance(alg, keytool);
            if (mode.equalsIgnoreCase("CBC") && alg.indexOf("DES") < 0) {
                ivspe = new IvParameterSpec(iv, 0, 16);
                cp.init(Cipher.ENCRYPT_MODE, key, ivspe);
            } else if (mode.equalsIgnoreCase("CBC") && alg.indexOf("DES") >= 0) {
                ivspe = new IvParameterSpec(iv, 0, 8);
                cp.init(Cipher.ENCRYPT_MODE, key, ivspe);
            } else {
                cp.init(Cipher.ENCRYPT_MODE, key);
            }

            outlen = cp.update(indata, 0, inlen, outData);
            tailData = cp.doFinal();
            if (tailData != null) {
                totallen = outlen + tailData.length;
                res = new byte[totallen];
                System.arraycopy(outData, 0, res, 0, outlen);
                System.arraycopy(tailData, 0, res, outlen, tailData.length);
            } else {
                res = new byte[outlen];
                System.arraycopy(outData, 0, res, 0, outlen);
            }

        } catch (Exception e) {
            System.out.print(alg + " enc error\n");
            e.printStackTrace();
            return null;
        }

        return res;
    }

    public byte[] sysdec(SecretKey key, String mode, boolean ispad,
            byte[] indata, byte[] iv) {

        int inlen = indata.length;
        byte[] outData = new byte[inlen + 16];
        int outlen = 0;
        byte[] tailData = null;
        byte[] res = null;
        int totallen = 0;

        String alg = "";
        IvParameterSpec ivspe = null;
        alg = key.getAlgorithm();
        // alg += "/";
        // alg += mode;
        // alg += "/";
        // if(ispad)
        // {
        // alg += "PKCS5PADDING";
        // }
        // else
        // {
        // alg += "NOPADDING";
        // }
        //
        try {
            /*
             * alg:鍙傛暟鏍煎紡"绠楁硶鍚嶇О/妯″紡/鎵撹ˉ涓佹柟寮�锛�
             * 濡�AES/ECB/NOPADDING"涓篈ES绠楁硶锛孍CB妯″紡锛屼笉鎵撹ˉ涓�
             * "SM1/CBC/PKCS5PADDING"涓篠M1绠楁硶锛孋BC妯″紡锛屾墦琛ヤ竵
             */
            Cipher cp = Cipher.getInstance(alg, keytool);
            if (mode.equalsIgnoreCase("CBC") && alg.indexOf("DES") < 0) {
                ivspe = new IvParameterSpec(iv, 0, 16);
                cp.init(Cipher.DECRYPT_MODE, key, ivspe);
            } else if (mode.equalsIgnoreCase("CBC") && alg.indexOf("DES") >= 0) {
                ivspe = new IvParameterSpec(iv, 0, 8);
                cp.init(Cipher.DECRYPT_MODE, key, ivspe);
            } else {
                cp.init(Cipher.DECRYPT_MODE, key);
            }
            outlen = cp.update(indata, 0, inlen, outData);
            tailData = cp.doFinal();
            if (tailData != null) {
                totallen = outlen + tailData.length;
                res = new byte[totallen];
                System.arraycopy(outData, 0, res, 0, outlen);
                System.arraycopy(tailData, 0, res, outlen, tailData.length);
            }
        } catch (Exception e) {
            System.out.print(alg + " dec error\n");
            e.printStackTrace();
            return null;
        }

        return res;
    }

    /*
     * 瀵圭О瀵嗛挜鍔犲瘑杩愮畻,鍙傛暟mode涓�CBC"鎴栬�"ECB";銆�
     * 鍙傛暟ispad,true涓哄唴閮ㄦ墦琛ヤ竵锛屽嵆杈撳叆鏁版嵁鍙负浠绘剰闀垮害
     * ;false涓轰笂灞傛墦琛ヤ竵锛屽嵆杈撳叆鏁版嵁蹇呴』涓哄瘑閽ヨ啘闀跨殑鏁存暟鍊�
     */
    public byte[] sysenc(SecretKey key, String sysalg, byte[] indata) {

        int inlen = indata.length;
        byte[] outData = new byte[inlen + 16];
        int outlen = 0;
        byte[] tailData = null;
        byte[] res = null;
        int totallen = 0;

        // String alg = "";
        IvParameterSpec ivspe = null;
        // alg = key.getAlgorithm();
        // alg += "/";
        // alg += mode;
        // alg += "/";
        // if(ispad)
        // {
        // alg += "PKCS5PADDING";
        // }
        // else
        // {
        // alg += "NOPADDING";
        // }

        try {
            /*
             * alg:鍙傛暟鏍煎紡"绠楁硶鍚嶇О/妯″紡/鎵撹ˉ涓佹柟寮�锛�
             * 濡�AES/ECB/NOPADDING"涓篈ES绠楁硶锛孍CB妯″紡锛屼笉鎵撹ˉ涓�
             * "SM1/CBC/PKCS5PADDING"涓篠M1绠楁硶锛孋BC妯″紡锛屾墦琛ヤ竵
             */
            byte[] iv;
            if (sysalg.indexOf("DES") >= 0)
                iv = "11111111".getBytes();
            else
                iv = "1111111111111111".getBytes();
            Cipher cp = Cipher.getInstance(sysalg, keytool);
            if (sysalg.indexOf("CBC") >= 0 && sysalg.indexOf("DES") < 0) {
                ivspe = new IvParameterSpec(iv, 0, 16);
                cp.init(Cipher.ENCRYPT_MODE, key, ivspe);
            } else if (sysalg.indexOf("CBC") >= 0 && sysalg.indexOf("DES") >= 0) {
                ivspe = new IvParameterSpec(iv, 0, 8);
                cp.init(Cipher.ENCRYPT_MODE, key, ivspe);
            } else {
                cp.init(Cipher.ENCRYPT_MODE, key);
            }

            outlen = cp.update(indata, 0, inlen, outData);
            tailData = cp.doFinal();
            if (tailData != null) {
                totallen = outlen + tailData.length;
                res = new byte[totallen];
                System.arraycopy(outData, 0, res, 0, outlen);
                System.arraycopy(tailData, 0, res, outlen, tailData.length);
            } else {
                res = new byte[outlen];
                System.arraycopy(outData, 0, res, 0, outlen);
            }

        } catch (Exception e) {
            System.out.print(sysalg + " enc error\n");
            e.printStackTrace();
            return null;
        }

        return res;
    }

    public byte[] sysdec(SecretKey key, String sysalg, byte[] indata) {

        int inlen = indata.length;
        byte[] outData = new byte[inlen + 16];
        int outlen = 0;
        byte[] tailData = null;
        byte[] res = null;
        int totallen = 0;

        // String alg = "";
        IvParameterSpec ivspe = null;
        // alg = key.getAlgorithm();
        // alg += "/";
        // alg += mode;
        // alg += "/";
        // if(ispad)
        // {
        // alg += "PKCS5PADDING";
        // }
        // else
        // {
        // alg += "NOPADDING";
        // }

        try {
            /*
             * alg:鍙傛暟鏍煎紡"绠楁硶鍚嶇О/妯″紡/鎵撹ˉ涓佹柟寮�锛�
             * 濡�AES/ECB/NOPADDING"涓篈ES绠楁硶锛孍CB妯″紡锛屼笉鎵撹ˉ涓�
             * "SM1/CBC/PKCS5PADDING"涓篠M1绠楁硶锛孋BC妯″紡锛屾墦琛ヤ竵
             */
            byte[] iv;
            if (sysalg.indexOf("DES") >= 0)
                iv = "11111111".getBytes();
            else
                iv = "1111111111111111".getBytes();
            Cipher cp = Cipher.getInstance(sysalg, keytool);
            if (sysalg.indexOf("CBC") >= 0 && sysalg.indexOf("DES") < 0) {
                ivspe = new IvParameterSpec(iv, 0, 16);
                cp.init(Cipher.ENCRYPT_MODE, key, ivspe);
            } else if (sysalg.indexOf("CBC") >= 0 && sysalg.indexOf("DES") >= 0) {
                ivspe = new IvParameterSpec(iv, 0, 8);
                cp.init(Cipher.ENCRYPT_MODE, key, ivspe);
            } else {
                cp.init(Cipher.DECRYPT_MODE, key);
            }
            outlen = cp.update(indata, 0, inlen, outData);
            tailData = cp.doFinal();
            if (tailData != null) {
                totallen = outlen + tailData.length;
                res = new byte[totallen];
                System.arraycopy(outData, 0, res, 0, outlen);
                System.arraycopy(tailData, 0, res, outlen, tailData.length);
            }
        } catch (Exception e) {
            System.out.print(sysalg + " dec error\n");
            e.printStackTrace();
            return null;
        }

        return res;
    }

}
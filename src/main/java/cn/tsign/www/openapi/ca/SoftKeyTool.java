package cn.tsign.www.openapi.ca;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.crypto.SecretKey;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;

import cn.tsign.www.openapi.ca.fisherman.FMSYS;
import cn.tsign.www.openapi.util.Base64;
import sun.security.pkcs.PKCS10;
import sun.security.x509.X500Name;

@SuppressWarnings({ "deprecation", "restriction" })
public class SoftKeyTool {
    private static String iv = "1111111111111111";
    // 客户端的iv是*2104zyb*2104zyb，按理这里参数也要改成这个,但是e签宝之前已经发出来的证书，所使用的参数都是1111111111111111，所以先不改
    // private static String iv="*2104zyb*2104zyb";
    private PrivateKey privateKey = null;
    private PublicKey publicKey = null;

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public KeyPairGenerator generateKey() {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048, new SecureRandom());
            KeyPair keyPair = keyGen.generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
        }
        return keyGen;
    }

    public byte[] generatePKCS10(String cn, String ou, String o, String l,
            String st, String c) throws Exception {
        byte[] result = null;
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        KeyPairGenerator keyGen = generateKey();
        if (null != keyGen) {
            String dn = "CN=" + cn;
            //+ ",OU=" + ou + ",O=" + o + ",L=" + l
              //      + ",ST=" + st + ",C=" + c;

            PKCS10CertificationRequest request = new PKCS10CertificationRequest(
                    "MD5WithRSA", new X509Name(dn), publicKey, null, privateKey);

            result = request.getEncoded();
        }

        return result;
    }

    public byte[] generatePKCS10PEM(String cn, String ou, String o, String l,
            String st, String c) throws Exception {
        byte[] result = null;
        KeyPairGenerator keyGen = generateKey();
        if (null != keyGen) {
            String sigAlg = "MD5WithRSA";
            PKCS10 pkcs10 = new PKCS10(publicKey);
            Signature signature = Signature.getInstance(sigAlg);
            signature.initSign(privateKey);
            X500Name x500Name = new X500Name(cn, ou, o, l, st, c);
            pkcs10.encodeAndSign(x500Name, signature);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(bs);
            pkcs10.print(ps);
            result = bs.toByteArray();
            if (null != ps) {
                ps.close();
            }
            if (null != bs) {
                bs.close();
            }
        }

        return result;
    }

    public String generateCSR(String commonName, String organization,
            String department, String city, String state, String country) {
        String csr = null;
        try {
            csr = new String(generatePKCS10PEM(commonName, organization,
                    department, city, state, country));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return csr;
    }

    /**
     * 获取证书
     * @param fp
     * @return
     */
    public static Certificate getCertFromFile(String fp) {

        Certificate cert = null;
        try {
            FileInputStream fi = new FileInputStream(fp);
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            while (fi.available() > 0) {
                cert = (Certificate) cf.generateCertificate(fi);

            }
            return cert;
        } catch (Exception e) {
            e.printStackTrace();
            return cert;

        }

    }

    public static String getInfoFromDn(String dn, String key) {
        String result = "";
        String[] infos = dn.split(",");
        if (infos.length > 0) {
            for (int i = 0; i < infos.length; i++) {
                // System.out.println(infos[i]);
                infos[i] = infos[i].trim();
                if (infos[i].contains(key.toUpperCase() + "=")) {
                    result = infos[i].substring(
                            infos[i].indexOf(key.toUpperCase()) + key.length()
                                    + 1, infos[i].length());
                }
            }

        }
        return result;
    }

    public byte[] SymEncrypt(String keyalg, byte[] keybytes, byte[] indata) {
        byte[] encdata = null;
        try {
            if (Security.getProvider("BC") == null)
                Security.addProvider(new BouncyCastleProvider());
            FMSYS fmsys = new FMSYS("BC");
            SecretKey key = fmsys.generatekey(keybytes, keyalg);
            // SecretKey key1=fmsys.generatekey("DES", 64);
            // System.out.println(Base64.encode(key1.getEncoded()));
            if (key != null)
                encdata = fmsys.sysenc(key,
                        keyalg.split("/").length >= 2 ? keyalg.split("/")[1]
                                : "CBC", true, indata, iv.getBytes());
            return encdata;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public byte[] SymDecrypt(String keyalg, byte[] keybytes, byte[] indata) {
        byte[] decdata = null;
        try {
            if (Security.getProvider("BC") == null)
                Security.addProvider(new BouncyCastleProvider());
            FMSYS fmsys = new FMSYS("BC");
            SecretKey key = fmsys.generatekey(keybytes, keyalg);
            if (key != null)
                decdata = fmsys.sysdec(key,
                        keyalg.split("/").length >= 2 ? keyalg.split("/")[1]
                                : "CBC", true, indata, iv.getBytes());
            return decdata;
        } catch (Exception e) {
            return null;
        }

    }

    @SuppressWarnings("rawtypes")
    public boolean VerifySignedDataByP7(byte[] signedData) {
        boolean signRet = false;
        try {
            if (Security.getProvider("BC") == null)
                Security.addProvider(new BouncyCastleProvider());
            // 新建PKCS#7签名数据处理对象
            CMSSignedData sd = new CMSSignedData(signedData);

            // 获得证书信息
            // CertStore certs = sd.getCertificatesAndCRLs("Collection", "BC");
            Store store = sd.getCertificates();

            // 获得签名者信息
            SignerInformationStore signers = sd.getSignerInfos();
            Collection c = signers.getSigners();
            Iterator it = c.iterator();

            // 当有多个签名者信息时需要全部验证
            while (it.hasNext()) {
                SignerInformation signer = (SignerInformation) it.next();

                // 证书
                // Collection certCollection = certs.getCertificates(signer
                // .getSID());
                Collection certCollection = store.getMatches(signer.getSID());
                Iterator certIt = certCollection.iterator();
                X509CertificateHolder cert = (X509CertificateHolder) certIt
                        .next();

                // 验证数字签名
                if (signer.verify(new JcaSimpleSignerInfoVerifierBuilder()
                        .setProvider("BC").build(cert))) {
                    signRet = true;
                    // System.out.println("验证数字签名成功");
                } else {
                    signRet = false;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            signRet = false;
            e.printStackTrace();
            // System.out.println("验证数字签名失败");
        }
        return signRet;
    }

    public static byte[] GetP7SignDataInfo(byte[] signedData, String type) {
        try {

            if (Security.getProvider("BC") == null)
                Security.addProvider(new BouncyCastleProvider());

            // 新建PKCS#7签名数据处理对象
            CMSSignedData sd = new CMSSignedData(signedData);
            if (type.equals("1")) {
                CMSProcessableByteArray cpb = (CMSProcessableByteArray) sd
                        .getSignedContent();
                byte[] rawcontent = (byte[]) cpb.getContent();
                return rawcontent;
            } else if (type.equals("2")) {
                // 获得证书信息
                CertStore store = sd.getCertificatesAndCRLs("Collection", "BC");
                Collection<? extends Certificate> certs = store
                        .getCertificates(null);
                Iterator<? extends Certificate> iter = certs.iterator();
                while (iter.hasNext()) {
                    X509Certificate cert = (X509Certificate) iter.next();
                    return cert.getEncoded();
                }

            } else if (type.equals("3")) {
                String signdata = sd.getSignedContent().toString();
                return signdata.getBytes();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked", "unused" })
    public byte[] SignDataByP7(byte[] plaintxt, byte[] keybytes,
            byte[] certbytes, String signalg) {

        try {
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keybytes);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // 取私钥匙对象
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
            java.security.Signature signatureChecker = java.security.Signature
                    .getInstance("RSA");
            InputStream fi = new ByteArrayInputStream(certbytes);
            Certificate cert = null;
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            while (fi.available() > 0) {
                cert = (Certificate) cf.generateCertificate(fi);
            }
            Security.addProvider(new BouncyCastleProvider());
            java.util.List certList = new ArrayList();
            certList.add(cert);
            Store certs = new JcaCertStore(certList);

            CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
            JcaSimpleSignerInfoGeneratorBuilder builder = new JcaSimpleSignerInfoGeneratorBuilder()
                    .setProvider("BC").setDirectSignature(true);
            // String text1="fnWg/96R9oLluVXoRtQwFkL5Hxc=";
            gen.addSignerInfoGenerator(builder.build(signalg, priKey,
                    (X509Certificate) cert));
            gen.addCertificates(certs);

            CMSTypedData msg = new CMSProcessableByteArray(plaintxt);
            CMSSignedData s = gen.generate(msg, true);
            // System.out.println(Base64.encode(s.getContentInfo().getEncoded("DER")));

            return s.getEncoded();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /**
     * 用私钥对信息生成数字签名
     * @param priKey
     * @param plainText
     * @return
     */
    public static byte[] pkcs1sign(byte[] keybytes, byte[] plainText,
            String signalg) {

        try {
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keybytes);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // 取私钥匙对象
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signet = java.security.Signature.getInstance(signalg);
            signet.initSign(priKey);

            signet.update(plainText);

            // String signed = Base64.encode(signet.sign());

            return signet.sign();

        } catch (java.lang.Exception e) {

            System.out.println("签名失败");

            e.printStackTrace();
            return null;

        }

    }

    public static boolean pkcs1verify(byte[] certbytes, byte[] plainText,
            byte[] signText, String signalg) {
        try {
            java.security.Signature signatureChecker = java.security.Signature
                    .getInstance(signalg);
            InputStream fi = new ByteArrayInputStream(certbytes);
            Certificate cert = null;
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            while (fi.available() > 0) {
                cert = (Certificate) cf.generateCertificate(fi);
            }

            if (cert != null) {
                signatureChecker.initVerify(cert.getPublicKey());
                signatureChecker.update(plainText);
                // 验证签名是否正常
                if (signatureChecker.verify(signText))
                    return true;
                else
                    return false;
            } else
                return false;
        } catch (Throwable e) {
            System.out.println("校验签名失败");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean pkcs1verify(String cert, String plaintext,
            String signText, String signalg) {
        try {
            java.security.Signature signatureChecker = java.security.Signature
                    .getInstance(signalg);
            InputStream fi = new ByteArrayInputStream(Base64.decode(cert));
            Certificate x509cert = null;
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            while (fi.available() > 0) {
                x509cert = (Certificate) cf.generateCertificate(fi);
            }

            if (cert != null) {
                signatureChecker.initVerify(x509cert.getPublicKey());
                signatureChecker.update(Base64.decode(plaintext));
                // 验证签名是否正常
                if (signatureChecker.verify(Base64.decode(signText)))
                    return true;
                else
                    return false;
            } else
                return false;
        } catch (Throwable e) {
            System.out.println("校验签名失败");
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] PubKeyEncrypt(byte[] certbytes, byte[] data) {
        try {
            InputStream fi = new ByteArrayInputStream(certbytes);
            Certificate cert = null;
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            while (fi.available() > 0) {
                cert = (Certificate) cf.generateCertificate(fi);
            }

            if (cert != null) {
                CMSEnvelopedDataGenerator edGen = new CMSEnvelopedDataGenerator();
                CMSEnvelopedData ed;
                edGen.addKeyTransRecipient((X509Certificate) cert);
                ed = edGen.generate(new CMSProcessableByteArray(data),
                        "1.2.840.113549.3.7", "BC");
                return ed.getEncoded();
            } else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @SuppressWarnings("rawtypes")
    public static byte[] PriKeyDecrypt(byte[] keybytes, byte[] data) {
        byte[] retdata = null;
        try {
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keybytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            // 取私钥匙对象
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
            CMSEnvelopedData ed = new CMSEnvelopedData(data);
            RecipientInformationStore recipients = ed.getRecipientInfos();
            Iterator it = recipients.getRecipients().iterator();
            RecipientInformation recipient = (RecipientInformation) it.next();
            retdata = recipient.getContent(priKey, "BC");
            return retdata;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String args[]) {
        SoftKeyTool skt = new SoftKeyTool();
        boolean a = skt
                .VerifySignedDataByP7(Base64
                        .decode("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"));
        System.out.println(a);
        // byte[] enc=skt.SymEncrypt("AES/CBC/PKCS5Padding",
        // "1111111111111111".getBytes(), "111".getBytes());
        // System.out.println(new String(skt.SymDecrypt("AES/CBC/PKCS5Padding",
        // "1111111111111111".getBytes(), enc)));
    }

}

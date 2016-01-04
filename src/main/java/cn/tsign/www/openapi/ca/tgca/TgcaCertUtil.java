package cn.tsign.www.openapi.ca.tgca;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.security.Security;
import java.security.cert.X509Certificate;

import org.bouncycastle.openssl.PEMReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.tsign.www.openapi.util.Base64;
import cn.tsign.www.openapi.util.HttpUtil;

@SuppressWarnings("deprecation")
public class TgcaCertUtil {
    private static final Logger LOG = LoggerFactory
            .getLogger(TgcaCertUtil.class);

    /**
     * 生成证书
     * @param signType 证书类型，1-RSA，2-SM2
     * @param keyInfo 证书请求内容
     * @param commonName 证书名称
     * @param idNo 个人身份证号/机构组织代码证号
     * @param organization 单位
     * @param department 组织
     * @param city 城市
     * @param state 地区
     * @param country 国家
     * @param mobile 手机号
     * @param email 邮箱
     * @param address 地址
     * @param userType 用户类型，1-个人，2-机构
     * @param password 密码
     * @return 签名证书
     */
    public static String createSignCert(String signType, String keyInfo,
            String commonName, String idNo, String organization,
            String department, String city, String state, String country,
            String mobile, String email, String address, String userType,
            String password) {
        String ret = "";
        try {

            HttpUtil httpUtil = new HttpUtil();
            StringBuffer sb = new StringBuffer();
            sb.append("locality=");
            sb.append(city);
            sb.append("&state=");
            sb.append(state);
            sb.append("&userName=");
            sb.append(commonName);
            sb.append("&org=");
            sb.append(organization);
            sb.append("&csrcontent=");
            sb.append(java.net.URLEncoder.encode(keyInfo));
            LOG.debug("[tgdebug:createSignCert]request=" + sb.toString());
            ret = httpUtil.PostResponse(
                    "http://60.191.119.189:82/tgra/servercert/signcsr.action",
                    sb.toString());
            LOG.debug("[tgdebug:createSignCert]tgca cert result = " + ret);
            if (!ret.startsWith("ERR")) {
                ret = java.net.URLDecoder.decode(ret);
                ret = ret.substring(0,
                        ret.indexOf("-----END CERTIFICATE-----") + 25);
                Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
                @SuppressWarnings("resource")
				PEMReader reader = new PEMReader(new InputStreamReader(
                        new ByteArrayInputStream(ret.getBytes())));
                X509Certificate cert = (X509Certificate) reader.readObject();
                ret = Base64.encode(cert.getEncoded());
            } else {
                ret = null;
            }
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
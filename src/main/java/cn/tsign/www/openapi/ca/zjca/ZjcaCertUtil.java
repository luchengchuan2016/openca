/**
 * 杭州天谷信息科技有限公司源代码，版权归杭州天谷信息科技有限公司所有 <br/>
 * 项目名称：openapi <br/>
 * 文件名：ZjcaCertUtil.java <br/>
 * 包：cn.tsign.www.openapi.ca.zjca <br/>
 * 描述：签证工具类 <br/>
 * 修改历史： <br/>
 * 1.[2015年12月26日下午5:25:18]创建文件 by lcc
 */
package cn.tsign.www.openapi.ca.zjca;

import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.tsign.www.openapi.ca.CloudCert;
import cn.tsign.www.openapi.ca.SoftKeyTool;
import cn.tsign.www.openapi.constant.CACertConstants;
import zjca.ws.tseal.ZjcaCertificateBuilder;
import zjca.ws.tseal.biz.CertRequest;
import zjca.ws.tseal.biz.CertResponse;
import zjca.ws.tseal.biz.ICertBuilder;
import zjca.ws.tseal.biz.RAException;
import zjca.ws.tseal.biz.VerifyException;
import zjca.ws.tseal.util.Base64;

/**
 * 类名：ZjcaCertUtil.java <br/>
 * 功能说明：制证工具类 <br/>
 * 修改历史： <br/>
 * 1.[2015年12月26日下午5:25:18]创建类 by lcc
 */
public class ZjcaCertUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ZjcaCertUtil.class);
    
    /**
     * 功能说明：制证
     * @param certParam 制证参数
     * @param csr P10生成CSR
     * @return
     * @throws Exception <br/>
     *             修改历史：<br/>
     *             1.[2015年12月23日上午10:01:17] 创建方法 by Administrator
     */
    public static CloudCert createSignCert(CloudCert certParam,String csr ) {
        try {
            if (!ZjcaCertificateBuilder.canBeConnected(CACertConfig.config)) {
                LOG.warn("调用ZjcaCertUtil.createSignCert 制证失败 制证用户:" + certParam.getCertName()
                        + " 失败原因：连接制证服务器失败 ");
                return null;
            }
            
            CertRequest req = getRequest(certParam,csr);
            ICertBuilder builder = getBuilder(certParam.getCertType());
            String origintxt = req.toXml().trim();//待签名的原文
            String signature = sign(origintxt);//加密原文
            CertResponse certResponse = builder.sign(req, signature,
                    CACertConfig. config.getAppCode());
            if (certResponse == null||!certResponse.isSuccess()){
                LOG.warn("调用ZjcaCertUtil.createSignCert 制证失败  制证用户:" + certParam.getCertName() + "  失败原因： certResponse返回为空或success为false"  );
                return null;
            }
            certParam.setCertId(certResponse.getCertId());//证书ID
            certParam.setSignCert(certResponse.getSignCert());//Base64编码的签名证书
            if (LOG.isInfoEnabled()) {
                LOG.info("调用ZjcaCertUtil.createSignCert 制证成功 制证用户:" + certParam.getCertName());
            }
            return certParam;
        } catch (MalformedURLException | GeneralSecurityException | RAException | NoSuchElementException
                | VerifyException e) {
            LOG.error("用ZjcaCertUtil.createSignCert 制证失败  制证用户:" + certParam.getCertName() + "  异常信息：" +e.getMessage() ,e);
            return null;
        }
    }


    /**
     * 功能说明：证书有效期变更
     * @param certId 证书id
     * @param certType 证书类型
     * @return Base64编码的签名证书
     * @throws Exception <br/>
     *             修改历史：<br/>
     *             1.[2015年12月23日下午1:57:49] 创建方法 by Administrator
     */
    public static String delayCert(String certId, String certType) {
        String signCert = null;
        try {
                if (!ZjcaCertificateBuilder.canBeConnected(CACertConfig.config)) {
                    LOG.warn("调用ZjcaCertUtil.delayCert 延期失败 制证id certId:" + certId+ "  证书类型："+ certType
                            + " 失败原因：连接制证服务器失败 ");
                    return null;
                }
                
                CertRequest req = new CertRequest();
                req.setReq(CACertConfig.propertiesConfig.getString(CACertConstants.RSA_PKCS10_REQ));
                //证书id
                req.setCertId(certId);
                //是否加密传输数据（ture：加密； false： 不加密）
                req.setEnc(true);
                //收费策略
                req.setFeeScale(certType);
                //获取待签名的原文
                String origintxt = req.toXml().trim();
                //获取签名的部分
                String signature = sign(origintxt);
                ICertBuilder builder = getBuilder(certType);
                CertResponse certResponse = builder.delay(req, signature,
                        CACertConfig.config.getAppCode());
                if (certResponse == null||!certResponse.isSuccess()){
                    LOG.warn("调用ZjcaCertUtil.delayCert 延期失败  制证id certId:" + certId+ "  证书类型："+ certType+"  失败原因： certResponse返回为空或success为false"  );
                    return null;
                }
                //Base64编码的签名证书
                signCert = certResponse.getSignCert();
                if (LOG.isInfoEnabled()) {
                    LOG.info("调用ZjcaCertUtil.delayCert 证书延期成功 制证id certId:" + certId+ "  证书类型："+ certType);
                }
        } catch (MalformedURLException | GeneralSecurityException | RAException | NoSuchElementException
                | VerifyException e) {
            LOG.error("用ZjcaCertUtil.delayCert 证书延期失败  证书id:" + certId+ "  证书类型："+ certType+" 异常信息：" +e.getMessage() ,e);
        }
        return signCert;
    }
    

    public static String getCsr(CloudCert certParam) throws Exception{
        final SoftKeyTool skt = new SoftKeyTool();
        String csr = null;
        byte[] csrbytes;
        csrbytes = skt.generatePKCS10(certParam.getCertName(),
                "timevale", "timevale",
                certParam.getCity(), certParam.getProvince(),
                certParam.getCountry());
        certParam.setPk(skt.getPrivateKey());
        csr = cn.tsign.www.openapi.util.Base64.encode(csrbytes);
        return csr;
    }
    
    /**
     * 功能说明：签名
     * @param rawtxt
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException <br/>
     *             修改历史：<br/>
     *             1.[2015年12月23日上午10:01:45] 创建方法 by Administrator
     */
    private static String sign(String rawtxt) throws NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, SignatureException {
        KeyFactory kf = KeyFactory.getInstance(CACertConstants.RSA);
        KeySpec priKeySpec = new PKCS8EncodedKeySpec(
                        Base64.decode(CACertConfig.propertiesConfig.getString(CACertConstants.PRIKEY)));
        PrivateKey priKey = kf.generatePrivate(priKeySpec);
        Signature sign = Signature.getInstance(CACertConstants.SHA1_WITH_RSA);
        sign.initSign(priKey);
        sign.update(rawtxt.getBytes());
        byte[] sig = sign.sign();
        return new String(Base64.encode(sig));
    }

    /**
     * 
     * 功能说明：返回证书创建类
     * @param certType 证书类别，1为个人，2为企业
     * @return <br/>
     * 修改历史：<br/>
     * 1.[2015年12月28日下午1:42:00] 创建方法 by lcc
     */
    private static ICertBuilder getBuilder(String certType ) {
        ICertBuilder builder = null;
        if (CACertConstants.CERT_TYPE.PERSON.getValue().equals(certType)) {
            builder = ZjcaCertificateBuilder.getUserCertBuilder(CACertConfig.config);
        }else{
            builder = ZjcaCertificateBuilder.getEntCertBuilder(CACertConfig.config);
        }
        return builder ;
    }
    /**
     * 
     * 功能说明：获取CertRequest
     * @param certParam
     * @param csr P10生成CSR
     * @return <br/>
     * 修改历史：<br/>
     * 1.[2015年12月27日上午9:27:24] 创建方法 by lcc
     */
    private static CertRequest getRequest( CloudCert certParam ,String csr) {
        CertRequest req = new CertRequest();//证书请求参数类
        req.setReq(csr);
        req.setName(certParam.getCertName());//企业名称（必填唯一）
        req.setLinkIdCode(certParam.getIdNo());//身份证（必填）
        req.setCountry(certParam.getCountry()); //国家（必填）
        req.setProvince(certParam.getProvince()); //省（必填）
        req.setCity(certParam.getCity());  //城市（选填）
        req.setAddress(certParam.getAddress());//地址（必填）
        req.setLinkMobile(certParam.getMobile());//手机号码（必填）
        req.setEmail(certParam.getEmail()); //电子邮件（选填）
        req.setEnc(true);//是否加密传输数据（ture：加密； false： 不加密）

        String certPolicy = null; //证书策略（必填）
        String feeScale = null; //收费策略（必填）
        if (CACertConstants.CERT_TYPE.ENTERPRICE.getValue().equals(certParam.getCertType())) {
            certPolicy = CACertConfig.propertiesConfig.getString(CACertConstants.ENT_POLICY);
            feeScale = CACertConfig.propertiesConfig.getString(CACertConstants.ENT_FEE);
            req.setPhone(certParam.getPhone());//电话
            if(certParam.getOrgCode()!=null){
                req.setOrgCode(certParam.getOrgCode());  //组织机构代码
            }else{
                req.setUnitedCode(certParam.getUnitedCode());//统一社会信用代码
            }
            req.setIcCode(certParam.getRegCode()); //工商注册号（必填唯一）
            req.setLinkMan(certParam.getLinkMan());//联系人（必填）
        } 
        else if(CACertConstants.CERT_TYPE.PERSON.getValue().equals(certParam.getCertType())){
            certPolicy = CACertConfig.propertiesConfig.getString(CACertConstants.USER_POLICY);
            feeScale = CACertConfig.propertiesConfig.getString(CACertConstants.USER_FEE);
        }
        else{
            throw new RuntimeException("用户类型错误，可接受类型为1个人，2企业");
        }
        req.setCertPolicy(certPolicy); //证书策略（必填）
        req.setFeeScale(feeScale); //收费策略（必填）
        
        return req;
    }

}

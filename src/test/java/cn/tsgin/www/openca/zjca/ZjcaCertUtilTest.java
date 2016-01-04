/**
 * 杭州天谷信息科技有限公司源代码，版权归杭州天谷信息科技有限公司所有 <br/>
 * 项目名称：openapi <br/>
 * 文件名：ZjcaCertUtilTest.java <br/>
 * 包：cn.tsign.www.openapi.ca.zjca <br/>
 * 描述：TODO <br/>
 * 修改历史： <br/>
 * 1.[2015年12月27日下午5:04:41]创建文件 by lcc
 */
package cn.tsgin.www.openca.zjca;

import cn.tsgin.www.openca.util.IdCardGenerator;
import cn.tsgin.www.openca.util.NamePhoneGenerator;
import cn.tsign.www.openapi.ca.CloudCert;
import cn.tsign.www.openapi.ca.SoftKeyTool;
import cn.tsign.www.openapi.ca.zjca.ZjcaCertUtil;
import cn.tsign.www.openapi.util.Base64;
import junit.framework.TestCase;

/**
 * 类名：ZjcaCertUtilTest.java <br/>
 * 功能说明：证书签名测试类 <br/>
 * 修改历史： <br/>
 * 1.[2015年12月27日下午5:04:41]创建类 by lcc
 */
public class ZjcaCertUtilTest  extends TestCase  {

    /** 测试个人 */
    public static void testPerson() throws Exception {
        
        
        CloudCert certParam = getPerson();
        ZjcaCertUtil.createSignCert(certParam,getCsr(certParam));
        System.out.println("个人证书certId:"+certParam.getCertId());
        //ZjcaCertUtil.delayCert(certParam.getCertId(), CACertConstants.CERT_TYPE.PERSON.getValue());
    }

    /** 测试企业  */
    public static void testEnterPrice() throws Exception {
        
        CloudCert certParam = getEnterPrice();

        ZjcaCertUtil.createSignCert(certParam,getCsr(certParam));
        System.out.println("企业证书certId:"+certParam.getCertId());
        //ZjcaCertUtil.delayCert(certParam.getCertId(), CACertConstants.CERT_TYPE.ENTERPRICE.getValue());
        
    }

    public static CloudCert getPerson() {
        //名称
         String certName = "13288888888" ;
        //国家
         String country ="cn";
        //省
         String province ="zj" ;
        //市
         String city ="hangzhou";
        //手机
         String mobile = "13288888888";
        //邮箱
         String email ="test@163.com";
        //地址 
         String address ="address";
        //密码
         String password ="111111";
         String idNo = new IdCardGenerator().generate();
        CloudCert certParam = new CloudCert();
        certParam.initPerson(certName, country, province, city, idNo, mobile, email, address, password);
        
        return certParam;
    }

    public static CloudCert getEnterPrice() {

        //名称
         String certName = NamePhoneGenerator.getName();
        //国家
         String country ="cn";
        //省
         String province ="zj" ;
        //市
         String city ="hangzhou";
        //身份证
         String idNo = new IdCardGenerator().generate();
        //手机
         String mobile = NamePhoneGenerator.getTelphone();
        //邮箱
         String email ="test@163.com";
        //地址 
         String address ="address";
        //密码
         String password ="111111";
        //工商注册号
         String regCode =NamePhoneGenerator.getTelphone();
        //联系人
         String linkMan =NamePhoneGenerator.getName();
        //组织机构代码
         String orgCode ="88888888-9";
        //电话
         String phone ="";
        //统一社会信用代码
         //oragnize表，licenceType=1是三证合一，存的是统一社会信用代码，如果是0则存组织机构代码
         String unitedCode ="";
         String licenceType = "1";
        CloudCert certParam = new CloudCert();
        certParam.initEnterPrice(certName, country, province, city, idNo, mobile, email, address, password, regCode, linkMan, orgCode, phone, unitedCode);
        return certParam;
    }

    private static String getCsr(CloudCert certParam) throws Exception{
        final SoftKeyTool skt = new SoftKeyTool();
        String csr = null;
        byte[] csrbytes;
        csrbytes = skt.generatePKCS10(certParam.getCertName(),
                "timevale", "timevale",
                certParam.getCity(), certParam.getProvince(),
                certParam.getCountry());
        certParam.setPk(skt.getPrivateKey());
        csr = Base64.encode(csrbytes);
        return csr;
    }
}

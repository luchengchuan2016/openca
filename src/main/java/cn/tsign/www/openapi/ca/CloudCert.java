/**
 * 杭州天谷信息科技有限公司源代码，版权归杭州天谷信息科技有限公司所有 <br/>
 * 项目名称：openapi <br/>
 * 文件名：CloudCert.java <br/>
 * 包：cn.tsign.www.openapi.ca <br/>
 * 描述：TODO <br/>
 * 修改历史： <br/>
 * 1.[2015年5月29日上午9:31:21]创建文件 by jsh
 */
package cn.tsign.www.openapi.ca;

import java.security.PrivateKey;

import cn.tsign.www.openapi.constant.CACertConstants;
import cn.tsign.www.openapi.util.StringUtil;

/**
 * 类名：CloudCert.java <br/>
 * 功能说明：云证书信息 <br/>
 * 修改历史： <br/>
 * 1.[2015年5月29日上午9:31:21]创建类 by jsh
 */
public class CloudCert {

	// 名称
	private String certName;
	// 国家
	private String country;
	// 省
	private String province;
	// 市
	private String city;
	// 身份证
	private String idNo;
	// 手机
	private String mobile;
	// 邮箱
	private String email;
	// 地址
	private String address;
	// 证书类型 1-个人,2-企业
	private String certType;
	// 密码
	private String password;
	// 证书ID
	private String certId;
	// Base64编码的签名证书
	private String signCert;
	// 私钥
	private PrivateKey pk;
	// 工商注册号
	private String regCode;
	// 联系人
	private String linkMan;
	// 组织机构代码
	private String orgCode;
	// 电话
	private String phone;
	// 统一社会信用代码
	private String unitedCode;

	/**
	 * 
	 * 功能说明：初始化企业证书信息
	 * @param certName 名称
	 * @param country 国家
	 * @param province 省
	 * @param city 市 
	 * @param idNo 身份证
	 * @param mobile 手机
	 * @param email 邮箱
	 * @param address 地址 
	 * @param password 密码
	 * @param regCode 工商注册号
	 * @param linkMan 联系人
	 * @param orgCode  组织机构代码
	 * @param phone 电话 
	 * @param unitedCode 统一社会信用代码<br/>
	 * 修改历史：<br/>
	 * 1.[2015年12月29日下午4:25:59] 创建方法 by lcc
	 */
	public void initEnterPrice(String certName, String country, String province, String city, String idNo, String mobile,
			String email, String address, String password, String regCode, String linkMan, String orgCode, String phone, String unitedCode
			) {
                init(certName,country,province,city,idNo,mobile,email,address,password);
		this.regCode = regCode;
		this.linkMan = linkMan;
		this.orgCode = orgCode;
		this.phone = phone;
		this.unitedCode = unitedCode;
                this.certType =  CACertConstants.CERT_TYPE.ENTERPRICE.getValue();


	}

	/**
	 * 
	 * 功能说明：设置个人证书信息
	 * @param certName 名称
	 * @param country 国家
	 * @param province 省
	 * @param city 市
	 * @param idNo 身份证
	 * @param mobile 手机
	 * @param email 邮箱
	 * @param address 地址
	 * @param password 密码<br/>
	 * 修改历史：<br/>
	 * 1.[2015年12月29日下午4:15:33] 创建方法 by lcc
	 */
        public void initPerson(String certName, String country, String province, 
                String city, String idNo, String mobile,
                        String email, String address, String password) {
                init(certName,country,province,city,idNo,mobile,email,address,password);
                this.certType =  CACertConstants.CERT_TYPE.PERSON.getValue();

        }
        
        private void init(String certName, String country, String province, 
                String city, String idNo, String mobile,
                String email, String address, String password){

            this.certName = certName;
            if (StringUtil.isNull(this.certName)) {
                    if (!StringUtil.isNull(email)) {
                            this.certName = email;
                    } else if (!StringUtil.isNull(mobile)) {
                            this.certName = mobile;
                    }
            }
            this.country = country;
            this.province = province;
            this.city = city;
            this.idNo = idNo;
            this.mobile = mobile;
            this.email = email;
            this.address = address;
            this.password = password;
        }

	/**
	 * 功能说明：certName getter
	 * 
	 * @return <br/>
	 *         修改历史：<br/>
	 *         1.[2015年5月29日上午9:52:15] 创建方法 by jsh
	 */
	public final String getCertName() {
		return this.certName;
	}

	/**
	 * 设置 certName
	 */
	public void setCertName(String certName) {
		this.certName = certName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * 获取 省
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * 设置 省
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 获取 email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置 email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取 address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 设置 address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取 password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置 password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public PrivateKey getPk() {
		return pk;
	}

	public void setPk(PrivateKey pk) {
		this.pk = pk;
	}

	public String getSignCert() {
		return signCert;
	}

	public void setSignCert(String signCert) {
		this.signCert = signCert;
	}

	/**
	 * 获取 regCode
	 */
	public String getRegCode() {
		return regCode;
	}

	/**
	 * 设置 regCode
	 */
	public void setRegCode(String regCode) {
		this.regCode = regCode;
	}

	/**
	 * 获取 linkMan
	 */
	public String getLinkMan() {
		return linkMan;
	}

	/**
	 * 设置 linkMan
	 */
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	/**
	 * 获取 orgCode
	 */
	public String getOrgCode() {
		return orgCode;
	}

	/**
	 * 设置 orgCode
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	/**
	 * 获取 phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置 phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取 unitedCode
	 */
	public String getUnitedCode() {
		return unitedCode;
	}

	/**
	 * 设置 unitedCode
	 */
	public void setUnitedCode(String unitedCode) {
		this.unitedCode = unitedCode;
	}

	/**
	 * 获取 certId
	 */
	public String getCertId() {
		return certId;
	}

	/**
	 * 设置 certId
	 */
	public void setCertId(String certId) {
		this.certId = certId;
	}

	/**
	 * 获取 certType
	 */
	public String getCertType() {
		return certType;
	}

	/**
	 * 设置 certType
	 */
	public void setCertType(String certType) {
		this.certType = certType;
	}

}

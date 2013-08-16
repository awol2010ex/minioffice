package com.minioffice.user.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.hibernate.H3BeanConverter;

//角色表VO
@Entity
@Table(name = "T_BIZ_ROLE")
@DataTransferObject(converter=H3BeanConverter.class)
public class TBizRoleVO  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 916798383401748781L;
	private String id;// 角色ID
	private String rolename;// 角色名
	private String delflag ;//删除标记
	

	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "rolename")
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	
	@Column(name = "delflag")
	public String getDelflag() {
		return delflag;
	}
	public void setDelflag(String delflag) {
		this.delflag = delflag;
	}
	
	
}

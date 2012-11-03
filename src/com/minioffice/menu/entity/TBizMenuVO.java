package com.minioffice.menu.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.hibernate.H3BeanConverter;
//菜单表VO
@Entity
@Table(name = "T_BIZ_MENU")
@DataTransferObject(converter=H3BeanConverter.class)
public class TBizMenuVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6453845087411678899L;
	private String id;// 菜单ID
	private String menuName;// 菜单名
	private Integer menuIndex;// 菜单序号
	private String menuUrl;// 菜单URL
	private TBizMenuVO parentNode;// 父节点
	private List<TBizMenuVO> children;// 父节点

	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "menu_name")
	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	@Column(name = "menu_index")
	public Integer getMenuIndex() {
		return menuIndex;
	}

	public void setMenuIndex(Integer menuIndex) {
		this.menuIndex = menuIndex;
	}

	@Column(name = "menu_url")
	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH }, optional = true)
	@JoinColumn(name = "pid")
	public TBizMenuVO getParentNode() {
		return parentNode;
	}

	public void setParentNode(TBizMenuVO parentNode) {
		this.parentNode = parentNode;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "parentNode")
	public List<TBizMenuVO> getChildren() {
		return children;
	}

	public void setChildren(List<TBizMenuVO> children) {
		this.children = children;
	}
}

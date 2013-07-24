package com.minioffice.menu.dwr;

import net.sf.json.JSONObject;

import org.directwebremoting.annotations.RemoteProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.minioffice.menu.entity.TBizMenuVO;
import com.minioffice.menu.service.ITBizMenuService;
import com.minioffice.utils.UUIDGenerator;
//菜单相关DWR操作
@RemoteProxy
public class TBizMenuService {
	private final static Logger logger = LoggerFactory
			.getLogger(TBizMenuService.class);
	@Autowired
	ITBizMenuService tBizMenuService;

	//保存菜单
	public boolean saveMenu(String id, String menuName, int menuIndex,
			String menuUrl, String pid) {
		try {
			TBizMenuVO vo = new TBizMenuVO();
			if (id != null && !"".equals(id)) {
				vo=tBizMenuService.getMenu(id);
			} else {
				vo.setId(UUIDGenerator.generate());
				if (pid != null && !"".equals(pid)) {
					vo.setParentNode(tBizMenuService.getMenu(pid));
				}
			}
			vo.setMenuIndex(menuIndex);
			vo.setMenuUrl(menuUrl);
			vo.setMenuName(menuName);
			
			tBizMenuService.saveMenu(vo);
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
		return true;
	}
	//取得菜单信息
	public TBizMenuVO getMenu(String id) {
		
		TBizMenuVO menu=null;
		try {
			menu=tBizMenuService.getMenu(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("",e);
		}
		return menu;
	}
	//删除菜单信息
	public JSONObject removeMenu(String id){
		boolean result =true;
		String msg ="";
		try {
			tBizMenuService.removeMenu(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
			msg =e.getLocalizedMessage();
			result= false;
		}
		return new JSONObject().element("result", result).element("msg", msg);
	}
}

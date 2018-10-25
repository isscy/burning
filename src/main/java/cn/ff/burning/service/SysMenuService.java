package cn.ff.burning.service;

import cn.ff.burning.constant.BaseConstant;
import cn.ff.burning.entity.SysMenu;
import cn.ff.burning.entity.SysUser;
import cn.ff.burning.mapper.SysMenuMapper;
import cn.ff.burning.mapper.SysUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysMenuService extends ServiceImpl<SysMenuMapper, SysMenu> {
    private final SysMenuMapper sysMenuMapper;

    /**
     * 获取所有目录和菜单的url 和角色
     */
    public List<SysMenu> allMenuUrlAndRole() {
        return sysMenuMapper.getUrlAndRole();
    }

    /**
     * 根据用户Id 获取他所能看到的菜单
     */
    public List<SysMenu> menusByUser(String userId) {
        List<SysMenu> menus = new LinkedList<>();
        if (BaseConstant.USER_SUPER_ID.equals(userId))
            menus = sysMenuMapper.selectMenuNormalAll();
        else
            menus = sysMenuMapper.selectMenusByUserId(userId);
        return getChildPerms(menus, "0");
    }


    /**
     * 根据父节点的ID获取所有子节点
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, String parentId) {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = (SysMenu) iterator.next();
            if (parentId.equals(t.getParentId())) {//根据传入的父节点ID,遍历该父节点的所有子节点
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                Iterator<SysMenu> it = childList.iterator();
                while (it.hasNext()) {
                    SysMenu n = (SysMenu) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0;
    }

}

package com.smallway.framework.web.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.smallway.common.core.domain.entity.SysRole;
import com.smallway.common.core.domain.entity.SysUser;
import com.smallway.system.service.ISysMenuService;
import com.smallway.system.service.ISysRoleService;

/**
 * 用户权限处理
 * 
 * @author horzits
 */
@Component
public class SysPermissionService
{
    private static final Pattern CHART_PERMS_PATTERN = Pattern.compile("\\\"chartPerms\\\"\\s*:\\s*\\[(.*?)\\]");

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    /**
     * 获取角色数据权限
     * 
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user)
    {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin())
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     * 
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user)
    {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin())
        {
            perms.add("*:*:*");
        }
        else
        {
            List<SysRole> roles = user.getRoles();
            if (!CollectionUtils.isEmpty(roles))
            {
                // 多角色设置permissions属性，以便数据权限匹配权限
                for (SysRole role : roles)
                {
                    Set<String> rolePerms = menuService.selectMenuPermsByRoleId(role.getRoleId());
                    role.setPermissions(rolePerms);
                    perms.addAll(rolePerms);
                }
            }
            else
            {
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }

    public Set<String> getChartPermissions(SysUser user)
    {
        Set<String> perms = new HashSet<String>();
        if (user.isAdmin())
        {
            perms.add("dashboard:chart:*");
            return perms;
        }
        List<SysRole> roles = user.getRoles();
        if (CollectionUtils.isEmpty(roles))
        {
            return perms;
        }
        for (SysRole role : roles)
        {
            perms.addAll(extractChartPerms(role.getRemark()));
        }
        return perms;
    }

    private Set<String> extractChartPerms(String remark)
    {
        Set<String> perms = new HashSet<String>();
        if (remark == null || remark.trim().isEmpty())
        {
            return perms;
        }
        Matcher matcher = CHART_PERMS_PATTERN.matcher(remark);
        if (!matcher.find())
        {
            return perms;
        }
        String raw = matcher.group(1);
        if (raw == null || raw.trim().isEmpty())
        {
            return perms;
        }
        String[] items = raw.split(",");
        for (String item : items)
        {
            String perm = item.trim();
            if (perm.startsWith("\"") && perm.endsWith("\""))
            {
                perm = perm.substring(1, perm.length() - 1);
            }
            if (!perm.isEmpty())
            {
                perms.add(perm);
            }
        }
        return perms;
    }
}

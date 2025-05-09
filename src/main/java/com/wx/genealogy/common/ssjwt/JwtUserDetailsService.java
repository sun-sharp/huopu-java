package com.wx.genealogy.common.ssjwt;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wx.genealogy.common.util.ObjectUtil;
import com.wx.genealogy.system.entity.SysPermission;
import com.wx.genealogy.system.entity.SysRolePermission;
import com.wx.genealogy.system.entity.SysRoleUser;
import com.wx.genealogy.system.entity.SysUser;
import com.wx.genealogy.system.mapper.SysPermissionDao;
import com.wx.genealogy.system.mapper.SysRolePermissionDao;
import com.wx.genealogy.system.mapper.SysRoleUserDao;
import com.wx.genealogy.system.mapper.SysUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName JwtUserDetailsService
 * @Author hangyi
 * @Data 2020/1/20 15:16
 * @Description TODO
 * @Version 1.0
 **/
@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserDao userDao;
    @Autowired
    private SysRoleUserDao sysRoleUserDao;
    @Autowired
    private SysRolePermissionDao sysRolePermissionDao;
    @Autowired
    private SysPermissionDao permissionDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Collection<? extends GrantedAuthority> authorities = null;
        SysUser sysUser1 = userDao.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, userName));
        if (sysUser1 == null) {
            return null;
        }
        if (sysUser1.getDelFlag() != 1) {
            return null;
        }
        if (sysUser1.getStatus() != 1) {
            return null;
        }
        List<SysRoleUser> sysRoleUsers = sysRoleUserDao.selectList(new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getUserId, sysUser1.getId()));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < sysRoleUsers.size(); i++) {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setRoleId(sysRoleUsers.get(i).getRoleId());
            List<SysRolePermission> sysRolePermissions = sysRolePermissionDao.selectList(new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, sysRoleUsers.get(i).getRoleId()));
            for (int j = 0; j < sysRolePermissions.size(); j++) {
                SysPermission sysPermission = permissionDao.selectById(sysRolePermissions.get(j).getPermissionId());
                if (ObjectUtil.isNotNull(sysPermission)) {
                    list.add(sysPermission.getPermission());
                }
            }
        }
        authorities = list.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());


        return new SecurityUserDetails(sysUser1.getUserName(), sysUser1.getPassword(), authorities);
    }

}

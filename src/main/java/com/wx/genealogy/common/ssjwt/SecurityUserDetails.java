package com.wx.genealogy.common.ssjwt;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @ClassName SecurityUserDetails
 * @Author hangyi
 * @Data 2020/1/20 15:12
 * @Description TODO
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SecurityUserDetails implements UserDetails {
    private String userName;

    private String Password;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public SecurityUserDetails(String userName, String password, Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
        this.setUserName(userName);
        this.setPassword(password);
        this.setAuthorities(authorities);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    public void setPassword(String password) {
        Password = password;
    }

    /**
     * 账户是否过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 是否禁用
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否启用
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}

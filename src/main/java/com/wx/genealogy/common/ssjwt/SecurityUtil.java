package com.wx.genealogy.common.ssjwt;

import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    public static String getUserName() {
        UserDetails userDetails = (UserDetails) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

}

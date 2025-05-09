package com.wx.genealogy.common.ssjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

/**
 * @ClassName SecurityConfig
 * @Author hangyi
 * @Data 2020/1/20 15:09
 * @Description TODO
 * @Version 1.0
 **/
@Configuration
@EnableWebSecurity// 这个注解必须加，开启Security
@EnableGlobalMethodSecurity(prePostEnabled = true)//保证post之前的注解可以使用
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    JwtAuthorizationTokenFilter authenticationTokenFilter;

    @Autowired
    JwtAccessDeniedHandler accessDeniedHandler;


    //先来这里认证一下
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoderBean());
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //@formatter:off
//        super.configure(web);
        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
    }


    //拦截在这配
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/system/sysUser/login").permitAll()
                .antMatchers("/user/*").permitAll()
                .antMatchers("/family/*").permitAll()
                .antMatchers("/familyUser/*").permitAll()
                .antMatchers("/familyMessage/*").permitAll()
                .antMatchers("/tombstone/*").permitAll()
                .antMatchers("/familyPedigree/*").permitAll()
                .antMatchers("/familyPedigreeApply/*").permitAll()
                .antMatchers("/familyMailbox/*").permitAll()
                .antMatchers("/familyDownload/*").permitAll()
                .antMatchers("/douDeliver/*").permitAll()


                .antMatchers("/userFamilyFollow/*").permitAll()
                .antMatchers("/familyManageLog/*").permitAll()
                .antMatchers("/familyClean/*").permitAll()

                .antMatchers("/familyGenealogy/*").permitAll()
                .antMatchers("/essay/*").permitAll()
                .antMatchers("/article/*").permitAll()
                .antMatchers("/articleSupport/*").permitAll()
                .antMatchers("/articleDiscuss/*").permitAll()

                .antMatchers("/video/*").permitAll()
                .antMatchers("/videoSupport/*").permitAll()
                .antMatchers("/videoDiscuss/*").permitAll()


                .antMatchers("/essayShare/*").permitAll()
                .antMatchers("/taskUser/*").permitAll()
                .antMatchers("/task/*").permitAll()
                .antMatchers("/douRecord/*").permitAll()

                .antMatchers("/essaySupport/*").permitAll()
                .antMatchers("/essayDiscuss/*").permitAll()
                .antMatchers("/essayFamily/*").permitAll()
                .antMatchers("/riceRecord/*").permitAll()
                .antMatchers("/trade/*").permitAll()
                .antMatchers("/fire/*").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/file/*").permitAll()
                .antMatchers("/druid/**").permitAll()
                .antMatchers("/cockpit/**").permitAll()
                .antMatchers("/familyGenealogyReceive/*").permitAll()
                // Swagger2 权限放行
                .antMatchers("/doc.html").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/oauth/**").permitAll()
                .antMatchers("/actuator", "/actuator/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").anonymous()
                .anyRequest().authenticated()       // 剩下所有的验证都需要验证
                .and()
                .csrf().disable()
                .requestMatchers().anyRequest()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()

                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
        // 定制我们自己的 session 策略：调整为让 Spring Security 不创建和使用 session
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

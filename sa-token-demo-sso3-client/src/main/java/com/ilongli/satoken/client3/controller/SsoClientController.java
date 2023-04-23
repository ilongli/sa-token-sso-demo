package com.ilongli.satoken.client3.controller;

import cn.dev33.satoken.config.SaSsoConfig;
import cn.dev33.satoken.sso.SaSsoProcessor;
import cn.dev33.satoken.sso.SaSsoUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.dtflys.forest.Forest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sa-Token-SSO Client端 Controller 
 */
@RestController
public class SsoClientController {

    // 首页 
    @RequestMapping("/")
    public String index() {
        String str = "<h2>Sa-Token SSO-Client 应用端</h2>" + 
                    "<p>当前会话是否登录：" + StpUtil.isLogin() + "</p>" +
                    "<p><a href=\"javascript:location.href='/sso/login?back=' + encodeURIComponent(location.href);\">登录</a> " + 
                    "<a href='/sso/logout?back=self'>注销</a></p>";
        return str;
    }
    
    /*
     * SSO-Client端：处理所有SSO相关请求 
     *         http://{host}:{port}/sso/login          -- Client端登录地址，接受参数：back=登录后的跳转地址 
     *         http://{host}:{port}/sso/logout         -- Client端单点注销地址（isSlo=true时打开），接受参数：back=注销后的跳转地址 
     *         http://{host}:{port}/sso/logoutCall     -- Client端单点注销回调地址（isSlo=true时打开），此接口为框架回调，开发者无需关心
     */
    @RequestMapping("/sso/*")
    public Object ssoRequest() {
        return SaSsoProcessor.instance.clientDister();
    }

    // 配置SSO相关参数
    @Autowired
    private void configSso(SaSsoConfig sso) {
        // ... 其他代码

        // 配置 Http 请求处理器
        sso.setSendHttp(url -> {
            System.out.println("------ 发起请求：" + url);
            return Forest.get(url).executeAsString();
        });
    }

    // 查询我的账号信息
    @RequestMapping("/sso/myinfo")
    public Object myinfo() {
        Object userinfo = SaSsoUtil.getUserinfo(StpUtil.getLoginId());
        System.out.println("--------info：" + userinfo);
        return userinfo;
    }

    // 查询我的账号信息
    @RequestMapping("/sso/myFollowList")
    public Object myFollowList() {
        // 组织url，加上签名参数
        String url = SaSsoUtil.addSignParams("http://sa-sso-server.com:7080/sso/getFollowList", StpUtil.getLoginId());

        // 调用，并返回 SaResult 结果
        SaResult res = SaSsoUtil.request(url);

        // 返回给前端
        return res;
    }

}

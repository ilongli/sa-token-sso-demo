package com.ilongli.satoken.sso.server.controller;

import cn.dev33.satoken.config.SaSsoConfig;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.sso.SaSsoProcessor;
import cn.dev33.satoken.sso.SaSsoUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.dtflys.forest.Forest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Sa-Token-SSO Server端 Controller 
 */
@RestController
public class SsoServerController {

    /**
     * 聚合式路由
     * 如果想自定义其API地址，请参考：https://sa-token.cc/doc.html#/sso/sso-custom-api?id=%e6%96%b9%e5%bc%8f%e4%b8%80%ef%bc%9a%e4%bf%ae%e6%94%b9%e5%85%a8%e5%b1%80%e5%8f%98%e9%87%8f
     * SSO-Server端：处理所有SSO相关请求 (下面的章节我们会详细列出开放的接口) 
     */
    @RequestMapping("/sso/*")
    public Object ssoRequest() {
        return SaSsoProcessor.instance.serverDister();
    }

    // region # 拆分式路由

/*

    // SSO-Server：统一认证地址
    @RequestMapping("/sso/auth")
    public Object ssoAuth() {
        return SaSsoProcessor.instance.ssoAuth();
    }

    // SSO-Server：RestAPI 登录接口
    @RequestMapping("/sso/doLogin")
    public Object ssoDoLogin() {
        return SaSsoProcessor.instance.ssoDoLogin();
    }

    // SSO-Server：校验ticket 获取账号id
    @RequestMapping("/sso/checkTicket")
    public Object ssoCheckTicket() {
        return SaSsoProcessor.instance.ssoCheckTicket();
    }

    // SSO-Server：单点注销
    @RequestMapping("/sso/signout")
    public Object ssoSignout() {
        return SaSsoProcessor.instance.ssoSignout();
    }

    // ...

    */

    // endregion

    
    /**
     * 配置SSO相关参数 
     */
    @Autowired
    private void configSso(SaSsoConfig sso) {
        // 配置：未登录时返回的View 
        sso.setNotLoginView(() -> {
            String msg = "当前会话在SSO-Server端尚未登录，请先访问"
                    + "<a href='/sso/doLogin?name=sa&pwd=123456' target='_blank'> doLogin登录 </a>"
                    + "进行登录之后，刷新页面开始授权";
            return msg;
        });
        
        // 配置：登录处理函数 
        sso.setDoLoginHandle((name, pwd) -> {
            // 此处仅做模拟登录，真实环境应该查询数据进行登录 
            if("sa".equals(name) && "123456".equals(pwd)) {
                StpUtil.login(10001);
                return SaResult.ok("登录成功！").setData(StpUtil.getTokenValue());
            }
            return SaResult.error("登录失败！");
        });
        
        // 配置 Http 请求处理器 （在模式三的单点注销功能下用到，如不需要可以注释掉） 
        sso.setSendHttp(url -> {
            try {
                // 发起 http 请求 
                System.out.println("------ 发起请求：" + url);
                return Forest.get(url).executeAsString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    // 自定义接口：获取userinfo
    @RequestMapping("/sso/userinfo")
    public Object userinfo(String loginId) {
        System.out.println("---------------- 获取userinfo --------");

        // 校验签名，防止敏感信息外泄
        SaSsoUtil.checkSign(SaHolder.getRequest());

        // 自定义返回结果（模拟）
        return SaResult.ok()
                .set("id", loginId)
                .set("name", "linxiaoyu")
                .set("sex", "女")
                .set("age", 18);
    }

    // 获取指定用户的关注列表
    @RequestMapping("/sso/getFollowList")
    public SaResult ssoRequest(Long loginId) {

        // 校验签名，签名不通过直接抛出异常
        SaSsoUtil.checkSign(SaHolder.getRequest());

        // 查询数据 (此处仅做模拟)
        List<Integer> list = Arrays.asList(10041, 10042, 10043, 10044);

        // 返回
        return SaResult.data(list);
    }


}

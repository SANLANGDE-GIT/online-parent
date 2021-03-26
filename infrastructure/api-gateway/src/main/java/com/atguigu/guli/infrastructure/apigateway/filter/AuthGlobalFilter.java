package com.atguigu.guli.infrastructure.apigateway.filter;

import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.google.gson.JsonObject;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getURI().getPath();
        AntPathMatcher matcher =new AntPathMatcher();
        //验证接口用户必须登录
        boolean match = matcher.match("/api/**/auth/**", path);

        if(match){
            boolean token = JwtHelper.checkToken(request.getHeaders().getFirst("token"));
            //System.out.println("token = " + token);
            if(token){
                return chain.filter(exchange);
            }

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("success", ResultCodeEnum.LOGIN_AUTH.getSuccess());
            jsonObject.addProperty("code", ResultCodeEnum.LOGIN_AUTH.getCode());
            jsonObject.addProperty("message", ResultCodeEnum.LOGIN_AUTH.getMessage());
            jsonObject.addProperty("data","");
            response.getHeaders().set("Content-Type","application/json;charset=UTF-8");
            DataBuffer buffer = response.bufferFactory().wrap(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

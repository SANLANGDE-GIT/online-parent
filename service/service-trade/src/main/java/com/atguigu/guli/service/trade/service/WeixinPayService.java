package com.atguigu.guli.service.trade.service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface WeixinPayService {
    Map createNative(String memberId, String orderNo, String remoteAddr);

    String doNotify(HttpServletRequest request);

}

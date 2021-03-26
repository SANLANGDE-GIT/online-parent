package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.common.util.HttpClientUtils;
import com.atguigu.guli.common.util.StreamUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.entity.PayLog;
import com.atguigu.guli.service.trade.feign.EduCourseService;
import com.atguigu.guli.service.trade.service.OrderService;
import com.atguigu.guli.service.trade.service.PayLogService;
import com.atguigu.guli.service.trade.service.WeixinPayService;
import com.atguigu.guli.service.trade.util.WeixinPayProperties;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Autowired
    OrderService orderService;

    @Autowired
    WeixinPayProperties weixinPayProperties;

    @Override
    public Map createNative(String memberId, String orderNo, String remoteAddr) {

        try {
            Order order = orderService.getOrderByOrderNo(memberId,orderNo);

            //https://api.mch.weixin.qq.com/pay/unifiedorder
            String nativeUrl="https://api.mch.weixin.qq.com/pay/unifiedorder";
            HttpClientUtils clientUtils = new HttpClientUtils(nativeUrl);

            Map<String, String> params =new HashMap<>();
            params.put("mch_id",weixinPayProperties.getPartner());
            params.put("nonce_str", WXPayUtil.generateNonceStr());  //随机字符串
            params.put("appid",weixinPayProperties.getAppId());
            params.put("body",order.getCourseTitle());
            params.put("out_trade_no",orderNo);  //商户订单号
            params.put("total_fee",order.getTotalFee()+"");  //订单金额，单位：分
            params.put("spbill_create_ip",remoteAddr);  //终端IP
            params.put("notify_url",weixinPayProperties.getNotifyUrl());  //响应地址
            params.put("trade_type","NATIVE"); //交易类型
            //获取签名
            System.out.println("weixinPayProperties = " + weixinPayProperties.getPartnerKey());
            String signedXml = WXPayUtil.generateSignedXml(params, weixinPayProperties.getPartnerKey());
            log.info("签名：{}",signedXml);
            /**
             * <xml>
             * <nonce_str>a0baf6465a1348079a6323de3fb6aefc</nonce_str>
             * <out_trade_no>20210106192159516</out_trade_no>
             * <appid>wxf913bfa3a2c7eeeb</appid>
             * <total_fee>1</total_fee>
             * <sign>ACFF83CA196E57DBFCC60985B6492D67</sign>
             * <trade_type>NATIVE</trade_type>
             * <mch_id>1543338551</mch_id>
             * <body>Docker解决分布式框架部署迁移问题</body>
             * <notify_url>用户支付后微信发送支付信息的回调地址</notify_url>
             * <spbill_create_ip>192.168.200.1</spbill_create_ip>
             * </xml>
             */
            clientUtils.setXmlParam(signedXml);
            clientUtils.setHttps(true);
            clientUtils.post();
            String content = clientUtils.getContent();
            log.info("content：{}",content);
            /**
             * <xml><return_code><![CDATA[SUCCESS]]></return_code>
             * <return_msg><![CDATA[OK]]></return_msg>
             * <appid><![CDATA[wxf913bfa3a2c7eeeb]]></appid>
             * <mch_id><![CDATA[1543338551]]></mch_id>
             * <nonce_str><![CDATA[aGkiEdXM4Deu61ZC]]></nonce_str>
             * <sign><![CDATA[9F65D5D85F83BD25CB9DA5DA96CADB17]]></sign>
             * <result_code><![CDATA[SUCCESS]]></result_code>
             * <prepay_id><![CDATA[wx06193134183474ffc75e6ea58324f20000]]></prepay_id>
             * <trade_type><![CDATA[NATIVE]]></trade_type>
             * <code_url><![CDATA[weixin://wxpay/bizpayurl?pr=g8v2LwNzz]]></code_url>
             * </xml>
             */
            //将xml转成map对象
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
            if("FAIL".equals(resultMap.get("return_code"))||"FAIL".equals(resultMap.get("result_code"))){
                log.error("微信支付统一下单错误:"
                            +",return_code"+resultMap.get("return_code")
                            +",return_msg"+resultMap.get("return_msg")
                            +",result_code"+resultMap.get("result_code")
                            +",err_code"+resultMap.get("err_code")
                            +",err_code_des"+resultMap.get("err_code_des")
                );
                throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
            }
            //组装所需内柔
            HashMap map = new HashMap<>();
            map.put("return_code",resultMap.get("return_code"));
            map.put("code_url",resultMap.get("code_url"));
            map.put("total_fee",order.getTotalFee());
            map.put("out_trade_no",orderNo);
            map.put("course_id",order.getCourseId());
            return map;
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }
    }

    @Autowired
    PayLogService payLogService;

    @Autowired
    EduCourseService eduCourseService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String doNotify(HttpServletRequest request) {
        try {
            String content = StreamUtils.inputStream2String(request.getInputStream(), "UTF-8");
            log.info(content);
            boolean signatureValid = WXPayUtil.isSignatureValid(content, weixinPayProperties.getPartnerKey());
            //返回结果：
            Map<String,String> errorMap=new HashMap<>();
            if(!signatureValid){
                log.error("签名错误");
                errorMap.put("return_code","FAIL");
                errorMap.put("return_msg","签名失败");
                return WXPayUtil.mapToXml(errorMap);
                //throw new GuliException(ResultCodeEnum.UNKNOWN_REASON);
            }
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
            if("FAIL".equals(resultMap.get("return_code"))||"FAIL".equals(resultMap.get("result_code"))) {
                log.error("微信支付回调错误:"
                        +",return_code"+resultMap.get("return_code")
                        +",return_msg"+resultMap.get("return_msg")
                        +",result_code"+resultMap.get("result_code")
                        +",err_code"+resultMap.get("err_code")
                        +",err_code_des"+resultMap.get("err_code_des")
                );
                errorMap.put("return_code","FAIL");
                errorMap.put("return_msg","支付失败");
                return WXPayUtil.mapToXml(errorMap);
                //throw new GuliException(ResultCodeEnum.UNKNOWN_REASON);
            }
            //查询订单信息
            String orderNo = resultMap.get("out_trade_no");
            String totalFee = resultMap.get("total_fee");
            Order order = orderService.getOrderByOrderNo(orderNo);
            //判断金额支付是否正确
            if(order==null || (order.getTotalFee()!= Long.parseLong(totalFee))){
                errorMap.put("return_code","FAIL");
                errorMap.put("return_msg","订单出现错误");
                return WXPayUtil.mapToXml(errorMap);
            }
            log.info("响应结果为：{}",resultMap);
            /**
             * <xml><appid><![CDATA[wxf913bfa3a2c7eeeb]]></appid>
             * <bank_type><![CDATA[OTHERS]]></bank_type>
             * <cash_fee><![CDATA[1]]></cash_fee>
             * <fee_type><![CDATA[CNY]]></fee_type>
             * <is_subscribe><![CDATA[N]]></is_subscribe>
             * <mch_id><![CDATA[1543338551]]></mch_id>
             * <nonce_str><![CDATA[c6b854b69d8940e2950e758119d22d79]]></nonce_str>
             * <openid><![CDATA[oQTXC55WzrzJfKvI75lIVUcF4vyc]]></openid>
             * <out_trade_no><![CDATA[20210106205257099]]></out_trade_no>
             * <result_code><![CDATA[SUCCESS]]></result_code>
             * <return_code><![CDATA[SUCCESS]]></return_code>
             * <sign><![CDATA[B8943B80DA0EBA72A4FEACA0BC1687C1]]></sign>
             * <time_end><![CDATA[20210106205323]]></time_end>
             * <total_fee>1</total_fee>
             * <trade_type><![CDATA[NATIVE]]></trade_type>
             * <transaction_id><![CDATA[4200000840202101067791602142]]></transaction_id>
             * </xml>
             */
            //更新订单支付状态，和商品的销量，记录订单日志
            order.setStatus(1);
            orderService.updateById(order);

            PayLog payLog = new PayLog();
            payLog.setOrderNo(orderNo);
            payLog.setPayTime(new Date());
            payLog.setTotalFee(Long.parseLong(totalFee));
            payLog.setTransactionId(resultMap.get("transaction_id"));
            payLog.setAttr(new Gson().toJson(resultMap));
            payLog.setPayType(1);
            payLog.setTradeState(resultMap.get("result_code"));
            payLogService.save(payLog);
            System.out.println("order = " + order);

            eduCourseService.updateBuyCountById(order.getCourseId());

            //响应成功！
            Map<String,String> successMap=new HashMap<>();
            successMap.put("return_code","SUCCESS");
            successMap.put("return_msg","OK");
            return WXPayUtil.mapToXml(successMap);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.UNKNOWN_REASON);
        }
    }
}

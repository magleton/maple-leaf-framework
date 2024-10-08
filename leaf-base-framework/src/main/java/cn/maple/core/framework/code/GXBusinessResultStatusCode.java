package cn.maple.core.framework.code;

import lombok.Getter;

@Getter
public enum GXBusinessResultStatusCode implements GXResultStatusCode {
    NORMAL(0x0, "正常状态", "公用状态"),
    DELETED(0x10, "删除", "公用状态"),
    LOCKED(0x11, "账号锁定", "账号锁定"),
    ACCOUNT_OVERDUE(0x12, "账号过期", "账号过期"),
    PASSWORD_OVERDUE(0x13, "密码过期", "密码过期"),
    WAIT_REVIEW(0x20, "等待审核", ""),
    APPROVE(0x21, "审核通过", ""),
    DECLINE(0x22, "审核拒绝", ""),
    GOODS_PUT_AWAY(0x30, "上架", "商品上架"),
    GOODS_SOLD_OUT(0x31, "下架", "商品下架"),
    GOODS_SELL_OUT(0x32, "售完", "商品售完"),
    ORDER_PAYMENT_WAITING(0x40, "待付款", ""),
    ORDER_PAYMENT_SUCCESS(0x41, "支付成功", ""),
    ORDER_PAYMENT_FAILURE(0x42, "支付失败", ""),
    ORDER_WAITING_SHIPMENT(0x43, "待发货", ""),
    ORDER_WAITING_RECEIVING(0x44, "待收货状态", ""),
    ORDER_RECEIVED(0x45, "已收货", "已收货"),
    ORDER_APPRAISE(0x46, "已评价", "评价"),
    ORDER_ACCOMPLISH(0x47, "订单已经完成", "订单已经完成"),
    ON_GOING(0x50, "进行中", ""),
    NOT_STARTED(0x51, "未开始", ""),
    FINISH(0x52, "已结束", ""),
    EXPIRE(0x60, "过期", "");

    private final String msg;

    private final int code;

    private final String desc;

    GXBusinessResultStatusCode(int code, String msg, String desc) {
        this.code = code;
        this.msg = msg;
        this.desc = desc;
    }
}

open module MyTetr {
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires com.whitewoodcity.fxcity;
    requires javafx.media;
    requires atlantafx.base;
    requires java.desktop;
    requires com.alibaba.fastjson2;
    requires static lombok;

    requires io.netty.all;
    requires io.netty.buffer;
    requires io.netty.codec;
//    requires io.netty.codec.dns;
//    requires io.netty.codec.haproxy;
//    requires io.netty.codec.http;
//    requires io.netty.codec.http2;
//    requires io.netty.codec.memcache;
//    requires io.netty.codec.mqtt;
//    requires io.netty.codec.redis;
//    requires io.netty.codec.smtp;
//    requires io.netty.codec.socks;
//    requires io.netty.codec.stomp;
//    requires io.netty.codec.xml;
    requires io.netty.common;
    requires io.netty.handler;
    requires io.netty.handler.proxy;
    requires io.netty.resolver;
    requires io.netty.resolver.dns;
    requires io.netty.transport;
//    requires io.netty.transport.epoll;
//    requires io.netty.transport.kqueue;
    requires io.netty.transport.unix.common;
    requires io.netty.transport.rxtx;
    requires io.netty.transport.sctp;
    requires io.netty.transport.udt;
}
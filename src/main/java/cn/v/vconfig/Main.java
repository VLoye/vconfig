package cn.v.vconfig;/*
 * Copyright(C) 2013 Agree Corporation. All rights reserved.
 * 
 * Contributors:
 *     Agree Corporation - initial API and implementation
 */

import cn.v.vconfig.example.Order;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author V
 * @data 2019/9/30 21:54
 * @Description
 **/
public class Main {

    public static void main(String[] args)throws Exception{
        Config config = ConfigFactory.load();
//        System.out.println(config.getString("app.name"));
//        System.out.println(config.getString("app.service"));
        System.out.println(config.getList("app"));
//        System.out.println(config.getList("persion"));
//        Object o = config.getList("persion").unwrapped();
//        System.out.println((config.getList("persion").get(0).unwrapped()));
        Order order = ConfigBootstrap.load(Order.class,"order");
        System.out.println(order);
////        System.out.println(byte[].class.getName());
////        System.out.println(int[].class.getName());
        List<String> list = new ArrayList<String>();
        System.out.println(list.getClass().getCanonicalName());
        System.out.println(list.getClass().getSimpleName());
        System.out.println(list.getClass().getTypeName());
        System.out.println(list.getClass().getName());
        System.out.println(list.getClass().getTypeName());
//        System.out.println(list.getClass().);
//        System.out.println(config.getList());


    }

}

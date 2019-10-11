package cn.v.vconfig.example;/*
 * Copyright(C) 2013 Agree Corporation. All rights reserved.
 * 
 * Contributors:
 *     Agree Corporation - initial API and implementation
 */

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author V
 * @data 2019/10/7 20:07
 * @Description
 **/
public class Order {
    private int id;
    private String name;
    private int count;
    private boolean type;
    private byte[] ack;
    private Persion persion;
    private Map<String,String> map;
    private List<String> list;
    private List<Persion> persions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public byte[] getAck() {
        return ack;
    }

    public void setAck(byte[] ack) {
        this.ack = ack;
    }

    @Override
    public String toString() {
        return "cn.v.vconfig.example.Order{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", type=" + type +
                ", ack=" + Arrays.toString(ack) +
                ", persion=" + persion +
                ", map=" + map +
                ", list=" + list +
                ", persions=" + persions +
                '}';
    }
}

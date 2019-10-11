/*
 * Copyright(C) 2013 Agree Corporation. All rights reserved.
 * 
 * Contributors:
 *     Agree Corporation - initial API and implementation
 */
package cn.v.vconfig.example;

/**
 * @author V
 * @data 2019/10/10 19:49
 * @Description
 **/
public class Body {
    private int hands;
    private int foots;

    public int getHands() {
        return hands;
    }

    public void setHands(int hands) {
        this.hands = hands;
    }

    public int getFoots() {
        return foots;
    }

    public void setFoots(int foots) {
        this.foots = foots;
    }

    @Override
    public String toString() {
        return "Body{" +
                "hands=" + hands +
                ", foots=" + foots +
                '}';
    }
}

package cn.v.vconfig;/*
 * Copyright(C) 2013 Agree Corporation. All rights reserved.
 * 
 * Contributors:
 *     Agree Corporation - initial API and implementation
 */

import com.typesafe.config.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author V
 * @data 2019/10/7 16:49
 * @Description
 **/
public class ConfigBootstrap {

    /**
     * todo exception
     *
     * @param clazz
     * @param key
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T extends Object> T load(Class<T> clazz, String key) throws Exception {
        Config config = ConfigFactory.load();
        ConfigObject object = config.getObject("order");
        return transfer(clazz,object);
    }

    public static <T extends Object> T load(Class<T> clazz) throws Exception{
        T o = null;
        try {
            Config config = ConfigFactory.load();
            o = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                Class c = field.getType();
                //base type
//                System.out.println(c.getCanonicalName());
                switch (c.getCanonicalName()) {
                    case "java.lang.String":
                        String v1 = config.getString(name);
                        field.set(o, v1);
                        break;
                    case "int":
                    case "java.lang.Integer":
                        Integer v2 = config.getInt(name);
                        field.set(o, v2);
                        break;
                    case "double":
                    case "java.lang.Double":
                        Double v3 = config.getDouble(name);
                        field.set(o, v3);
                        break;
                    case "long":
                    case "java.lang.Long":
                        Long v4 = config.getLong(name);
                        field.set(o, v4);
                        break;
                    case "boolean":
                    case "java.lang.Boolean":
                        Boolean v5 = config.getBoolean(name);
                        field.set(o, v5);
                        break;
                    case "byte":
                    case "java.lang.Byte":
                        byte v6 = (byte) config.getInt(name);
                        field.set(o, v6);
                        break;
                    case "byte[]":
                        byte[] v7 = config.getString(name).getBytes();
                        field.set(o, v7);
                        break;
                    case "java.util.Map":
                        Map v8 = config.getObject(name).unwrapped();
                        field.set(o,v8);
                        break;
                    case "java.util.List":
                        List v9 = null;
                        ConfigList configList = config.getList(name);
                        Class actualType = (Class)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
                        v9 = new ArrayList();
                        for(int i=0;i<configList.size();i++){
                            Object o1 = transferConfigValue(actualType,configList.get(i));
                            v9.add(o1);
                        }
//                        Object v9 = transferConfigValue(actualType,config.getList(name).get(0));
                        field.set(o,v9);
                        break;
                    default:
                        Object v = transfer(c, config.getObject(name));
                        field.set(o,v);
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }

    private static Object transferConfigValue(Class actualType, ConfigValue configValue) throws Exception{
        Object o = null;
        if(configValue instanceof ConfigObject){
            o = transfer(actualType,(ConfigObject) configValue);
        }else {
            o = configValue.unwrapped();
        }
        return o;
    }

    private static <T extends Object> T transfer(Class<T> c, ConfigObject configObject) throws Exception {
        T o = c.newInstance();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            System.out.println(name);
            Class c1 = field.getType();
            switch (c1.getCanonicalName()) {
                case "java.lang.String":
                    String v1 = configObject.get(name).unwrapped().toString();
                    field.set(o, v1);
                    break;
                case "int":
                case "java.lang.Integer":
                    Integer v2 = Integer.valueOf(configObject.get(name).unwrapped().toString());
                    field.set(o, v2);
                    break;
                case "double":
                case "java.lang.Double":
                    Double v3 = Double.valueOf(configObject.get(name).unwrapped().toString());
                    field.set(o, v3);
                    break;
                case "long":
                case "java.lang.Long":
                    Long v4 = Long.valueOf(configObject.get(name).unwrapped().toString());
                    field.set(o, v4);
                    break;
                case "boolean":
                case "java.lang.Boolean":
                    Boolean v5 = Boolean.valueOf(configObject.get(name).unwrapped().toString());
                    field.set(o, v5);
                    break;
                case "byte":
                case "java.lang.Byte":
                    byte v6 = Byte.valueOf(configObject.get(name).unwrapped().toString());
                    field.set(o, v6);
                    break;
                case "byte[]":
                    byte[] v7 = configObject.get(name).unwrapped().toString().getBytes();
                    field.set(o, v7);
                    break;
                    //todo map跟list需要特别对待，未完善
                case "java.util.Map":
                    Map v8 = (Map)configObject.get(name);
                    field.set(o,v8);
                    break;
                case "java.util.List":
                    List v9 = null;
                    ConfigList configList = (ConfigList) configObject.get(name);
                    Class actualType = (Class)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
                    v9 = new ArrayList();
                    for(int i=0;i<configList.size();i++){
                        Object o1 = transferConfigValue(actualType,configList.get(i));
                        v9.add(o1);
                    }
//                        Object v9 = transferConfigValue(actualType,config.getList(name).get(0));
                    field.set(o,v9);
                    break;
                default:
                    Object v = transferConfigValue(c1, configObject.get(name));
                    field.set(o,v);
            }
        }
        return o;
    }

    /**
     *
     * @param obejct  [Config or ConfigObject]
     * @param name
     * @return
     */
    private static String  getString(Object object , String name){
        if(object instanceof Config){
            return ((Config)object).getString(name);
        }else if(object instanceof ConfigObject){
            return ((ConfigObject)object).get(name).unwrapped().toString();
        }
        return null;
    }

    private static Integer getInteger(Object object, String name){
        if(object instanceof Config){
            return ((Config)object).getInt(name);
        }else if(object instanceof ConfigObject){
            return Integer.valueOf(((ConfigObject)object).get(name).unwrapped().toString());
        }
        return 0;
    }

    private static Long getLong(Object object, String name){
        if(object instanceof Config){
            return ((Config)object).getLong(name);
        }else if(object instanceof ConfigObject){
            return Long.valueOf(((ConfigObject)object).get(name).unwrapped().toString());
        }
        return 0L;
    }

    private static Boolean getBoolean(Object object, String name){
        if(object instanceof Config){
            return ((Config)object).getBoolean(name);
        }else if(object instanceof ConfigObject){
            return Boolean.valueOf(((ConfigObject)object).get(name).unwrapped().toString());
        }
        return false;
    }

    private static Byte getByte(Object object, String name){
        if(object instanceof Config){
            return (byte)((Config)object).getInt(name);
        }else if(object instanceof ConfigObject){
            return Byte.valueOf(((ConfigObject)object).get(name).unwrapped().toString());
        }
        return 0;
    }

    private static Double getDouble(Object object, String name){
        if(object instanceof Config){
            return ((Config)object).getDouble(name);
        }else if(object instanceof ConfigObject){
            return Double.valueOf(((ConfigObject)object).get(name).unwrapped().toString());
        }
        return 0D;
    }

    private static Map getMap(Object object, String name){
        if(object instanceof Config){
            return ((Config)object).getObject(name).unwrapped();
        }else if(object instanceof ConfigObject){
            return (Map)((ConfigObject)object).get(name);
        }
        return null;
    }


    private static List getList(Object object, Class<?> actualType, String name) throws Exception {
        List list = new ArrayList();
        ConfigList configList = null;
        if(object instanceof Config){
            Config config = (Config)object;
            configList = config.getList(name);
        }else if(object instanceof ConfigObject){
            ConfigObject configObject = (ConfigObject)object;
            configList = (ConfigList) configObject.get(name);
        }
        for(int i=0;i<configList.size();i++){
            Object o1 = transferConfigValue(actualType,configList.get(i));
            list.add(o1);
        }
        return list;
    }
}

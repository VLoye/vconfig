package cn.v.vconfig;/*
 * Copyright(C) 2013 Agree Corporation. All rights reserved.
 * 
 * Contributors:
 *     Agree Corporation - initial API and implementation
 */

import com.typesafe.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(ConfigBootstrap.class);
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
        ConfigObject object = null;
        try{
            object = config.getObject("order");
        }catch (Exception e){
            logger.debug("Parse config fail, cause by: {}",e.getMessage());
            return null;
        }
        return transfer(clazz, object);
    }

    public static <T extends Object> T load(Class<T> clazz) throws Exception {
        T o = null;
        Config config = ConfigFactory.load();
        return transfer(clazz,config);
    }

    private static Object transferConfigValue(Class actualType, ConfigValue configValue) throws Exception {
        Object o = null;
        if (configValue instanceof ConfigObject) {
            o = transfer(actualType, (ConfigObject) configValue);
        } else {
            o = configValue.unwrapped();
        }
        return o;
    }

    private static <T extends Object> T transfer(Class<T> c, Object configObject) throws Exception {
        T o = c.newInstance();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
//            System.out.println(name);
            Class c1 = field.getType();
            try {
                switch (c1.getCanonicalName()) {
                    case "java.lang.String":
                        String v1 = getString(configObject, name);
                        field.set(o, v1);
                        break;
                    case "int":
                    case "java.lang.Integer":
                        Integer v2 = getInteger(configObject, name);
                        field.set(o, v2);
                        break;
                    case "double":
                    case "java.lang.Double":
                        Double v3 = getDouble(configObject, name);
                        field.set(o, v3);
                        break;
                    case "long":
                    case "java.lang.Long":
                        Long v4 = getLong(configObject, name);
                        field.set(o, v4);
                        break;
                    case "boolean":
                    case "java.lang.Boolean":
                        Boolean v5 = getBoolean(configObject, name);
                        field.set(o, v5);
                        break;
                    case "byte":
                    case "java.lang.Byte":
                        byte v6 = getByte(configObject, name);
                        field.set(o, v6);
                        break;
                    case "byte[]":
                        byte[] v7 = getBytesArray(configObject, name);
                        field.set(o, v7);
                        break;
                    //todo map跟list需要特别对待，未完善
                    case "java.util.Map":
                        Map v8 = getMap(configObject, name);
                        field.set(o, v8);
                        break;
                    case "java.util.List":
                        Class actualType = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                        List v9 = getList(configObject, actualType, name);
                        field.set(o, v9);
                        break;
                    default:
                        Object v = getObject(configObject, c1, name);
                        field.set(o, v);
                }
            }catch (Exception e){
                logger.debug("Analyse {} cause a exc: {}",name,e.getMessage());
            }
        }
        return o;
    }

    /**
//     * @param obejct [Config or ConfigObject]
     * @param name
     * @return
     */
    private static String getString(Object object, String name) {
        if (object instanceof Config) {
            return ((Config) object).getString(name);
        } else if (object instanceof ConfigObject) {
            return ((ConfigObject) object).get(name).unwrapped().toString();
        }
        return null;
    }

    private static Integer getInteger(Object object, String name) {
        if (object instanceof Config) {
            return ((Config) object).getInt(name);
        } else if (object instanceof ConfigObject) {
            return Integer.valueOf(((ConfigObject) object).get(name).unwrapped().toString());
        }
        return 0;
    }

    private static Long getLong(Object object, String name) {
        if (object instanceof Config) {
            return ((Config) object).getLong(name);
        } else if (object instanceof ConfigObject) {
            return Long.valueOf(((ConfigObject) object).get(name).unwrapped().toString());
        }
        return 0L;
    }

    private static Boolean getBoolean(Object object, String name) {
        if (object instanceof Config) {
            return ((Config) object).getBoolean(name);
        } else if (object instanceof ConfigObject) {
            return Boolean.valueOf(((ConfigObject) object).get(name).unwrapped().toString());
        }
        return false;
    }

    private static Byte getByte(Object object, String name) {
        if (object instanceof Config) {
            return (byte) ((Config) object).getInt(name);
        } else if (object instanceof ConfigObject) {
            return Byte.valueOf(((ConfigObject) object).get(name).unwrapped().toString());
        }
        return 0;
    }

    private static Double getDouble(Object object, String name) {
        if (object instanceof Config) {
            return ((Config) object).getDouble(name);
        } else if (object instanceof ConfigObject) {
            return Double.valueOf(((ConfigObject) object).get(name).unwrapped().toString());
        }
        return 0D;
    }

    private static byte[] getBytesArray(Object object, String name) {
        if (object instanceof Config) {
            return ((Config) object).getString(name).getBytes();
        } else if (object instanceof ConfigObject) {
            return ((ConfigObject) object).get(name).unwrapped().toString().getBytes();
        }
        return null;
    }

    private static Map getMap(Object object, String name) {
        if (object instanceof Config) {
            return ((Config) object).getObject(name).unwrapped();
        } else if (object instanceof ConfigObject) {
            return (Map) ((ConfigObject) object).get(name);
        }
        return null;
    }


    private static List getList(Object object, Class<?> actualType, String name) throws Exception {
        List list = new ArrayList();
        ConfigList configList = null;
        if (object instanceof Config) {
            Config config = (Config) object;
            configList = config.getList(name);
        } else if (object instanceof ConfigObject) {
            ConfigObject configObject = (ConfigObject) object;
            configList = (ConfigList) configObject.get(name);
        }
        for (int i = 0; i < configList.size(); i++) {
            Object o1 = transferConfigValue(actualType, configList.get(i));
            list.add(o1);
        }
        return list;
    }

    private static Object getObject(Object object, Class<?> actualType, String name) throws Exception {
        if (object instanceof Config) {
            Config config = (Config) object;
            return transfer(actualType, config.getObject(name));
        } else if (object instanceof ConfigObject) {
            ConfigObject configObject = (ConfigObject) object;
            return transferConfigValue(actualType, configObject.get(name));
        }
        return null;
    }

}

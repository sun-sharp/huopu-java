package com.wx.genealogy.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ObjectUtil
 * @Author hangyi
 * @Data 2020/6/16 10:27
 * @Description
 * @Version 1.0
 **/
public class ObjectUtil {


    /****
     * @param object 对象
     * @return 如果对象不为空，且没有空值。返回false，对象为空或者有空值，返回true
     * */
    public static boolean checkObjFieldIsNull(Object object) throws IllegalAccessException {
        boolean flag = false;
        if (null != object) {
            for (Field field : object.getClass().getDeclaredFields()) {
                //在用反射时访问私有变量（private修饰变量）
                field.setAccessible(true);
                if (field.get(object) == null || field.get(object).equals("")) {
                    flag = true;
                    return flag;
                }
                if (field.get(object) != null && field.get(object).toString().trim().equals("")) {
                    flag = true;
                    return flag;
                }
            }
        } else {
            flag = true;
        }
        return flag;
    }

    /****
     * @param object 对象
     * @return 如果对象不为空，且没有空值。返回false，对象为空或者有空值，返回true
     * */
    public static boolean checkPartObjFieldIsNull(Object object, String... attributes) throws IllegalAccessException {
        boolean flag = false;
        if (null != object) {
            for (Field field : object.getClass().getDeclaredFields()) {
                //在用反射时访问私有变量（private修饰变量）
                field.setAccessible(true);
                if (ArraysUtil.useLoop(attributes, field.getName())) {
                    continue;
                }
                if (field.get(object) == null || field.get(object).equals("")) {
                    flag = true;
                    return flag;
                }
                if (field.get(object) != null && field.get(object).toString().trim().equals("")) {
                    flag = true;
                    return flag;
                }
            }
        } else {
            flag = true;
        }
        return flag;
    }


    /**
     * @return void
     * @Author hangyi
     * @Description 复制同名参数
     * @Date 10:29 2020/6/16
     * @Param [src, target]
     **/
    public static void copyByName(Object src, Object target) {
        if (src == null || target == null) {
            return;
        }
        try {
            Map<String, Field> srcFieldMap = getAssignableFieldsMap(src);
            Map<String, Field> targetFieldMap = getAssignableFieldsMap(target);
            for (String srcFieldName : srcFieldMap.keySet()) {
                Field srcField = srcFieldMap.get(srcFieldName);
                if (srcField == null) {
                    continue;
                }
                // 变量名需要相同
                if (!targetFieldMap.keySet().contains(srcFieldName)) {
                    continue;
                }
                Field targetField = targetFieldMap.get(srcFieldName);
                if (targetField == null) {
                    continue;
                }
                // 类型需要相同
                if (!srcField.getType().equals(targetField.getType())) {
                    continue;
                }

                targetField.set(target, srcField.get(src));
            }
        } catch (Exception e) {
            // 异常
        }
        return;
    }

    private static Map<String, Field> getAssignableFieldsMap(Object obj) {
        if (obj == null) {
            return new HashMap<String, Field>();
        }
        Map<String, Field> fieldMap = new HashMap<String, Field>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            // 过滤不需要拷贝的属性
            if (Modifier.isStatic(field.getModifiers())
                    || Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            fieldMap.put(field.getName(), field);
        }
        return fieldMap;
    }

    public final static boolean isNull(Object[] objs) {
        return (objs == null || objs.length == 0);
    }


    public final static boolean isNull(Object obj) {
        return (obj == null || isNull(obj.toString()));
    }

    public final static boolean isNull(Integer integer) {
        return integer == null;
    }


    public final static boolean isNull(Collection collection) {
        return (collection == null || collection.size() == 0);
    }


    public final static boolean isNull(Map map) {
        return (map == null || map.size() == 0);
    }


    public final static boolean isNull(String str) {
        return str == null || "".equals(str.trim())
                || "null".equals(str.toLowerCase());
    }


    public final static boolean isNull(Long longs) {
        return (longs == null || longs == 0);
    }


    public final static boolean isNotNull(Long longs) {
        return !isNull(longs);
    }


    public final static boolean isNotNull(String str) {
        return !isNull(str);
    }


    public final static boolean isNotNull(Collection collection) {
        return !isNull(collection);
    }


    public final static boolean isNotNull(Map map) {
        return !isNull(map);
    }


    public final static boolean isNotNull(Integer integer) {
        return !isNull(integer);
    }


    public final static boolean isNotNull(Object[] objs) {
        return !isNull(objs);
    }


    public final static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }
}

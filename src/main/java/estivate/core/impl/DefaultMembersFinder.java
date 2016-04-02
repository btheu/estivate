package estivate.core.impl;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import estivate.core.MembersFinder;

/**
 * 
 * @author Benoit Theunissen
 *
 */
public class DefaultMembersFinder implements MembersFinder {

    @Override
    public List<AccessibleObject> list(Class<?> clazz) {
        List<AccessibleObject> members = new ArrayList<>();

        members.addAll(getAllFields(clazz));

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            members.add(method);
        }

        return members;
    }

    public static List<Field> getAllFields(Class<?> clazz) {

        List<Field> res = new ArrayList<Field>();

        Class<?> index = clazz;
        while (index != Object.class) {
            res.addAll(Arrays.asList(index.getDeclaredFields()));

            index = index.getSuperclass();
        }

        return res;
    }

}

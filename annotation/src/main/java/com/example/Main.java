package com.example;

import com.example.annotation.MethodAnnotation;
import com.example.annotation.ParameterAnnotation;
import com.example.info.Position;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        Map<Class<?>, Integer> map = new HashMap<Class<?>, Integer>();
        map = analyze(Position.class, MethodAnnotation.class, ParameterAnnotation.class);

        Map<Class<?>, Integer> map2 = new HashMap<Class<?>, Integer>();
        map2 = analizeRecursion(Position.class, MethodAnnotation.class, ParameterAnnotation.class, map2);

        Map<Class<?>, Integer> map3 = new HashMap<Class<?>, Integer>();
        map3.put(MethodAnnotation.class, 0);
        map3.put(ParameterAnnotation.class, 0);
        map3 = analizeRecursionOneMap(Position.class, MethodAnnotation.class, ParameterAnnotation.class, map3);

        Map<Class<?>, Integer> map4 = new HashMap<Class<?>, Integer>();
        map4 = analizeRecursionOneEmptyMap(Position.class, MethodAnnotation.class, ParameterAnnotation.class, map4);

        System.out.println(map.toString());
        System.out.println(map2.toString());
        System.out.println(map3.toString());
        System.out.println(map4.toString());

    }

    public static Map<Class<?>, Integer> analyze(Class<?> cls, Class<? extends Annotation> methodAn, Class<? extends Annotation> parameterAn) {

        Map<Class<?>, Integer> map = new HashMap<Class<?>, Integer>();
        Integer a = 0;
        Integer b = 0;

        for (; !cls.equals(Object.class); ) {
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(methodAn)) {
                    a = a + 1;
                    map.put(methodAn, a);

                }
                Parameter[] parameters = method.getParameters();
                for (Parameter parameter : parameters) {
                    if (parameter.isAnnotationPresent(parameterAn)) {
                        b = b + 1;
                        map.put(parameterAn, b);
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return map;

    }

    public static Map<Class<?>, Integer> analizeRecursion(Class<?> cls, Class<? extends Annotation> methodAn, Class<? extends Annotation> parameterAn, Map<Class<?>, Integer> mapOld) {

        Map<Class<?>, Integer> map = new HashMap<Class<?>, Integer>();

        if (!cls.equals(Object.class)) {
            Map<Class<?>, Integer> mapNew = new HashMap<Class<?>, Integer>();
            Integer a = 0;
            Integer b = 0;
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(methodAn)) {
                    a = a + 1;
                    mapNew.put(methodAn, a);
                }
                Parameter[] parameters = method.getParameters();
                for (Parameter parameter : parameters) {
                    if (parameter.isAnnotationPresent(parameterAn)) {
                        b = b + 1;
                        mapNew.put(parameterAn, b);
                    }
                }
            }
            map = Stream.of(mapOld, mapNew)
                    .flatMap(m -> m.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue + newValue));
            cls = cls.getSuperclass();
            mapOld = analizeRecursion(cls, MethodAnnotation.class, ParameterAnnotation.class, map);


        }
        return mapOld;
    }

    public static Map<Class<?>, Integer> analizeRecursionOneMap(Class<?> cls, Class<? extends Annotation> methodAn, Class<? extends Annotation> parameterAn, Map<Class<?>, Integer> mapOld) {

        if (!cls.equals(Object.class)) {
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(methodAn)) {
                    mapOld.compute(methodAn, (key, value) -> value = value + 1);
                }
                Parameter[] parameters = method.getParameters();
                for (Parameter parameter : parameters) {
                    if (parameter.isAnnotationPresent(parameterAn)) {
                        mapOld.compute(parameterAn, (key, value) -> value = value + 1);
                    }
                }
            }
            cls = cls.getSuperclass();
            mapOld = analizeRecursion(cls, MethodAnnotation.class, ParameterAnnotation.class, mapOld);

        }
        return mapOld;
    }

    public static Map<Class<?>, Integer> analizeRecursionOneEmptyMap(Class<?> cls, Class<? extends Annotation> methodAn, Class<? extends Annotation> parameterAn, Map<Class<?>, Integer> mapOld) {

        if (!cls.equals(Object.class)) {
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(methodAn)) {
                    mapOld.computeIfAbsent(methodAn, (key) -> 0);
                    mapOld.computeIfPresent(methodAn, (key, value) -> value = value + 1);
                }
                Parameter[] parameters = method.getParameters();
                for (Parameter parameter : parameters) {
                    if (parameter.isAnnotationPresent(parameterAn)) {
                        mapOld.computeIfAbsent(parameterAn, (key) -> 0);
                        mapOld.computeIfPresent(parameterAn, (key, value) -> value = value + 1);
                    }
                }


            }
            cls = cls.getSuperclass();
            mapOld = analizeRecursion(cls, MethodAnnotation.class, ParameterAnnotation.class,
                    mapOld);
        }
        return mapOld;
    }
}
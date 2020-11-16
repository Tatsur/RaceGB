package gb.race.j3.h7;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class Main {

    private static Method beforeSuite = null;
    private static Method afterSuite = null;
    private static List<Method> tests = new LinkedList<>();

    public static void main(String[] args) {
        run(Target.class);
        run(Target.class.getName());
    }
    private static void run(Class targetClass){
        try {
            getTargetMethods(targetClass);
        }catch (RuntimeException e){
            System.err.println("@BeforeSuite or @AfterSuite methods not unique");
            e.printStackTrace();
            return;
        }
        startTargetMethods();
        release();
    }
    private static void run(String targetClassName){
        Class targetClass = null;
            try {
                targetClass = Class.forName(targetClassName);
            } catch (ClassNotFoundException e) {
                System.err.println("Target class not found by this name");
                e.printStackTrace();
            }
            run(targetClass);
    }

    private static void getTargetMethods(Class targetClass) {
        Method[] methods = targetClass.getDeclaredMethods();
        List<Method> tests = new ArrayList<>();
        for (Method method : methods) {
            if(method.getAnnotation(BeforeSuite.class) != null) {
                if(beforeSuite == null) beforeSuite = method;
                else throw new RuntimeException();
            }
            if(method.getAnnotation(AfterSuite.class)  != null) {
                if(afterSuite == null) afterSuite = method;
                else throw new RuntimeException();
            }
            if(method.getAnnotation(Test.class) != null) tests.add(method);
        }
        beforeSuite.setAccessible(true);
        afterSuite.setAccessible(true);
        int prio = Priority.MAXIMUM;
        while(prio >= Priority.MINIMUM){
            for (Method test : tests) {
                if(test.getAnnotation(Test.class).priority() == prio){
                    test.setAccessible(true);
                    Main.tests.add(test);
                }
            }
            prio--;
        }
    }
    private static void startTargetMethods()  {
        String[] str = new String[]{"one","two","three"};
        int[] ints = new int[]{1,2,3};
        Date date = new Date();
        try {
            Target target = (Target) beforeSuite.invoke(null);
            for (Method test : tests) {
                Type[] type = test.getParameterTypes();
                if(type.length != 0) {
                    if (String[].class.equals(type[0])) {
                        test.invoke(target, (Object) str);
                    } else if (int[].class.equals(type[0])) {
                        test.invoke(target, (Object) ints);
                    } else if (Date.class.equals(type[0])) {
                        test.invoke(target, date);
                    } else if (type[0] != null) {
                        throw new IllegalStateException("Unexpected value: " + type[0]);
                    }
                }else test.invoke(target);
            }
            afterSuite.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    private static void release(){
        beforeSuite = null;
        afterSuite = null;
        tests = new LinkedList<>();
    }
}

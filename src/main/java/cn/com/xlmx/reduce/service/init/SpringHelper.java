package cn.com.xlmx.reduce.service.init;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author luxiaohang
 * @Title:
 * @Description:
 * @date 2019/04/27/15:00
 **/
@Component
public class SpringHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(null == SpringHelper.getApplicationContext()){
            SpringHelper.applicationContext = applicationContext;
        }
    }

    /**
     * 获取applicationContext
     **/
    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    /**
     * 通过name获取Bean
     **/
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean
     **/
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name及Clazz获取指定的Bean
     **/
    public static <T> T getBean(String name , Class<T> clazz){
        return getApplicationContext().getBean(name,clazz);
    }

}

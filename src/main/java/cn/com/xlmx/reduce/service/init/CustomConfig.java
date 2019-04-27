package cn.com.xlmx.reduce.service.init;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author luxiaohang
 * @Title:
 * @Description: 读取application.yml中的自定义数据
 * 使用单例模式进行设计
 * @date 2019/04/27/14:31
 **/
@Component
public class CustomConfig {

    private volatile static CustomConfig customConfig = null;

    /**
     * 获取CustomConfig的单例
     **/
    public static CustomConfig getInstance(){
        if(null == customConfig){
            synchronized (CustomConfig.class){
                customConfig = SpringHelper.getBean(CustomConfig.class);
            }
        }

        return customConfig;
    }


}

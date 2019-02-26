package cn.notemi.annotations;

import cn.notemi.common.PlatformResult;
import cn.notemi.common.Result;

import java.lang.annotation.*;

/**
 * Title：ResponseResult
 * Description：接口返回结果增强  会通过拦截器拦截后放入标记，在ResponseResultHandler 进行结果处理
 *
 * @author Flicker
 * @create 2019/2/26 2:16 PM
 **/
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseResult {

    Class<? extends Result>  value() default PlatformResult.class;

}
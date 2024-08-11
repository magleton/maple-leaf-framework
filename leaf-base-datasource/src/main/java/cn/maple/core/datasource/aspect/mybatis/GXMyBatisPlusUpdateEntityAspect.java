package cn.maple.core.datasource.aspect.mybatis;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.maple.core.datasource.annotation.GXMyBatisListener;
import cn.maple.core.datasource.constant.GXMyBatisEventConstant;
import cn.maple.core.datasource.enums.GXModelEventNamingEnums;
import cn.maple.core.datasource.event.GXMyBatisModelUpdateEntityEvent;
import cn.maple.core.datasource.service.GXMybatisListenerService;
import cn.maple.core.framework.exception.GXBusinessException;
import cn.maple.core.framework.util.GXEventPublisherUtils;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.apache.ibatis.annotations.Mapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 更新实体(Entity)切面类
 */
@Aspect
@Component
@Slf4j
@SuppressWarnings("all")
public class GXMyBatisPlusUpdateEntityAspect {
    @Around("target(cn.maple.core.datasource.mapper.GXBaseMapper) && execution(* update(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.debug("发布更新前的事件开始");
        Object proceed = point.proceed();
        publishEvent(point);
        log.debug("发布更新后的事件结束");
        return proceed;
    }


    /**
     * 处理切点的参数
     *
     * @param type  类型
     * @param point 切点对象
     */
    private Dict handlePointArgs(Type type, ProceedingJoinPoint point) {
        Dict retDict = Dict.create();
        Class<Mapper> mapper = convertTypeToMapper(type);
        if (ObjectUtil.isNotNull(mapper)) {
            Object[] args = point.getArgs();
            Object entity = args[0];
            Object objectWrapper = args[1];
            Dict updateCondition = handleUpdateWrapper(objectWrapper, entity.getClass());
            Dict entityData = Convert.convert(Dict.class, entity);
            retDict.set("entityData", entityData).set("keyOperatorPairs", updateCondition.get("keyOperatorPairs")).set("keyValuePairs", updateCondition.get("keyValuePairs"));
        }
        return retDict;
    }

    /**
     * 发布事件
     *
     * @param point 切点对象
     */
    private void publishEvent(ProceedingJoinPoint point) {
        Type[] myBatisMapper = AopUtils.getTargetClass(point.getTarget()).getInterfaces();
        for (Type type : myBatisMapper) {
            Class<Mapper> mapper = convertTypeToMapper(type);
            if (ObjectUtil.isNotNull(mapper)) {
                GXMyBatisListener myBatisListener = AnnotationUtil.getAnnotation(mapper, GXMyBatisListener.class);
                if (ObjectUtil.isNull(myBatisListener)) {
                    return;
                }
                Dict source = handlePointArgs(type, point);
                Class<? extends GXMybatisListenerService> aClass = myBatisListener.listenerClazz();
                String eventType = GXModelEventNamingEnums.SYNC_UPDATE_ENTITY.getEventType();
                String eventName = GXModelEventNamingEnums.SYNC_UPDATE_ENTITY.getEventName();
                Dict eventParam = Dict.create().set("listenerClazzName", aClass.getSimpleName()).set("listenerClazz", aClass);
                String runType = myBatisListener.runType();
                if (runType.equals(GXMyBatisEventConstant.MYBATIS_ASYNC_EVENT)) {
                    eventType = GXModelEventNamingEnums.ASYNC_UPDATE_ENTITY.getEventType();
                    eventName = GXModelEventNamingEnums.ASYNC_UPDATE_ENTITY.getEventName();
                }
                GXMyBatisModelUpdateEntityEvent<Dict> updateEntityEvent = new GXMyBatisModelUpdateEntityEvent<>(source, eventType, eventParam, eventName);
                GXEventPublisherUtils.publishEvent(updateEntityEvent);
            }
        }
    }

    /**
     * 将Type转换为Mapper
     *
     * @param type 待转换的对象
     */
    private Class<Mapper> convertTypeToMapper(Type type) {
        return Convert.convert(new TypeReference<>() {
        }, type);
    }

    /**
     * 处理 UpdateWrapper条件对象
     *
     * @param objectWrapper 更新条件对象
     * @param clazz         条件的泛型对象
     */
    private <T> Dict handleUpdateWrapper(Object objectWrapper, T clazz) {
        UpdateWrapper<T> updateWrapper = Convert.convert(new TypeReference<UpdateWrapper<T>>() {
        }, objectWrapper);
        Dict conditionDict = parseWhereSQL(updateWrapper);
        return conditionDict;
    }

    /**
     * 使用JSQLParser库进行Where语句SQL的解析
     *
     * @param updateWrapper 更新的Wrapper
     */
    private <T> Dict parseWhereSQL(UpdateWrapper<T> updateWrapper) {
        final Integer[] paramNameSeq = new Integer[]{0};
        String whereSQL = updateWrapper.getTargetSql();
        Dict keyValuePairs = Dict.create();
        Dict keyOperatorPairs = Dict.create();
        Map<String, Object> paramNameValuePairs = updateWrapper.getParamNameValuePairs();
        try {
            Expression expression = CCJSqlParserUtil.parseCondExpression(whereSQL);
            expression.accept(new ExpressionVisitorAdapter() {
                @Override
                public void visit(AndExpression andExpression) {
                    andExpression.getLeftExpression().accept(this);
                    andExpression.getRightExpression().accept(this);
                }

                @Override
                public void visit(OrExpression orExpression) {
                    orExpression.getLeftExpression().accept(this);
                    orExpression.getRightExpression().accept(this);
                }

                @Override
                public void visit(EqualsTo equalsTo) {
                    final String genParamName = Constants.WRAPPER_PARAM + (++paramNameSeq[0]);
                    String leftExpressionStr = CharSequenceUtil.toCamelCase(equalsTo.getLeftExpression().toString());
                    keyOperatorPairs.set(leftExpressionStr, equalsTo.getStringExpression());
                    keyValuePairs.set(leftExpressionStr, paramNameValuePairs.get(genParamName));
                }

                @Override
                public void visit(GreaterThanEquals greaterThanEquals) {
                    final String genParamName = Constants.WRAPPER_PARAM + (++paramNameSeq[0]);
                    String leftExpressionStr = CharSequenceUtil.toCamelCase(greaterThanEquals.getLeftExpression().toString());
                    keyOperatorPairs.set(leftExpressionStr, greaterThanEquals.getStringExpression());
                    keyValuePairs.set(leftExpressionStr, paramNameValuePairs.get(genParamName));
                }

                @Override
                public void visit(LikeExpression likeExpression) {
                    final String genParamName = Constants.WRAPPER_PARAM + (++paramNameSeq[0]);
                    String leftExpressionStr = CharSequenceUtil.toCamelCase(likeExpression.getLeftExpression().toString());
                    keyOperatorPairs.set(leftExpressionStr, likeExpression.getStringExpression());
                    keyValuePairs.set(leftExpressionStr, paramNameValuePairs.get(genParamName));
                }

                @Override
                public void visit(MinorThanEquals minorThanEquals) {
                    final String genParamName = Constants.WRAPPER_PARAM + (++paramNameSeq[0]);
                    String leftExpressionStr = CharSequenceUtil.toCamelCase(minorThanEquals.getLeftExpression().toString());
                    keyOperatorPairs.set(leftExpressionStr, minorThanEquals.getStringExpression());
                    keyValuePairs.set(leftExpressionStr, paramNameValuePairs.get(genParamName));
                }
            });
        } catch (Exception e) {
            log.info("解析SQL的Where语句失败");
            throw new GXBusinessException(e.getMessage(), e);
        }
        return Dict.create().set("keyOperatorPairs", keyOperatorPairs).set("keyValuePairs", keyValuePairs);
    }
}

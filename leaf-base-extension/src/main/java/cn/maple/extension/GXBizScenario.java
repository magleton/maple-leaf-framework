package cn.maple.extension;

/**
 * BizScenario（业务场景）= bizId + useCase + scenario,
 * which can uniquely identify a user scenario.
 *
 * @author britton
 */
public class GXBizScenario {
    /**
     * 默认业务ID
     */
    public static final String DEFAULT_BIZ_ID = "#defaultBizId#";

    /**
     * 默认用例
     */
    public static final String DEFAULT_USE_CASE = "#defaultUseCase#";

    /**
     * 默认场景
     */
    public static final String DEFAULT_SCENARIO = "#defaultScenario#";

    /**
     * 分隔符
     */
    private static final String DOT_SEPARATOR = ".";

    /**
     * bizId is used to identify a business,
     * such as "xx",
     * it's nullable if there is only one biz
     */
    private String bizId = DEFAULT_BIZ_ID;

    /**
     * useCase is used to identify a use case,
     * such as "placeOrder",
     * can not be null
     */
    private String useCase = DEFAULT_USE_CASE;

    /**
     * scenario is used to identify a use case,
     * such as "xx","normal",
     * can not be null
     */
    private String scenario = DEFAULT_SCENARIO;

    public static GXBizScenario valueOf(String bizId, String useCase, String scenario) {
        GXBizScenario bizScenario = new GXBizScenario();
        bizScenario.bizId = bizId;
        bizScenario.useCase = useCase;
        bizScenario.scenario = scenario;
        return bizScenario;
    }

    public static GXBizScenario valueOf(String bizId, String useCase) {
        return GXBizScenario.valueOf(bizId, useCase, DEFAULT_SCENARIO);
    }

    public static GXBizScenario valueOf(String bizId) {
        return GXBizScenario.valueOf(bizId, DEFAULT_USE_CASE, DEFAULT_SCENARIO);
    }

    public static GXBizScenario newDefault() {
        return GXBizScenario.valueOf(DEFAULT_BIZ_ID, DEFAULT_USE_CASE, DEFAULT_SCENARIO);
    }

    /**
     * For above case, the BizScenario will be "xx.placeOrder.yy",
     * with this code,
     * we can provide extension processing other than "xx.placeOrder.normal" scenario.
     *
     * @return 标识符
     */
    public String getUniqueIdentity() {
        return bizId + DOT_SEPARATOR + useCase + DOT_SEPARATOR + scenario;
    }

    public String getIdentityWithDefaultScenario() {
        return bizId + DOT_SEPARATOR + useCase + DOT_SEPARATOR + DEFAULT_SCENARIO;
    }

    public String getIdentityWithDefaultUseCase() {
        return bizId + DOT_SEPARATOR + DEFAULT_USE_CASE + DOT_SEPARATOR + DEFAULT_SCENARIO;
    }
}

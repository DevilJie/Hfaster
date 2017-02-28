package hsj.czy.mybatisframe.core;

import java.io.Serializable;

/**
 * @author Caizongyou
 */
public class IbatisCriterion implements Serializable {

    public enum IbatisCriteriaType {
        eq, ge, le, gt, lt, like, isNull, isNotNull, in
    }

    private static final long  serialVersionUID = -4327337412974065297L;

    private String             key;

    private Object             value;

    private IbatisCriteriaType type;

    private IbatisCriterion(){
    }

    private IbatisCriterion(String key, Object value, IbatisCriteriaType type){
        super();
        this.key = key;
        this.value = value;
        this.type = type;
    }

    /**
     * 相等
     * 
     * @param key
     * @param value
     * @return
     */
    public static IbatisCriterion eq(String key, Object value) {
        return new IbatisCriterion(key, value, IbatisCriteriaType.eq);
    }

    /**
     * 大于等于
     * 
     * @param key
     * @param value
     * @return
     */
    public static IbatisCriterion ge(String key, Object value) {
        return new IbatisCriterion(key, value, IbatisCriteriaType.ge);
    }

    /**
     * 小于等于
     * 
     * @param key
     * @param value
     * @return
     */
    public static IbatisCriterion le(String key, Object value) {
        return new IbatisCriterion(key, value, IbatisCriteriaType.le);
    }

    /**
     * 大于
     * 
     * @param key
     * @param value
     * @return
     */
    public static IbatisCriterion gt(String key, Object value) {
        return new IbatisCriterion(key, value, IbatisCriteriaType.gt);
    }

    /**
     * 小于
     * 
     * @param key
     * @param value
     * @return
     */
    public static IbatisCriterion lt(String key, Object value) {
        return new IbatisCriterion(key, value, IbatisCriteriaType.lt);
    }

    /**
     * 模糊匹配
     * 
     * @param key
     * @param value
     * @return
     */
    public static IbatisCriterion like(String key, Object value) {
        return new IbatisCriterion(key, value, IbatisCriteriaType.like);
    }

    /**
     * 为空
     * 
     * @param key
     * @param value
     * @return
     */
    public static IbatisCriterion isNull(String key) {
        return new IbatisCriterion(key, null, IbatisCriteriaType.isNull);
    }

    /**
     * 不为空
     * 
     * @param key
     * @param value
     * @return
     */
    public static IbatisCriterion isNotNull(String key) {
        return new IbatisCriterion(key, null, IbatisCriteriaType.isNotNull);
    }

    /**
     * in
     * 
     * @param key
     * @param value
     * @return
     */
    public static IbatisCriterion in(String key, Object value) {
        return new IbatisCriterion(key, value, IbatisCriteriaType.in);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public IbatisCriteriaType getType() {
        return type;
    }

    public void setType(IbatisCriteriaType type) {
        this.type = type;
    }
}

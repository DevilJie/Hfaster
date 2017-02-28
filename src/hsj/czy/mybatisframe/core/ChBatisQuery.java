package hsj.czy.mybatisframe.core;

import hsj.czy.mybatisframe.bean.Pager;
import hsj.czy.mybatisframe.exception.MyBatistFrameServiceException;

import java.util.HashMap;
import java.util.Map;

public abstract class ChBatisQuery {

    protected String sql;

    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * 返回list
     * 
     * @return
     */
    public Object list() {
        return null;
    };

    /**
     * 返回唯一一条数据
     * 
     * @return
     */
    public Object uniqueResult() {
        return null;
    };

    /**
     * 返回一个对象
     * 
     * @return
     */
    public Object uniqueObject() {
        return null;
    };

    /**
     * 执行更新sql
     * 
     * @return
     */
    public Long executeUpdate() throws MyBatistFrameServiceException {
        return null;
    };

    /**
     * 执行sql
     * 
     * @return
     * @throws MyBatistFrameServiceException
     */
    public Object execute() throws MyBatistFrameServiceException {
        return null;
    };

    /**
     * 分页查询
     * 
     * @param pager
     * @return
     */
    public Pager queryPager(Pager pager) {
        return null;
    };

    protected Map<String, Object> parameter = new HashMap<String, Object>();

    public ChBatisQuery setParameter(Map<String, Object> parameter) {
        this.parameter.putAll(parameter);
        return this;
    }

    public ChBatisQuery setParameter(String key, Object value) {
        this.parameter.put(key, value);
        return this;
    }
}

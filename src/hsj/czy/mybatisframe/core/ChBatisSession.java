package hsj.czy.mybatisframe.core;

import hsj.czy.mybatisframe.mapper.BaseMapper;

public abstract class ChBatisSession {

    protected BaseMapper baseMapper;

    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    /**
     * 创建查询类
     * 
     * @param sql 执行sql
     * @param c 返回对象
     * @return
     */
    public ChBatisQuery createQuery(String sql, Class c) {
        return null;
    };

    /**
     * 创建查询类
     * 
     * @param sql 执行sql
     * @param c 返回对象
     * @return
     */
    public ChBatisQuery createQuery(String sql) {
        return null;
    };
}

package hsj.czy.mybatisframe.core;

import hsj.czy.mybatisframe.mapper.BaseMapper;

public class SimpleBatisSession extends ChBatisSession {

    public static SimpleBatisSession getInstance(BaseMapper baseMapper) {
        ChBatisSession s = new SimpleBatisSession();
        s.setBaseMapper(baseMapper);
        return (SimpleBatisSession) s;
    }

    @Override
    public ChBatisQuery createQuery(String sql, Class c) {
        SimpleBatisQuery q = SimpleBatisQuery.getInstance(baseMapper, sql);
        q.setC(c);
        return q;
    }

    @Override
    public ChBatisQuery createQuery(String sql) {
        SimpleBatisQuery q = SimpleBatisQuery.getInstance(baseMapper, sql);
        return q;
    }

}

package hsj.czy.mybatisframe.core;

import hsj.czy.mybatisframe.bean.Pager;
import hsj.czy.mybatisframe.bean.Pager.Order;
import hsj.czy.mybatisframe.exception.MyBatistFrameServiceException;
import hsj.czy.mybatisframe.listener.ChBatisFrameConfigListener;
import hsj.czy.mybatisframe.mapper.BaseMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

@SuppressWarnings("rawtypes")
public class SimpleBatisQuery extends ChBatisQuery {

    private Logger     logger = Logger.getLogger(this.getClass());
    private BaseMapper baseMapper;
    private Class      c;

    @Override
    public Object execute() throws MyBatistFrameServiceException {
        try {
            Map<String, Object> map = CoreUtil.processSql(sql, parameter);
            List<Object> l = baseMapper.queryBySql(map);
            if (l == null || l.size() == 0) return new ArrayList<Object>();
            return l;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object list() {
        try {
            Map<String, Object> map = CoreUtil.processSql(sql, parameter);
            List<Object> l = baseMapper.queryBySql(map);
            if (l == null || l.size() == 0) return new ArrayList<Object>();
            Object e;
            HashMap<String, Object> hashMap;
            String key;
            List<Object> returnList = new ArrayList<Object>();
            if (c != null) {
                for (Object object : l) {
                    if (object == null) continue;
                    e = Class.forName(c.getName()).newInstance();
                    hashMap = (HashMap<String, Object>) object;
                    CoreUtil.process(e, hashMap);
                    returnList.add(e);
                }
                return returnList;
            } else return l;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object uniqueObject() {
        try {
            Map<String, Object> map = CoreUtil.processSql(sql, parameter);
            List<Object> l = baseMapper.queryBySql(map);
            if (l == null || l.size() == 0) return null;
            Object e;
            HashMap<String, Object> hashMap;
            String key;
            List<Object> returnList = new ArrayList<Object>();
            if (c != null) {
                for (Object object : l) {
                    if (object == null) continue;
                    e = Class.forName(c.getName()).newInstance();
                    hashMap = (HashMap<String, Object>) object;
                    CoreUtil.process(e, hashMap);
                    returnList.add(e);
                }
                if (returnList.size() > 1) throw new MyBatistFrameServiceException("not unique result");
                return returnList.get(0);
            } else {
                return l.get(0);
            }
        } catch (MyBatistFrameServiceException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object uniqueResult() {
        try {
            Map<String, Object> map = CoreUtil.processSql(sql, parameter);
            List<Object> l = baseMapper.queryBySql(map);
            if (l == null || l.size() == 0) return null;
            for (Object object : l) {
                if (object == null) continue;
                HashMap<String, Object> m = (HashMap<String, Object>) object;
                for (Map.Entry<String, Object> entry : m.entrySet()) {
                    return entry.getValue();
                }
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Pager queryPager(Pager pager) {
        try {

            Map<String, Object> map = CoreUtil.processSql(sql, parameter);
            sql = map.get("SQL").toString();
            if (pager != null && !StringUtils.isEmpty(pager.getOrderBy())
                && sql.toUpperCase().indexOf("ORDER BY") == -1) {
                sql += " ORDER BY " + ChBatisFrameConfigListener.pojoNameToColumnName(pager.getOrderBy()) + " ";
                if (pager.getOrder() != null) sql += pager.getOrder() == Order.asc ? "ASC" : "DESC";
                else sql += "DESC";
            }
            // sql += " GROUP BY " + sqlgroup;

            String countSql = "SELECT COUNT(*) FROM (" + sql + ") E";
            map.put("SQL", countSql);
            List<Object> l = baseMapper.queryBySql(map);
            Integer count = 0;
            for (Object object : l) {
                if (object == null) continue;
                HashMap<String, Object> m = (HashMap<String, Object>) object;
                for (Map.Entry<String, Object> entry : m.entrySet()) {
                    count = Integer.parseInt(entry.getValue().toString());
                }
            }
            pager.setTotalCount(count);
            map.put("ENDPOSITION", pager.getPageSize() * pager.getPageNumber());
            map.put("STARTPOSITION", pager.getPageSize() * (pager.getPageNumber() - 1));
            map.put("PAGESIZE", pager.getPageSize());

            map.put("SQL", sql);

            l = baseMapper.getPagerBySql(map);

            Object e;
            HashMap<String, Object> hashMap;
            if (c != null) {
                List<Object> returnList = new ArrayList<Object>();
                if (l != null) {
                    for (Object object : l) {
                        e = Class.forName(c.getName()).newInstance();
                        hashMap = (HashMap<String, Object>) object;
                        CoreUtil.process(e, hashMap);
                        returnList.add(e);
                    }
                }
                pager.setResult(returnList);
            } else {
                pager.setResult(l);
            }
            return pager;
        } catch (Exception e) {
            logger.error("if you query a object, make sure you are calling like this: ChBatisSession.createQuery(sql, Object.class)",
                         e);
            return pager;
        }
    }

    @Override
    public Long executeUpdate() throws MyBatistFrameServiceException {
        try {
            Map<String, Object> map = CoreUtil.processSql(sql, parameter);
            Long l = baseMapper.executeBySql(map);
            if (l == null) return null;
            return l;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    public static SimpleBatisQuery getInstance(BaseMapper baseMapper, String sql) {
        SimpleBatisQuery s = new SimpleBatisQuery();
        s.setBaseMapper(baseMapper);
        s.setSql(sql);
        return s;
    }

    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    public void setC(Class c) {
        this.c = c;
    }

    private SimpleBatisQuery(){
    }

}

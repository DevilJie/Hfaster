package hsj.czy.mybatisframe.service;

import hsj.czy.mybatisframe.bean.Pager;
import hsj.czy.mybatisframe.exception.MyBatistFrameServiceException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDao<T, PK extends Serializable> {

    /**
     * 根据ID获取实体对象
     * 
     * @param id 记录ID
     * @return 实体对象
     */
    public T get(PK id) throws MyBatistFrameServiceException;

    /**
     * 获取所有实体对象集合
     * 
     * @return 实体对象集合
     */
    public List<T> getAllList() throws MyBatistFrameServiceException;

    /**
     * 保存实体对象
     * 
     * @param entity 对象
     * @return ID
     */
    public Serializable save(T entity) throws MyBatistFrameServiceException;

    /**
     * 批量插入
     * 
     * @param list
     * @throws MyBatistFrameServiceException
     */
    public List<Serializable> batchSave(List<T> list) throws MyBatistFrameServiceException;

    /**
     * 更新实体对象
     * 
     * @param entity 对象
     */
    public void update(T entity) throws MyBatistFrameServiceException;

    // /**
    // * 批量更新实体对象
    // *
    // * @param entity 对象
    // */
    // public void batchUpdate(List<T> list) throws MyBatistFrameServiceException;

    /**
     * 根据ID删除实体对象
     * 
     * @param id 记录ID
     */
    public void delete(PK id) throws MyBatistFrameServiceException;

    /**
     * 根据ID数组删除实体对象
     * 
     * @param ids ID数组
     */
    public void delete(PK[] ids) throws MyBatistFrameServiceException;

    /**
     * 查询分页对象
     * 
     * @param pager
     * @param entity
     * @return
     * @throws MyBatistFrameServiceException
     */
    public Pager findPager(Pager pager) throws MyBatistFrameServiceException;

    /**
     * 查询分页对象
     * 
     * @param pager
     * @param entity
     * @return
     * @throws MyBatistFrameServiceException
     */
    public Pager findPagerByEntity(Pager pager, T entity) throws MyBatistFrameServiceException;

    /**
     * 根据某一列查询单条数据
     * 
     * @param column
     * @param value
     * @return
     * @throws MyBatistFrameServiceException
     */
    public T findByColumn(String column, String value) throws MyBatistFrameServiceException;

    /**
     * 根据某一列查询数据列表
     * 
     * @param column
     * @param value
     * @return
     * @throws MyBatistFrameServiceException
     */
    public List<T> findListByColumn(String column, String value) throws MyBatistFrameServiceException;

    /**
     * 根据某一列查询数据列表
     * 
     * @param column
     * @param value
     * @return
     * @throws MyBatistFrameServiceException
     */
    public List<T> findListByColumn(String column, String[] value) throws MyBatistFrameServiceException;

    /**
     * 根据某一列查询数据列表
     * 
     * @param column
     * @param value
     * @param order 排序方式
     * @param orderBy 排序字段
     * @return
     * @throws HsjServiceException
     */
    public List<T> findListByColumn(String column, String value, String order, String orderBy)
                                                                                              throws MyBatistFrameServiceException;

    /**
     * 自定义查询条件获取结果集大小
     * 
     * @param IbatisCriteria criteria
     * @return
     */
    public Long findListCountByColumn(String column, String value) throws MyBatistFrameServiceException;

    //
    // /**
    // * 根据sql语句获取
    // * @param paramMap
    // * @return
    // */
    // public List<T> getListBySql(String sql) throws MyBatistFrameServiceException;
    //
    // /**
    // * 根据sql语句分页获取数据
    // * @param sql
    // * @return
    // * @throws MyBatistFrameServiceException
    // */
    // public Pager getListBySqlByPager(String sql) throws MyBatistFrameServiceException;

    /**
     * 执行sql返回Integer类型对象
     * 
     * @param sql
     * @return
     */
    public Integer queryForInt(String sql) throws MyBatistFrameServiceException;

    /**
     * 执行sql返回Integer类型对象
     * 
     * @param sql
     * @return
     */
    public Integer queryForInt(String sql, Map<String, Object> param) throws MyBatistFrameServiceException;

    /**
     * 执行sql返回String类型对象
     * 
     * @param sql
     * @return
     */
    public String queryForString(String sql) throws MyBatistFrameServiceException;

    /**
     * 执行sql返回String类型对象
     * 
     * @param sql
     * @return
     */
    public String queryForString(String sql, Map<String, Object> param) throws MyBatistFrameServiceException;

    // /**
    // * 执行sql返回hashmap类型对象
    // * @param sql
    // * @return
    // */
    // public Object queryForObject(String sql) throws MyBatistFrameServiceException;
    //
    // /**
    // * 执行sql返回hashmap类型对象
    // * @param sql
    // * @return
    // */
    // public Object queryForObject(String sql, Map<String, Object> param) throws MyBatistFrameServiceException;

    /**
     * 执行sql返回指定类型对象
     * 
     * @param sql
     * @return
     */
    public Object queryForObject(String sql, Class c) throws MyBatistFrameServiceException;

    /**
     * 执行sql返回指定类型对象
     * 
     * @param sql
     * @return
     */
    public Object queryForObject(String sql, Map<String, Object> param, Class c) throws MyBatistFrameServiceException;

    /**
     * 执行sql返回Integer类型对象
     * 
     * @param sql
     * @return
     */
    public Integer executeBySql(String sql) throws MyBatistFrameServiceException;

    /**
     * 执行sql返回Integer类型对象
     * 
     * @param sql
     * @return
     */
    public Integer executeBySql(String sql, Map<String, Object> param) throws MyBatistFrameServiceException;

}

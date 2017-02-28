package hsj.czy.mybatisframe.service;

import hsj.czy.mybatisframe.bean.Pager;
import hsj.czy.mybatisframe.exception.MyBatistFrameServiceException;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T, PK extends Serializable> {

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
    public String save(T entity) throws MyBatistFrameServiceException;

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
     * @param order 排序方式
     * @param orderBy 排序字段
     * @return
     * @throws MyBatistFrameServiceException
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

    /**
     * 根据某一列查询数据列表
     * 
     * @param column
     * @param value
     * @return
     * @throws MyBatistFrameServiceException
     */
    public List<T> findListByColumn(String column, String[] value) throws MyBatistFrameServiceException;

}

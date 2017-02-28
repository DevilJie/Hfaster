package hsj.czy.mybatisframe.mapper;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * baseMapper 类
 * 
 * @author Hong
 * @param <T> 范类型实体类
 * @param <PK> 主键类型
 */
public interface BaseMapper<T, PK extends Serializable> {

    /**
     * 根据ID获取实体对象
     * 
     * @param id 记录ID
     * @return 实体对象
     */
    public HashMap<String, Object> get(Map<String, Object> paramMap);

    /**
     * 获取所有实体对象集合
     * 
     * @return 实体对象集合
     */
    public List<HashMap<String, Object>> getAllList(Map<String, Object> paramMap);

    /**
     * 保存实体对象
     * 
     * @param entity 对象
     * @return ID
     */
    public void save(Map<String, Object> paramMap);

    /**
     * 批量保存实体对象
     * 
     * @param entity 对象
     * @return ID
     */
    public void batchSave(Map<String, Object> paramMap);

    /**
     * 更新实体对象
     * 
     * @param entity 对象
     */
    public void update(Map<String, Object> paramMap);

    /**
     * 批量更新实体对象
     * 
     * @param entity 对象
     */
    public void batchUpdate(List<Map<String, Object>> list);

    /**
     * 根据ID删除实体对象
     * 
     * @param id 记录ID
     */
    public void delete(Map<String, Object> paramMap);

    /**
     * 获取分页数据
     * 
     * @param paramMap
     * @return
     */
    public List<HashMap<String, Object>> getPager(Map<String, Object> paramMap);

    /**
     * 获取分页数据
     * 
     * @param paramMap
     * @return
     */
    public Long getPagerCount(Map<String, Object> paramMap);

    /**
     * 获取分页数据
     * 
     * @param paramMap
     * @return
     */
    public List<HashMap<String, Object>> getPagerBySql(Map<String, Object> paramMap);

    /**
     * 获取分页数据
     * 
     * @param paramMap
     * @return
     */
    public Long getPagerCountBySql(Map<String, Object> paramMap);

    /**
     * 自定义查询条件获取对象
     * 
     * @param paramMap
     * @return
     */
    public HashMap<String, Object> getObject(Map<String, Object> paramMap);

    /**
     * 自定义查询条件获取对象列表
     * 
     * @param paramMap
     * @return
     */
    public List<HashMap<String, Object>> getObjectList(Map<String, Object> paramMap);

    /**
     * 自定义查询条件获取对象列表集合大小
     * 
     * @param paramMap
     * @return
     */
    public Long getObjectListCount(Map<String, Object> paramMap);

    // /**
    // * 根据sql语句获取
    // * @param paramMap
    // * @return
    // */
    // public List<HashMap<String, Object>> queryListBySql(Map<String, Object> paramMap);
    //
    //
    // /**
    // * 根据sql语句获取集合大小
    // * @param paramMap
    // * @return
    // */
    // public Long queryListBySqlCount(Map<String, Object> paramMap);

    /**
     * 执行sql查询数据，返回对象集合
     */
    public List<Object> queryBySql(Map<String, Object> paramMap);

    /**
     * 直接执行sql语句
     */
    public Long executeBySql(Map<String, Object> paramMap);
}

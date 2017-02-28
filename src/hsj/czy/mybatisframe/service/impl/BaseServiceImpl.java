package hsj.czy.mybatisframe.service.impl;

import hsj.czy.mybatisframe.bean.Pager;
import hsj.czy.mybatisframe.exception.MyBatistFrameServiceException;
import hsj.czy.mybatisframe.service.BaseDao;
import hsj.czy.mybatisframe.service.BaseService;

import java.io.Serializable;
import java.util.List;

public class BaseServiceImpl<T, PK extends Serializable> implements BaseService<T, PK> {

    protected BaseDao<T, PK> baseDao;

    public void setBaseDao(BaseDao<T, PK> baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public T get(PK id) throws MyBatistFrameServiceException {
        try {
            return baseDao.get(id);
        } catch (Exception e) {
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<T> getAllList() throws MyBatistFrameServiceException {
        try {
            return baseDao.getAllList();
        } catch (Exception e) {
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public String save(T entity) throws MyBatistFrameServiceException {
        try {
            return (String) baseDao.save(entity);
        } catch (Exception e) {
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void update(T entity) throws MyBatistFrameServiceException {
        try {
            baseDao.update(entity);
        } catch (Exception e) {
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause());
        }

    }

    @Override
    public void delete(PK id) throws MyBatistFrameServiceException {
        try {
            baseDao.delete(id);
        } catch (Exception e) {
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void delete(PK[] ids) throws MyBatistFrameServiceException {
        try {
            baseDao.delete(ids);
        } catch (Exception e) {
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Pager findPager(Pager pager) throws MyBatistFrameServiceException {
        try {
            return baseDao.findPager(pager);
        } catch (Exception e) {
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Pager findPagerByEntity(Pager pager, T entity) throws MyBatistFrameServiceException {
        try {
            return baseDao.findPagerByEntity(pager, entity);
        } catch (Exception e) {
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public T findByColumn(String column, String value) throws MyBatistFrameServiceException {
        try {
            return baseDao.findByColumn(column, value);
        } catch (Exception e) {
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<T> findListByColumn(String column, String value) throws MyBatistFrameServiceException {
        try {
            return baseDao.findListByColumn(column, value);
        } catch (Exception e) {
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<T> findListByColumn(String column, String value, String order, String orderBy)
                                                                                              throws MyBatistFrameServiceException {
        try {
            return baseDao.findListByColumn(column, value, order, orderBy);
        } catch (Exception e) {
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Long findListCountByColumn(String column, String value) throws MyBatistFrameServiceException {
        try {
            return baseDao.findListCountByColumn(column, value);
        } catch (Exception e) {
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<T> findListByColumn(String column, String[] value) throws MyBatistFrameServiceException {
        return baseDao.findListByColumn(column, value);
    }

    @Override
    public List<Serializable> batchSave(List<T> list) throws MyBatistFrameServiceException {
        return baseDao.batchSave(list);
    }
}

package hsj.czy.mybatisframe.service.impl;

import hsj.czy.mybatisframe.annotation.Ignore;
import hsj.czy.mybatisframe.annotation.PrimaryKey;
import hsj.czy.mybatisframe.bean.Pager;
import hsj.czy.mybatisframe.bean.Pager.Order;
import hsj.czy.mybatisframe.core.ChBatisQuery;
import hsj.czy.mybatisframe.core.ChBatisSession;
import hsj.czy.mybatisframe.core.CoreUtil;
import hsj.czy.mybatisframe.core.IbatisCriteria;
import hsj.czy.mybatisframe.core.IbatisCriterion;
import hsj.czy.mybatisframe.core.IbatisCriterion.IbatisCriteriaType;
import hsj.czy.mybatisframe.core.SimpleBatisSession;
import hsj.czy.mybatisframe.entity.ChBatisEntity;
import hsj.czy.mybatisframe.exception.MyBatistFrameServiceException;
import hsj.czy.mybatisframe.listener.ChBatisFrameConfigListener;
import hsj.czy.mybatisframe.mapper.BaseMapper;
import hsj.czy.mybatisframe.service.BaseDao;
import hsj.czy.mybatisframe.util.ChBatisValidate;
import hsj.czy.mybatisframe.util.ReflectionUtil;
import hsj.czy.mybatisframe.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseDaoImpl<T, PK extends Serializable> implements BaseDao<T, PK> {

    private BaseMapper<T, PK> baseMapper;
    private Class<T>          entityClass;
    private ChBatisSession    batisSession;
    protected Log             logger = LogFactory.getLog(this.getClass());

    public BaseDaoImpl(){
        Class c = getClass();
        Type type = c.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
            this.entityClass = (Class<T>) parameterizedType[0];
        }
    }

    public Logger log = Logger.getLogger(this.getClass());

    @Autowired
    public void setBaseDao(BaseMapper<T, PK> baseMapper) {
        this.baseMapper = baseMapper;
        batisSession = SimpleBatisSession.getInstance(baseMapper);
    }

    @Override
    public T get(PK id) throws MyBatistFrameServiceException {
        Map<String, Object> map = new HashMap<String, Object>();
        ChBatisEntity e = null;
        try {
            e = (ChBatisEntity) Class.forName(entityClass.getName()).newInstance();
            map.put("TABLE", ChBatisFrameConfigListener.entity_table.get(e.getClass().getName()));
            map.put("id", id);
            Method[] methods = e.getClass().getMethods();
            PrimaryKey key;
            String primaryKey = "";
            for (Method method : methods) {
                key = (PrimaryKey) method.getAnnotation(PrimaryKey.class);
                if (key != null) {
                    primaryKey = key.key();
                    break;
                }
            }
            map.put("ID_COLUMN", primaryKey.toUpperCase());
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        HashMap<String, Object> m = baseMapper.get(map);

        if (m == null || m.size() == 0) return null;

        CoreUtil.process(e, m);
        return (T) e;
    }

    @Override
    public List<T> getAllList() throws MyBatistFrameServiceException {
        Map<String, Object> map = new HashMap<String, Object>();
        ChBatisEntity e = null;
        List<T> list = new ArrayList<T>();
        try {
            e = (ChBatisEntity) Class.forName(entityClass.getName()).newInstance();
            map.put("TABLE", ChBatisFrameConfigListener.entity_table.get(e.getClass().getName()));
            List<HashMap<String, Object>> mapList = baseMapper.getAllList(map);
            if (mapList == null || mapList.size() == 0) return list;
            for (HashMap<String, Object> hashMap : mapList) {
                e = (ChBatisEntity) Class.forName(entityClass.getName()).newInstance();
                CoreUtil.process(e, hashMap);
                list.add((T) e);
            }
        } catch (InstantiationException e1) {
            e1.printStackTrace();
            throw new MyBatistFrameServiceException(e1.getMessage(), e1.getCause(), e1.getStackTrace());
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
            throw new MyBatistFrameServiceException(e1.getMessage(), e1.getCause(), e1.getStackTrace());
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
            throw new MyBatistFrameServiceException(e1.getMessage(), e1.getCause(), e1.getStackTrace());
        }
        return list;
    }

    @Override
    public Serializable save(T entity) throws MyBatistFrameServiceException {
        try {

            Method[] methods = entity.getClass().getMethods();

            PrimaryKey priKey;
            String k = "";
            Method primaryMethod = null;
            String setPriMethod = null;
            for (Method method : methods) {
                priKey = (PrimaryKey) method.getAnnotation(PrimaryKey.class);
                if (priKey != null) {
                    setPriMethod = StringUtils.capitalise(method.getName().substring(3));
                    k = priKey.key();
                    primaryMethod = method;
                    break;
                }
            }

            Map<String, Object> map = new HashMap<String, Object>();

            Method mSave = entity.getClass().getMethod(ChBatisEntity.ON_SAVE_METHOD_NAME);
            mSave.invoke(entity);

            Set<Field> f = new HashSet<Field>();
            f.addAll(new HashSet(Arrays.asList(ReflectionUtils.getAllField(entity.getClass()))));

            Map<String, Field> fieldMap = new HashMap<String, Field>();
            for (Field field : f) {
                if (field.getAnnotation(Ignore.class) != null) continue;
                fieldMap.put(field.getName().toUpperCase(), field);
            }

            Field field;
            String column = "";
            String value = "";
            Object vobj;
            String v;
            String key;
            Ignore ignore = null;
            for (Method method : methods) {
                if (method.getName().indexOf("set") != -1) {
                    if (method.getName().equals("setCreateDate")) {
                        System.out.println("1");
                    }
                    if (StringUtils.capitalise(method.getName().substring(3)).equals(StringUtils.capitalise(primaryMethod.getName().substring(3)))) continue;
                    ignore = (Ignore) method.getAnnotation(Ignore.class);
                    if (ignore != null) continue;
                    field = fieldMap.get(method.getName().replace("set", "").toUpperCase());
                    if (field != null) {
                        vobj = ReflectionUtil.invokeGetterMethod(entity, field.getName());
                        if (vobj == null) continue;
                        key = ChBatisFrameConfigListener.pojoNameToColumnName(field.getName());
                        column += "," + ChBatisFrameConfigListener.f + key.toUpperCase() + ChBatisFrameConfigListener.f;
                        if (field.getType().equals(Boolean.class) || field.getType().equals(Double.class)
                            || field.getType().equals(Integer.class) || field.getType().equals(double.class)
                            || field.getType().equals(int.class) || field.getType().equals(BigDecimal.class)
                            || field.getType().isEnum()) {
                            v = vobj.toString();

                            if (field.getType().isEnum()) {
                                v = CoreUtil.processEnum(v, field);
                            }

                            if (v.equals("true")) {
                                value += ",1";
                            } else if (v.equals("false")) {
                                value += ",0";
                            } else {
                                map.put(field.getName(), v);
                                value += ",#{" + field.getName() + ",jdbcType=NUMERIC}";
                            }
                        } else if (field.getType().equals(Date.class)) {
                            map.put(field.getName(), vobj);
                            value += ",#{" + field.getName() + ",jdbcType=TIMESTAMP}";
                        } else {
                            v = vobj.toString();
                            map.put(field.getName(), v);
                            value += ",#{" + field.getName() + ",jdbcType=VARCHAR}";
                        }
                    }
                }
            }
            String id = UUID.randomUUID().toString().replace("-", "");

            map.put("COLUMN", column.substring(1) + "," + k.toUpperCase());
            map.put("VALUE", value.substring(1) + "," + "'" + id + "'");
            map.put("TABLE", ChBatisFrameConfigListener.entity_table.get(entity.getClass().getName()));
            baseMapper.save(map);

            ReflectionUtil.invokeSetterMethod(entity, setPriMethod, id);
            return id;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public List<Serializable> batchSave(List<T> list) throws MyBatistFrameServiceException {
        try {
            String column = "";
            List<String> values = new ArrayList<String>();
            List<Serializable> ids = new ArrayList<Serializable>();
            Map<String, Object> map = new HashMap<String, Object>();
            String k = "";
            Method primaryMethod = null;
            String setPriMethod = null;
            PrimaryKey priKey;
            for (T entity : list) {
                Method[] methods = entity.getClass().getMethods();
                if (list.indexOf(entity) == 0) {
                    for (Method method : methods) {
                        priKey = (PrimaryKey) method.getAnnotation(PrimaryKey.class);
                        if (priKey != null) {
                            setPriMethod = StringUtils.capitalise(method.getName().substring(3));
                            k = priKey.key();
                            primaryMethod = method;
                            break;
                        }
                    }
                }

                Method mSave = entity.getClass().getMethod(ChBatisEntity.ON_SAVE_METHOD_NAME);
                mSave.invoke(entity);

                Set<Field> f = new HashSet<Field>();
                f.addAll(new HashSet(Arrays.asList(ReflectionUtils.getAllField(entity.getClass()))));

                Map<String, Field> fieldMap = new HashMap<String, Field>();
                for (Field field : f) {
                    fieldMap.put(field.getName().toUpperCase(), field);
                }

                Field field;
                String value = "";
                Object vobj;
                String v;
                String key;
                for (Method method : methods) {
                    if (method.getName().indexOf("set") != -1) {
                        if (StringUtils.capitalise(method.getName().substring(3)).equals(StringUtils.capitalise(primaryMethod.getName().substring(3)))) continue;
                        field = fieldMap.get(method.getName().replace("set", "").toUpperCase());
                        if (field != null) {
                            vobj = ReflectionUtil.invokeGetterMethod(entity, field.getName());
                            if (vobj == null) continue;
                            key = ChBatisFrameConfigListener.pojoNameToColumnName(field.getName());
                            if (list.indexOf(entity) == 0) column += ",`" + key.toUpperCase() + "`";
                            if (field.getType().equals(Boolean.class) || field.getType().equals(Double.class)
                                || field.getType().equals(Integer.class) || field.getType().equals(double.class)
                                || field.getType().equals(int.class) || field.getType().equals(BigDecimal.class)
                                || field.getType().isEnum()) {
                                v = vobj.toString();
                                if (field.getType().isEnum()) {
                                    v = CoreUtil.processEnum(v, field);
                                }
                                if (v.equals("true")) {
                                    value += ",1";
                                } else if (v.equals("false")) {
                                    value += ",0";
                                } else {
                                    map.put(field.getName() + "_" + list.indexOf(entity), v);
                                    value += ",#{" + field.getName() + "_" + list.indexOf(entity)
                                             + ",jdbcType=NUMERIC}";
                                }
                            } else if (field.getType().equals(Date.class)) {
                                map.put(field.getName() + "_" + list.indexOf(entity), vobj);
                                value += ",#{" + field.getName() + "_" + list.indexOf(entity) + ",jdbcType=TIMESTAMP}";
                            } else {
                                v = vobj.toString();
                                map.put(field.getName() + "_" + list.indexOf(entity), v);
                                value += ",#{" + field.getName() + "_" + list.indexOf(entity) + ",jdbcType=VARCHAR}";
                            }
                        }
                    }
                }
                String id = UUID.randomUUID().toString().replace("-", "");
                ids.add(id);
                values.add(value.substring(1) + "," + "\"" + id + "\"");
                map.put("TABLE", ChBatisFrameConfigListener.entity_table.get(entity.getClass().getName()));

                ReflectionUtil.invokeSetterMethod(entity, setPriMethod, id);
            }
            map.put("COLUMN", column.substring(1) + "," + k.toUpperCase());
            map.put("VALUES_LIST", values);
            baseMapper.batchSave(map);

            return ids;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    // @Override
    // public void batchUpdate(List<T> list) throws MyBatistFrameServiceException {
    // try {
    // Map<String, Object> map;
    // List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
    // String prefix = StrUtil.RadomCode(36, "other");
    // for (T entity : list) {
    // prefix = StrUtil.RadomCode(36, "other");
    // map = new HashMap<String, Object>();
    // Method uUpdate = entity.getClass().getMethod(ChBatisEntity.ON_UPDATE_METHOD_NAME);
    // uUpdate.invoke(entity);
    //
    // Set<Field> f = new HashSet<Field>();
    // f.addAll(new HashSet(Arrays.asList(entity.getClass().getFields())));
    // f.addAll(new HashSet(Arrays.asList(entity.getClass().getDeclaredFields())));
    //
    // Map<String, Field> fieldMap = new HashMap<String, Field>();
    // for (Field field : f) {
    // fieldMap.put(field.getName().toUpperCase(), field);
    // }
    //
    // Method[] methods = entity.getClass().getMethods();
    //
    // PrimaryKey pkey;
    // String primaryKey = "";
    // String getPriMethod = "";
    // Method primaryMethod = null;
    // for (Method m : methods) {
    // pkey = (PrimaryKey) m.getAnnotation(PrimaryKey.class);
    // if (pkey != null) {
    // primaryKey = pkey.key();
    // getPriMethod = StringUtils.capitalise(m.getName().substring(3));
    // primaryMethod = m;
    // break;
    // }
    // }
    //
    // Field field;
    // String set_value = "";
    // Object vobj;
    // String v;
    // String key;
    // for (Method method : methods) {
    // if (method.getName().indexOf("set") != -1) {
    // if
    // (StringUtils.capitalise(method.getName().substring(3)).equals(StringUtils.capitalise(primaryMethod.getName().substring(3))))
    // continue;
    // field = fieldMap.get(method.getName().replace("set", "").toUpperCase());
    // if (field != null) {
    // key = ChBatisFrameConfigListener.pojoNameToColumnName(field.getName());
    // vobj = ReflectionUtil.invokeGetterMethod(entity, field.getName());
    // if (vobj == null) set_value += "," + key.toUpperCase() + " = null";
    // else {
    // if (field.getType().equals(Boolean.class) || field.getType().equals(Double.class)
    // || field.getType().equals(Integer.class) || field.getType().equals(double.class)
    // || field.getType().equals(int.class) || field.getType().equals(BigDecimal.class)
    // || field.getType().isEnum()) {
    // v = vobj.toString();
    // if (field.getType().isEnum()) {
    // v = CoreUtil.processEnum(v, field);
    // }
    // if (v.equals("true")) {
    // set_value += "," + key.toUpperCase() + " = 1";
    // } else if (v.equals("false")) {
    // set_value += "," + key.toUpperCase() + " = 0";
    // } else {
    // map.put(prefix + key, v);
    // set_value += "," + key.toUpperCase() + " = #{" + prefix + key
    // + ",jdbcType=NUMERIC}";
    // }
    // } else if (field.getType().equals(Date.class)) {
    // map.put(prefix + key, vobj);
    // set_value += "," + key.toUpperCase() + " = #{" + prefix + key
    // + ",jdbcType=TIMESTAMP}";
    // } else {
    // v = vobj.toString();
    // map.put(prefix + key, v);
    // set_value += "," + key.toUpperCase() + " = #{" + prefix + key
    // + ",jdbcType=VARCHAR}";
    // }
    // }
    // }
    // }
    // }
    //
    // map.put("SET_VALUE", set_value.substring(1));
    // map.put("TABLE", ChBatisFrameConfigListener.entity_table.get(entity.getClass().getName()));
    //
    // map.put("id", ReflectionUtil.invokeGetterMethod(entity, getPriMethod));
    // map.put("ID_COLUMN", primaryKey.toUpperCase());
    // l.add(map);
    // if (list.indexOf(entity) != 0 && list.indexOf(entity) % 2 == 0) {
    // net.sf.json.JSONArray j = net.sf.json.JSONArray.fromObject(l);
    // System.out.println(j.toString());
    // baseMapper.batchUpdate(l);
    // l = new ArrayList<Map<String, Object>>();
    // map = new HashMap<String, Object>();
    // }
    // }
    // if (l.size() > 0) baseMapper.batchUpdate(l);
    // } catch (Exception e) {
    // logger.error(e.getMessage(), e);
    // throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
    // }
    // }

    @Override
    public void update(T entity) throws MyBatistFrameServiceException {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            Method uUpdate = entity.getClass().getMethod(ChBatisEntity.ON_UPDATE_METHOD_NAME);
            uUpdate.invoke(entity);

            Set<Field> f = new HashSet<Field>();
            f.addAll(new HashSet(Arrays.asList(ReflectionUtils.getAllField(entity.getClass()))));

            Map<String, Field> fieldMap = new HashMap<String, Field>();
            for (Field field : f) {
                fieldMap.put(field.getName().toUpperCase(), field);
            }

            Method[] methods = entity.getClass().getMethods();

            PrimaryKey pkey;
            String primaryKey = "";
            String getPriMethod = "";
            Method primaryMethod = null;
            for (Method m : methods) {
                pkey = (PrimaryKey) m.getAnnotation(PrimaryKey.class);
                if (pkey != null) {
                    primaryKey = pkey.key();
                    getPriMethod = StringUtils.capitalise(m.getName().substring(3));
                    primaryMethod = m;
                    break;
                }
            }

            Field field;
            String set_value = "";
            Object vobj;
            String v;
            String key;
            Ignore ignore;
            for (Method method : methods) {
                if (method.getName().indexOf("set") != -1) {
                    if (StringUtils.capitalise(method.getName().substring(3)).equals(StringUtils.capitalise(primaryMethod.getName().substring(3)))) continue;
                    field = fieldMap.get(method.getName().replace("set", "").toUpperCase());
                    ignore = (Ignore) method.getAnnotation(Ignore.class);
                    if (ignore != null) continue;
                    if (field != null) {
                        key = ChBatisFrameConfigListener.pojoNameToColumnName(field.getName());
                        vobj = ReflectionUtil.invokeGetterMethod(entity, field.getName());
                        if (vobj == null) set_value += "," + key.toUpperCase() + " = null";
                        else {
                            if (field.getType().equals(Boolean.class) || field.getType().equals(Double.class)
                                || field.getType().equals(Integer.class) || field.getType().equals(double.class)
                                || field.getType().equals(int.class) || field.getType().equals(BigDecimal.class)
                                || field.getType().isEnum()) {
                                v = vobj.toString();
                                if (field.getType().isEnum()) {
                                    v = CoreUtil.processEnum(v, field);
                                }
                                if (v.equals("true")) {
                                    set_value += "," + key.toUpperCase() + " = 1";
                                } else if (v.equals("false")) {
                                    set_value += "," + key.toUpperCase() + " = 0";
                                } else {
                                    map.put(key, v);
                                    set_value += "," + key.toUpperCase() + " = #{" + key + ",jdbcType=NUMERIC}";
                                }
                            } else if (field.getType().equals(Date.class)) {
                                map.put(key, vobj);
                                set_value += "," + key.toUpperCase() + " = #{" + key + ",jdbcType=TIMESTAMP}";
                            } else {
                                v = vobj.toString();
                                map.put(key, v);
                                set_value += "," + key.toUpperCase() + " = #{" + key + ",jdbcType=VARCHAR}";
                            }
                        }
                    }
                }
            }

            map.put("SET_VALUE", set_value.substring(1));
            map.put("TABLE", ChBatisFrameConfigListener.entity_table.get(entity.getClass().getName()));

            map.put("id", ReflectionUtil.invokeGetterMethod(entity, getPriMethod));
            map.put("ID_COLUMN", primaryKey.toUpperCase());
            baseMapper.update(map);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public void delete(PK id) throws MyBatistFrameServiceException {
        try {
            T entity = get(id);
            Method method = entity.getClass().getMethod(ChBatisEntity.ON_DELETE_METHOD_NAME);
            method.invoke(entity);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("TABLE", ChBatisFrameConfigListener.entity_table.get(entity.getClass().getName()));
            Method[] methods = entity.getClass().getMethods();
            PrimaryKey key;
            String getPriMethod = null;
            String primaryKey = "";
            for (Method m : methods) {
                key = (PrimaryKey) m.getAnnotation(PrimaryKey.class);
                if (key != null) {
                    primaryKey = key.key();
                    getPriMethod = StringUtils.capitalise(m.getName().substring(3));
                    break;
                }
            }
            map.put("ID_COLUMN", primaryKey.toUpperCase());
            map.put("id", ReflectionUtil.invokeGetterMethod(entity, getPriMethod));
            baseMapper.delete(map);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public void delete(PK[] ids) throws MyBatistFrameServiceException {
        try {
            ChBatisEntity e = (ChBatisEntity) Class.forName(entityClass.getName()).newInstance();
            String tableName = ChBatisFrameConfigListener.entity_table.get(e.getClass().getName());

            Method[] methods = entityClass.getMethods();
            PrimaryKey key;
            String primaryKey = "";
            for (Method m : methods) {
                key = (PrimaryKey) m.getAnnotation(PrimaryKey.class);
                if (key != null) {
                    primaryKey = key.key();
                    break;
                }
            }

            String sql = "DELETE FROM " + tableName.toUpperCase() + " WHERE " + primaryKey.toUpperCase() + " IN :id";
            ChBatisQuery query = this.getBatisSession().createQuery(sql, null);
            query.setParameter("id", ids);
            Long i = query.executeUpdate();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public Pager findPagerByEntity(Pager pager, T entity) throws MyBatistFrameServiceException {
        return findPager(pager);
    }

    @Override
    public Pager findPager(Pager pager) throws MyBatistFrameServiceException {
        try {
            ChBatisEntity e = (ChBatisEntity) Class.forName(entityClass.getName()).newInstance();
            Map<String, Object> map = new HashMap<String, Object>();
            String body = "";
            if (!StringUtils.isEmpty(pager.getOrderBy())) {
                body += " ORDER BY " + ChBatisFrameConfigListener.pojoNameToColumnName(pager.getOrderBy()) + " ";
                if (pager.getOrder() != null) body += pager.getOrder() == Order.asc ? "ASC" : "DESC";
                else body += "DESC";
            }
            map.put("TABLE", ChBatisFrameConfigListener.entity_table.get(e.getClass().getName()));
            map.put("WHERE", body);
            map.put("STARTPOSITION", pager.getPageSize() * (pager.getPageNumber() - 1));
            map.put("ENDPOSITION", pager.getPageSize() * (pager.getPageNumber()));
            map.put("PAGESIZE", pager.getPageSize());
            Long count = baseMapper.getPagerCount(map);
            pager.setTotalCount(count.intValue());
            List<T> list = new ArrayList<T>();
            String key;
            List<HashMap<String, Object>> mapList = baseMapper.getPager(map);
            if (mapList != null) {
                for (HashMap<String, Object> hashMap : mapList) {
                    e = (ChBatisEntity) Class.forName(entityClass.getName()).newInstance();
                    CoreUtil.process(e, hashMap);
                    list.add((T) e);
                }
            }
            pager.setResult(list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
        return pager;
    }

    protected Pager findPager(Pager pager, IbatisCriteria criteria) throws MyBatistFrameServiceException {
        if (criteria == null) return findPager(pager);
        else {
            try {
                Class clazz = criteria.getEntityClass();
                ChBatisEntity e = (ChBatisEntity) Class.forName(clazz.getName()).newInstance();
                List<IbatisCriterion> criteriaList = criteria.getCriterionList();
                String body = "";
                String v = "";
                Map<String, Object> map = new HashMap<String, Object>();
                String key;
                if (criteriaList != null) {
                    map = initSqlBody(criteriaList, body, pager);
                }
                map.put("TABLE", ChBatisFrameConfigListener.entity_table.get(e.getClass().getName()));
                map.put("STARTPOSITION", pager.getPageSize() * (pager.getPageNumber() - 1));
                map.put("ENDPOSITION", pager.getPageSize() * (pager.getPageNumber()));
                map.put("PAGESIZE", pager.getPageSize());
                Long count = baseMapper.getPagerCount(map);
                pager.setTotalCount(count.intValue());
                List<T> list = new ArrayList<T>();
                List<HashMap<String, Object>> mapList = baseMapper.getPager(map);
                if (mapList != null) {
                    for (HashMap<String, Object> hashMap : mapList) {
                        e = (ChBatisEntity) Class.forName(clazz.getName()).newInstance();
                        CoreUtil.process(e, hashMap);
                        list.add((T) e);
                    }
                }
                pager.setResult(list);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
            }
            return pager;
        }
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    private T getObject(IbatisCriteria criteria) throws MyBatistFrameServiceException {
        try {
            Long l = new Date().getTime();
            ChBatisValidate.notNull(criteria);

            Class clazz = criteria.getEntityClass();
            ChBatisEntity e = (ChBatisEntity) Class.forName(clazz.getName()).newInstance();
            List<IbatisCriterion> criteriaList = criteria.getCriterionList();
            String body = "";
            String v = "";
            Map<String, Object> map = new HashMap<String, Object>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String key;
            if (criteriaList != null) {
                map = initSqlBody(criteriaList, body, null);
            }
            map.put("TABLE", ChBatisFrameConfigListener.entity_table.get(e.getClass().getName()));

            l = new Date().getTime();
            HashMap<String, Object> m = baseMapper.getObject(map);
            l = new Date().getTime();
            if (m == null || m.size() == 0) return null;
            CoreUtil.process(e, m);
            return (T) e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    public List<T> getObjectList(IbatisCriteria criteria) throws MyBatistFrameServiceException {
        try {
            ChBatisValidate.notNull(criteria);
            Class clazz = criteria.getEntityClass();
            ChBatisEntity e = (ChBatisEntity) Class.forName(clazz.getName()).newInstance();
            List<IbatisCriterion> criteriaList = criteria.getCriterionList();
            String body = "";
            String v = "";
            String key;
            Map<String, Object> map = new HashMap<String, Object>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (criteriaList != null) {
                map = initSqlBody(criteriaList, body, null);
            }
            map.put("TABLE", ChBatisFrameConfigListener.entity_table.get(e.getClass().getName()));

            if (!StringUtils.isEmpty(criteria.getOrder())) {
                String order = criteria.getOrder();
                if (StringUtils.isEmpty(order)) order = IbatisCriteria.ORDER_DESC;
                map.put("ORDERBY",
                        " ORDER BY " + ChBatisFrameConfigListener.pojoNameToColumnName(criteria.getOrderBy()) + " "
                                + order);
            }

            List<T> list = new ArrayList<T>();
            List<HashMap<String, Object>> mapList = baseMapper.getObjectList(map);
            if (mapList == null || mapList.size() == 0) return list;
            for (HashMap<String, Object> hashMap : mapList) {
                e = (ChBatisEntity) Class.forName(clazz.getName()).newInstance();
                CoreUtil.process(e, hashMap);
                list.add((T) e);
            }
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    public Long getObjectListCount(IbatisCriteria criteria) {
        try {
            ChBatisValidate.notNull(criteria);
            Class clazz = criteria.getEntityClass();
            ChBatisEntity e = (ChBatisEntity) Class.forName(clazz.getName()).newInstance();
            List<IbatisCriterion> criteriaList = criteria.getCriterionList();
            String body = "";
            Map<String, Object> map = new HashMap<String, Object>();
            if (criteriaList != null) {
                map = initSqlBody(criteriaList, body, null);
            }
            map.put("TABLE", ChBatisFrameConfigListener.entity_table.get(e.getClass().getName()));

            Long size = baseMapper.getObjectListCount(map);

            return size;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public T findByColumn(String column, String value) throws MyBatistFrameServiceException {
        try {
            IbatisCriteria criteria = IbatisCriteria.getInstance(this.getEntityClass());
            criteria.add(IbatisCriterion.eq(column, value));
            return getObject(criteria);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public List<T> findListByColumn(String column, String value) throws MyBatistFrameServiceException {
        try {
            IbatisCriteria criteria = IbatisCriteria.getInstance(this.getEntityClass());
            criteria.add(IbatisCriterion.eq(column, value));
            return getObjectList(criteria);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public List<T> findListByColumn(String column, String[] value) throws MyBatistFrameServiceException {
        try {
            IbatisCriteria criteria = IbatisCriteria.getInstance(this.getEntityClass());
            criteria.add(IbatisCriterion.in(column, value));
            return getObjectList(criteria);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public List<T> findListByColumn(String column, String value, String order, String orderBy)
                                                                                              throws MyBatistFrameServiceException {
        try {
            IbatisCriteria criteria = IbatisCriteria.getInstance(this.getEntityClass());
            criteria.add(IbatisCriterion.eq(column, value));
            criteria.setOrder(order);
            criteria.setOrderBy(orderBy);
            return getObjectList(criteria);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public Long findListCountByColumn(String column, String value) throws MyBatistFrameServiceException {
        try {
            IbatisCriteria criteria = IbatisCriteria.getInstance(this.getEntityClass());
            criteria.add(IbatisCriterion.eq(column, value));
            return getObjectListCount(criteria);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public Integer queryForInt(String sql) {
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("SQL", sql);
            List<Object> l = baseMapper.queryBySql(map);
            if (l == null || l.size() == 0) return null;
            for (Object object : l) {
                HashMap<String, Object> m = (HashMap<String, Object>) object;
                for (Map.Entry<String, Object> entry : m.entrySet()) {
                    return Integer.parseInt(entry.getValue().toString());
                }
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public Integer queryForInt(String sql, Map<String, Object> param) {
        try {
            List<Object> l = baseMapper.queryBySql(CoreUtil.processSql(sql, param));
            if (l == null || l.size() == 0) return null;
            for (Object object : l) {
                HashMap<String, Object> m = (HashMap<String, Object>) object;
                for (Map.Entry<String, Object> entry : m.entrySet()) {
                    return Integer.parseInt(entry.getValue().toString());
                }
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public String queryForString(String sql) {
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("SQL", sql);
            List<Object> l = baseMapper.queryBySql(map);
            if (l == null || l.size() == 0) return null;
            for (Object object : l) {
                HashMap<String, Object> m = (HashMap<String, Object>) object;
                for (Map.Entry<String, Object> entry : m.entrySet()) {
                    return entry.getValue().toString();
                }
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public String queryForString(String sql, Map<String, Object> param) throws MyBatistFrameServiceException {
        try {
            List<Object> l = baseMapper.queryBySql(CoreUtil.processSql(sql, param));
            if (l == null || l.size() == 0) return null;
            for (Object object : l) {
                HashMap<String, Object> m = (HashMap<String, Object>) object;
                for (Map.Entry<String, Object> entry : m.entrySet()) {
                    return entry.getValue().toString();
                }
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    // @Override
    // public Object queryForObject(String sql) {
    // try{
    // HashMap<String, Object> map = new HashMap<String, Object>();
    // map.put("SQL", sql);
    // List<Object> l = baseMapper.queryBySql(map);
    // if(l == null || l.size() == 0) return null;
    // return l;
    // }catch(Exception e){
    // throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
    // }
    // }
    //
    // @Override
    // public Object queryForObject(String sql, Map<String, Object> param)
    // throws MyBatistFrameServiceException {
    // try{
    // HashMap<String, Object> map = new HashMap<String, Object>();
    // if(param == null || param.length == 0) throw new MyBatistFrameServiceException("param can't be null");
    // int i = 0;
    // for (Object object : param) {
    // i++;
    // if(object instanceof String){
    // sql = sql.replace("\\?", "#{param"+i+", jdbcType=VARCHAR}");
    // map.put("param"+i, object);
    // }else if(object instanceof Integer || object instanceof Long || object instanceof Double || object instanceof
    // BigDecimal || object instanceof Float){
    // sql = sql.replace("\\?", "#{param"+i+", jdbcType=NUMERIC}");
    // map.put("param"+i, object);
    // }else if(object instanceof Date){
    // sql = sql.replace("\\?", "#{param"+i+", jdbcType=TIMESTAMP}");
    // map.put("param"+i, object);
    // }
    // }
    // map.put("SQL", sql);
    // List<Object> l = baseMapper.queryBySql(map);
    // if(l == null || l.size() == 0) return null;
    // return l;
    // }catch(Exception e){
    // throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
    // }
    // }

    @Override
    public Object queryForObject(String sql, Class c) throws MyBatistFrameServiceException {
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("SQL", sql);
            List<Object> l = baseMapper.queryBySql(map);
            if (l == null || l.size() == 0) return null;
            Object e;
            HashMap<String, Object> hashMap;
            String key;
            List<Object> returnList = new ArrayList<Object>();
            for (Object object : l) {
                e = Class.forName(c.getName()).newInstance();
                hashMap = (HashMap<String, Object>) object;
                CoreUtil.process(e, hashMap);
                returnList.add((T) e);
            }
            return returnList;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public Object queryForObject(String sql, Map<String, Object> param, Class c) throws MyBatistFrameServiceException {
        try {
            List<Object> l = baseMapper.queryBySql(CoreUtil.processSql(sql, param));
            if (l == null || l.size() == 0) return null;
            Object e;
            HashMap<String, Object> hashMap;
            String key;
            List<Object> returnList = new ArrayList<Object>();
            for (Object object : l) {
                e = Class.forName(c.getName()).newInstance();
                hashMap = (HashMap<String, Object>) object;
                CoreUtil.process(e, hashMap);
                returnList.add((T) e);
            }
            return returnList;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public Integer executeBySql(String sql) {
        try {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("SQL", sql);
            Long l = baseMapper.executeBySql(map);
            if (l == null) return null;
            return l.intValue();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    @Override
    public Integer executeBySql(String sql, Map<String, Object> param) {
        try {
            Long l = baseMapper.executeBySql(CoreUtil.processSql(sql, param));
            if (l == null) return null;
            return l.intValue();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MyBatistFrameServiceException(e.getMessage(), e.getCause(), e.getStackTrace());
        }
    }

    /**
     * 获取连接对象
     * 
     * @return
     */
    public ChBatisSession getBatisSession() {
        return batisSession;
    }

    public Map<String, Object> initSqlBody(List<IbatisCriterion> criteriaList, String body, Pager pager) {
        Map<String, Object> map = new HashMap<String, Object>();
        String key, v;
        for (IbatisCriterion c : criteriaList) {
            key = ChBatisFrameConfigListener.pojoNameToColumnName(c.getKey());
            body += " AND " + key.toUpperCase();
            if (c.getType() == IbatisCriteriaType.eq) {
                body += " = ";
            } else if (c.getType() == IbatisCriteriaType.lt) {
                body += " > ";
            } else if (c.getType() == IbatisCriteriaType.gt) {
                body += " < ";
            } else if (c.getType() == IbatisCriteriaType.le) {
                body += " >= ";
            } else if (c.getType() == IbatisCriteriaType.ge) {
                body += " <= ";
            } else if (c.getType() == IbatisCriteriaType.like) {
                body += " LIKE ";
            } else if (c.getType() == IbatisCriteriaType.isNull) {
                body += " IS NULL";
                continue;
            } else if (c.getType() == IbatisCriteriaType.isNotNull) {
                body += " IS NO NULL";
                continue;
            } else if (c.getType() == IbatisCriteriaType.in) {
                body += " IN";
            }

            if (c.getValue().getClass().equals(Boolean.class) || c.getValue().getClass().equals(Double.class)
                || c.getValue().getClass().equals(Integer.class) || c.getValue().getClass().equals(double.class)
                || c.getValue().getClass().equals(int.class) || c.getValue().getClass().equals(BigDecimal.class)
                || c.getValue().getClass().isEnum()) {
                v = c.getValue().toString();
                if (c.getValue().getClass().isEnum()) {
                    v = CoreUtil.processEnum(v, c.getValue().getClass());
                }
                if (v.equals("true")) {
                    body += "1";
                } else if (v.equals("false")) {
                    body += "0";
                } else {
                    map.put(key, v);
                    body += "#{" + key + ", jdbcType=NUMERIC}";
                }
            } else if (c.getValue().getClass().equals(Date.class)) {
                map.put(key, c.getValue());
                body += "#{" + key + ", jdbcType=TIMESTAMP}";
            } else if (c.getValue() instanceof Object[] || c.getValue() instanceof List) {
                Object[] o;
                if (c.getValue() instanceof List) {
                    o = ((List) c.getValue()).toArray();
                } else {
                    o = (Object[]) c.getValue();
                }
                body += "(";
                String in = "";
                int i = 0;
                for (Object obj : o) {
                    i++;
                    in += ",#{in_" + i + "}";
                    map.put("in_" + i, obj);
                }
                body += in.substring(1) + ")";
            } else {
                v = c.getValue().toString();
                if (c.getType() == IbatisCriteriaType.like) {
                    boolean start = v.indexOf("%") == 0;
                    boolean end = v.lastIndexOf("%") == v.length() - 1;
                    if (start && !end) {
                        body += "concat('%',#{" + key + ", jdbcType=VARCHAR})";
                    } else if (start && end) {
                        body += "concat('%',#{" + key + ", jdbcType=VARCHAR}, '%')";
                    } else if (!start && end) {
                        body += "concat(#{" + key + ", jdbcType=VARCHAR}, '%')";
                    } else {
                        body += "#{" + key + ", jdbcType=VARCHAR}";
                    }
                    if (v.indexOf("%") == 0) v = v.replace("\\%", "");
                } else body += "#{" + key + ", jdbcType=VARCHAR}";
                map.put(key, v);
            }
        }

        if (pager != null && !StringUtils.isEmpty(pager.getOrderBy()) && body.toUpperCase().indexOf("ORDER BY") == -1) {
            body += " ORDER BY " + ChBatisFrameConfigListener.pojoNameToColumnName(pager.getOrderBy()) + " ";
            if (pager.getOrder() != null) body += pager.getOrder() == Order.asc ? "ASC" : "DESC";
            else body += "DESC";
        }

        map.put("WHERE", body);
        return map;
    }
}

package hsj.czy.mybatisframe.entity;

import hsj.czy.mybatisframe.annotation.Ignore;

import java.io.Serializable;

public abstract class ChBatisEntity implements Serializable {

    private static final long  serialVersionUID      = 7298950045922296271L;

    @Ignore
    protected String           tableName             = null;

    public static final String ON_SAVE_METHOD_NAME   = "onSave";            // "保存"方法名称

    public static final String ON_UPDATE_METHOD_NAME = "onUpdate";          // "更新"方法名称

    public static final String ON_DELETE_METHOD_NAME = "onDelete";          // "删除"方法名称

    public abstract void onSave();

    public abstract void onUpdate();

    public abstract void onDelete();

    // @Override
    // public boolean equals(Object object) {
    // if (object == null) {
    // return false;
    // }
    // if (object instanceof ChBatisEntity) {
    // ChBatisEntity baseEntity = (ChBatisEntity)object;
    // if (this.getId() == null || baseEntity.getId() == null) {
    // return false;
    // }
    // else {
    // return (this.getId().equals(baseEntity.getId()));
    // }
    // }
    // return false;
    // }
    //
    // @Override
    // public int hashCode() {
    // return id == null ? System.identityHashCode(this) : (this.getClass().getName() + this.getId()).hashCode();
    // }
}

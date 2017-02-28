package hsj.czy.mybatisframe.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * MyBatis适配器
 * 
 * @author Caizongyou
 * 
 */
public class IbatisCriteria implements Serializable {
    
	public static final String ORDER_DESC = "desc";
	public static final String ORDER_ASC = "asc";
	
    private static final long serialVersionUID = -3743532897703591927L;
    
    private Class entityClass;
    
    private List<IbatisCriterion> criterionList = new ArrayList<IbatisCriterion>();
    
    private String order;
    
    private String orderBy;
    
    public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	private IbatisCriteria() {
    };
    
    public static IbatisCriteria getInstance(Class clazz) {
        IbatisCriteria c = new IbatisCriteria();
        c.setEntityClass(clazz);
        return c;
    }
    
    public void add(IbatisCriterion c) {
        criterionList.add(c);
    }
    
    public Class getEntityClass() {
        return entityClass;
    }
    
    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }
    
    public List<IbatisCriterion> getCriterionList() {
        return criterionList;
    }
    
    public void setCriterionList(List<IbatisCriterion> criterionList) {
        this.criterionList = criterionList;
    }
    
}

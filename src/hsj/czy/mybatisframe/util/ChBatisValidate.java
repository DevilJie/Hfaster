package hsj.czy.mybatisframe.util;

import hsj.czy.mybatisframe.exception.MyBatistFrameServiceException;

/**
 * 对象校验工具类
 * @author Hong
 *
 */
public class ChBatisValidate {
	
	/**
	 * 非空校验
	 * @return
	 */
	public static boolean notNull(Object obj) throws MyBatistFrameServiceException{
		if(obj == null) throw new MyBatistFrameServiceException("The obejct can not be null");
		return true;
	}
	
	/**
	 * 为空校验
	 * @return
	 */
	public static boolean isNull(Object obj) throws MyBatistFrameServiceException{
		if(obj != null) throw new MyBatistFrameServiceException("The obejct must be null");
		return true;
	}
	
	/**
	 * 有效数字校验
	 * @param obj
	 * @return
	 * @throws MyBatistFrameServiceException
	 */
	public static boolean isInt(Object obj) throws MyBatistFrameServiceException{
		try{
			if(obj == null) throw new MyBatistFrameServiceException("The obejct is not 'int' type");
			Integer.parseInt(obj.toString());
			return true;
		}catch(Exception e){
			 throw new MyBatistFrameServiceException("The obejct is not 'int' type");
		}
	}
}

package hsj.czy.mybatisframe.exception;

import java.io.Serializable;

public class MyBatistFrameServiceException extends RuntimeException implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public MyBatistFrameServiceException() {
		super();
	}

	public MyBatistFrameServiceException(String msg) {
		super(msg);
	}

	public MyBatistFrameServiceException(Throwable cause) {
		super(cause);
	}

	public MyBatistFrameServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public MyBatistFrameServiceException(String msg, Throwable cause, StackTraceElement[] stackTrace) {
		super(msg, cause);
		this.setStackTrace(stackTrace);
	}
}
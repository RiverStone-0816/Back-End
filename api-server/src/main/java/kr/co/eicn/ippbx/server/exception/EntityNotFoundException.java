package kr.co.eicn.ippbx.server.exception;

public class EntityNotFoundException extends RuntimeException {
	public EntityNotFoundException() {
	}

	public EntityNotFoundException(String var1) {
		super(var1);
	}

	public EntityNotFoundException(String var1, Throwable var2) {
		super(var1, var2);
	}

	public EntityNotFoundException(Throwable var1) {
		super(var1);
	}
}

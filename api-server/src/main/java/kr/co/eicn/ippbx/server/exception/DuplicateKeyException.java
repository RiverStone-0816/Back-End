package kr.co.eicn.ippbx.server.exception;

public class DuplicateKeyException extends RuntimeException {
	public DuplicateKeyException() {
	}

	public DuplicateKeyException(String var1) {
		super(var1);
	}

	public DuplicateKeyException(String var1, Throwable var2) {
		super(var1, var2);
	}

	public DuplicateKeyException(Throwable var1) {
		super(var1);
	}
}

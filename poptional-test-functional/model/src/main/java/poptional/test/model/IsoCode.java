package poptional.test.model;

import poptional.OptionalObject;

import java.util.Optional;

@OptionalObject
public class IsoCode {

	private Code code;

	public IsoCode(Code code) {
		this.code = code;
	}

	public IsoCode() {
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public Code getCode() {
		return this.code;
	}

	@OptionalObject.NotNull
	public Code getCodePlain() {
		return this.code;
	}
}


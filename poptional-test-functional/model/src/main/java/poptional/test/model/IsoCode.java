package poptional.test.model;

import poptional.OptionalObject;

import java.util.Optional;

@OptionalObject
public class IsoCode {

	private Code code;

	public Code getCode() {
		return this.code;
	}

	public Optional<Code> getCodeOptional() {
		return Optional.ofNullable(this.code);
	}

	@OptionalObject.NotNull
	public Code getCodePlain() {
		return this.code;
	}

	public void setCode(Code code) {
		this.code = code;
	}
}


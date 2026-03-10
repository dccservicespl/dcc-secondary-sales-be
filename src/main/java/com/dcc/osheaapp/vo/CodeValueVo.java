package com.dcc.osheaapp.vo;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CodeValueVo {
	
	@Id
	private Long id;
	
    private String code;
    private String value;

    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CodeValueDto{" +
                "code='" + code + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}


package test.base.core.model;


import blue.base.core.dict.State;
import blue.base.core.dict.Type;

/**
 * @author Jin Zheng
 * @since 1.0 2019-10-25
 */
public class Group {
	private Integer id;
	private String name;
	private Type type;
	private State state;

	public Group() {
	}

	public Group(Integer id, String name, Type type, State state) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.state = state;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}

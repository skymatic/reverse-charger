package de.skymatic.app;

import javafx.stage.Stage;
import javafx.util.Callback;

public class ControllerFactory implements Callback<Class<?>, Object> {

	private final Stage caller;
	private final Object[] parameters;

	public ControllerFactory(Stage caller, Object... parameters) {
		this.caller = caller;
		this.parameters = parameters;
	}

	@Override
	public Object call(Class<?> aClass) {
		if (aClass.equals(PrimaryController.class)) {
			return new PrimaryController(caller);
		} else {
			throw new IllegalArgumentException("Unknown Controller");
		}
	}
}

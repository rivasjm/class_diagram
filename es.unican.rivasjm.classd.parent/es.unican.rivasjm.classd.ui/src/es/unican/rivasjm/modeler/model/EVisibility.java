package es.unican.rivasjm.modeler.model;

public enum EVisibility {
	PUBLIC("+"), PACKAGE("~"), PROTECTED("#"), PRIVATE("-");
	
	private final String symbol;
	
	private EVisibility(String symbol) {
		this.symbol = symbol;
	}
	
	public final String getSymbol() {
		return symbol;
	}
}

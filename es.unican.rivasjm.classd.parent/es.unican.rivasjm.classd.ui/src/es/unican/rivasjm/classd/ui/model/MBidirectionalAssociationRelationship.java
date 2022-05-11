package es.unican.rivasjm.classd.ui.model;

public class MBidirectionalAssociationRelationship extends MRelationship {
	
	private MAssociationRelationship direct;
	private MAssociationRelationship opposite;

	public MAssociationRelationship getDirect() {
		return direct;
	}
	
	public void setDirect(MAssociationRelationship direct) {
		this.direct = direct;
	}
	
	public MAssociationRelationship getOpposite() {
		return opposite;
	}

	public void setOpposite(MAssociationRelationship opposite) {
		this.opposite = opposite;
	}

	@Override
	public MClass getSource() {
		return direct.getSource();
	}

	@Override
	public MClass getTarget() {
		return direct.getTarget();
	}

	@Override
	public void setSource(MClass source) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTarget(MClass target) {
		throw new UnsupportedOperationException();
	}

}

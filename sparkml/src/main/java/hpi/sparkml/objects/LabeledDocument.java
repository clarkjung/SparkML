package hpi.sparkml.objects;

public class LabeledDocument {

	private long id;
	private String text;
	private double label;
	
	public LabeledDocument(long id, String text, double label){
		this.setId(id);
		this.setText(text);
		this.setLabel(label);
	}

	public long getId() {
		return id;
	}

	private void setId(long id) {
		this.id = id;
	}

	public double getLabel() {
		return label;
	}

	private void setLabel(double label) {
		this.label = label;
	}

	public String getText() {
		return text;
	}

	private void setText(String text) {
		this.text = text;
	}
	
}
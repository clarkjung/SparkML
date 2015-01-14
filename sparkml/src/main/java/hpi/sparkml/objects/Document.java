package hpi.sparkml.objects;

public class Document {

	private long id;
	private String text;
	
	public Document(long id, String text){
		this.setId(id);
		this.setText(text);
	}

	public long getId() {
		return id;
	}

	private void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	private void setText(String text) {
		this.text = text;
	}
	
}

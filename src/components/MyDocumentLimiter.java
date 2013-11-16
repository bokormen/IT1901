package components;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class MyDocumentLimiter extends PlainDocument {
	private int limit;

	public MyDocumentLimiter(int limit) {
		super();
		this.limit = limit;
	}

	/**
	 * Metode som ordner opp i strengen hvis den er for lang
	 */
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null)
			return;

		if ((getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);
		}
	}
}
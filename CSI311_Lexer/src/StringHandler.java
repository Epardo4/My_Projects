import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class StringHandler {
	private String document;
	private int index = 0;
	public StringHandler() {
		document = "No Document Specified";
	}
	public StringHandler(String d)  {
		document = d;
	}

	public char peek(int i) {
		char peakedCharacter = document.charAt(index + i);
		return peakedCharacter;
	}
	public String peekString(int i) {
		String peakedString = document.substring(index+1, index+i);
		return peakedString;
	}
	public char getChar() {
		if(!isDone()) index++;
		return document.charAt(index);
	}
	public void swallow(int i) {
		index += i;
	}
	public boolean isDone() {
		if(index >= document.length() - 1) return true;
		return false;
	}
	public String remainder() {
		return document.substring(index);
	}
}
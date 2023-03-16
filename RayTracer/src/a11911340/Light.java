package a11911340;

public interface Light {
	
	public MyVector getColor();
	
	public static MyVector getAmbient() {
		return Output.AMBIENT;
	}
}

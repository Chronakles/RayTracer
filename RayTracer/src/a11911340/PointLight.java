package a11911340;

public class PointLight implements Light{
	
	private MyVector color;
	private MyVector position;
	
	PointLight(MyVector col, MyVector pos){
		this.color = col;
		this.position = pos;
	}
	
	public MyVector getColor() {
		return this.color;
	}
	
	public MyVector getPosition() {
		return this.position;
	}
}

package a11911340;

public class ParallelLight implements Light {

	MyVector parallelcolor;
	MyVector direction;
	
	ParallelLight(MyVector col, MyVector dir) {
		this.parallelcolor = col;
		this.direction = dir;
	}
	
	public ParallelLight() {
		this.parallelcolor = new MyVector(1, 1, 1);
		this.direction = new MyVector(0, 0, 1);
	}

	public MyVector getDirection() {
		return direction;
	}
	
	public MyVector getColor() {
		return parallelcolor;
	}
	
}

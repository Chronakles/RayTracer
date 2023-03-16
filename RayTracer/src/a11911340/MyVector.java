package a11911340;

public class MyVector {
	public double x;
	public double y;
	public double z;
	
	public MyVector() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public MyVector(double x) {
		this.x = x;
		this.y = 0;
		this.z = 0;
	}
	
	public MyVector(double x, double y) {
		this.x = x;
		this.y = y;
		this.z = 0;
	}
	
	public MyVector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void add(MyVector b) {
		this.x += b.x; 
		this.y += b.y; 
		this.z += b.z; 
	}
	
	
	
	public double scalarProduct(MyVector b) {
		return Math.sqrt(this.x*b.x + this.y*b.y + this.z*b.z);
	}
	
	public MyVector scalarMultiplication(double s) {
		return new MyVector(this.x * s, this.y * s, this.z * s);
	}
	
	public void divide(int a) {
		this.x /= a;
		this.y /= a;
		this.z /= a;
	}
	
	public MyVector subtract(MyVector v) {
		return new MyVector(this.x-v.x, this.y-v.y, this.z-v.z);
	}
	
	public MyVector normalize() {
		double norm = this.scalarProduct(this);
		if(Math.abs(norm) == 0) {
			return new MyVector();
		}
		return new MyVector(this.x/norm, this.y/norm, this.z/norm);
	}
	
	public MyVector crossProduct(MyVector b) {
		return new MyVector(this.y * b.z - this.z * b.y,
							this.z * b.x - this.x * b.z,
							this.x * b.y - this.y * b.x);
	}
	
	public double product(MyVector b) {
		return this.x*b.x + this.y*b.y + this.z*b.z;
	}
	
	public MyVector multiply(MyVector v) {
		return new MyVector(this.x * v.x, this.y * v.y, this.z * v.z);
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getZ() {
		return this.z;
	}
	
	public double length() {
		return this.scalarProduct(this);
	}
	
	public void move(MyVector dir) {
		double epsilon = 0.0004;
		this.x = this.x + (dir.x * epsilon);
		this.y = this.y + (dir.y * epsilon);
		this.z = this.z + (dir.z * epsilon);
	}

}

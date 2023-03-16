package a11911340;

public class Util {

	public static MyVector add(MyVector a, MyVector b) {
		return new MyVector(a.x + b.x, a.y + b.y,	a.z + b.z);		
	}
	
	public static MyVector add(MyVector a, MyVector b, MyVector c) {
		return Util.add(a, Util.add(b, c));		
	}
	
	//code from morpheus
	public static MyVector mitternachtsformel(double a, double b, double c)
	{
		double diskriminante = b*b-4*a*c;
		if(a == 0)
		{
			return new MyVector();
		}
		if(diskriminante < 0)
		{
			return new MyVector();
		}
		else if(diskriminante == 0)
		{
			return new MyVector(-b/(2*a), 0, 1);
		}
		else
		{
			double rightPart = Math.sqrt(diskriminante);
			return new MyVector((-b+rightPart)/(2*a), (-b-rightPart)/(2*a), 2);
		}
	}
	
	
	public static double abs(double d) {
		if(d < 0) {
			d *= -1;
		}
		return d;
	}
}

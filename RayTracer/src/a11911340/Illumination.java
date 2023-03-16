package a11911340;

public class Illumination {

	Sphere reference; // um es mit einer sphere zu verknuepfen
	
	double ka;
	double kd;
	double ks;
	double exponent;
	
	public int getRGB(MyVector pos) {
		if(reference == null) return 0; //wenn es kein objekt dazu gibt, sofort raus
		
		
		return 0;
	}
	
	public void setReference(Sphere ref) {
		this.reference = ref;
	}
}

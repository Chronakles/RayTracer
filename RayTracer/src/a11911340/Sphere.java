package a11911340;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Sphere implements Objects {

	public double radius;
	public MyVector position;
	public MyVector color;
	
	public double ka;
	public double kd;
	public double ks;
	public double exponent;
	
	public double reflectance;
	public double transmittance;
	public double refraction;
	
	public BufferedImage texture;
	
	Sphere(MyVector pos, MyVector col, double rad, double ambient, double diffuse, double specular, double exponent){
		this.position = pos;
		this.color = col;
		this.radius = rad;
		this.ka = ambient;
		this.kd = diffuse;
		this.ks = specular;
		this.exponent = exponent;
		this.reflectance = 0.0;
		this.transmittance = 0.0;
		this.refraction = 0.0;
		this.texture = null;
	}
	
	Sphere(MyVector pos, MyVector col, double rad, double ambient, double diffuse, double specular, double exponent, double refl, double refr, double trans){
		this.position = pos;
		this.color = col;
		this.radius = rad;
		this.ka = ambient;
		this.kd = diffuse;
		this.ks = specular;
		this.exponent = exponent;
		this.reflectance = refl;
		this.transmittance = refr;
		this.refraction = trans;
		this.texture = null;
	}
	
	Sphere(MyVector pos, MyVector col, double rad, double ambient, double diffuse, double specular, double exponent, double refl, double refr, double trans, BufferedImage img){
		this.position = pos;
		this.color = col;
		this.radius = rad;
		this.ka = ambient;
		this.kd = diffuse;
		this.ks = specular;
		this.exponent = exponent;
		this.reflectance = refl;
		this.transmittance = refr;
		this.refraction = trans;
		this.texture = img;
	}
	
	Sphere(MyVector pos, MyVector col, double rad){
		this.position = pos;
		this.color = col;
		this.radius = rad;
	}
	
	Sphere(MyVector pos, MyVector col){
		this.position = pos;
		this.color = col;
		this.radius = 1;
	}
	
	public MyVector getPosition() {
		return position;
	}
	public void setPosition(MyVector help) {
		this.position = help;
	}
	
	public int getColor() {
		double r1 = this.color.getX() * 255;
		double g1 = this.color.getY() * 255;
		double b1 = this.color.getZ() * 255;
		int r = (int) r1;
		int g = (int) g1;
		int b = (int) b1;
		Color col = new Color(r, g, b);
		return col.getRGB();
	}
	
	public double getRadius() {
		return radius;
	}
	
	
	
	public double intersect(Ray ray){
		
		//code from morpheus tutorial using the analytic solution from scratch a pixel 
		//(https://www.scratchapixel.com/lessons/3d-basic-rendering/minimal-ray-tracer-rendering-simple-shapes/ray-sphere-intersection)
		double a = ray.direction.product(ray.direction);
		MyVector ec = ray.position.subtract(position);
		double b = 2 * ray.direction.product(ec);
		double c = ec.product(ec) - radius*radius;
		MyVector result = Util.mitternachtsformel(a, b, c);
		switch((int)Math.round(result.z))
		{
		case 0:
			return Double.MAX_VALUE;
		case 1:
			return result.x;
		case 2:
			if(result.x < 0)
			{
				if(result.y < 0)
				{
					return Double.MAX_VALUE;
				}
				else
				{
					return result.y;
				}
			}
			else
			{
				if(result.y < 0)
				{
					return result.x;
				}
				else
				{
					return Math.min(result.x, result.y);
				}
			}
			default:
				return Double.MAX_VALUE;
		}
		
		// the code from the slides didn't work for me :/
		/*
		double t0 = Double.MAX_VALUE;
		double radius2 = this.radius*this.radius;
	
		MyVector L = this.position.subtract(ray.position);
		double tca = L.scalarProduct(ray.direction);
		double d2 = L.scalarProduct(L) - (tca * tca);
		double thc = (double) Math.sqrt(radius2 - d2);
		t0 = tca - thc;
			
		return t0;*/
		
	}
	
	public boolean checkIntersection(Ray ray) {
		
		MyVector L = this.position.subtract(ray.position);
		double tca = L.scalarProduct(ray.direction);
		if(tca < 0) {
			return false;
		}
		double d2 = L.scalarProduct(L) - tca*tca;
		if(d2 > this.radius*this.radius) {
			return false;
		}
		
		return true;
	}
	
	public int getColor(Ray ray, MyVector hitPoint, int depth) {
		
		MyVector newColor = new MyVector();
		
		for(int i = 0; i < Output.lightSource.size(); i++) {
					
			
			//Calculate for ParallelLight here
			if(Output.lightSource.get(i) instanceof ParallelLight) {
				
				ParallelLight light = (ParallelLight) Output.lightSource.get(i);
				
				MyVector l = (light.getDirection().scalarMultiplication(-1)).normalize();
				hitPoint.move(l);
				
				//formular from the slides of older semester
				//Lo = ka * La + SUM( kd * (viewRay * hitpointNormal) + ks * (viewerDirection * reflectedRay)^alpha ) * non-ambient-Light
				
				//calculate ambient part
				MyVector intensityAmbient = this.color.multiply(Light.getAmbient().scalarMultiplication(this.ka));
				MyVector normal = this.getNormal(hitPoint);
				
				newColor.add(this.color.multiply(intensityAmbient));
				
				
				Ray shadow = new Ray(hitPoint, l);
				boolean shadowed = shadow.checkShadow(Double.MAX_VALUE);
				
				//Check if objects obscures the light source
				if(!shadowed) {
					//Check if reflected light will hit the eye
					double nl = Math.max((normal.product(l)), 0);
					if(nl > 0) {
						//calculate diffuse part
						MyVector diffuse = this.color.multiply(light.getColor().scalarMultiplication((nl*this.kd)));
						
						
						//calculate specular part
						MyVector v = (Output.camera.getPosition().subtract(hitPoint)).normalize();
						MyVector reflected = (normal.scalarMultiplication(nl*2)).subtract(l).normalize();
						double rv = Math.max(reflected.product(v), 0);
						MyVector specular = light.getColor().scalarMultiplication(Math.pow(rv, this.exponent)*this.ks);
				
						
						//MyVector other = this.color.multiply(light.getColor().scalarMultiplication(diffuse + specular));
						
						//now add all this parts together and return
						
						newColor.add(diffuse);
						newColor.add(this.color.multiply(specular));
					
					}
				}
				
				
			//Calculate for PointLight here
			}else if(Output.lightSource.get(i) instanceof PointLight) {
				
				PointLight light = (PointLight) Output.lightSource.get(i);
				
				MyVector l = (light.getPosition().subtract(hitPoint)).normalize();
				hitPoint.move(l);
				
				//calculate ambient part
				MyVector intensityAmbient = this.color.multiply(Output.AMBIENT.scalarMultiplication(this.ka));
				MyVector normal = this.getNormal(hitPoint);
				
				newColor.add(this.color.multiply(intensityAmbient));
				if(this.reflectance <= 0) {
					Ray shadow = new Ray(hitPoint, l);
					double limit = (light.getPosition().subtract(hitPoint)).length();
					boolean shadowed = shadow.checkShadow(limit);
					
					//Check if objects obscures the light source
					if(!shadowed) {
						//Check if reflected light will hit the eye
						double nl = Math.max((normal.product(l)), 0);
						if(nl > 0) {
							//calculate diffuse part
							MyVector diffuse = this.color.multiply(light.getColor().scalarMultiplication((nl*this.kd)));
							
							
							//calculate specular part
							MyVector v = (Output.camera.getPosition().subtract(hitPoint)).normalize();
							MyVector reflected = (normal.scalarMultiplication(nl*2)).subtract(l).normalize();
							double rv = Math.max(reflected.product(v), 0);
							MyVector specular = light.getColor().scalarMultiplication(Math.pow(rv, this.exponent)*this.ks);
					
							
							//MyVector other = this.color.multiply(light.getColor().scalarMultiplication(diffuse + specular));
							
							//now add all this parts together and return
							
							newColor.add(diffuse);
							newColor.add(specular);
						
						}
					}
				}
			}
			
		}
		
		//newColor = newColor.scalarMultiplication(255);
		
		if(this.reflectance > 0) {
			MyVector viewRay = (hitPoint.subtract(ray.position)).normalize();
			MyVector r = (this.getReflected(viewRay, hitPoint)).normalize();
			hitPoint.move(r);
			Ray reflect = new Ray(hitPoint, r);
			int res = reflect.castRay(depth + 1);
			Color c = new Color(res);
			MyVector temp = new MyVector((double)c.getRed()/255, (double)c.getGreen()/255, (double)c.getBlue()/255);
			MyVector v = temp.scalarMultiplication(this.reflectance);
			newColor.add(v);
			
			/*MyVector normalThis = this.getNormal(hitPoint);
			MyVector V = Output.camera.getPosition().subtract(hitPoint).normalize();
			double NV = Math.max(normalThis.product(V), 0);
			MyVector refl = (normalThis.scalarMultiplication(NV*2).subtract(V)).normalize();
			MyVector hit = hitPoint;
			hit.move(refl);
			Ray reflect = new Ray(hit, refl);
			int res = reflect.castRay(depth + 1);
			Color c = new Color(res);
			MyVector temp = new MyVector(c.getRed()/255, c.getGreen()/255, c.getBlue()/255);
			MyVector v = temp.scalarMultiplication(this.reflectance);
			newColor.add(v); */
		}
		
		newColor = newColor.scalarMultiplication(255);
		
		
		newColor.x = Math.min(255, newColor.x);
		newColor.y = Math.min(255, newColor.y);
		newColor.z = Math.min(255, newColor.z);
		
		newColor.x = Math.max(0, newColor.x);
		newColor.y = Math.max(0, newColor.y);
		newColor.z = Math.max(0, newColor.z);
		
		Color col = new Color((int)Math.round(newColor.x), (int)Math.round(newColor.y), (int)Math.round(newColor.z));
		int rgb = col.getRGB();
		//System.out.println("vec: " + newColor.x + ", " + newColor.y + ", " + newColor.z);
		//System.out.println("rgb: " + rgb);
		
		return rgb;
		
		
	}
	
	public MyVector getNormal(MyVector hitpoint) {
		//subtract the intersection point from the center and normalize it to get the normal of that point.
		return hitpoint.subtract(this.position).normalize();
	}

	public MyVector getReflected(MyVector I, MyVector hitPoint) {
		MyVector N = this.getNormal(hitPoint);
		double IN = I.product(N);
		return I.subtract(N.scalarMultiplication(IN*2)) ;
	}
	
	
	//for supersampling
	public MyVector getColorVec(Ray ray, MyVector hitPoint, int depth) {
		
		MyVector newColor = new MyVector();
		
		if(this.texture == null) {
			for(int i = 0; i < Output.lightSource.size(); i++) {
						
				
				//Calculate for ParallelLight here
				if(Output.lightSource.get(i) instanceof ParallelLight) {
					
					ParallelLight light = (ParallelLight) Output.lightSource.get(i);
					
					MyVector l = (light.getDirection().scalarMultiplication(-1)).normalize();
					hitPoint.move(l);
					
					//formular from the slides of older semester
					//Lo = ka * La + SUM( kd * (viewRay * hitpointNormal) + ks * (viewerDirection * reflectedRay)^alpha ) * non-ambient-Light
					
					//calculate ambient part
					MyVector intensityAmbient = this.color.multiply(Light.getAmbient().scalarMultiplication(this.ka));
					MyVector normal = this.getNormal(hitPoint);
					
					newColor.add(this.color.multiply(intensityAmbient));
					
					
					Ray shadow = new Ray(hitPoint, l);
					boolean shadowed = shadow.checkShadow(Double.MAX_VALUE);
					
					//Check if objects obscures the light source
					if(!shadowed) {
						//Check if reflected light will hit the eye
						double nl = Math.max((normal.product(l)), 0);
						if(nl > 0) {
							//calculate diffuse part
							MyVector diffuse = this.color.multiply(light.getColor().scalarMultiplication((nl*this.kd)));
							
							
							//calculate specular part
							MyVector v = (Output.camera.getPosition().subtract(hitPoint)).normalize();
							MyVector reflected = (normal.scalarMultiplication(nl*2)).subtract(l).normalize();
							double rv = Math.max(reflected.product(v), 0);
							MyVector specular = light.getColor().scalarMultiplication(Math.pow(rv, this.exponent)*this.ks);
					
							
							//MyVector other = this.color.multiply(light.getColor().scalarMultiplication(diffuse + specular));
							
							//now add all this parts together and return
							
							newColor.add(diffuse);
							newColor.add(this.color.multiply(specular));
						
						}
					}
					
					
				//Calculate for PointLight here
				}else if(Output.lightSource.get(i) instanceof PointLight) {
				
					PointLight light = (PointLight) Output.lightSource.get(i);
					if(this.reflectance <= 0) {
						
						MyVector l = (light.getPosition().subtract(hitPoint)).normalize();
						hitPoint.move(l);
						
						//calculate ambient part
						MyVector intensityAmbient = this.color.multiply(Output.AMBIENT.scalarMultiplication(this.ka));
						MyVector normal = this.getNormal(hitPoint);
						
						newColor.add(this.color.multiply(intensityAmbient));
					
						//if(this.transmittance <= 0) {
						
							Ray shadow = new Ray(hitPoint, l);
							double limit = (light.getPosition().subtract(hitPoint)).length();
							boolean shadowed = shadow.checkShadow(limit);
							
							//Check if objects obscures the light source
							if(!shadowed) {
								//Check if reflected light will hit the eye
								double nl = Math.max((normal.product(l)), 0);
								if(nl > 0) {
									//calculate diffuse part
									MyVector diffuse = this.color.multiply(light.getColor().scalarMultiplication((nl*this.kd)));
									
									
									//calculate specular part
									MyVector v = (Output.camera.getPosition().subtract(hitPoint)).normalize();
									MyVector reflected = (normal.scalarMultiplication(nl*2)).subtract(l).normalize();
									double rv = Math.max(reflected.product(v), 0);
									MyVector specular = light.getColor().scalarMultiplication(Math.pow(rv, this.exponent)*this.ks);
							
									
									//MyVector other = this.color.multiply(light.getColor().scalarMultiplication(diffuse + specular));
									
									//now add all this parts together and return
									
									newColor.add(diffuse);
									newColor.add(specular);
								
								}
							}
						//}
					}
				}
				
			}
			
			//newColor = newColor.scalarMultiplication(255);
			
			if(this.reflectance > 0) {
				MyVector viewRay = ray.direction.normalize();
				MyVector r = (this.getReflected(viewRay, hitPoint)).normalize();
				hitPoint.move(r);
				Ray reflect = new Ray(hitPoint, r);
				int res = reflect.castRay(depth + 1);
				Color c = new Color(res);
				MyVector temp = new MyVector((double)c.getRed()/255, (double)c.getGreen()/255, (double)c.getBlue()/255);
				MyVector v = temp.scalarMultiplication(this.reflectance);
				newColor.add(v);
				
				
			}
			
			if(this.transmittance > 0) {
				MyVector incoming = ray.direction.normalize();
				MyVector normal = this.getNormal(hitPoint);
				double etai = 1.0;
				double etaPrime = this.refraction;
				double thetaI = incoming.product(normal);
				if(thetaI > 0) {
					thetaI = -thetaI;
				}
				else {
					incoming = incoming.scalarMultiplication(-1);
					double help = etai;
					etai = etaPrime;
					etaPrime = help;
				}
				double eta = etai / etaPrime;
				double thetaT = Math.sqrt(1-eta*eta*Math.sin(thetaI)*Math.sin(thetaI));
				
				MyVector t = ((Util.add(incoming, normal.scalarMultiplication(thetaI))).scalarMultiplication(eta)).subtract(normal).scalarMultiplication(thetaT);
				
				double cosi = Math.cos(thetaI);
				double k = 1 - eta * eta * (1-cosi*cosi);
				
				if(k < 0) {
					//total internal reflection. no refraction
				}
				
			}
			
			return newColor;
		
		}else { //if we have a texture use this			
			double srcX = 0.5 + ((Math.atan2(this.getNormal(hitPoint).x, this.getNormal(hitPoint).z))/(Math.PI*2));
					
			double srcY = 0.5 - ((Math.asin(this.getNormal(hitPoint).y))/Math.PI);
			
			int myX = (int) Math.round(srcX * (this.texture.getWidth()-1));
			int myY = (int) Math.round(srcY * (this.texture.getHeight()-1));
			
			
			int result = this.texture.getRGB(myX, myY);
			
			Color help = new Color(result);
			MyVector texel = new MyVector((double)help.getRed()/255, (double)help.getGreen()/255, (double) help.getBlue()/255);
			
			//Calculate for ParallelLight here
			for(int i = 0; i < Output.lightSource.size(); i++) {
				if(Output.lightSource.get(i) instanceof ParallelLight) {
				
					ParallelLight light = (ParallelLight) Output.lightSource.get(i);
					
					MyVector l = (light.getDirection().scalarMultiplication(-1)).normalize();
					hitPoint.move(l);
					
					//formular from the slides of older semester
					//Lo = ka * La + SUM( kd * (viewRay * hitpointNormal) + ks * (viewerDirection * reflectedRay)^alpha ) * non-ambient-Light
					
					//calculate ambient part
					MyVector intensityAmbient = texel.multiply(Light.getAmbient().scalarMultiplication(this.ka));
					MyVector normal = this.getNormal(hitPoint);
					
					newColor.add(texel.multiply(intensityAmbient));
					
					
					Ray shadow = new Ray(hitPoint, l);
					boolean shadowed = shadow.checkShadow(Double.MAX_VALUE);
					
					//Check if objects obscures the light source
					if(!shadowed) {
						//Check if reflected light will hit the eye
						double nl = Math.max((normal.product(l)), 0);
						if(nl > 0) {
							//calculate diffuse part
							MyVector diffuse = texel.multiply(light.getColor().scalarMultiplication((nl*this.kd)));
							
							
							//calculate specular part
							MyVector view = (ray.position.subtract(hitPoint)).normalize();
							MyVector reflected = (normal.scalarMultiplication(nl*2)).subtract(l).normalize();
							double rv = Math.max(reflected.product(view), 0);
							MyVector specular = light.getColor().scalarMultiplication(Math.pow(rv, this.exponent)*this.ks);
					
							
							//MyVector other = this.color.multiply(light.getColor().scalarMultiplication(diffuse + specular));
							
							//now add all this parts together and return
							
							newColor.add(diffuse);
							newColor.add(specular);
						
						}
					}
					
					
				//Calculate for PointLight here
				}else if(Output.lightSource.get(i) instanceof PointLight) {
					
					PointLight light = (PointLight) Output.lightSource.get(i);
					
					MyVector l = (light.getPosition().subtract(hitPoint)).normalize();
					hitPoint.move(l);
					
					//calculate ambient part
					MyVector intensityAmbient = texel.multiply(Output.AMBIENT.scalarMultiplication(this.ka));
					MyVector normal = this.getNormal(hitPoint);
					
					newColor.add(texel.multiply(intensityAmbient));
					if(this.reflectance <= 0 || this.transmittance <= 0) {
						Ray shadow = new Ray(hitPoint, l);
						double limit = (light.getPosition().subtract(hitPoint)).length();
						boolean shadowed = shadow.checkShadow(limit);
						
						//Check if objects obscures the light source
						if(!shadowed) {
							//Check if reflected light will hit the eye
							double nl = Math.max((normal.product(l)), 0);
							if(nl > 0) {
								//calculate diffuse part
								MyVector diffuse = texel.multiply(light.getColor().scalarMultiplication((nl*this.kd)));
								
								
								//calculate specular part
								MyVector view = (ray.position.subtract(hitPoint)).normalize();
								MyVector reflected = (normal.scalarMultiplication(nl*2)).subtract(l).normalize();
								double rv = Math.max(reflected.product(view), 0);
								MyVector specular = light.getColor().scalarMultiplication(Math.pow(rv, this.exponent)*this.ks);
						
								
								//MyVector other = this.color.multiply(light.getColor().scalarMultiplication(diffuse + specular));
								
								//now add all this parts together and return
								
								newColor.add(diffuse);
								newColor.add(specular);
							
							}
						}
					}
				}
				
			}
			
			//newColor = newColor.scalarMultiplication(255);
			
			if(this.reflectance > 0) {
				MyVector viewRay = ray.direction.normalize();
				MyVector r = (this.getReflected(viewRay, hitPoint)).normalize();
				hitPoint.move(r);
				Ray reflect = new Ray(hitPoint, r);
				int res = reflect.castRay(depth + 1);
				Color c = new Color(res);
				MyVector temp = new MyVector((double)c.getRed()/255, (double)c.getGreen()/255, (double)c.getBlue()/255);
				MyVector v = temp.scalarMultiplication(this.reflectance);
				newColor.add(v);
			}
			
			if(this.transmittance > 0) {
				MyVector incoming = ray.direction.normalize();
				MyVector normal = this.getNormal(hitPoint);
				double eta = 1.0;
				double etaPrime = this.refraction;
				double cosThetaI = Math.min((incoming.scalarMultiplication(-1)).product(normal), 1.0);
				
			}
	
			return newColor;
			
		}
		
		
	}
	
	
}

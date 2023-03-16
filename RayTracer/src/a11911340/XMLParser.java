package a11911340;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class XMLParser {

	public static void printCamPos(Document doc) {
		MyVector lookat = new MyVector(0, 0, 0);
		MyVector position = new MyVector(0, 0, 1);
		MyVector up = new MyVector(0, 1, 0);
		double fov = Math.PI/4;
		
		NodeList nList = doc.getElementsByTagName("camera");
		
		for(int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			
			if(nNode.getNodeType() == Node.ELEMENT_NODE) {
				NodeList itemList = nNode.getChildNodes();
				
				for(int help = 0; help < itemList.getLength(); help++) {
					
					Node cNode = itemList.item(help);
					if(cNode.getNodeType() == Node.ELEMENT_NODE) {
						
						Element eElement = (Element) cNode;
						
						if(cNode.getNodeName() == "position") {
							System.out.println("position: Vec3[" + eElement.getAttribute("x") + ", " + eElement.getAttribute("y") + ", " + eElement.getAttribute("z") + "]");
							double x = Double.parseDouble(eElement.getAttribute("x"));
							double y = Double.parseDouble(eElement.getAttribute("y"));
							double z = Double.parseDouble(eElement.getAttribute("z"));
							position = new MyVector(x, y, z);
						}
						
						if(cNode.getNodeName() == "lookat") {
							System.out.println("lookat: Vec3[" + eElement.getAttribute("x") + ", " + eElement.getAttribute("y") + ", " + eElement.getAttribute("z") + "]");
							double x = Double.parseDouble(eElement.getAttribute("x"));
							double y = Double.parseDouble(eElement.getAttribute("y"));
							double z = Double.parseDouble(eElement.getAttribute("z"));
							lookat = new MyVector(x, y, z);
						}
						
						if(cNode.getNodeName() == "up") {
							System.out.println("up: Vec3[" + eElement.getAttribute("x") + ", " + eElement.getAttribute("y") + ", " + eElement.getAttribute("z") + "]");
							double x = Double.parseDouble(eElement.getAttribute("x"));
							double y = Double.parseDouble(eElement.getAttribute("y"));
							double z = Double.parseDouble(eElement.getAttribute("z"));
							up = new MyVector(x, y, z);
						}
						
						if(cNode.getNodeName() == "horizontal_fov") {
							System.out.println("horizontalFOV: " + eElement.getAttribute("angle"));
							fov = Double.parseDouble(eElement.getAttribute("angle"));
						}
						
						if(cNode.getNodeName() == "resolution") {
							System.out.println("resolution: " + eElement.getAttribute("horizontal") + " x " + eElement.getAttribute("vertical"));
							int widthx = Integer.parseInt(eElement.getAttribute("horizontal"));
							int heightx = Integer.parseInt(eElement.getAttribute("vertical"));
							Output.WIDTH = widthx;
							Output.HEIGHT = heightx;
						}
						
						if(cNode.getNodeName() == "max_bounces") {
							System.out.println("maxBounces: " + eElement.getAttribute("n"));
							Output.maxBounces = Integer.parseInt(eElement.getAttribute("n"));
						}
						
					}		
				}
			}
		}
		Output.camera = new Camera(fov, up, position, lookat);
		System.out.println("----------------------------");
	}
	
	
	public static void printLights(Document doc) {
		NodeList nList = doc.getElementsByTagName("lights");
		
		MyVector ambient = new MyVector(1, 1, 1);
		MyVector parallelColor = new MyVector(0, 10, 3);
		MyVector direction = new MyVector(1, 1, 1);
		MyVector pointColor = new MyVector(1, 1, 1);
		MyVector position = new MyVector(1, 1, 1);
		
		
		boolean parallel = false;
		boolean point = false;
		
		
		for(int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			
			
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					NodeList itemList = nNode.getChildNodes();
					
					for(int help = 0; help < itemList.getLength(); help++) {
						Node cNode = itemList.item(help);
						
						if(cNode.getNodeType() == Node.ELEMENT_NODE) {
							NodeList lightList = cNode.getChildNodes();
							System.out.println(cNode.getNodeName());
							
							for(int count = 0; count < lightList.getLength(); count++) {
								Node leaf = lightList.item(count);
								
								if(leaf.getNodeType() == Node.ELEMENT_NODE) {
									Element eElement = (Element) leaf;
									
									// Here it prints ambient_light
									if(cNode.getNodeName() == "ambient_light") {
										System.out.println(eElement.getNodeName() + ": Color[" + eElement.getAttribute("r") + ", " + eElement.getAttribute("g") + ", " + eElement.getAttribute("b") + "]");
										double x = Double.parseDouble(eElement.getAttribute("r"));
										double y = Double.parseDouble(eElement.getAttribute("g"));
										double z = Double.parseDouble(eElement.getAttribute("b"));
										Output.AMBIENT = new MyVector(x, y, z);
									}
									
									// Here it prints parallel_lights
									if(cNode.getNodeName() == "parallel_light") {
										parallel = true;
										if(leaf.getNodeName() == "color") {
											System.out.println(eElement.getNodeName() + ": Color[" + eElement.getAttribute("r") + ", " + eElement.getAttribute("g") + ", " + eElement.getAttribute("b") + "]");
											double x = Double.parseDouble(eElement.getAttribute("r"));
											double y = Double.parseDouble(eElement.getAttribute("g"));
											double z = Double.parseDouble(eElement.getAttribute("b"));
											parallelColor = new MyVector(x, y, z);
										}
										
										if(leaf.getNodeName() == "direction") {
											System.out.println(eElement.getNodeName() + ": Vec3["+ eElement.getAttribute("x") + ", " + eElement.getAttribute("y") + ", " + eElement.getAttribute("z") + "]");
											double x = Double.parseDouble(eElement.getAttribute("x"));
											double y = Double.parseDouble(eElement.getAttribute("y"));
											double z = Double.parseDouble(eElement.getAttribute("z"));
											direction = new MyVector(x, y, z);
										}
									}
									if(cNode.getNodeName() == "point_light") {
										point = true;
										if(leaf.getNodeName() == "color") {
											System.out.println(eElement.getNodeName() + ": Color[" + eElement.getAttribute("r") + ", " + eElement.getAttribute("g") + ", " + eElement.getAttribute("b") + "]");
											double x = Double.parseDouble(eElement.getAttribute("r"));
											double y = Double.parseDouble(eElement.getAttribute("g"));
											double z = Double.parseDouble(eElement.getAttribute("b"));
											pointColor = new MyVector(x, y, z);
										}
										
										if(leaf.getNodeName() == "position") {
											System.out.println(eElement.getNodeName() + ": Vec3["+ eElement.getAttribute("x") + ", " + eElement.getAttribute("y") + ", " + eElement.getAttribute("z") + "]");
											double x = Double.parseDouble(eElement.getAttribute("x"));
											double y = Double.parseDouble(eElement.getAttribute("y"));
											double z = Double.parseDouble(eElement.getAttribute("z"));
											position = new MyVector(x, y, z);
										}
									}
									//System.out.println(leaf.getNodeName());
								}
							}
							if(cNode.getNodeName() == "ambient_light") System.out.println("");
							if(parallel) {
								Output.lightSource.add(new ParallelLight(parallelColor, direction));
							}
							if(point) {
								Output.lightSource.add(new PointLight(pointColor, position));
							}
						}
						
					}
				}
			}
		
		
		parallel = false;
		point = false;
		System.out.println("----------------------------");
	}
	
	
	public static void printSurface(NodeList nNode) {
		for(int i = 0; i < nNode.getLength(); i++) {
			Node first = nNode.item(i);
			
			String objectName = null;
			MyVector position = null;
			MyVector color = null;
			BufferedImage img = null;
			double radius = 1;
			double ka = 0.2;
			double kd = 0.7;
			double ks = 1.0;
			double exp = 200;
			double refl = 0;
			double trans = 0;
			double refr = 0;
			
			if(first.getNodeType() == Node.ELEMENT_NODE) {
				System.out.println(first.getNodeName());
				objectName = first.getNodeName();
				
				if(objectName == "sphere") {
					Element el = (Element) first;
					radius = Double.parseDouble(el.getAttribute("radius"));
					NodeList sphere = first.getChildNodes();
					
					for(int help = 0; help < sphere.getLength(); help++) {
						Node insideSphere = sphere.item(help);
						
						if(insideSphere.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) insideSphere;
							
							//Here position will be printed
							if(insideSphere.getNodeName() == "position") {
								System.out.println("position: Vec3[" + eElement.getAttribute("x") + ", " + eElement.getAttribute("y") + ", " + eElement.getAttribute("z") + "]");
								double x = Double.parseDouble(eElement.getAttribute("x"));
								double y = Double.parseDouble(eElement.getAttribute("y"));
								double z = Double.parseDouble(eElement.getAttribute("z"));
								position = new MyVector(x, y, z);
							}
							
							
							// Here material Solid will be printed
							if(insideSphere.getNodeName() == "material_solid") {
								NodeList material = insideSphere.getChildNodes();
								
								for(int temp = 0; temp < material.getLength(); temp++) {
									Node insideMaterial = material.item(temp);
									
									if(insideMaterial.getNodeType() == Node.ELEMENT_NODE) {
										Element mElement = (Element) insideMaterial;
										
										if(mElement.getNodeName() == "color") {
											System.out.println(mElement.getNodeName() + ": Color[" + mElement.getAttribute("r") + ", " + mElement.getAttribute("g") + ", " + mElement.getAttribute("b")+ "]");
											double r = Double.parseDouble(mElement.getAttribute("r"));
											double g = Double.parseDouble(mElement.getAttribute("g"));
											double b = Double.parseDouble(mElement.getAttribute("b"));
											color = new MyVector(r, g, b);
										}
										
										if(mElement.getNodeName() == "phong") {
											System.out.println(mElement.getNodeName() + ": Phong{ka=" + mElement.getAttribute("ka") + ", kd=" + mElement.getAttribute("kd") + ", ks=" + mElement.getAttribute("ks") + ", exponent=" + mElement.getAttribute("exponent") + "}");
											ka = Double.parseDouble(mElement.getAttribute("ka"));
											kd = Double.parseDouble(mElement.getAttribute("kd"));
											ks = Double.parseDouble(mElement.getAttribute("ks"));
											exp = Double.parseDouble(mElement.getAttribute("exponent"));
										}
										
										if(mElement.getNodeName() == "reflectance") {
											refl = Double.parseDouble(mElement.getAttribute("r"));
											System.out.println(mElement.getNodeName() + ": " + mElement.getAttribute("r"));
										}
										
										if(mElement.getNodeName() == "transmittance") {
											trans = Double.parseDouble(mElement.getAttribute("t"));
											System.out.println(mElement.getNodeName() + ": " + mElement.getAttribute("t"));
										}
										
										if(mElement.getNodeName() == "refraction") {
											refr = Double.parseDouble(mElement.getAttribute("iof"));
											System.out.println(mElement.getNodeName() + ": " + mElement.getAttribute("iof"));
										}
									}
									
								}
							}
							
							
							// Here material textured will be printed
							if(insideSphere.getNodeName() == "material_textured") {
								NodeList material = insideSphere.getChildNodes();
								
								for(int temp = 0; temp < material.getLength(); temp++) {
									Node insideMaterial = material.item(temp);
									
									if(insideMaterial.getNodeType() == Node.ELEMENT_NODE) {
										Element mElement = (Element) insideMaterial;
										
										if(mElement.getNodeName() == "texture") {
											try {
												img = ImageIO.read(new File(mElement.getAttribute("name")));
											}catch(IOException e) {
												
											}
											System.out.println(mElement.getNodeName() + ": " + mElement.getAttribute("name"));
										}
										
										if(mElement.getNodeName() == "phong") {
											System.out.println(mElement.getNodeName() + ": Phong{ka=" + mElement.getAttribute("ka") + ", kd=" + mElement.getAttribute("kd") + ", ks=" + mElement.getAttribute("ks") + ", exponent=" + mElement.getAttribute("exponent") + "}");
										}
										
										if(mElement.getNodeName() == "reflectance") {
											System.out.println(mElement.getNodeName() + ": " + mElement.getAttribute("r"));
										}
										
										if(mElement.getNodeName() == "transmittance") {
											System.out.println(mElement.getNodeName() + ": " + mElement.getAttribute("t"));
										}
										
										if(mElement.getNodeName() == "refraction") {
											System.out.println(mElement.getNodeName() + ": " + mElement.getAttribute("iof"));
										}
									}
									
								}
							}
							
							
							// Here transformation will be printed
							if(insideSphere.getNodeName() == "transforms") {
								NodeList transforms = insideSphere.getChildNodes();
								
								for(int temp = 0; temp < transforms.getLength(); temp++) {
									Node insideTransforms = transforms.item(temp);
									
									if(insideTransforms.getNodeType() == Node.ELEMENT_NODE) {
										Element tElement = (Element) insideTransforms;
										
										if(tElement.getNodeName() == "translate") {
											System.out.println(tElement.getNodeName() + ": Vec3[" + tElement.getAttribute("x") + ", " + tElement.getAttribute("y") + ", " + tElement.getAttribute("z") + "]");
										}
										
										if(tElement.getNodeName() == "scale") {
											System.out.println(tElement.getNodeName() + ": Vec3[" + tElement.getAttribute("x") + ", " + tElement.getAttribute("y") + ", " + tElement.getAttribute("z") + "]");
										}
										
										if(tElement.getNodeName() == "rotateX") {
											System.out.println(tElement.getNodeName() + ": " + tElement.getAttribute("theta"));
										}
										
										if(tElement.getNodeName() == "rotateY") {
											System.out.println(tElement.getNodeName() + ": " + tElement.getAttribute("theta"));
										}
										
										if(tElement.getNodeName() == "rotateZ") {
											System.out.println(tElement.getNodeName() + ": " + tElement.getAttribute("theta"));
										}
									}
									
								}
							}
							
							
							
						}
					}
					System.out.println("");
					if(objectName == "sphere") {
						Output.myObjects.add(new Sphere(position, color, radius, ka, kd, ks, exp, refl, refr, trans, img));
					}
					
				}else if(objectName == "mesh") {
					Element el = (Element) first;
					Output.filename = el.getAttribute("name");
					NodeList mesh = first.getChildNodes();
					
					for(int help = 0; help < mesh.getLength(); help++) {
						Node meshMaterial = mesh.item(help);		
							
							// Here material Solid will be printed
							if(meshMaterial.getNodeName() == "material_solid") {
								NodeList material = meshMaterial.getChildNodes();
								
								for(int temp = 0; temp < material.getLength(); temp++) {
									Node insideMaterial = material.item(temp);
									
									if(insideMaterial.getNodeType() == Node.ELEMENT_NODE) {
										Element mElement = (Element) insideMaterial;
										
										if(mElement.getNodeName() == "color") {
											System.out.println(mElement.getNodeName() + ": Color[" + mElement.getAttribute("r") + ", " + mElement.getAttribute("g") + ", " + mElement.getAttribute("b")+ "]");
											double r = Double.parseDouble(mElement.getAttribute("r"));
											double g = Double.parseDouble(mElement.getAttribute("g"));
											double b = Double.parseDouble(mElement.getAttribute("b"));
											Output.wallColor = new MyVector(r, g, b);
										}
										
										if(mElement.getNodeName() == "phong") {
											System.out.println(mElement.getNodeName() + ": Phong{ka=" + mElement.getAttribute("ka") + ", kd=" + mElement.getAttribute("kd") + ", ks=" + mElement.getAttribute("ks") + ", exponent=" + mElement.getAttribute("exponent") + "}");
											Output.wallKa = Double.parseDouble(mElement.getAttribute("ka"));
											Output.wallKd = Double.parseDouble(mElement.getAttribute("kd"));
											Output.wallKs = Double.parseDouble(mElement.getAttribute("ks"));
											Output.wallExp = Double.parseDouble(mElement.getAttribute("exponent"));
										}
										
										if(mElement.getNodeName() == "reflectance") {
											Output.wallrefl = Double.parseDouble(mElement.getAttribute("r"));
											System.out.println(mElement.getNodeName() + ": " + mElement.getAttribute("r"));
										}
										
										if(mElement.getNodeName() == "transmittance") {
											Output.walltrans = Double.parseDouble(mElement.getAttribute("t"));
											System.out.println(mElement.getNodeName() + ": " + mElement.getAttribute("t"));
										}
										
										if(mElement.getNodeName() == "refraction") {
											Output.wallrefr = Double.parseDouble(mElement.getAttribute("iof"));
											System.out.println(mElement.getNodeName() + ": " + mElement.getAttribute("iof"));
										}
									}
									
								}
							}
							
							
							// Here material textured will be printed
							if(meshMaterial.getNodeName() == "material_textured") {
								NodeList material = meshMaterial.getChildNodes();
								
								for(int temp = 0; temp < material.getLength(); temp++) {
									Node insideMaterial = material.item(temp);
									
									if(insideMaterial.getNodeType() == Node.ELEMENT_NODE) {
										Element mElement = (Element) insideMaterial;
										
										if(mElement.getNodeName() == "texture") {
											Output.meshTextureName = mElement.getAttribute("name");
											System.out.println(mElement.getNodeName() + ": " + mElement.getAttribute("name"));
										}
										
										if(mElement.getNodeName() == "phong") {
											System.out.println(mElement.getNodeName() + ": Phong{ka=" + mElement.getAttribute("ka") + ", kd=" + mElement.getAttribute("kd") + ", ks=" + mElement.getAttribute("ks") + ", exponent=" + mElement.getAttribute("exponent") + "}");
											Output.wallKa = Double.parseDouble(mElement.getAttribute("ka"));
											Output.wallKd = Double.parseDouble(mElement.getAttribute("kd"));
											Output.wallKs = Double.parseDouble(mElement.getAttribute("ks"));
											Output.wallExp = Double.parseDouble(mElement.getAttribute("exponent"));
										}
										
										if(mElement.getNodeName() == "reflectance") {
											System.out.println(mElement.getNodeName() + ": " + mElement.getAttribute("r"));
											Output.wallrefl = Double.parseDouble(mElement.getAttribute("r"));
										}
										
										if(mElement.getNodeName() == "transmittance") {
											System.out.println(mElement.getNodeName() + ": " + mElement.getAttribute("t"));
											Output.walltrans = Double.parseDouble(mElement.getAttribute("t"));
										}
										
										if(mElement.getNodeName() == "refraction") {
											System.out.println(mElement.getNodeName() + ": " + mElement.getAttribute("iof"));
											Output.wallrefr = Double.parseDouble(mElement.getAttribute("iof"));
										}
									}
									
								}
							}
							
							
							// Here transformation will be printed
							if(meshMaterial.getNodeName() == "transforms") {
								NodeList transforms = meshMaterial.getChildNodes();
								
								for(int temp = 0; temp < transforms.getLength(); temp++) {
									Node insideTransforms = transforms.item(temp);
									
									if(insideTransforms.getNodeType() == Node.ELEMENT_NODE) {
										Element tElement = (Element) insideTransforms;
										
										if(tElement.getNodeName() == "translate") {
											System.out.println(tElement.getNodeName() + ": Vec3[" + tElement.getAttribute("x") + ", " + tElement.getAttribute("y") + ", " + tElement.getAttribute("z") + "]");
										}
										
										if(tElement.getNodeName() == "scale") {
											System.out.println(tElement.getNodeName() + ": Vec3[" + tElement.getAttribute("x") + ", " + tElement.getAttribute("y") + ", " + tElement.getAttribute("z") + "]");
										}
										
										if(tElement.getNodeName() == "rotateX") {
											System.out.println(tElement.getNodeName() + ": " + tElement.getAttribute("theta"));
										}
										
										if(tElement.getNodeName() == "rotateY") {
											System.out.println(tElement.getNodeName() + ": " + tElement.getAttribute("theta"));
										}
										
										if(tElement.getNodeName() == "rotateZ") {
											System.out.println(tElement.getNodeName() + ": " + tElement.getAttribute("theta"));
										}
									}
									
								}
							}
							
					
							
						
					}
				}
			}
		
		}
		
	}
	
	
	public void parseFile(File inputFile) {
		
		
		try {
			//File inputFile = new File("example3.xml");
			//Create a DocumentBuilder
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			
			//Create a Document from a file or stream
			StringBuilder xmlStringBuilder = new StringBuilder();
			xmlStringBuilder.append("<?xml version='1.0'?> <class> </class>");
			Document doc = builder.parse(inputFile);
			
			Element root = doc.getDocumentElement();
			System.out.println("outputFile: " + root.getAttribute("output_file"));
			
			System.out.println("----------------------------");
			
			NodeList childNodes = root.getChildNodes();
			
			for(int some = 0; some < childNodes.getLength(); some++) {
				Node nNode = childNodes.item(some);
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					//System.out.println(nNode.getNodeName());
					if(nNode.getNodeName() == "background_color") {
						Element eElement = (Element) nNode;
						double r1 = Double.parseDouble(eElement.getAttribute("r")) * 255;
						double g1 = Double.parseDouble(eElement.getAttribute("g")) * 255;
						double b1 = Double.parseDouble(eElement.getAttribute("b")) * 255;
						int r = (int) Math.round(r1);
						int g = (int) Math.round(g1);
						int b = (int) Math.round(b1);
						Output.background = new Color(r, g, b);
					}
					if(nNode.getNodeName() == "camera") {
						System.out.println(nNode.getNodeName());
						printCamPos(doc);
					}
					if(nNode.getNodeName() == "lights") {
						printLights(doc);
					}
					
					if(nNode.getNodeName() == "surfaces") {
						System.out.println(nNode.getNodeName());
						printSurface(nNode.getChildNodes());
					}
					
					
				}
			}			
			
				
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

	}
	
	public static void parseMesh(NodeList nNode){
		
	}
	
	
}

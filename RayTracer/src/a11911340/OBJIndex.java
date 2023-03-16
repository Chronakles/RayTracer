package a11911340;

public class OBJIndex {

	public int vertexIndex;
	public int texCoIndex;
	public int normalIndex;

	public boolean equals(Object obj)
	{
		OBJIndex index = (OBJIndex)obj;

		return vertexIndex == index.vertexIndex
				&& texCoIndex == index.texCoIndex
				&& normalIndex == index.normalIndex;
	}

	public int hashCode()
	{
		final int BASE = 17;
		final int MULTIPLIER = 31;

		int result = BASE;

		result = MULTIPLIER * result + vertexIndex;
		result = MULTIPLIER * result + texCoIndex;
		result = MULTIPLIER * result + normalIndex;

		return result;
	}
	
	public int getVertexIndex()   { 
		return vertexIndex; 
	}
	
	public int getTexCoIndex() { 
		return texCoIndex; 
	}
	
	public int getNormalIndex()   { 
		return normalIndex; 
	}

	public void setVertexIndex(int val)   { 
		vertexIndex = val; 
	}
	
	public void setTexCoIndex(int val) { 
		texCoIndex = val; 
	}
	
	public void setNormalIndex(int val)   { 
		normalIndex = val; 
	}
	
}

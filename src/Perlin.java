import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.HashMap; 
import java.lang.Math; 
import java.util.Random; 

class Vector{
	float x, y;
	public Vector(float x, float y){
		this.x = x; 
		this.y = y; 
	}
	public float dotProduct(Vector v){
		return x*v.x + y*v.y;
	}
}

class perlin{
	float[][] arr;
	Vector[][] gradients;
	int width, height;
	//int[] permutation;
	int[] permutation = { 151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 
                      103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 
                      26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 
                      87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 
                      77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 
                      46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 
                      187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 
                      198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 
                      255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 
                      170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 
                      172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 
                      104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 
                      241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 
                      157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 
                      93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180, 
                      151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 
                      103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 
                      26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 
                      87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 
                      77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 
                      46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 
                      187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 
                      198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 
                      255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 
                      170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 
                      172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 
                      104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 
                      241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 
                      157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 
                      93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180};



	// Gradient Vectors
	Vector[] gradient_vectors = {new Vector(1f, 1f), new Vector(-1f, 1f), new Vector(-1f, -1f), new Vector(1f, -1f)};
	public perlin(int width, int height){
		this.width = width; 
		this.height = height;
		this.permutation = permutation;
		shuffle();
		//this.gradients = new Vector[gridCells + 1][gridCells + 1];
		this.arr = new float[width][height];
		generateNoise(0.01f, 1, 0.1f, 0.1f, 0.01f);
	}


	public void generatePermutation(){
		int[] p = new int[512];
		for (int i = 0; i < 512; ++i){
			p[i] = permutation[i%256];
		}
		permutation = p;
	}

	public void shuffle(){
		Random rand = ThreadLocalRandom.current();
		for (int i = permutation.length - 1; i > 0; --i){
			int ix = rand.nextInt(i + 1); 
			int a = permutation[ix]; 
			permutation[ix] = permutation[i]; 
			permutation[i] = a;
		}
	}

	// Random Noise Function
	// contrast between Perlin Noise function
	public void generateArray(){
		float[][] a = new float[width][height]; 
		for (int i = 0; i < width; ++i){
			for (int j = 0; j < height; ++j){
				a[i][j] = -1.0f + (float)Math.random() * (1.0f - -1.0f);
			}
		}
		arr = a;
	}

	public void generateNoise(float freq, int octaves, float gain, float amplitude, float lacunarity){
		float[][] a = new float[width][height];

		for (int n = 0; n < octaves; ++n){
			for (int i = 0; i < width; ++i){
				for (int j = 0; j < height; ++j){
					a[i][j] += Noise((float)i*freq, (float)j*freq); 
				}
			}
			freq *= lacunarity;
		}

		arr = a;
	}

	public void classicNoise(){
		float[][] a = new float[width][height];
		for (int i = 0; i < width; ++i){
			for (int j = 0; j < height; ++j){
				a[i][j] = Noise((float)i * 0.01f, (float)j * 0.01f);
			}
		}
		arr = a;
	}

	// interpolation function
	public float lerp(float p1, float p2, float weight){
		if (weight < 0.0){
			return p1; 
		}
		if (weight > 1.0){
			return p2;
		}
		//return (p2 - p1) * weight + p1;
		// cubic interpolation: 
		return (p2 - p1) * (3f - weight * 2f) * weight * weight + p1;
	}

	public Vector getGradientVector(int i){
		int h = i & 3;
		return gradient_vectors[h];
	}

	// dont dare use in end implementation (from wikipedia)
	public Vector randomgradientVector(int x, int y){
		// need index into permutation table

		//Vector[] gradient_vectors = new Vector[] {new Vector(1.0f, 1.0f), new Vector(-1.0f, -1.0f), new Vector(-1.0f, 1.0f), new Vector(1.0f, -1.0f)};
		//int ind = ThreadLocalRandom.current().nextInt(gradient_vectors.length);
		float random = 2920f * (float) Math.sin(x * 21942f + y * 171324f + 8912f) * (float) Math.cos(x * 23157f * y * 217832f + 9758f);
		return new Vector((float)Math.cos(random), (float)Math.sin(random));
	}
	
	public float cellDotProduct(int cx, int cy, float x, float y, Vector gradient){
		Vector distanceVector = new Vector(x - (float)cx, y - (float)cy);
		return distanceVector.dotProduct(gradient);  
	}

	public float fade(float t){
		//return 3 * t*t - 2*t*t*t;
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

	// Noise function will return a noise value for a given x and y position, (x and y must be floats. in generation loop will multiply x and y by frequency)
	// Steps of the algorithim: 
	// 1 Define Grid size, divide the world into cells of given length 
	// when determining the perlin value noise we must convert the x y position into the relative position within the cell
	// 2 Compute Gradient Vectors for each corner of the cells, this will use a permutation table. 
	// for the point within the cell, compute a distance vector to each corner of the cell, 
	// for each corner of the cell, take the dot product of the relevant distance and gradient vector
	// 3 interpolate between these four dot product values to get the point's noise value 
	public float Noise(float x, float y){
		int X = (int)Math.floor(x) & 255; 
		int Y = (int)Math.floor(y) & 255; 

		// grid cell corner coordinates 
		int x0 = (int)x; 
		int x1 = x0 + 1; 
		int y0 = (int)y; 
		int y1 = y0 + 1; 

		int x0Value, x1Value, y0Value, y1Value;
		x0Value = permutation[permutation[X]+Y]; 
		x1Value = permutation[permutation[X + 1]+Y]; 
		y0Value = permutation[permutation[X]+Y + 1]; 
		y1Value = permutation[permutation[X + 1]+Y + 1]; 


		// compute interpolation weights
		float wx = (float)x - (float)x0;
		float wy = (float)y - (float)y0;

		// Compute dotproducts of gradient and distance vectors 
		// then interpolate and return Noise value; 
		float g0, g1, ix0, ix1, value; 

		g0 = cellDotProduct(x0, y0, x, y, getGradientVector(x0Value)); 
		g1 = cellDotProduct(x1, y0, x, y, getGradientVector(x1Value));
		ix0 = lerp(g0, g1, wx); 


		g0 = cellDotProduct(x0, y1, x, y, getGradientVector(y0Value)); 
		g1 = cellDotProduct(x1, y1, x, y, getGradientVector(y1Value)); 
		ix1 = lerp(g0, g1, wx); 

		value = lerp(ix0, ix1, wy); 
		return value;
	}
}
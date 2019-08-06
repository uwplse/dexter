#include <cstdint>

#include "HalideBuffer.h"
#include "Dexter.h"

template<typename T>
using Buffer = Halide::Runtime::Buffer<T>;

// 9.94 ms per megapixel on Intel x86
void blur (Buffer<uint8_t> input, Buffer<uint8_t> output) 
{
	Buffer<uint8_t> temp(input.width(), input.height());
	
	for (int y = 0; y < input.height(); y++)
		for (int x = 0; x < input.width(); x++)
			temp(x, y) = (input(x-1, y) + input(x, y) + input(x+1, y)) / 3;
	
	for (int y = 0; y < input.height(); y++)
		for (int x = 0; x < input.width(); x++)
			output(x, y) = (temp(x, y-1) + temp(x, y) + temp(x, y+1)) / 3;
}

void harrisCorners (Buffer<float> input, Buffer<float> output)
{
	Buffer<float> grayscale(input.width(), input.height());

	// Convert Image to Grayscale
	for (int y = 0; y < input.height(); y++)
		for (int x = 0; x < input.width(); x++)
            grayscale(x, y) = 
            	0.299f * input(x, y, 0) +
            	0.587f * input(x, y, 1) +
            	0.114f * input(x, y, 2);


	// Compute Scharr Derivations
	Buffer<float> temp(input.width(), input.height());
	Buffer<float> Dx(input.width(), input.height());
	Buffer<float> Dy(input.width(), input.height());

	// Horizontal derivative Dx (2 stages)
	for (int y = 0; y < input.height(); y++) {
		for (int x = 0; x < input.width(); x++) {
			// Boundary conditions on x
			int x_minus_1 = (x - 1 < 0 ? 0 : x - 1);
			int x_plus_1 = (x + 1 >= input.width() ? input.width() - 1 : x + 1);

			temp(x, y) =  
				-1 * grayscale(x_minus_1, y) + grayscale(x_plus_1, y);
		}
	}

	for (int y = 0; y < input.height(); y++) {
		for (int x = 0; x < input.width(); x++) {
			// Boundary conditions on y
			int y_minus_1 = (y - 1 < 0 ? 0 : y - 1);
			int y_plus_1 = (y + 1 >= input.height() ? input.height() - 1 : y + 1);

			Dx(x, y) =  
				3 * temp(x, y_minus_1) + 
				10 * temp(x, y) +
				3 * temp(x, y_plus_1);
		}
	}

	// Vertical derivative Dy (2 stages)
	for (int y = 0; y < input.height(); y++) {
		for (int x = 0; x < input.width(); x++) {
			// Boundary conditions on x
			int x_minus_1 = (x - 1 < 0 ? 0 : x - 1);
			int x_plus_1 = (x + 1 >= input.width() ? input.width() - 1 : x + 1);

			temp(x, y) =  
				3 * grayscale(x_minus_1, y) + 
				10 * grayscale(x, y) +
				3 * grayscale(x_plus_1, y);
		}
	}

	for (int y = 0; y < input.height(); y++) {
		for (int x = 0; x < input.width(); x++) {
			// Boundary conditions on y
			int y_minus_1 = (y - 1 < 0 ? 0 : y - 1);
			int y_plus_1 = (y + 1 >= input.height() ? input.height() - 1 : y + 1);

			Dy(x, y) =  
				-1 * temp(x, y_minus_1) + temp(x, y_plus_1);
		}
	}

	// Calculate covariants
	Buffer<float> cov_xx(input.width(), input.height());
	Buffer<float> cov_yy(input.width(), input.height());
	Buffer<float> cov_xy(input.width(), input.height());

	for (int y = 0; y < input.height(); y++) {
		for (int x = 0; x < input.width(); x++) {
			cov_xx(x, y) =  Dx(x, y) * Dx(x, y);
			cov_yy(x, y) =  Dy(x, y) * Dy(x, y);
			cov_xy(x, y) =  Dx(x, y) * Dy(x, y);
		}
	}

	// Apply box filter without normalization
	for (int y = 0; y < input.height(); y++) {
		for (int x = 0; x < input.width(); x++) {
			// Boundary conditions on x
			int x_minus_1 = (x - 1 < 0 ? 0 : x - 1);
			int x_plus_1 = (x + 1 >= input.width() ? input.width() - 1 : x + 1);

			// Boundary conditions on y
			int y_minus_1 = (y - 1 < 0 ? 0 : y - 1);
			int y_plus_1 = (y + 1 >= input.height() ? input.height() - 1 : y + 1);

			cov_xx(x, y) =  
				cov_xx(x_minus_1, y_minus_1) + cov_xx(x, y_minus_1) + cov_xx(x_plus_1, y_minus_1) +
				cov_xx(x_minus_1, y) + cov_xx(x, y) + cov_xx(x_plus_1, y) +
				cov_xx(x_minus_1, y_plus_1) + cov_xx(x, y_plus_1) + cov_xx(x_plus_1, y_plus_1);

			cov_yy(x, y) =  
				cov_yy(x_minus_1, y_minus_1) + cov_yy(x, y_minus_1) + cov_yy(x_plus_1, y_minus_1) +
				cov_yy(x_minus_1, y) + cov_yy(x, y) + cov_yy(x_plus_1, y) +
				cov_yy(x_minus_1, y_plus_1) + cov_yy(x, y_plus_1) + cov_yy(x_plus_1, y_plus_1);

			cov_xy(x, y) =  
				cov_xy(x_minus_1, y_minus_1) + cov_xy(x, y_minus_1) + cov_xy(x_plus_1, y_minus_1) +
				cov_xy(x_minus_1, y) + cov_xy(x, y) + cov_xy(x_plus_1, y) +
				cov_xy(x_minus_1, y_plus_1) + cov_xy(x, y_plus_1) + cov_xy(x_plus_1, y_plus_1);
		}
	}

	// Compute harris corners
	for (int y = 0; y < input.height(); y++) {
		for (int x = 0; x < input.width(); x++) {
			float a = cov_xx(x, y);
            float b = cov_xy(x, y);
            float c = cov_yy(x, y);
			output(x, y) = (float)(a*c - b*b - 0.04*(a + c)*(a + c)); // k = 0.04
		}
	}
}

DEXTER_REGISTER_INTENTIONAL_FUNC(harrisCorners, Dexter::Target::Halide)
DEXTER_REGISTER_INTENTIONAL_FUNC(blur, Dexter::Target::Halide)
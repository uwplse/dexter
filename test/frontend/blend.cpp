#include <cstdint>
#include <stdlib.h>
#include "halide_benchmark.h"

#include "Dexter.h"

/* Utility Functions */
inline uint8_t Mul8x8Div255 (uint8_t a, uint8_t b)
{
	return (a * b) / 128;
}

inline uint8_t Screen8x8 (uint8_t a, uint8_t b)
{
	return a + b - Mul8x8Div255(a, b);
}

/* 
 * ## Blend Modes
 * Blend modes (or mixing modes) in digital image editing and computer graphics are 
 * used to determine how two layers are blended into each other.
 *
 * Implementations based on the following resources
 *
 * 1) https://en.wikipedia.org/wiki/Blend_modes
 * 2) https://photoshoptrainingchannel.com/blending-modes-explained/
 * 3) https://photoblogstop.com/photoshop/photoshop-blend-modes-explained
 * 4) https://helpx.adobe.com/photoshop/using/blending-modes.html
 *
 */


/*** Normal Blending Modes ***/

// Normal blend (alpha compositing)
// + Layers are represented as 1D buffers
void normalBlendf (Buffer<float,1> base, Buffer<float,1> active, Buffer<float,1> out, float opacity)
{
	for (int pixel=0; pixel<out.width(); pixel++) {
		out(pixel) = opacity * active(pixel) + (1.0f - opacity) * base(pixel);
	}
}
DEXTER_REGISTER_INTENTIONAL_FUNC(normalBlendf, Dexter::Target::Halide)

// Normal blend (alpha compositing)
// + Implemented for 8-bit layers
void normalBlend8 (Buffer<uint8_t,1> base, Buffer<uint8_t,1> active, Buffer<uint8_t,1> out, uint8_t opacity)
{
	for (int pixel=0; pixel<out.width(); pixel++) {
		out(pixel) = Mul8x8Div255(opacity, active(pixel)) * Mul8x8Div255(255 - opacity, base(pixel));
	}
}
DEXTER_REGISTER_INTENTIONAL_FUNC(normalBlend8, Dexter::Target::Halide)

// Dissolve blending
// + Layers are represented as 2D buffers
void dissolveBlend8 (Buffer<float,2> base, Buffer<float,2> active, Buffer<float,2> out, float opacity)
{
	if (opacity != 0.0f)
	{
		for (int row=0; row<out.height(); row++) {
			for (int col=0; col<out.width(); col++) {
				float rand_val = ((rand() % 100) + 1) / 100.0f;
				if (opacity - rand_val >= 0.0f)
					out(col,row) = active(col,row);
				else
					out(col,row) = base(col,row);
			}
		}
	}
}
DEXTER_REGISTER_INTENTIONAL_FUNC(dissolveBlend8, Dexter::Target::Halide)

/*** Darken Blending Modes ***/

// Darken blend for 8-bit images.
// + Layers are represented as 3D buffers
void darkenBlend8 (Buffer<uint8_t,3> base, Buffer<uint8_t,3> active, Buffer<uint8_t,3> out)
{
	for (int row=0; row<out.height(); row++) {
		for (int col=0; col<out.width(); col++) {
			for (int cn=0; cn<out.channels(); cn++) {
				if (base(col,row,cn) > active(col,row,cn))
					out(col,row,cn) = active(col,row,cn);
				else
					out(col,row,cn) = base(col,row,cn);
			}
		}
	}
}
//DEXTER_REGISTER_INTENTIONAL_FUNC(darkenBlend8, Dexter::Target::Halide)

// Multiply blend for 8-bit images.
void multiplyBlend8 (Buffer<uint8_t,2> base, Buffer<uint8_t,2> active, Buffer<uint8_t,2> out)
{
	for (int row=0; row<out.height(); row++) {
		for (int col=0; col<out.width(); col++) {
			out(col,row) = Mul8x8Div255(base(col,row), active(col,row));
		}
	}
}
//DEXTER_REGISTER_INTENTIONAL_FUNC(multiplyBlend8, Dexter::Target::Halide)

// Lienar burn for 8-bit images.
void linearBurn8 (Buffer<uint8_t,2> base, Buffer<uint8_t,2> active, Buffer<uint8_t,2> out)
{
	for (int row=0; row<out.height(); row++) {
		for (int col=0; col<out.width(); col++) {
			out(col,row) = (base(col,row) + active(col,row)) - 255;
		}
	}
}
//DEXTER_REGISTER_INTENTIONAL_FUNC(linearBurn8, Dexter::Target::Halide)

// Color burn for 8-bit images.
void colorBurn8 (Buffer<uint8_t,2> base, Buffer<uint8_t,2> active, Buffer<uint8_t,2> out)
{
	for (int row=0; row<out.height(); row++) {
		for (int col=0; col<out.width(); col++) {
			if (active(col, row) == 0)
				out(col,row) = 255;
			else
				out(col,row) = 255 - (255 - base(col,row)) / active(col,row);
		}
	}
}
//DEXTER_REGISTER_INTENTIONAL_FUNC(colorBurn8, Dexter::Target::Halide)

/*** Lighten Blending Modes ***/

// Lighten blend for 8-bit images.
void lightenBlend8 (Buffer<uint8_t,2> base, Buffer<uint8_t,2> active, Buffer<uint8_t,2> out)
{
	for (int row=0; row<out.height(); row++) {
		for (int col=0; col<out.width(); col++) {
			if (base(col,row) < active(col,row))
				out(col,row) = active(col,row);
			else
				out(col,row) = base(col,row);
		}
	}
}
//DEXTER_REGISTER_INTENTIONAL_FUNC(lightenBlend8, Dexter::Target::Halide)

// Screen blend for 8-bit images.
void screenBlend8 (Buffer<uint8_t,2> base, Buffer<uint8_t,2> active, Buffer<uint8_t,2> out)
{
	for (int row=0; row<out.height(); row++) {
		for (int col=0; col<out.width(); col++) {
			out(col,row) = Screen8x8(base(col,row), active(col,row));
		}
	}
}
//DEXTER_REGISTER_INTENTIONAL_FUNC(screenBlend8, Dexter::Target::Halide)

// Lienar burn for 8-bit images.
void linearDodge8 (Buffer<uint8_t,2> base, Buffer<uint8_t,2> active, Buffer<uint8_t,2> out)
{
	for (int row=0; row<out.height(); row++) {
		for (int col=0; col<out.width(); col++) {
			out(col,row) = (base(col,row) + active(col,row));
		}
	}
}
//DEXTER_REGISTER_INTENTIONAL_FUNC(linearDodge8, Dexter::Target::Halide)

// Color burn for 8-bit images.
void colorDodge8 (Buffer<uint8_t,2> base, Buffer<uint8_t,2> active, Buffer<uint8_t,2> out)
{
	for (int row=0; row<out.height(); row++) {
		for (int col=0; col<out.width(); col++) {
			if (active(col, row) == 255)
				out(col,row) = 255;
			else
				out(col,row) = base(col,row) / (255 - active(col,row));
		}
	}
}
//DEXTER_REGISTER_INTENTIONAL_FUNC(colorDodge8, Dexter::Target::Halide)

/*** Contrast Blending Modes ***/

// Overlay blend for 8-bit images.
// Strength reductioning of indexing expression using pointers arithmetic
void overlayBlend8 (Buffer<uint8_t,2> base, Buffer<uint8_t,2> active, Buffer<uint8_t,2> out)
{
	for (int row=0; row<out.height(); row++) {
		for (int col=0; col<out.width(); col++) {
			int delta;
			uint8_t a = base(col,row);
			uint8_t b = base(col,row);

			if (b >= 128)
				out(col,row) = Screen8x8(2 * a, b) - 255;
			else
				out(col,row) = Mul8x8Div255(2 * a, b);
		}
	}
}
//DEXTER_REGISTER_INTENTIONAL_FUNC(overlayBlend8, Dexter::Target::Halide)

/*int main(int argc, char **argv)
{
	const int width = 12816;
  const int height = 9604;
  const int pixels = width * height;

  
  float* img1f = new float[pixels];
  float* img2f = new float[pixels];
  float* outf = new float[pixels];

  for (int y = 0; y < height; y++) {
    for (int x = 0; x < width; x++) {
      img1f[x + y * width] = (rand() & 0xff) / 255.0;
      img2f[x + y * width] = (rand() & 0xff) / 255.0;
    }
  }

  double time = Halide::Tools::benchmark(5, 1, [&]() {
		normalBlendf(img1f, img2f, outf, 0.5, pixels);
	});

	printf("normalBlendf avg runtime (5 samples): %f\n", time);

  uint8_t* img1 = new uint8_t[pixels];
  uint8_t* img2 = new uint8_t[pixels];
  uint8_t* out = new uint8_t[pixels];

  for (int y = 0; y < height; y++) {
    for (int x = 0; x < width; x++) {
      img1[x + y * width] = rand() & 0xff;
      img2[x + y * width] = rand() & 0xff;
    }
  }

	time = Halide::Tools::benchmark(5, 1, [&]() {
		normalBlend8(img1, img2, out, 100, pixels);
	});

	printf("normalBlend8 avg runtime (5 samples): %f\n", time);

	time = Halide::Tools::benchmark(5, 1, [&]() {
		multiplyBlend8(img1, img2, out, height, width);
	});

	printf("multiplyBlend8 avg runtime (5 samples): %f\n", time);

	time = Halide::Tools::benchmark(5, 1, [&]() {
		screenBlend8(img1, img2, out, height, width);
	});

	printf("screenBlend8 avg runtime (5 samples): %f\n", time);

	time = Halide::Tools::benchmark(5, 1, [&]() {
		overlayBlend8(img1, img2, out, height, width);
	});

	printf("overlayBlend8 avg runtime (5 samples): %f\n", time);

	time = Halide::Tools::benchmark(5, 1, [&]() {
		boxblur(img1, out, height, width);
	});

	printf("boxblur avg runtime (5 samples): %f\n", time);
    
	return 0;
}*/
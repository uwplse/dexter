#include <cstdint>

#include "HalideBuffer.h"
#include "Dexter.h"

template<typename T>
using Buffer = Halide::Runtime::Buffer<T>;

void test (Buffer<uint8_t> input, Buffer<uint8_t> output)
{
	/* Standard C++ code ... */

	Dexter::Intentional::Execute(Dexter::Target::Halide, [=, &output] () {

		Buffer<uint8_t> temp(input.width(), input.height());
	
		for (int y = 0; y < input.height(); y++)
			for (int x = 0; x < input.width(); x++)
				temp(x, y) = (input(x-1, y) + input(x, y) + input(x+1, y)) / 3;
		
		for (int y = 0; y < input.height(); y++)
			for (int x = 0; x < input.width(); x++)
				output(x, y) = (temp(x, y-1) + temp(x, y) + temp(x, y+1)) / 3;

	});

	/* Standard C++ code ... */
}
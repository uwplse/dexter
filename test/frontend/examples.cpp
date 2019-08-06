#include "Dexter.h"

void example1()
{
	int x, y;

	x = x + 3;
	y = x;
	y = y - 1;
}

void example2()
{
	int sum = 0;
	for (int i=0; i<10; ++i) {
		sum += i;
	}
}

void example3(float* in, float* out, int pixels)
{
	for (int i=0; i<pixels; ++i) {
		out[i] = (in[i-1] + in[i] + in[i]) / 3;
	}
}

void example4(float* out, float mask, int pixels)
{
	for (int i=0; i<pixels; ++i) {
		out[i] = out[i] * mask;
	}
}

DEXTER_REGISTER_INTENTIONAL_FUNC(example1, Dexter::Target::Halide)
DEXTER_REGISTER_INTENTIONAL_FUNC(example2, Dexter::Target::Halide)
DEXTER_REGISTER_INTENTIONAL_FUNC(example3, Dexter::Target::Halide)
DEXTER_REGISTER_INTENTIONAL_FUNC(example4, Dexter::Target::Halide)

// Paper: Machine learning to predict unsat cores
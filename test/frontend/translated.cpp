Func halide_blur(Func in) {
	Func tmp, blurred;
	Var x, y, xi, yi;

	// The algorithm
	tmp(x, y) = (in(x-1, y) + in(x, y) + in(x+1, y))/3;
	blurred(x, y) = (tmp(x, y-1) + tmp(x, y) + tmp(x, y+1))/3;
	
	// The schedule
	blurred.tile(x, y, xi, yi, 256, 32)
		.vectorize(xi, 8).parallel(y);
	tmp.chunk(x).vectorize(x, 8);
	
	return blurred;
}
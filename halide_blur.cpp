#include "Halide.h"

class Gen_blur : public Halide::Generator<Gen_blur> {
public:
  Input<Halide::Buffer<unsigned short>> input{"input", 2};
  Input<Halide::Buffer<unsigned short>> out_init{"out_init", 2};
  Output<Halide::Buffer<unsigned short>> out{"out", 2};
  
  Halide::Var x{"x"};
  Halide::Var y{"y"};
  
  Halide::Func tmp;
  
  void generate() {
    tmp(x, y) = Halide::cast<uint16_t>((((Halide::cast<int32_t>(input(Halide::cast<int32_t>(x), Halide::cast<int32_t>(y))) + Halide::cast<int32_t>(input(Halide::cast<int32_t>((x + 1)), Halide::cast<int32_t>(y)))) + Halide::cast<int32_t>(input(Halide::cast<int32_t>((x + 2)), Halide::cast<int32_t>(y)))) / 3));
    out(x, y) = Halide::cast<uint16_t>((((Halide::cast<int32_t>(tmp(Halide::cast<int32_t>(x), Halide::cast<int32_t>(y))) + Halide::cast<int32_t>(tmp(Halide::cast<int32_t>(x), Halide::cast<int32_t>((y + 1))))) + Halide::cast<int32_t>(tmp(Halide::cast<int32_t>(x), Halide::cast<int32_t>((y + 2))))) / 3));
    
  }
  void schedule() {
    input.dim(0).set_bounds_estimate(0, 1024);
    input.dim(1).set_bounds_estimate(0, 1024);
    out_init.dim(0).set_bounds_estimate(0, 1024);
    out_init.dim(1).set_bounds_estimate(0, 1024);
    out.estimate(x, 0, 1024);
    out.estimate(y, 0, 1024);
    
  }
};
HALIDE_REGISTER_GENERATOR(Gen_blur, blur)

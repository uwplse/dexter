#ifndef DEXTER_H_
#define DEXTER_H_

namespace Dexter {

	namespace Target {
		enum TargetLang {Halide};
	}

	class Intentional {
	public:
		template<typename Lambda>
		static void Execute(Target::TargetLang target, Lambda fn) {
			fn();
		}
	};

	class Legacy {
  public:
    template<typename Lambda>
    static void Lift(Target::TargetLang target, Lambda fn) {
      fn();
    }
  };

	void Precondition (bool c) { }
}

// Since Clang does not support custom compiler-derivatives (afaik),
// We use these decls as a workaround.
#define DEXTER_REGISTER_INTENTIONAL_FUNC(fn, target) \
	Dexter::Target::TargetLang _dexter_##fn##_target = target;

#define DEXTER_REGISTER_LEGACY_FUNC(fn, target) \
	Dexter::Target::TargetLang _dexter_##fn##_target = target;

// Typedef buffer class
#include "HalideBuffer.h"

template<typename T, int D>
using Buffer = Halide::Runtime::Buffer<T,D>;

#endif /* DEXTER_H_ */
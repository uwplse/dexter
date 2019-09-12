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

	void Precondition (bool c) { }
}

// Since Clang does not support custom compiler-derivatives (afaik),
// We use these decls as a workaround.
#define DEXTER_REGISTER_INTENTIONAL_FUNC(fn, target) \
	Dexter::Target::TargetLang _dexter_##fn##_target = target;

#endif /* DEXTER_H_ */

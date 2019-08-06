/******** Exercise 1: Pre-conditions and Post-conditions *******/

// Goal: Lift lines 14-16 to DummyIR
// DummyIR: Has just two operators `incr(x)` and `decr(x)`

int x, y = 1, z = 2;

// Pre-condition: What is true here?
//   1) true                     (Weak Pre-condition)	
//   2) x == 0                   (Invalid Pre-condition)
//   3) y == 1 && z > y          (Stronger Pre-condition)
//   4) y == 1 && z == 2         (Strongest Pre-condition)

x = x + 3;
y = x;
y = y - 1;

// Post-condition: What is true here? (Prime variables
// represent the value of a variable at line 13)
//   1) x > y                    				(Weak post-condition)              
//   2) y == x - 1               				(Stronger post-condition)
//   3) y == x′ + 2 && x == x′ + 3      (Postcondition that describes outputs in terms of inputs)
//   ...
//   4) y == incr(incr(x′)) && x == incr(incr(incr(x′)))

int x0 = x; 
x == incr(incr(incr(x0)))
y == incr(incr(x0)) 



// Goal: Lift laplacian filter (lines 37-43) to Halide

void laplacian (float* inp, float* out, int rows, int cols)
{
	for (int y=0; y<rows; y++) {
		for (int x=0; x<cols; x++) {
			out[x] = inp[x-cols] + inp[x-1] + (-4 * inp[x]) + inp[x+1] + inp[x+cols];
		}
		out += cols;
		inp += cols;
	}

	// forall 0 <= y < rows, 0 <= x < cols.
	//     out(x, y) = inp(x,y-1) + inp(x-1,y) + (-4 * inp(x,y)) + inp(x+1,y) + inp(x,y+1);
}

// ** Key take-away 1 **
// A post-condition describes a valid translation IFF:
//        1. It *correctly* describes the value of *all* output variables
//        2. It describes the outputs using the target IR operators

============================================================================================





============================================================================================

/******** Exercise 2: Verifying Post-Conditions *******/

// Question: How do we find such post-conditions?
// Answer: Program Synthesis

// The class of synthesis algorithms that we use fall under the
// Generator<-->Verifier framework. High level summary of this
// framework:

S = SearchSpace();
while (!S.empty()) {
  PC = GeneratePostcondition(S); // Via enumeration or ML or CEGIS or ... (Focus next week)
  if (Verify(PC)) // <== Focus of this section
		printf("Synthesis Successful!");
}
printf("Synthesis Failed!");

// Goal: Verify a postcondition (line 86) for a given progam P (line 82-84)

int x, y;

x = x + 3;
y = x;
y = y - 1;

// Post-condition: y == incr(incr(x′)) && x == incr(incr(incr(x′)))




// Step 1. Abstract the post-condition into function

postcond (x: int, y: int, x′: int, y′: int) : bool ->
	x == incr(incr(incr(x′)))	&& y == incr(incr(x′))

// Step 2. Compute the verification condition(s) using
// the input code that PC must satisfy

int x, y;

assert ( ?? );

x = x + 3;
y = x;
y = y - 1;

assert (PC(x, y, x′, y′));

// Start from the end and perform substitution...
// (Read lines 112 to 128 in reverse)

assert (PC(x + 3, (x + 3) - 1, x, y) // <== This is the final VC

// Since we are the beginning of the code x == x′ and y == y′..

assert (PC(x + 3, (x + 3) - 1, x′, y′)

x = x + 3;

assert (PC(x, x - 1, x′, y′)

y = x;

assert (PC(x, y - 1, x′, y′)

y = y - 1;

PC(x, y, x′, y′)

// The process for generating VCs of any code is given by 
// hoare logic.

// Hoare logic also describes rules to handle conditional statements:

assert ((c && PC(x + 1, y)) || (!c && PC(x - 1, y)))

if (c) x = x + 1 else x = x - 1;

assert (PC(x, y));

// and loops (inductive proof using an loop-invariant Inv):

assert (Inv(x′, y′));
assert (Inv(x, y) && c ==> Inv(x + 1, y));
assert (Inv(x, y) && !c ==> PC(x, y));

while (c) x = x + 1;

assert (PC(x, y));

// ** Key take-away 2 **
// Synthesis works with a Generator-Verifier framework

// ** Key take-away 3 **
// Generate verification conditions to check validity
// of any postcondition
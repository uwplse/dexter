#include <cstdint>

#include "HalideBuffer.h"

template<typename T>
using Buffer = Halide::Runtime::Buffer<T>;

// 0.90 ms per megapixel on Intel x86
void fast_blur(Buffer<uint8_t> in, Buffer<uint8_t> blurred) 
{
	m128i one_third = _mm_set1_epi16(21846);
	#pragma omp parallel for
	for (int yTile = 0; yTile < in.height(); yTile += 32) {
		m128i a, b, c, sum, avg;
		m128i tmp[(256/8)*(32+2)];
		for (int xTile = 0; xTile < in.width(); xTile += 256) {
			m128i *tmpPtr = tmp;
			for (int y = -1; y < 32+1; y++) {
				const uint16_t *inPtr = &(in(xTile, yTile+y));
				for (int x = 0; x < 256; x += 8) {
					a = _mm_loadu_si128(( m128i*)(inPtr-1));
					b = _mm_loadu_si128(( m128i*)(inPtr+1));
					c = _mm_load_si128(( m128i*)(inPtr));
					sum = _mm_add_epi16(_mm_add_epi16(a, b), c);
					avg = _mm_mulhi_epi16(sum, one_third);
					_mm_store_si128(tmpPtr++, avg);
					inPtr += 8;
				}
			}
			tmpPtr = tmp;
			for (int y = 0; y < 32; y++) {
				m128i *outPtr = ( m128i *)(&(blurred(xTile, yTile+y)));
				for (int x = 0; x < 256; x += 8) {
					a = _mm_load_si128(tmpPtr+(2*256)/8);
					b = _mm_load_si128(tmpPtr+256/8);
					c = _mm_load_si128(tmpPtr++);
					sum = _mm_add_epi16(_mm_add_epi16(a, b), c);
					avg = _mm_mulhi_epi16(sum, one_third);
					_mm_store_si128(outPtr++, avg);
				}
			}
		}
	}
}
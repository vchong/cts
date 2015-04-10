#include "shared.rsh"

// Testing primitive types
float floatTest = 1.99f;
double doubleTest = 2.05;
char charTest = -8;
short shortTest = -16;
int intTest = -32;
long longTest = 17179869184l; // 1 << 34
long long longlongTest = 68719476736l; // 1 << 36
bool boolTest = false;

rs_allocation allocationTest;
int *intPtrTest;

uchar ucharTest = 8;
ushort ushortTest = 16;
uint uintTest = 32;
ulong ulongTest = 4611686018427387904L;
int64_t int64_tTest = -17179869184l; // - 1 << 34
uint64_t uint64_tTest = 117179869184l;

void test_primitive_types() {
    bool failed = false;
    start();

    _RS_ASSERT(floatTest == 2.99f);
    _RS_ASSERT(doubleTest == 3.05);
    _RS_ASSERT(charTest == -16);
    _RS_ASSERT(shortTest == -32);
    _RS_ASSERT(intTest == -64);
    _RS_ASSERT(longTest == 17179869185l);
    _RS_ASSERT(longlongTest == 68719476735l);

    _RS_ASSERT(ucharTest == 8);
    _RS_ASSERT(ushortTest == 16);
    _RS_ASSERT(uintTest == 32);
    _RS_ASSERT(ulongTest == 4611686018427387903L);
    _RS_ASSERT(int64_tTest == -17179869184l);
    _RS_ASSERT(uint64_tTest == 117179869185l);

    float time = end();

    if (failed) {
        rsDebug("test_primitive_types FAILED", time);
        rsSendToClientBlocking(RS_MSG_TEST_FAILED);
    }
    else {
        rsDebug("test_primitive_types PASSED", time);
        rsSendToClientBlocking(RS_MSG_TEST_PASSED);
    }

}

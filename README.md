# ExternalSorting

Program takes a binary file of at least size 8 blocks (it can be greater than 8), where a block is 16384 bytes, those bytes being organized as follows:
8 byte positive long          8 byte double

The program will sort the two values by the double using no more than 8 blocks of data by replacement selection and merge sort.

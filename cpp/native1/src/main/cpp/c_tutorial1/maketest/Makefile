# make -f beginer3
# make -f beginer3 clean

#t1.o: t1.c
#	gcc t1.c mk2.c -o mk2 # step1
#
#clean:
#	rm -f t1.o mk2.o mk2

CC=gcc
CFLAGS=-I.
DEPS = hellomake.h
OBJ = hellomake.o hellofunc.o

%.o: %.c $(DEPS)
	$(CC) -c -o $@ $< $(CFLAGS)

hellomake: $(OBJ)
	$(CC) -o $@ $^ $(CFLAGS)
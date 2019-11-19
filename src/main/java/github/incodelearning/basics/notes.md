## Memory Use
Object overhead is 16 bytes, which includes a reference to the object’s class, garbage collection information, and synchronization information.

Memory usage is typically padded to a multiple of 8 bytes (word size) on 64-bit machines.

Linked lists with non-static inner Node class. extra 8 bytes overhead for the reference to its enclosing instance (no this extra overhead for static inner class). Node needs 40 bytes,  16 Object overhead, 8 bytes for references to Item and next Node. A stack (built with linked list) of Integers use 32 + 64 N bytes.

Arrays in java are implemented as Objects. Primitive type arrays require 24 bytes header (16 Object overhead, 4 bytes for length, 4 bytes padding). N int values use 24 + 4 N bytes.

Each String Object uses 40 bytes: 16 object overhead, 12 bytes for int variables for offset, count, hashcode, 8 bytes for array reference, and 4 bytes of padding.

import sys
import numpy as np
import struct


class socket_io():
    def __init__(self, socket):
        self.socket = socket

    def write(self, buffer):
        self.socket.send(buffer)

    def read(self, no):
        return self.socket.recv(no)


class process_io():
    def write(self, buffer):
        sys.stdout.buffer.write(buffer)

    def read(self, no):
        return sys.stdin.buffer.read(no)


class pj_io(process_io, socket_io):
    def __init__(self, socket=None):
        if socket is None:
            self.read = super(pj_io, self).read
            self.write = super(pj_io, self).write
        else:
            super(process_io, self).__init__(socket)
            self.read = super(process_io, self).read
            self.write = super(process_io, self).write

    # x - ndarray float64,float
    def write_float_array(self, x):
        self.write_int(len(x))
        c = np.ndarray.tobytes(x)
        self.write(c)

    # x - ndarray int
    def write_int_array(self, x):
        self.write_int(len(x))
        x_int = np.array(x, dtype=np.int)
        c = np.ndarray.tobytes(x_int)
        self.write(c)

    def write_int(self, a):
        self.write(a.to_bytes(4, "big", signed=True))

    def write_double(self, a):
        self.write(struct.pack("d", a))

    # x - list/str ndarray
    def write_str_list(self, x):
        self.write_int(len(x))
        for sir in x:
            self.write_str(sir)

    # x - ndarray
    def write_matrix(self, x):
        n, m = np.shape(x)
        self.write_int(n)
        self.write_int(m)
        self.write(np.ndarray.tobytes(x))

    # Intoarce lista de siruri
    def read_str_list(self):
        n = int.from_bytes(self.read(4), "big", signed=True)
        siruri = []
        for i in range(n):
            sir = self.read_str()
            siruri.append(sir)
        return siruri

    def read_str(self):
        nr_octeti = int.from_bytes(self.read(4), "big", signed=True)
        return self.read(nr_octeti).decode("utf-8")

    def write_str(self, sir):
        self.write_int(len(sir))
        self.write(sir.encode(encoding="utf-8"))

    def read_matrix(self):
        n = int.from_bytes(self.read(4), "big", signed=True)
        m = int.from_bytes(self.read(4), "big", signed=True)
        x = np.zeros(shape=(n, m), dtype=np.float64)
        for i in range(n):
            x[i, :] = self.read_float_array()
        return x

    def read_float_array(self):
        n = int.from_bytes(self.read(4), "big", signed=True)
        x = np.zeros(shape=(n,), dtype=np.float64)
        for i in range(n):
            x[i] = struct.unpack('d', self.read(8))[0]
        return x

    def read_double(self):
        return struct.unpack('d', self.read(8))[0]

    def read_int_array(self):
        n = int.from_bytes(self.read(4), "big")
        x = np.zeros(shape=(n,), dtype=np.int)
        for i in range(n):
            x[i] = int.from_bytes(self.read(4), "big", signed=True)
        return x

    def read_int(self):
        return int.from_bytes(self.read(4), "big", signed=True)

    def flush(self):
        sys.stdout.buffer.flush()

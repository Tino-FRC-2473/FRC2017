from __future__ import division
import cv2
import numpy as np
import math
import scipy.integrate as integrate
import scipy.special as special
import socket
from json import JSONEncoder
from json import JSONDecoder
import time

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
host = ''
port = 5811
x = 1
s.bind((host, port))
print ('Listening')
s.listen(1)
conn, addr = s.accept()
print 'Connected by:', addr
st = ""

while (1):
	returned = conn.recv(1024)
	results = returned
	print(results)
	'''conn.send(JSONEncoder().encode({"Distance": results,
	"Angle A": results,
	"Bearing": results,
	"Left or Right": results,
	"Time Stamp": time.time()}))'''
	conn.send(JSONEncoder().encode({"Distance": 1.1,
	"Angle A": 2.2,
	"Bearing": 3.3,
	"Left or Right": 4.4,
	"Time Stamp": 5.5}))
	
'''
Traceback (most recent call last):
  File "NetTest.py", line 31, in <module>
    "Time Stamp": time.localtime()}))
  File "/usr/lib/python2.7/json/encoder.py", line 207, in encode
    chunks = self.iterencode(o, _one_shot=True)
  File "/usr/lib/python2.7/json/encoder.py", line 270, in iterencode
    return _iterencode(o, 0)
  File "/usr/lib/python2.7/json/encoder.py", line 184, in default
    raise TypeError(repr(o) + " is not JSON serializable")
TypeError: time.struct_time(tm_year=2017, tm_mon=2, tm_mday=4, tm_hour=5, tm_min=22, tm_sec=14, tm_wday=5, tm_yday=35, tm_isdst=0) is not JSON serializable

ABHI/SOHAN:
CHANGING TIME FORMAT BECAUSE THIS OBJECT CAN'T BE JSON ENCODED
'''

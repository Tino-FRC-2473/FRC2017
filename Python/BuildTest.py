import socket
import python
import math
from json import JSONEncoder, JSONDecoder
from picamera.array import PiRGBArray
from picamera import PiCamera
import time
import numpy as np
import cv2
from video import create_capture

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
host = ''
port = 54321
x = 1
s.bind((host, port))
print ('Listening')
s.listen(1)
conn, addr = s.accept()
print 'Connected by:', addr
st = ""

camera = PiCamera()
rawCapture = PiRGBArray(camera)
time.sleep(0.1)

def dummy():
    print('dummy is working')
    sendVal = "nlkm"
    conn.send(sendVal.encode('utf-8'))
    print (sendVal + " has been sent")

def masterFunction():

def imageThresholding():
    insideCoord1= [1,1]
    outsideCoord2= [1,1]
    insideCoord1= [1,1]
    insideCoord1= [1,1]

def findCenterAntiRectRatios (insideCoord1, outsideCoord1, insideCoord2, outsideCoord2, centroid1, centroid2):
    avgXCoord1 = (insideCoord1[0]+insideCoord1[0])/2
    centerY = (centroid1[1]+centroid2[1])/2
    avgXCoord2 = (insideCoord2[0]+insideCoord2[0])/2
    differenceXCoord1 = insideCoord1[0]-outsideCoord1[0]
    differenceXCoord2 = insideCoord2[0]-outsideCoord2[0]
    differenceYCoord1 = insideCoord1[1]-outsideCoord1[1]
    differenceYCoord2 = insideCoord2[1]-outsideCoord2[1]
    centerRatioX=differenceXCoord1/(differenceXCoord1+differenceXCoord2)
    differenceMediumX = (avgXCoord2+avgXCoord1)*centerRatioX
    centerX = avgXCoord1+differenceMediumX
    #mediumY= avgYCoord1+differenceMediumY
    sizeAvgRect = ((differenceXCoord1+differenceXCoord2)/2)*((differenceYCoord1+differenceYCoord2)/2)
    #ratioRect = #insert vision target width/height ratio
    #ratioOfRect1 = differenceXCoord1/differenceYCoord1
    #ratioOfRect2 = differenceXCoord2/differenceYCoord2
    point = [centerX, centerY, sizeAvgRect]
    return point

def distance (area):


def geometry(point, image):
	calVal = 25
	x = point[0]
	y = point[1]
	area = point[2]
	dInInches = distance(area)
	print dInInches
	width = len(image)
	offCenter = x-width/2
	inchesOffCenter = calVal*dInInches*offCenter
	angle = math.asin(inchesOffCenter/dInInches)
	return dInInches, angle

while (1) :
    st = conn.recv(1024)
    returned = st.decode()
    eval(returned)
    distance, angle = geometry(findCenterAntiRectRatios())
    time.sleep(1)

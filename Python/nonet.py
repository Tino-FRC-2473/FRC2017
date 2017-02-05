from __future__ import division
import os
import cv2
import numpy as np
import math
import scipy.integrate as integrate
import scipy.special as special
import socket
from json import JSONEncoder
from json import JSONDecoder
import time

os.system("v4l2-ctl --set-ctrl=exposure_auto_priority=1")
os.system("v4l2-ctl --set-ctrl=exposure_auto=1")
os.system("v4l2-ctl --set-ctrl=exposure_auto_priority=0")
os.system("v4l2-ctl --set-ctrl=exposure_absolute=4")

'''s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
host = ''
port = 5811
x = 1
s.bind((host, port))
print ('Listening')
s.listen(1)
conn, addr = s.accept()
print 'Connected by:', addr
st = ""'''

capture = cv2.VideoCapture(0)




def distanceRelation(averageHeight):
	x = float(averageHeight)
	print('distHeight: ', x)
	if averageHeight != 0:
		return 3150.0 / averageHeight #calibrate through regression

def widthDistRelation(averageWidth):
	x = float(averageWidth)
	print('distWidth: ', x)
	if averageWidth != 0:
		return 1260.0 / averageWidth  #calibrate through regression


def angleOfAttack(firstHeight, secondHeight, rectX, image, width1, width2, toggleVar):
	maxRectHeight = 0
	minRectHeight = 0
	if firstHeight > secondHeight:
		maxRectHeight = firstHeight
		minRectHeight = secondHeight

	elif secondHeight > firstHeight:
		maxRectHeight = secondHeight
		minRectHeight = firstHeight
	else:
		maxRectHeight = secondHeight
		minRectHeight = firstHeight
	if width1 > width2:
		maxRectWidth = width1
		minRectWidth = width2
	elif width2 > width1:
		maxRectWidth = width2
		minRectWidth = width1
	else:
		maxRectWidth = width2
		minRectWidth = width1
	print('width1: ', width1)
	print('width2: ', width2)
	print('height1: ', firstHeight)
	print('height2: ', secondHeight)
	if toggleVar == 0:
		y = 3300.0/minRectHeight
		z = 3300.0/maxRectHeight
		#print((math.pow(z,2)-math.pow(y,2)-64)/(-16*y))
	else:
		y = 1400.0/minRectWidth
		z = 1400.0/maxRectWidth
	#print((math.pow(z,2)-math.pow(y,2)-64)/(-16*y))
	if(((math.pow(z,2)-math.pow(y,2)-64)/(-16*y))>1):
		return -1
	else:
		angleOpposite = 0
		closeAngle = 0
		try:
			angleOpposite = math.acos((math.pow(z,2)-math.pow(y,2)-64)/(-16*y))
			closeAngle = math.asin((4.0 * math.sin(angleOpposite))/y) * (180/math.pi)
		except ValueError:
			print("arcsin or arccos error")
			return None
		angleOpposite = angleOpposite * (180/math.pi)
		angleOfAttack = 90.0 - angleOpposite - closeAngle
		if rectX > len(image[0])/2:
			angleOfAttack = angleOfAttack * -1.0
			return angleOfAttack
		else:
			return angleOfAttack

def height(topmost, bottommost):
	height = abs(topmost[1] - bottommost[1])
	return height

def width (leftmost, rightmost):
	width = abs(leftmost[0] - rightmost[0])
	return width

def bearing(image, centerX):
	middle = len(image[0])/2
	print('middle: ', middle)
	leftBearingCoord = 0 - middle
	rightBearingCoord = middle
	negativeCheck = centerX - middle
	chordWidth = len(image[0])
	middle = len(image[0])/2
	angle = 70.42*math.pi/180.0
	bisAngle = angle/2

	r = abs(middle/(math.sin(bisAngle)))
	print (r)
	#print (x)
	func = 0
	try:
		func = lambda x: math.sqrt(abs(+ x**2/(r**2 - x**2)))
	except ValueError:
		print("DomainError integration")
		return None

	imageSector = integrate.quad(func, leftBearingCoord, rightBearingCoord)
	turnSector = integrate.quad(func, centerX, 0)
	turnSectorLength = turnSector[0]
	turnSectorLength = abs(turnSectorLength)

	bearingAngle = abs(angle * 180 / math.pi * turnSectorLength / imageSector[0] - 35.21)
	if negativeCheck < 0:
		return -bearingAngle
	else:
		return bearingAngle


def analyze(recSt, image):
	calHWThreshold = 9/10
	if len(recSt) == 3:
		toggleVar = 1
		maxHeight = 0
		avgHeight = 0
		firstRecData = recSt[1]
		secondRecData = recSt[2]
		thirdRecData = recSt[3]
		height1 = firstRecData['height']
		height2 = secondRecData['height']
		height3 = thirdRecData['height']
		width1 = firstRecData['width']
		width2 = secondRecData['width']
		width3 = thirdRecData['width']
		if height1 >= height2 and height1 >= height3:
			maxHeight = height1
		if height2 >= height3 and height2 >= height1:
			maxHeight = height2
		if height3 >= height2 and height3 >= height1:
			maxHeight = height3
		if maxHeight == height1:
			centerY2 = secondRecData['yCoord']
			centerY3 = thirdRecData['yCoord']
			centerX = (secondRecData['xCoord'] + firstRecData['xCoord'])/2
			width1 = firstRecData['width']
			width2 = secondRecData['width']
			if height1/height2 > calHWThreshold and height2/height1 > calHWThreshold:
				toggleVar = 0
			else:
				toggleVar = 1
			averageWidth = float(secondRecData['width'] + firstRecData['width'])/2.0
			if centerY2 > centerY3:
				heightSecondary = secondRecData['top'] - thirdRecData['bottom']
				averageHeight = float(height1+heightSecondary)/2
			else:
				heightSecondary = thirdRecData['top'] - secondRecData['bottom']
				averageHeight = float(height1+heightSecondary)/2
		if maxHeight == height2:
			centerY1 = firstRecData['yCoord']
			centerY3 = thirdRecData['yCoord']
			centerX = (secondRecData['xCoord'] + firstRecData['xCoord'])/2
			width1 = secondRecData['width']
			width2 = firstRecData['width']
			if height1/height2 > calHWThreshold and height2/height1 > calHWThreshold:
				toggleVar = 0
			else:
				toggleVar = 1
				averageWidth = float(secondRecData['width'] + firstRecData['width'])/2.0
			if centerY1 > centerY3:
				heightSecondary = firstRecData['top'] - thirdRecData['bottom']
				averageHeight = float(height2+heightSecondary)/2
			else:
				heightSecondary = thirdRecData['top'] - firstRecData['bottom']
				averageHeight = float(height2+heightSecondary)/2
		if maxHeight == height3:
			centerY1 = firstRecData['yCoord']
			centerY2 = secondRecData['yCoord']
			centerX = (secondRecData['xCoord'] + thirdRecData['xCoord'])/2
			width1 = secondRecData['width']
			width2 = thirdRecData['width']
			if height2/height3 > calHWThreshold and height3/height2 > calHWThreshold:
				toggleVar = 0
			else:
				toggleVar = 1
			averageWidth = float(secondRecData['width'] + thirdRecData['width'])/2.0
			if centerY1 > centerY2:
				heightSecondary = firstRecData['top'] - secondRecData['bottom']
				averageHeight = float(height3+heightSecondary)/2
			else:
				heightSecondary = secondRecData['top'] - firstRecData['bottom']
				averageHeight = float(height3+heightSecondary)/2
		if toggleVar == 0:
			distance = distanceRelation(averageHeight)
		else:
			distance = widthDistRelation(averageWidth)
		bearingAngle = bearing(image, centerX)
		angleAttack = angleOfAttack(maxHeight, heightSecondary, centerX, image, width1, width2, toggleVar)
		if angleAttack > bearingAngle:
			leftOrRight = 1
		else:
			leftOrRight = 0
		return [distance, bearingAngle, angleAttack, leftOrRight]


	if len(recSt) == 2: #two rectangle case
		firstRecData = recSt[1]
		secondRecData = recSt[2]

		firstX = firstRecData['xCoord']
		secondX = secondRecData['xCoord']
		rectX = (firstX+secondX)/2

		firstArea = firstRecData['area']
		secondArea = secondRecData['area']

		firstHeight = firstRecData['height']
		secondHeight = secondRecData['height']

		firstWidth = firstRecData['width']
		secondWidth = secondRecData['width']
		if firstHeight/secondHeight > calHWThreshold and secondHeight/firstHeight > calHWThreshold:
			toggleVar = 0
		else:
			toggleVar = 1

		angleAttack = angleOfAttack(firstHeight, secondHeight, rectX, image, firstWidth, secondWidth, toggleVar)

		averageArea = (firstArea+secondArea)/2
		averageHeight = (firstHeight+secondHeight)/2
		averageWidth = (firstWidth+secondWidth)/2

		averageCenter = ((firstRecData['xCoord']+secondRecData['xCoord'])/2, (firstRecData['yCoord']+secondRecData['yCoord'])/2)
		differenceCenter = ((firstRecData['xCoord']-secondRecData['xCoord']), (firstRecData['yCoord']-secondRecData['yCoord']))

		bearingAngle = bearing(image, averageCenter[0])
		if firstHeight/secondHeight > calHWThreshold and secondHeight/firstHeight > calHWThreshold:
			distance = distanceRelation(averageHeight)
		else:
			distance = widthDistRelation(averageWidth)
		if angleAttack > bearingAngle:
			leftOrRight = 1
		else:
			leftOrRight = 0
		return [distance, bearingAngle, angleAttack, leftOrRight]
	if len(recSt) == 1: #desperation if there is only one rectangle
		firstRecData = recSt[1]
		center = firstRecData['xCoord']
		bearingAngle = bearing(image, center)
		return [0, bearingAngle, 0, 2]

def CV():
	_, frame = capture.read()
	hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
	lower_tape = np.array([60, 50, 50], dtype=np.uint8)
	upper_tape = np.array([80, 255, 255], dtype=np.uint8)
	mask = cv2.inRange(hsv, lower_tape, upper_tape)
	cv2.imshow('mask', mask)
	res = cv2.bitwise_and(frame, frame, mask= mask)
	(res, cnts, _) = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
	recNumber = 1
	recStorage = dict()
	'''print(cnts)'''
	for c in cnts:
		approx = cv2.approxPolyDP(c,0.01*cv2.arcLength(c,True),True)
		if len(approx)<9 and len(approx)>3 and cv2.contourArea(c)>100:
			print('passed')
			M = cv2.moments(c)
			cx = int(M['m10']/M['m00'])
			cy = int(M['m01']/M['m00'])
			cArea = cv2.contourArea(c)
			topmost = tuple(c[c[:,:,1].argmin()][0])
			bottommost = tuple(c[c[:,:,1].argmax()][0])
			leftmost = tuple(c[c[:,:,0].argmin()][0])
			rightmost = tuple(c[c[:,:,0].argmax()][0])
			yCoordinateHeight = c[1]
			rectHeight = height(topmost, bottommost)
			rectWidth = width(leftmost, rightmost)
			quadNumber = "rect: " + str(recNumber)
			cv2.drawContours(frame,[c],-1,(0,0,255),2)
			recMiniData = {'xCoord':cx, 'yCoord':cy, 'area':cArea, 'height': rectHeight, 'top': topmost[1], 'bottom': bottommost[1], 'width': rectWidth}
			recStorage.update({recNumber: recMiniData})
			recNumber += 1

	cv2.imshow('frame',frame)

	k = cv2.waitKey(5) & 0xFF
	if len(recStorage) > 0:
		results = analyze(recStorage, frame)
		return results

	#if k == ord("q"):
		#break

while (1):
	results = CV()
	if results != None:
		print(results)

		

cv2.destroyAllWindows()

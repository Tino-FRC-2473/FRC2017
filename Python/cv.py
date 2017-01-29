from __future__ import division
import cv2
import numpy as np
import math
import scipy.integrate as integrate
import scipy.special as special

capture = cv2.VideoCapture(0)

def distanceRelation(averageHeight):
	x = float(averageHeight)
	return 5200.0 / averageHeight

def angleOfAttack(averageHeight, averageCenter, distanceCenter):
	d1 = distanceRelation(averageHeight)
	#    calibration value          *  centerOffset
	i = (8.0 / differenceCenter[0]) * (len(image[0]) / 2.0 - averageCenter[0])
	d2 = math.sqrt( abs( math.pow(d1,2) - math.pow(i, 2) ) + math.pow( (abs(i) + 5.125), 2) )
	return ( math.acos( (math.pow(d1, 2) - math.pow(d2, 2) - math.pow(5.125, 2)) / ( (-2) * (d1) * (d2) ) ) * (float(180)/math.pi)  )

def intergrand(r, x):
	func = math.sqrt(r**2 - x**2))
	dydxSquared = (x**2) / (func**2)
	return math.sqrt(1 + dydxSquared)

def bearing(image, centerX):
	chordWidth = len(image[0])
	middle = len(image[0])/2
	leftBearingCoord = 0 - middle
	rightBearingCoord = middle
	negativeCheck = centerX - middle
	angle = 60
	bisAngle = angle/2
	r = chordWidth/2

	imageSector = quad(integrand, leftBearingCoord, rightBearingCoord, args=(r))
	turnSector = abs(quad(integrand, centerX, 0, args=(r)))

	bearingAngle = angle * turnSector / imageSector
	if negativeCheck < 0:
		return -bearingAngle
	else:
		return bearingAngle


def analyze(recSt, image):

	if len(recSt) == 3: #three rectangle case
		maxHeight = 0
		avgHeight = 0
		firstRecData = recSt[1]
		secondRecData = recSt[2]
		thirdRecData = recSt[3]
		height1 = firstRecData['height']
		height2 = secondRecData['height']
		height3 = thirdRecData['height']

		if height1 >= height2 and height1 >= height3:
			maxHeight = 1
		if height2 >= height3 and height2 >= height1:
			maxHeight = 2
		if height3 >= height2 and height3 >= height1:
			maxHeight = 3

		if maxHeight == 1:
			centerY2 = secondRecData['yCoord']
			centerY3 = thirdRecData['yCoord']
			centerX = (secondRecData['xCoord'] + firstRecData['xCoord'])/2
			if centerY2 > centerY3:
				heightSecondary = secondRecData['top'] - thirdRecData['bottom']
				averageHeight = float(height1+heightSecondary)/2
			else:
				heightSecondary = thirdRecData['top'] - secondRecData['bottom']
				averageHeight = float(height1+heightSecondary)/2
		if maxHeight == 2:
			centerY1 = firstRecData['yCoord']
			centerY3 = thirdRecData['yCoord']
			centerX = (secondRecData['xCoord'] + firstRecData['xCoord'])/2
			if centerY1 > centerY3:
				heightSecondary = firstRecData['top'] - thirdRecData['bottom']
				averageHeight = float(height2+heightSecondary)/2
			else:
				heightSecondary = thirdRecData['top'] - firstRecData['bottom']
				averageHeight = float(height2+heightSecondary)/2
		if maxHeight == 3:
			centerY1 = firstRecData['yCoord']
			centerY2 = secondRecData['yCoord']
			centerX = (secondRecData['xCoord'] + thirdRecData['xCoord'])/2
			if centerY1 > centerY2:
				heightSecondary = firstRecData['top'] - secondRecData['bottom']
				averageHeight = float(height3+heightSecondary)/2
			else:
				heightSecondary = secondRecData['top'] - firstRecData['bottom']
				averageHeight = float(height3+heightSecondary)/2

		bearingAngle = bearing(image, centerX)
		distance = (distanceRelation(averageHeight))
		print(distance)

	if len(recSt) == 2: #two rectange case
		firstRecData = recSt[1]
		secondRecData = recSt[2]

		firstArea = firstRecData['area']
		secondArea = secondRecData['area']

		firstHeight = firstRecData['height']
		secondHeight = secondRecData['height']

		averageArea = (firstArea+secondArea)/2
		averageHeight = (firstHeight+secondHeight)/2

		averageCenter = ((firstRecData['xCoord']+secondRecData['xCoord'])/2, (firstRecData['yCoord']+secondRecData['yCoord'])/2)
		differenceCenter = ((firstRecData['xCoord']-secondRecData['xCoord']), (firstRecData['yCoord']-secondRecData['yCoord']))

		bearingAngle = bearing(image, averageCenter[0])
		distance = (distanceRelation(averageHeight))
		print(angleOfAttack(averageHeight, ))

	if len(recSt) == 1: #one rectangle case aka cant do jack shit

while(1):

	_, frame = capture.read()
	hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
	lower_tape = np.array([0, 0, 249], dtype=np.uint8)
	upper_tape = np.array([1, 10, 255], dtype=np.uint8)
	mask = cv2.inRange(hsv, lower_tape, upper_tape)
	res = cv2.bitwise_and(frame, frame, mask= mask)
	(cnts, _) = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
	recNumber = 1
	recStorage = dict()

	for c in cnts:
		approx = cv2.approxPolyDP(c,0.01*cv2.arcLength(c,True),True)
		if len(approx)<6 and len(approx)>3 and cv2.contourArea(c)>40:
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
			quadNumber = "rect: " + str(recNumber)
			cv2.drawContours(frame,[c],-1,(0,0,255),2)
			recMiniData = {'xCoord':cx, 'yCoord':cy, 'area':cArea, 'height': rectHeight, 'top': topmost[1], 'bottom': bottommost[1]}
			recStorage.update({recNumber: recMiniData})
			recNumber += 1

	cv2.imshow('frame',frame)

	k = cv2.waitKey(5) & 0xFF
	if len(recStorage) > 0:
		analyze(recStorage, frame)
	if k == ord("q"):
		break

cv2.destroyAllWindows()

print(np.average(reject_outliers(np.asarray(areas))))

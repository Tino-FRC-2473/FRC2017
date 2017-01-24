import cv2
import numpy as np
import math

capture = cv2.VideoCapture(0)

'''def oneRect(frame, recStorage):
	recData = recStorage['1']
	width, height = frame[:2]
	if recData['xCoord'] < width/2:
		print 'left by: ' + str(width/2-recData['xCoord'])
	elif recData['xCoord'] > width/2:
		print 'right by: ' + str(recData['xCoord']-width/2)
	elif recData['xCoord'] == width/2:
		print 'centered horizontally'
	else:
		print 'horizontal N/A'''

def distanceRelation(averageHeight):
	x = averageHeight
	return 5200/averageHeight
	#return (19423*(x**(-0.735)))
	#return -.0009829417475*x + 33.0713297
	#return (2.937*(10**-11)*x**3 - 1.102 * (10**-6) * x**2 - 1.12 * (10 ** -2) * x - 4.223)
	#return (5.92*10**-18*averageArea**5 - 3.45*10**-13*averageArea**4 + 7.55*10**-9*averageArea**3 - 7.70*10**-5*averageArea**2 + 0.3638*averageArea - 600.939)

def reject_outliers(data, m=2):
	return data[abs(data - np.mean(data)) < m * np.std(data)]

areas = []

def distance(recSt, image):
	#for
	if len(recSt) == 3:
		maxHeight = 0
		avgHeight = 0
		firstRecData = recSt[1]
		secondRecData = recSt[2]
		thirdRecData = recSt[3]
		height1 = firstRecData['height']
		height2 = secondRecData['height']
		height3 = thirdRecData['height']
		#print (height1)
		#print (height2)
		#print (height3)
		#print ("")
		if height1 >= height2 and height1 >= height3:
			maxHeight = 1
		if height2 >= height3 and height2 >= height1:
			maxHeight = 2
		if height3 >= height2 and height3 >= height1:
			maxHeight = 3
		if maxHeight == 1:
			centerY2 = secondRecData['yCoord']
			centerY3 = thirdRecData['yCoord']
			if centerY2 > centerY3:
				heightSecondary = secondRecData['top'] - thirdRecData['bottom']
				averageHeight = (height1+heightSecondary)/2
			else:
				heightSecondary = thirdRecData['top'] - secondRecData['bottom']
				averageHeight = (height1+heightSecondary)/2
		if maxHeight == 2:
			centerY1 = firstRecData['yCoord']
			centerY3 = thirdRecData['yCoord']
			if centerY1 > centerY3:
				heightSecondary = firstRecData['top'] - thirdRecData['bottom']
				averageHeight = (height2+heightSecondary)/2
			else:
				heightSecondary = thirdRecData['top'] - firstRecData['bottom']
				averageHeight = (height2+heightSecondary)/2
		if maxHeight == 3:
			centerY1 = firstRecData['yCoord']
			centerY2 = secondRecData['yCoord']
			if centerY1 > centerY2:
				heightSecondary = firstRecData['top'] - secondRecData['bottom']
				averageHeight = (height3+heightSecondary)/2
			else:
				heightSecondary = secondRecData['top'] - firstRecData['bottom']
				averageHeight = (height3+heightSecondary)/2
		#print(averageHeight)
		#print ('lol')
		#print(distanceRelation(averageHeight))
		#print("_")
	if len(recSt) == 2:
		firstRecData = recSt[1]
		secondRecData = recSt[2]
		firstArea = firstRecData['area']
		secondArea = secondRecData['area']
		firstHeight = firstRecData['height']
		secondHeight = secondRecData['height']
		firstX = firstRecData['xCoord']
		secondX = secondRecData['xCoord']
		firstY = firstRecData['yCoord']
		secondY = secondRecData['yCoord']
		averageArea = (firstArea+secondArea)/2
		averageHeight = (firstHeight+secondHeight)/2
		averageCenter = ((firstRecData['xCoord']+secondRecData['xCoord'])/2, (firstRecData['yCoord']+secondRecData['yCoord'])/2)
		differenceCenter = ((firstRecData['xCoord']-secondRecData['xCoord']), (firstRecData['yCoord']-secondRecData['yCoord']))
		calVal = 8/differenceCenter[0]
		centerOffset = (len(image[0]))/2 - averageCenter[0]
		distance = distanceRelation(averageHeight)
		angle = math.atan((distance-13)/centerOffset)
		angle = angle*180/math.pi
		print(angle)
		#print (averageHeight)
		#areas.append(averageArea)
		#print(distance)
	if len(recSt) == 1:
		firstRecData = recSt[1]
		firstArea = firstRecData['area']
		firstHeight = firstRecData['height']
		#print(distanceRelation(firstHeight))
def height(topmost, bottommost):
	height = abs(topmost[1] - bottommost[1])
	return height

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
			#heightNumber=1
			'''for i in c[0]:
				if i == cx:
					HeightCoordinates[heightNumber-1] = tuple(i,yCoordinateHeight[i])
					heightNumber += 1
				if heightNumber >= 2:
					break'''
			rectHeight = height(topmost, bottommost)
			quadNumber = "rect: " + str(recNumber)
			cv2.drawContours(frame,[c],-1,(0,0,255),2)
			recMiniData = {'xCoord':cx, 'yCoord':cy, 'area':cArea, 'height': rectHeight, 'top': topmost[1], 'bottom': bottommost[1]}
			recStorage.update({recNumber: recMiniData})
			recNumber += 1
	cv2.imshow('frame',frame)
	k = cv2.waitKey(5) & 0xFF
	if len(recStorage) > 0:
		distance(recStorage, frame)
	if k == ord("q"):
		break

cv2.destroyAllWindows()

print(np.average(reject_outliers(np.asarray(areas))))

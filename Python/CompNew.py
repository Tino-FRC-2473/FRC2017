from __future__ import division
import time
import os
import cv2
import numpy as np
import math
import scipy.integrate as integrate
import scipy.special as special
import socket
#from json import JSONEncoder
#from json import JSONDecoder
import atexit
import logging
import datetime 

#logger initialization
ts = time.time()
st = datetime.datetime.fromtimestamp(ts).strftime('%Y-%m-%d %H:%M:%S')
name = st+'.log'
logging.basicConfig(filename=name,level=logging.DEBUG)

#set the camera exposure
os.system("v4l2-ctl --set-ctrl=exposure_auto_priority=1")
os.system("v4l2-ctl --set-ctrl=exposure_auto=1")
os.system("v4l2-ctl --set-ctrl=exposure_auto_priority=0")
os.system("v4l2-ctl --set-ctrl=exposure_absolute=3")

#bind to and open socket

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
host = ''
port = 5807
x = 1
s.bind((host, port))
logging.info('Listening')
print ('Listening on Comp')
s.listen(1)
conn, addr = s.accept()
logging.info("Connected by:", addr)
print 'Connected by:', addr
st = ""

def exit_handler(): #closes socket when exiting
	s.shutdown(socket.SHUT_RDWR)
	s.close()
	logging.info("socket closed")
	print "socket closed"

atexit.register(exit_handler) #registers exit

def reject_outliers(data): #rejectsdataoutliers, inconsistent
	m = 2
	u = np.mean(data)
	s = np.std(data)
	filtered = [e for e in data if (u - 2 * s < e < u + 2 * s) and e != 0]
	return filtered


def distanceRelation(averageHeight): #calibration used to find distance via pixel height
	x = float(averageHeight)
	#print('distHeight: ', x)
	if averageHeight != 0:
		diag = 3050.0 / averageHeight #calibrate through regression
		return math.sqrt(diag*diag - 25)


def widthDistRelation(averageWidth): #calibration used to find distance via pixel width
	x = float(averageWidth)
	#print('distWidth: ', x)
	if averageWidth != 0:
		diag = 1260.0 / averageHeight #calibrate through regression
		return math.sqrt(diag*diag - 25)


def angleOfAttack(firstHeight, secondHeight, rectX, image, width1, width2, toggleVar, Bearing, AX, Distance): #finds angle of attack, not used anymore
	#maxRectHeight = 0.0
	AoA = -1
	#print("first: ", firstHeight)
	#print("second: ", secondHeight)
	#print("distance: ", Distance)
	#minRectHeight = 0.0
	#print("firstheight: ", firstHeight)
	#print("secondheight: ", secondHeight)
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
	#print('width1: ', width1)
	#print('width2: ', width2)
	#print('height1: ', firstHeight)
	#print('height2: ', secondHeight)
	if toggleVar == 1:
		y = 3150.0/minRectHeight
		z = 3150.0/maxRectHeight
		#print("Heighty: ", y)
		#print("Heightz: ", z)
		#print((math.pow(z,2)-math.pow(y,2)-64)/(-16*y))
	else:
		y = 1260.0/minRectWidth
		z = 1260.0/maxRectWidth
		#print("Widthy: ", y)
		#print("Widthz: ", z)
	#print((math.pow(z,2)-math.pow(y,2)-64)/(-16*y))
	#print(z)
	aDist = float(z)
	if y > z:
		aDist = float(y)

	'''recBearing = bearing(image, AX)
	theta = abs(Bearing - recBearing)
	print(Bearing)
	print(recBearing)
	#print z
	print math.sin(theta * math.pi/180.0)

	sinA = (z*math.sin(theta*math.pi/180.0))/4.065
	print(sinA)
	if abs(sinA) <= 1:
		AoA = math.asin(sinA)
		AoA = AoA * 180.0 / math.pi
	return AoA'''
	if abs(Distance - aDist) > 8:
		return None
	if abs((Distance**2 + 4.065**2 - aDist**2)/(Distance*2*4.065)) < 1:
		AoA = math.acos((Distance**2 + 4.065**2 - aDist**2)/(Distance*2*4.065)) * 180 / math.pi
	else:
		print (Distance**2 + 4.065**2 - aDist**2)/(Distance*2*4.065) * 180 / math.pi
	if AoA > 90.0:
		AoA = 180.0 - AoA
	return AoA

	'''if(((math.pow(z,2)-math.pow(y,2)-64)/(-16*y))>1):
		return -1
	else:
		angleOpposite = 0.0
		closeAngle = 0.0
		a = 0.0
		#a = float(math.sqrt(((z*z)+(y*y)-32.0)/2.0))
		#theta = float(math.acos(((a*a)+(y*y) - 16.0)/(2.0*a*y)))
		#beta = float(math.asin(a*math.sin(theta)/4.0))
		try:
			angleOpposite = float(math.acos((math.pow(z,2)-math.pow(y,2)-64)/(-16*y))) * (180/math.pi)
			closeAngle = float(math.asin((4.0 * math.sin(angleOpposite))/y) * (180/math.pi))
			meme = 0
		except ValueError:
			print("arcsin or arccos error")
			return None
		''''''theta = theta * (180.0/math.pi)
		beta = beta * (180.0/math.pi)
		print("a: ", a)
		print("theta: ", theta)
		print("beta: ", beta)
		print("z: ", z)
		print("y: ", y)''''''
		angleOfAttack = 90.0 - angleOpposite - closeAngle - abs(bearing)
		if rectX > len(image[0])/2:
			#angleOfAttack = angleOfAttack * -1.0
			return angleOfAttack
		else:
			return angleOfAttack'''

def height(topmost, bottommost): #returns height in pixels
	height = abs(topmost[1] - bottommost[1])
	return height

def width (leftmost, rightmost): #returns width in pixels
	width = abs(leftmost[0] - rightmost[0])
	return width

def bearing(image, centerX): #used to find bearing, aka simple angle to turn to point to peg
	middle = len(image[0])/2
	leftBearingCoord = 0 - middle
	rightBearingCoord = middle
	negativeCheck = centerX - middle
	chordWidth = len(image[0])
	angle = 62.42
	bisAngle = (angle/2)*math.pi/180.0

	#first method of calculating bearing (better, currently used)
	r = abs(middle/(math.sin(bisAngle)))
	straight = r*math.cos(bisAngle)
	bearing2 = math.atan(abs(centerX - middle)/straight) * 180.0 / math.pi

	#second method of calculating bearing using calculus, more complex, unneeded
	func = 0
	'''try:
		func = lambda x: math.sqrt(abs(1+ x**2/(r**2 - x**2)))
	except ZeroDivisionError:
		print("DomainError integration")
		return None

	imageSector = integrate.quad(func, leftBearingCoord, rightBearingCoord)
	turnSector = integrate.quad(func, centerX, 0)
	turnSectorLength = turnSector[0]
	turnSectorLength = abs(turnSectorLength)
	bearingAngle = abs(angle * 180 / math.pi * turnSectorLength / imageSector[0] - 35.21)
	#returns final bearing'''
	if negativeCheck < 0:
		return -bearing2
	else:
		return bearing2

def end():
	s.close()

def analyze(recSt, image): #general analysis function
	calHWThreshold = 9/10
	if len(recSt) == 3: #sorts through 3 rectangle case, figures out which rectangle goes where
		logging.info("3 rectangles detected")
		#print("3rd")
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
		area1 = firstRecData['area']
		area2 = secondRecData['area']
		area3 = thirdRecData['area']
		firstX = firstRecData['xCoord']
		secondX = secondRecData['xCoord']
		thirdX = thirdRecData['xCoord']
		#print("firstRectop: ", firstRecData['top'])
		#print("firstRecbottom: ", firstRecData['bottom'])
		#print("secondRectop: ", secondRecData['top'])
		#print("secondRecbottom: ", secondRecData['bottom'])
		#print("thirdRectop: ", thirdRecData['top'])
		#print("thirdRecbottom: ", thirdRecData['bottom'])

		if height1 >= height2 and height1 >= height3:
			maxHeight = height1
			if firstX < secondX and firstX < thirdX:
				LR = 1.0
			else:
				LR = 0.0
		if height2 >= height3 and height2 >= height1:
			maxHeight = height2
			if secondX < firstX and secondX < thirdX:
				LR = 1.0
			else:
				LR = 0.0
		if height3 >= height2 and height3 >= height1:
			maxHeight = height3
			if thirdX < firstX and thirdX < secondX:
				LR = 1.0
			else:
				LR = 0.0
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
			if centerY2 < centerY3:
				heightSecondary = secondRecData['top'] - thirdRecData['bottom']
				heightSecondary = abs(heightSecondary)
				averageHeight = float(height1+heightSecondary)/2
			else:
				heightSecondary = thirdRecData['top'] - secondRecData['bottom']
				heightSecondary = abs(heightSecondary)
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
			if centerY1 < centerY3:
				heightSecondary = firstRecData['top'] - thirdRecData['bottom']
				heightSecondary = abs(heightSecondary)
				averageHeight = float(height2+heightSecondary)/2
			else:
				heightSecondary = thirdRecData['top'] - firstRecData['bottom']
				heightSecondary = abs(heightSecondary)
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
			if centerY1 < centerY2:
				heightSecondary = firstRecData['top'] - secondRecData['bottom']
				heightSecondary = abs(heightSecondary)
				averageHeight = float(height3+heightSecondary)/2
			else:
				heightSecondary = secondRecData['top'] - firstRecData['bottom']
				heightSecondary = abs(heightSecondary)
				averageHeight = float(height3+heightSecondary)/2
		#print("HeightSecondary: ", heightSecondary)
		#print("averageHeight: ", averageHeight)
		#print("maxHeight: ", maxHeight)
		if toggleVar == 1:
			distance = distanceRelation(averageHeight)
		else:
			distance = widthDistRelation(averageWidth)
		bearingAngle = bearing(image, centerX)
		AoAX = 0.0
		angleAttack = angleOfAttack(maxHeight, heightSecondary, centerX, image, float(width1), float(width2), toggleVar, bearingAngle, AoAX, distance)
		return [distance, bearingAngle, angleAttack, LR, 3]


	if len(recSt) == 2: #same as three rectangle case, except with 2 rectangles
		logging.info("2 rectangles detected")
		toggleVar = 0
		firstRecData = recSt[1]
		secondRecData = recSt[2]

		firstX = firstRecData['xCoord']
		secondX = secondRecData['xCoord']
		rectX = (firstX+secondX)/2

		firstArea = firstRecData['area']
		secondArea = secondRecData['area']

		firstDummyHeight = firstRecData['height']
		secondDummyHeight = secondRecData['height']

		firstHeight = firstRecData['case2H']
		secondHeight = secondRecData['case2H']

		firstWidth = firstRecData['width']
		secondWidth = secondRecData['width']

		#print("1case2Height: ", firstHeight)
		#print("2case2Height: ", secondHeight)


		averageArea = (firstArea+secondArea)/2
		averageHeight = (firstHeight+secondHeight)/2
		averageWidth = (firstWidth+secondWidth)/2
		averageCenter = ((firstRecData['xCoord']+secondRecData['xCoord'])/2, (firstRecData['yCoord']+secondRecData['yCoord'])/2)
		differenceCenter = ((firstRecData['xCoord']-secondRecData['xCoord']), (firstRecData['yCoord']-secondRecData['yCoord']))

		if firstHeight/secondHeight > calHWThreshold and secondHeight/firstHeight > calHWThreshold:
			toggleVar = 1
			print("using height")
			distance = distanceRelation(averageHeight)
			aOfAX = firstRecData['xCoord']
			if secondHeight > firstHeight:
				aOfAX = secondRecData['xCoord']
			if firstHeight > secondHeight:
				if firstX < secondX:
					LR = 1.0
				else:
					LR = 0.0
			else:
				if secondX < firstX:
					LR = 1.0
				else:
					LR = 0.0
		else:
			toggleVar = 0
			print("using width")
			distance = widthDistRelation(averageWidth)
			aOfAX = firstRecData['xCoord']
			if secondWidth > firstWidth:
				aOfAX = secondRecData['xCoord']
			if firstWidth > secondWidth:
				if firstX < secondX:
					LR = 1.0
				else:
					LR = 0.0
			else:
				if secondX < firstX:
					LR = 1.0
				else:
					LR = 0.0
		#print(firstHeight)
		#print(secondHeight)


		#print(aOfAX)
		bearingAngle = bearing(image, averageCenter[0])
		angleAttack = angleOfAttack(float(firstHeight), float(secondHeight), rectX, image, float(firstWidth), float(secondWidth), toggleVar, bearingAngle, float(aOfAX), distance)

		return [distance, bearingAngle, angleAttack, LR, 2]
	if len(recSt) == 1: #desperation if there is only one rectangle, however we don't return because we assume we can see 2
		logging.debug("1 rectangle detected")
		firstRecData = recSt[1]
		center = firstRecData['xCoord']
		bearingAngle = bearing(image, center)
		#return [0.0, bearingAngle, 0.0, 2.0]
		return None

def mean(numbers): #average calculation
	return float(sum(numbers)) / max(len(numbers), 1)

def findValues(array): #compiles values of an array to average and return accurate values
	distance = []
	bearing = []
	angleOfAttack = []
	r = []
	for i in range(1, len(array)):
		c = i - 1
		results = array[c]
		distance.append(results[0])
		bearing.append(results[1])
		angleOfAttack.append(results[2])
		r = results
	distance = mean(reject_outliers(distance))
	bearing = mean(reject_outliers(bearing))
	angleOfAttack = mean(reject_outliers(angleOfAttack))
	if r == None or distance == None or bearing == None or angleOfAttack == None or abs(bearing) > 32 or angleOfAttack < 60 or angleOfAttack > 90 or distance < 10:
		return None
	else:
		return [distance, bearing, angleOfAttack, r[3]]


def twothree(resArray): #sorts through second case and third case values to figure out which one is the worse option to use
	twoCase = []
	threeCase = []
	twoRes = None
	threeRes = None
	for i in range(1, len(resArray)):
		c = i - 1
		results = resArray[c]
		if results[4] == 2:
			twoCase.append(results)
		elif results[4] == 3:
			threeCase.append(results)
	if twoCase != []:
		twoRes = findValues(twoCase)
		print("twoRes", twoRes)
	else:
		twoCase = None
	if threeCase != []:
		threeRes = findValues(threeCase)
		print("threeRes", threeRes)
	else:
		threeCase = None


	if twoRes != None and threeRes == None:
		return twoRes
	elif twoRes == None and threeRes != None:
		return threeRes
	elif twoRes == None and threeRes == None:
		return None
	else:
		if abs(twoRes[2]-90) > abs(threeRes[2]-90):
			print("returning2")
			return twoRes
		else:
			print("returning3")
			return threeRes




def stat(returned): #generic stat function to iterate through frames to get multiple different values
	distance = []
	bearing = []
	angleOfAttack = []
	r = []
	resArray = []
	values = []
	results = None
	for i in range(1,10):
		results = None
		#print(results)
		while results == None:
			results = CV()
		distance.append(results[0])
		bearing.append(results[1])
		angleOfAttack.append(results[2])
		r = results
		if r[0] > 10 and abs(r[1]) < 32 and r[2] > 60 and r[2] < 90:
			resArray.append(results)
		else:
			print("append error")
		#print("resultsiteration: ", results)
	values = twothree(resArray)
	return values

def cap(): #captures frame, but need to fix buffer issue
	capture = cv2.VideoCapture(0)
	for i in range(1, 7): #goes through multiple frames to account for frame buffer, still not fully tested
		ret, frame = capture.read()
	capture.release()
	return frame

def CV(): #general function that runs through frame, thresholds it, and extracts basic info from the rectangles to be passed into analyze
	frame = None
	frame = cap()
	logging.info("frame received")
	hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
	'''cv2.imwrite('hsvorig', hsv)'''
	'''cv2.imwrite("frame.jpg", frame)'''
	lower_tape = np.array([40, 100, 90], dtype=np.uint8)
	upper_tape = np.array([100, 250, 225], dtype=np.uint8)
	mask = cv2.inRange(hsv, lower_tape, upper_tape)
	'''cv2.imshow('mask', mask)'''
	'''cv2.imwrite('maskwrite.jpg', mask)'''
	'''cv2.imwrite("mask.jpg", mask)'''
	res = cv2.bitwise_and(frame, frame, mask= mask)
	(res, cnts, _) = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
	recNumber = 1
	recStorage = dict()
	'''print(cnts)'''
	for c in cnts:
		approx = cv2.approxPolyDP(c,0.01*cv2.arcLength(c,True),True)
		if len(approx)<18 and len(approx)>3 and cv2.contourArea(c)>275:
			M = cv2.moments(c)
			#print(c)
			cx = int(M['m10']/M['m00'])
			cy = int(M['m01']/M['m00'])
			cArea = cv2.contourArea(c)
			topmost = tuple(c[c[:,:,1].argmin()][0])
			bottommost = tuple(c[c[:,:,1].argmax()][0])
			leftmost = tuple(c[c[:,:,0].argmin()][0])
			rightmost = tuple(c[c[:,:,0].argmax()][0])
			#print("topmost: ", topmost)
			#print("bottommost: ", bottommost)
			#print("leftmost: ", leftmost)
			#print("rightmost: ", rightmost)
			minx = 10000
			maxx = 0
			y = 0
			for i in range (0,len(c)):
				array = c[i]
				coord = array[0]
				y += coord[1]
			y /= len(c)
			for i in range (0,len(c)):
				array = c[i]
				coord = array[0]
				#print(coord[0])
				if coord[1] < y:
					if coord[0] < minx:
						minx = coord[0]
					if coord[0] > maxx:
						maxx = coord[0]
			minx += 10
			maxx -= 10
			#note: top is actually the bottom and bottom is actually the top, as pixel values increase from top to bottom
			toplefty = 1000
			toprighty = 1000
			bottomlefty = 0
			bottomrighty = 0
			for i in range (0,len(c)):
				array = c[i]
				coord = array[0]
				if coord[0] < minx:
					if coord[1] < toplefty:
						toplefty = coord[1]
					if coord[1] > bottomlefty:
						bottomlefty = coord[1]
				else:
					if coord[1] < toprighty:
						toprighty = coord[1]
					if coord[1] > bottomrighty:
						bottomrighty = coord[1]
			h1 = (bottomlefty - toplefty)
			h2 = (bottomrighty - toprighty)
			diffTop = abs(toplefty - toprighty)
			diffBottom = abs(bottomlefty - bottomrighty)
			#print(toplefty)
			#print(toprighty)
			#print(bottomlefty)
			#print(bottomrighty)
			#print("")
			case2H = 0
			if diffBottom > diffTop:
				if h1 < h2:
					case2H = h1
				else:
					case2H = h2
			else:
				if h1 < h2:
					case2H = h2
				else:
					case2H = h1
			XMin = 10000
			XMax = 0
			for i in range (0,len(c)): #untested method of calculating centerX, will rely on OpenCV protocol for now;
				array = c[i]
				coord = array[0]
				if coord[1] == bottomlefty:
					if coord[0] < XMin:
						XMin = coord[0]
				elif coord[1] == bottomrighty:
					if coord[0] > XMax:
						XMax = coord[0]
			newCenterX = (XMax + XMin)/2
			print(newCenterX)
			print(cx)
			print("")
			#if abs(newCenterX - cx) < 5:
				#newCenterX = cx


			'''print("difftop", diffTop)
			print("diffbottom", diffBottom)
			print("h1", h1)
			print("h2", h2)
			print("case2H", case2H)'''

			yCoordinateHeight = c[1]
			rectHeight = height(topmost, bottommost)
			rectWidth = width(leftmost, rightmost)
			quadNumber = "rect: " + str(recNumber)
			cv2.drawContours(frame,[c],-1,(0,0,255),2)
			#Updates a database of storage info to pass to analyze
			recMiniData = {'xCoord':cx, 'yCoord':cy, 'area':cArea, 'height': rectHeight, 'top': topmost[1], 'bottom': bottommost[1], 'width': rectWidth, 'case2H': case2H}
			recStorage.update({recNumber: recMiniData})
			recNumber += 1

	cv2.imwrite("sick.jpg", frame)
	'''cv2.imshow('frame',frame)'''
	'''cv2.imwrite('frame.jpg', frame)'''
	
	k = cv2.waitKey(5) & 0xFF
	if len(recStorage) > 0:
		logging.info("rectangle(s) detected")
		#passes info to analyze
		results = analyze(recStorage, frame)
		return results

	#if k == ord("q"):
		#break


while (1):
	#scans for CV order
	logging.info("scanning")
	returned = conn.recv(1024)
	print(returned)
	results = None
	goodData = 1
	if "CV()" in returned:
		results = eval("CV()")
		if results == None:
			goodData = 0
			logging.debug("failure for unknown reason")				
        if goodData==1:
                logging.info("detection success")
        logging.info(str(float(meme) + " " + float(results[0])) + " " + str(float(results[2])) + " " + str(float((results[1]))) + " " + str(float(results[3])) + " " + str(float(time.time())) + "\n")
	print(results)
	#Sends results to RoboRIO
	if (results != None) and (len(results) >= 4):
		sending = str(float(goodData) + " " + float(results[0])) + " " + str(float(results[2])) + " " + str(float((results[1]))) + " " + str(float(results[3])) + " " + str(float(time.time())) + "\n"
		sending = str(sending)
		conn.send(sending)
		'''conn.send(JSONEncoder().encode({"Distance": results[0],
		"Angle A": results[2],
		"Bearing": results[1],
		"Left or Right": results[3],
		"Time Stamp": float(time.time())}))'''
		logging.info("successfully sent data")
		print("something sent")
		print(sending)
	else:
                sending = str('0 0 0 0 0 0')
		logging.debug("meme data sent")
	'''results = CV()'''


cv2.destroyAllWindows()


import cv2
import numpy as np

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

def distanceRelation(averageArea):
	x = averageArea
	return -.0009829417475*x + 33.0713297
	#return (2.937*(10**-11)*x**3 - 1.102 * (10**-6) * x**2 - 1.12 * (10 ** -2) * x - 4.223)
	#return (5.92*10**-18*averageArea**5 - 3.45*10**-13*averageArea**4 + 7.55*10**-9*averageArea**3 - 7.70*10**-5*averageArea**2 + 0.3638*averageArea - 600.939)

def reject_outliers(data, m=2):
	return data[abs(data - np.mean(data)) < m * np.std(data)]

areas = []

def distance(recSt):
	if len(recSt) == 2:
		firstRecData = recSt[1]
		secondRecData = recSt[2]
		firstArea = firstRecData['area']
		secondArea = secondRecData['area']
		averageArea = (firstArea+secondArea)/2
		#areas.append(averageArea)
		print(distanceRelation(averageArea))


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
			quadNumber = "rect: " + str(recNumber)
			cv2.drawContours(frame,[c],-1,(0,0,255),2)
			recMiniData = {'xCoord':cx, 'yCoord':cy, 'area':cArea} #
			recStorage.update({recNumber: recMiniData})
			recNumber += 1
	cv2.imshow('frame',frame)
	k = cv2.waitKey(5) & 0xFF
	if len(recStorage) > 0:
		distance(recStorage)
	if k == ord("q"):
		break

cv2.destroyAllWindows()

print(np.average(reject_outliers(np.asarray(areas))))

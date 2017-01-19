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

while(1):
	_, frame = capture.read()
	hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
	lower_tape = np.array([0, 0, 245], dtype=np.uint8)
	upper_tape = np.array([1, 10, 255], dtype=np.uint8)
	mask = cv2.inRange(hsv, lower_tape, upper_tape)
	res = cv2.bitwise_and(frame, frame, mask= mask)
	(cnts, _) = cv2.findContours(mask.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
	recNumber = 1
	recStorage = dict()
	for c in cnts:
		approx = cv2.approxPolyDP(c,0.01*cv2.arcLength(c,True),True)
		if len(approx)==4 and cv2.contourArea(c)>40:
			M = cv2.moments(c)
			cx = int(M['m10']/M['m00'])
			cy = int(M['m01']/M['m00'])
			cArea = cv2.contourArea(c)
			quadNumber = "rect: " + str(recNumber)
			cv2.drawContours(frame,approx,0,(0,0,255),-1)
			#print (quadNumber + " area: " + str(cArea))
			#print (quadNumber + " coordinates: " + str(cx) + ", " + str(cy))
			recMiniData = {'xCoord':cx, 'yCoord':cy, 'area':cArea} #
			recStorage.update({recNumber: recMiniData})
			recNumber += 1


	cv2.imshow('frame',frame)
	cv2.imshow('mask',mask)
	if len(recStorage) > 0:
		print recStorage
	#if len(recStorage) == 1:
	#	oneRect(frame, recStorage)
	#cv2.imshow('res',res)
	k = cv2.waitKey(5) & 0xFF
	if k == 27:
		break




cv2.destroyAllWindows()
